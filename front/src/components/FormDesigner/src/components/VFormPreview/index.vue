<!--
 * @Description: 渲染组件，无法使用Vben的组件
-->
<template>
  <a-modal
    title="预览(支持布局)"
    :open="state.visible"
    @ok="handleGetData"
    @cancel="handleCancel"
    okText="获取数据"
    cancelText="关闭"
    style="top: 20px"
    :destroyOnClose="true"
    :width="900"
  >
    <VFormCreate :form-config="state.formConfig as any" v-model:fApi="state.fApi" v-model:formModel="state.formModel" @submit="onSubmit">
      <template #slotName="{ formModel, field }">
        <a-input v-model:value="formModel[field]" placeholder="我是插槽传递的输入框" />
      </template>
    </VFormCreate>
    <JsonModal ref="jsonModal" />
  </a-modal>
</template>
<script lang="ts">
import { IFormConfig } from '../../typings/v-form-component';
import { IAnyObject } from '../../typings/base-type';
import VFormCreate from '../VFormCreate/index.vue';
import { formatRules } from '../../utils';
import { IVFormMethods } from '../../hooks/useVFormMethods';
import JsonModal from '../VFormDesign/components/JsonModal.vue';
import { IToolbarMethods } from '../../typings/form-type';

export default defineComponent({
  name: 'VFormPreview',
  components: {
    JsonModal,
    VFormCreate,
  },
  setup() {
    const jsonModal = ref<IToolbarMethods | null>(null);
    const state: any = reactive<{
      formModel: IAnyObject;
      visible: boolean;
      formConfig: IFormConfig;
      fApi: IVFormMethods;
    }>({
      formModel: {},
      formConfig: {} as IFormConfig,
      visible: false,
      fApi: {} as IVFormMethods,
    });

    /**
     * 显示Json数据弹框
     * @param jsonData
     */
    const showModal = (jsonData: IFormConfig) => {
      formatRules(jsonData.schemas);
      state.formConfig = jsonData as any;
      state.visible = true;
    };

    /**
     * 获取表单数据
     * @return {Promise<void>}
     */
    const handleCancel = () => {
      state.visible = false;
      state.formModel = {};
    };
    const handleGetData = async () => {
      const _data = await state.fApi.submit?.();
      jsonModal.value?.showModal?.(_data);
    };

    const onSubmit = (_data: IAnyObject) => {
      //
    };
    const onCancel = () => {
      state.formModel = {};
    };
    return {
      handleGetData,
      handleCancel,
      state,
      showModal,
      jsonModal,
      onSubmit,
      onCancel,
    };
  },
});
</script>
