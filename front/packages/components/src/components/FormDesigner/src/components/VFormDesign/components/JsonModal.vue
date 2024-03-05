<!--
 * @Description: 渲染JSON数据
-->
<template>
  <Modal
    title="JSON数据"
    :footer="null"
    :open="state.visible"
    @cancel="handleCancel"
    :destroyOnClose="true"
    wrapClassName="v-code-modal"
    style="top: 20px"
    width="850px"
  >
    <PreviewCode :editorJson="editorJson" />
  </Modal>
</template>
<script lang="ts">
import { computed, ComputedRef, defineComponent, reactive } from 'vue';
import PreviewCode from './PreviewCode.vue';
import { IFormConfig } from '../../../typings/v-form-component';
import { formatRules, removeAttrs } from '../../../utils';
import { Modal } from 'ant-design-vue';

export default defineComponent({
  name: 'JsonModal',
  components: {
    PreviewCode,
    Modal,
  },
  emits: ['cancel'],
  setup(_props, { emit }) {
    const state: any = reactive({
      visible: false, // 控制json数据弹框显示
      jsonData: {}, // json数据
    });
    /**
     * 显示Json数据弹框
     * @param jsonData
     */
    const showModal = (jsonData: IFormConfig): void => {
      formatRules(jsonData.schemas);
      state.jsonData = jsonData as any;
      state.visible = true;
    };

    // 计算json数据
    const editorJson: ComputedRef<string> = computed(() => {
      return JSON.stringify(removeAttrs(state.jsonData as IFormConfig), null, '\t');
    });

    // 关闭弹框
    const handleCancel = (): void => {
      state.visible = false;
      emit('cancel');
    };

    return { state, editorJson, handleCancel, showModal };
  },
});
</script>
