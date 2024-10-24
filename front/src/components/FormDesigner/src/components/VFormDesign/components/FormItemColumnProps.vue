<template>
  <div class="properties-content">
    <div class="properties-body" v-if="formConfig.currentItem">
      <a-empty class="hint-box" v-if="!formConfig.currentItem.key" description="未选择控件" />
      <a-form v-else label-align="left" layout="vertical">
        <div v-for="item of baseItemColumnProps" :key="item.name">
          <a-form-item :label="item.label" v-if="showProps(item.exclude)">
            <component
              v-if="formConfig.currentItem.colProps && item.component"
              class="component-props"
              v-bind="item.componentProps"
              :is="item.component"
              v-model:value="formConfig.currentItem.colProps[item.name]"
            />
          </a-form-item>
        </div>
      </a-form>
    </div>
  </div>
</template>
<script lang="ts">
import { baseItemColumnProps } from '../config/formItemPropsConfig';
import RuleProps from './RuleProps.vue';
import { useFormDesignState } from '../../../hooks/useFormDesignState';

export default defineComponent({
  name: 'FormItemProps',
  components: {
    RuleProps,
  },
  // props: {} as PropsOptions,

  setup() {
    const { formConfig } = useFormDesignState();
    const showProps = (exclude: string[] | undefined) => {
      if (!exclude) {
        return true;
      }

      return _isArray(exclude) ? !exclude.includes(formConfig.value.currentItem!.component) : true;
    };
    return {
      baseItemColumnProps,
      formConfig,
      showProps,
    };
  },
});
</script>
