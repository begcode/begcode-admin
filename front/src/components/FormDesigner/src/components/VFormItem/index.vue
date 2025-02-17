<template>
  <a-col v-bind="colPropsComputed">
    <a-form-item v-bind="{ ...formItemProps }">
      <template #label v-if="!formItemProps.hiddenLabel && schema.component !== 'Divider'">
        <a-tooltip>
          <span>{{ schema.label }}</span>
          <template #title v-if="schema.helpMessage"
            ><span>{{ schema.helpMessage }}</span></template
          >
          <Icon v-if="schema.helpMessage" class="ml-5" icon="ant-design:question-circle-outlined" />
        </a-tooltip>
      </template>
      <slot v-if="schema.componentProps && schema.componentProps?.slotName" :name="schema.componentProps.slotName" v-bind="schema"></slot>
      <a-divider v-else-if="schema.component == 'Divider' && schema.label && !formItemProps.hiddenLabel">{{ schema.label }}</a-divider>
      <!-- 部分控件需要一个空div -->
      <div>
        <component
          class="v-form-item-wrapper"
          :is="componentItem"
          v-bind="{ ...cmpProps, ...asyncProps }"
          :schema="schema"
          :style="schema.width ? { width: schema.width } : {}"
          @change="handleChange"
          @click="handleClick(schema)"
        />
      </div>

      <span v-if="['Button'].includes(schema.component)">{{ schema.label }}</span>
    </a-form-item>
  </a-col>
</template>
<script lang="ts">
import { Recordable } from '#/utils.d';
import { componentMap } from '../../core/formItemConfig';
import { IVFormComponent, IFormConfig } from '../../typings/v-form-component';
import { asyncComputed } from '@vueuse/core';
import { handleAsyncOptions } from '../../utils';
import { useFormModelState } from '../../hooks/useFormDesignState';

export default defineComponent({
  name: 'VFormItem',
  props: {
    formData: {
      type: Object,
      default: () => ({}),
    },
    schema: {
      type: Object as PropType<IVFormComponent>,
      required: true,
    },
    formConfig: {
      type: Object as PropType<IFormConfig>,
      required: true,
    },
  },
  emits: ['update:form-data', 'change'],
  setup(props, { emit }) {
    const { formModel: formData1, setFormModel } = useFormModelState();
    const colPropsComputed: any = computed(() => {
      const { colProps = {} } = props.schema;
      return colProps;
    });
    const formItemProps: any = computed(() => {
      const { formConfig } = unref(props);
      let { field, required, rules, labelCol, wrapperCol } = unref(props.schema);
      const { colon } = props.formConfig;

      const { itemProps } = unref(props.schema);

      //<editor-fold desc="布局属性">
      labelCol = labelCol
        ? labelCol
        : formConfig.layout === 'horizontal'
          ? formConfig.labelLayout === 'flex'
            ? { style: `width:${formConfig.labelWidth}px` }
            : formConfig.labelCol
          : {};

      wrapperCol = wrapperCol
        ? wrapperCol
        : formConfig.layout === 'horizontal'
          ? formConfig.labelLayout === 'flex'
            ? { style: 'width:auto;flex:1' }
            : formConfig.wrapperCol
          : {};

      const style = formConfig.layout === 'horizontal' && formConfig.labelLayout === 'flex' ? { display: 'flex' } : {};

      /**
       * 将字符串正则格式化成正则表达式
       */

      const newConfig = Object.assign(
        {},
        {
          name: field,
          style: { ...style },
          colon,
          required,
          rules,
          labelCol,
          wrapperCol,
        },
        itemProps,
      );
      if (!itemProps?.labelCol?.span) {
        newConfig.labelCol = labelCol;
      }
      if (!itemProps?.wrapperCol?.span) {
        newConfig.wrapperCol = wrapperCol;
      }
      if (!itemProps?.rules) {
        newConfig.rules = rules;
      }
      return newConfig;
    }) as Recordable<any>;

    const componentItem: Component = computed(() => componentMap.get(props.schema.component as string));

    // console.log('component change:', props.schema.component, componentItem.value);
    const handleClick = (schema: IVFormComponent): void => {
      if (schema.component === 'Button' && schema.componentProps?.handle) emit(schema.componentProps?.handle);
    };
    /**
     * 处理异步属性，异步属性会导致一些属性渲染错误，如defaultValue异步加载会导致渲染不出来，故而此处只处理options，treeData，同步属性在cmpProps中处理
     */
    const asyncProps: any = asyncComputed(async () => {
      let { options, treeData } = props.schema.componentProps ?? {};
      if (options) options = await handleAsyncOptions(options);
      if (treeData) treeData = await handleAsyncOptions(treeData);
      return {
        options,
        treeData,
      };
    });

    /**
     * 处理同步属性
     */
    const cmpProps: any = computed(() => {
      const isCheck = props.schema && ['Switch', 'Checkbox', 'Radio'].includes(props.schema.component);
      let { field } = props.schema;

      let { disabled, ...attrs } = _omit(props.schema.componentProps, ['options', 'treeData']) ?? {};

      disabled = props.formConfig.disabled || disabled;

      return {
        ...attrs,
        disabled,
        [isCheck ? 'checked' : 'value']: formData1.value[field!],
      };
    });

    const handleChange = function (e): void {
      const isCheck = ['Switch', 'Checkbox', 'Radio'].includes(props.schema.component);
      const target: any = e ? e.target : null;
      const value = target ? (isCheck ? target.checked : target.value) : e;
      if (setFormModel) {
        setFormModel(props.schema.field!, value);
      }
      emit('change', value);
    };
    return {
      componentItem,
      formItemProps,
      handleClick,
      asyncProps,
      cmpProps,
      handleChange,
      colPropsComputed,
    };
  },
});
</script>
<style lang="less" scoped>
.ml-5 {
  margin-left: 5px;
}

:deep(.ant-col) {
  width: auto;
}

.ant-form-item:not(.ant-form-item-with-help) {
  margin-bottom: 20px;
}
</style>
