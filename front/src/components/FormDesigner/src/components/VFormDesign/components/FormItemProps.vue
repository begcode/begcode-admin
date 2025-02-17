<template>
  <div class="properties-content">
    <div class="properties-body" v-if="formConfig.currentItem?.itemProps">
      <a-empty class="hint-box" v-if="!formConfig.currentItem.key" description="未选择控件" />
      <a-form v-else label-align="left" layout="vertical">
        <div v-for="item of baseFormItemProps" :key="item.name">
          <a-form-item :label="item.label" v-if="showProps(item.exclude)">
            <component
              v-if="item.component"
              class="component-props"
              v-bind="item.componentProps"
              :is="item.component"
              v-model:value="formConfig.currentItem[item.name]"
            />
          </a-form-item>
        </div>
        <div v-for="item of advanceFormItemProps" :key="item.name">
          <a-form-item :label="item.label" v-if="showProps(item.exclude)">
            <component
              v-if="item.component"
              class="component-props"
              v-bind="item.componentProps"
              :is="item.component"
              v-model:value="formConfig.currentItem.itemProps[item.name]"
            />
          </a-form-item>
        </div>
        <div v-for="item of advanceFormItemColProps" :key="item.name">
          <a-form-item :label="item.label" v-if="showProps(item.exclude)">
            <component
              v-if="item.component"
              class="component-props"
              v-bind="item.componentProps"
              :is="item.component"
              v-model:value="formConfig.currentItem.itemProps[item.name]['span']"
            />
          </a-form-item>
        </div>
        <a-form-item label="控制属性" v-if="controlPropsList.length">
          <a-col v-for="item of controlPropsList" :key="item.name">
            <a-checkbox v-model:checked="formConfig.currentItem.itemProps[item.name]">
              {{ item.label }}
            </a-checkbox>
          </a-col>
        </a-form-item>
        <a-form-item label="是否必选" v-if="!['Grid'].includes(formConfig.currentItem.component)">
          <a-switch v-model:checked="formConfig.currentItem.itemProps['required']" />
          <a-input
            v-if="formConfig.currentItem.itemProps['required']"
            v-model:value="formConfig.currentItem.itemProps['message']"
            placeholder="请输入必选提示"
          />
        </a-form-item>
        <a-form-item
          v-if="!['Grid'].includes(formConfig.currentItem.component)"
          label="校验规则"
          :class="{ 'form-rule-props': !!formConfig.currentItem.itemProps['rules'] }"
        >
          <RuleProps />
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {
  baseFormItemControlAttrs,
  baseFormItemProps,
  advanceFormItemProps,
  advanceFormItemColProps,
} from '../../VFormDesign/config/formItemPropsConfig';

import RuleProps from './RuleProps.vue';
import { useFormDesignState } from '../../../hooks/useFormDesignState';

defineOptions({
  name: 'FormItemProps',
});

const { formConfig } = useFormDesignState();

watch(
  () => formConfig.value,
  () => {
    if (formConfig.value.currentItem) {
      formConfig.value.currentItem.itemProps = formConfig.value.currentItem.itemProps || {};
      formConfig.value.currentItem.itemProps.labelCol = formConfig.value.currentItem.itemProps.labelCol || {};
      formConfig.value.currentItem.itemProps.wrapperCol = formConfig.value.currentItem.itemProps.wrapperCol || {};
    }
  },
  { deep: true, immediate: true },
);
const showProps = (exclude: string[] | undefined) => {
  if (!exclude) {
    return true;
  }
  return _isArray(exclude) ? !exclude.includes(formConfig.value.currentItem!.component) : true;
};

const controlPropsList = computed(() => {
  return baseFormItemControlAttrs.filter(item => {
    return showProps(item.exclude);
  });
});
</script>
