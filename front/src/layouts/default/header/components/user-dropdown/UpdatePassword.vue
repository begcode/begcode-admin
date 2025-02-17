<template>
  <basic-modal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="600px" data-cy="changePasswordModal">
    <basic-form @register="registerForm" />
  </basic-modal>
</template>
<script lang="ts" setup>
import { rules } from '@/utils/helper/validator';
import { BasicModal, useModalInner } from '@/components/Modal';
import { useForm, BasicForm } from '@/components/Form';
import { useMessage } from '@/hooks/web/useMessage';
import accountService from '@/api-service/account/account.service';
import { useI18n } from '@/hooks/web/useI18n';
import { useLocaleStore } from '@/store/modules/locale';

const localeStore = useLocaleStore();
const { t } = useI18n();

// 声明Emits
const emit = defineEmits(['register']);
const $message = useMessage();
const username = ref('');
const title = ref(t('layout.changePassword.changePassword'));
//表单配置
const [registerForm, { resetFields, validate, clearValidate }] = useForm({
  schemas: [
    {
      label: t('layout.changePassword.oldPassword'),
      field: 'currentPassword',
      component: 'InputPassword',
      required: true,
    },
    {
      label: t('layout.changePassword.newPassword'),
      field: 'newPassword',
      component: 'StrengthMeter',
      componentProps: {
        placeholder: t('layout.changePassword.pleaseEnterNewPassword'),
      },
      rules: [
        {
          required: true,
          message: t('layout.changePassword.pleaseEnterNewPassword'),
        },
      ],
    },
    {
      label: t('layout.changePassword.confirmNewPassword'),
      field: 'confirmpassword',
      component: 'InputPassword',
      dynamicRules: ({ values }) => rules.confirmPassword(values, true),
    },
  ],
  showActionButtonGroup: false,
  wrapperCol: null,
  labelWidth: localeStore.getLocale == 'zh_CN' ? 100 : 160,
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
  } catch (e) {
    console.log(e);
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
  title,
});
</script>
