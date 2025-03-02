<template>
  <a-modal
    title="预览(不支持布局)"
    :open="state.visible"
    @ok="handleGetData"
    @cancel="handleCancel"
    okText="获取数据"
    cancelText="关闭"
    style="top: 20px"
    :destroyOnClose="true"
    :width="900"
  >
    <basic-form v-bind="attrs" @register="registerForm" />
    <JsonModal ref="jsonModal" />
  </a-modal>
</template>
<script lang="ts" setup>
import { BasicForm, useForm } from '@/components/Form';
import { IFormConfig } from '../../typings/v-form-component';
import { IAnyObject } from '../../typings/base-type';
import JsonModal from '../VFormDesign/components/JsonModal.vue';
import { IToolbarMethods } from '../../typings/form-type';
import type { Recordable } from '#/utils';

const jsonModal = ref<IToolbarMethods | null>(null);
const state = reactive<{
  formModel: IAnyObject;
  visible: boolean;
  formConfig: IFormConfig;
}>({
  formModel: {},
  formConfig: {} as IFormConfig,
  visible: false,
});

const attrs = computed(() => {
  return {
    ...state.formConfig,
  } as Recordable;
});

/**
 * 显示Json数据弹框
 * @param jsonData
 */
const showModal = (jsonData: IFormConfig) => {
  state.formConfig = jsonData as any;
  state.visible = true;
};

//表单
const [registerForm, { validate }] = useForm();

const handleCancel = () => {
  state.visible = false;
};
/**
 * 获取表单数据
 * @return {Promise<void>}
 */
const handleGetData = async () => {
  let data = await validate();
  jsonModal.value?.showModal?.(data);
};

defineExpose({ showModal });
</script>
