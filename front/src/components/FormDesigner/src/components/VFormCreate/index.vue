<template>
  <div class="v-form-container">
    <a-form class="v-form-model" ref="eFormModel" :model="formModel" v-bind="formModelProps">
      <a-row>
        <FormRender
          v-for="(schema, index) of noHiddenList"
          :key="index"
          :schema="schema"
          :formConfig="formConfig"
          :formData="formModelNew"
          @change="handleChange"
          :setFormModel="setFormModel"
          @submit="handleSubmit"
          @reset="resetFields"
        >
          <template v-if="schema && schema.componentProps" #[`schema.componentProps!.slotName`]>
            <slot :name="schema.componentProps!.slotName" v-bind="{ formModel: formModel, field: schema.field, schema }"></slot>
          </template>
        </FormRender>
      </a-row>
    </a-form>
  </div>
</template>
<script lang="ts">
import { Row, Col } from 'ant-design-vue';
import { useVModel } from '@vueuse/core';
import FormRender from './components/FormRender.vue';
import { IFormConfig, AForm } from '../../typings/v-form-component';
import { useFormInstanceMethods } from '../../hooks/useFormInstanceMethods';
import { IProps, IVFormMethods, useVFormMethods } from '../../hooks/useVFormMethods';

import type { Recordable } from '#/utils';

export default defineComponent({
  name: 'FormCreate',
  components: {
    FormRender,
  },
  props: {
    fApi: {
      type: Object,
    },
    formModel: {
      type: Object,
      default: () => ({}),
    },
    formConfig: {
      type: Object as PropType<IFormConfig>,
      required: true,
    },
  },
  emits: ['submit', 'change', 'update:fApi', 'update:formModel'],
  setup(props, context) {
    const wrapperComp = props.formConfig.layout == 'vertical' ? Col : Row;
    const { emit } = context;
    const eFormModel = ref<AForm | null>(null);

    const formModelNew = computed({
      get: () => props.formModel,
      set: value => emit('update:formModel', value),
    });

    const noHiddenList = computed(() => {
      return props.formConfig.schemas && props.formConfig.schemas.filter(item => item.hidden !== true);
    });

    const fApi = useVModel(props, 'fApi', emit);

    const { submit, validate, clearValidate, resetFields, validateField } = useFormInstanceMethods<
      ['submit', 'change', 'update:fApi', 'update:formModel']
    >(props, formModelNew, context, eFormModel);

    const { linkOn, ...methods } = useVFormMethods<['submit', 'change', 'update:fApi', 'update:formModel']>(
      { formConfig: props.formConfig, formData: props.formModel } as unknown as IProps,
      context,
      eFormModel,
      {
        submit,
        validate,
        validateField,
        resetFields,
        clearValidate,
      },
    );

    fApi.value = methods;

    const handleChange = _event => {
      const { schema, value } = _event;
      const { field } = unref(schema);

      linkOn[field!]?.forEach(formItem => {
        formItem.update?.(value, formItem, fApi.value as IVFormMethods);
      });
    };
    /**
     * 获取表单属性
     */
    const formModelProps = computed(() => _omit(props.formConfig, ['disabled', 'labelWidth', 'schemas']) as Recordable);

    const handleSubmit = () => {
      submit();
    };

    provide('formModel', formModelNew);
    const setFormModel = (key, value) => {
      formModelNew.value[key] = value;
    };

    provide<(key: String, value: any) => void>('setFormModelMethod', setFormModel);

    // 把祖先组件的方法项注入到子组件中，子组件可通过inject获取
    return {
      eFormModel,
      submit,
      validate,
      validateField,
      resetFields,
      clearValidate,
      handleChange,
      formModelProps,
      handleSubmit,
      setFormModel,
      formModelNew,
      wrapperComp,
      noHiddenList,
    };
  },
});
</script>
<style lang="less" scoped>
.v-form-model {
  overflow: hidden;
}
</style>
