<template>
  <basic-modal @register="registerModal" :width="800" :title="title" @ok="handleSubmit">
    <basic-form @register="registerForm" />
  </basic-modal>
</template>

<script lang="ts">
import { useForm, BasicForm } from '@/components/Form';
import { BasicModal, useModalInner } from '@/components/Modal';
import { thirdAppFormSchema } from './ThirdApp.data';
import { getThirdConfigByTenantId, saveOrUpdateThirdConfig } from './ThirdApp.api';
export default defineComponent({
  name: 'ThirdAppConfigModal',
  components: { BasicModal, BasicForm },
  setup(props, { emit }) {
    const title = ref<string>('钉钉配置');
    //表单配置
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      schemas: thirdAppFormSchema,
      showActionButtonGroup: false,
      labelCol: { span: 24 },
      wrapperCol: { span: 24 },
    });
    //表单赋值
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async data => {
      setModalProps({ confirmLoading: true });
      //重置表单
      await resetFields();
      let values = await getThirdConfigByTenantId({ tenantId: data.tenantId, thirdType: data.thirdType });
      setModalProps({ confirmLoading: false });
      //表单赋值
      if (values) {
        await setFieldsValue(values);
      } else {
        await setFieldsValue(data);
      }
    });

    /**
     * 第三方配置点击事件
     */
    async function handleSubmit() {
      let values = await validate();
      let isUpdate = false;
      if (values.id) {
        isUpdate = true;
      }
      await saveOrUpdateThirdConfig(values, isUpdate);
      emit('success');
      closeModal();
    }

    return {
      title,
      registerForm,
      registerModal,
      handleSubmit,
    };
  },
});
</script>

<style scoped></style>
