<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="修改密码" @ok="handleSubmit" width="600px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
import { ref, unref } from 'vue';
import { rules } from '@/utils/helper/validator';
import { BasicModal, useModalInner, useForm, BasicForm } from '@begcode/components';
import { useMessage } from '@/hooks/web/useMessage';
import accountService from '@/api-service/account/account.service';

// 声明Emits
const emit = defineEmits(['register']);
const $message = useMessage();
const formRef = ref();
const username = ref('');
//表单配置
const [registerForm, { resetFields, validate, clearValidate }] = useForm({
  schemas: [
    {
      label: '旧密码',
      field: 'currentPassword',
      component: 'InputPassword',
      required: true,
    },
    {
      label: '新密码',
      field: 'newPassword',
      component: 'StrengthMeter',
      componentProps: {
        placeholder: '请输入新密码',
      },
      rules: [
        {
          required: true,
          message: '请输入新密码',
        },
      ],
    },
    {
      label: '确认新密码',
      field: 'confirmpassword',
      component: 'InputPassword',
      dynamicRules: ({ values }) => rules.confirmPassword(values, true),
    },
  ],
  showActionButtonGroup: false,
});
//表单赋值
const [registerModal, { setModalProps, closeModal }] = useModalInner();

//表单提交事件
async function handleSubmit() {
  try {
    const values = await validate();
    setModalProps({ confirmLoading: true });
    //提交表单
    let params = Object.assign({ login: unref(username) }, values);
    accountService
      .changePassword(params)
      .then(() => {
        $message.createMessage.success('修改密码成功');
        closeModal();
      })
      .catch(() => {
        $message.createMessage.error('修改密码失败');
      });
  } finally {
    setModalProps({ confirmLoading: false });
  }
}

async function show(login) {
  if (!login) {
    $message.createMessage.warning('当前系统无登录用户!');
    return;
  } else {
    username.value = login;
    await setModalProps({ open: true });
    await resetFields();
    await clearValidate();
  }
}

defineExpose({
  show,
});
</script>
