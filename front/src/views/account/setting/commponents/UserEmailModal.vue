<template>
  <BasicModal v-bind="$attrs" @register="registerModal" width="500px" :title="title" :showCancelBtn="false" :showOkBtn="false">
    <Form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
      <FormItem name="email">
        <Input size="large" v-model:value="formState.email" placeholder="请输入邮箱" />
      </FormItem>
      <FormItem>
        <Button size="large" type="primary" block @click="updateEmail"> 确认 </Button>
      </FormItem>
    </Form>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { Form, FormItem, Input, Button } from 'ant-design-vue';
import { BasicModal, useModalInner, Rule } from '@begcode/components';
import { useUserStore } from '@/store/modules/user';
import { useMessage } from '@/hooks/web/useMessage';
import { getSmsCaptcha } from '@/api-service/sys/user';
import { SmsEnum } from '@/views/account/login/useLogin';
import accountService from '@/api-service/account/account.service';

defineOptions({
  name: 'UserReplaceEmailModal',
});

const userStore = useUserStore();
const { createMessage } = useMessage();
const formState = reactive<Record<string, any>>({
  email: '',
});
const formRef = ref();

async function checkEmail(_rule, value, _callback) {
  return new Promise((resolve, reject) => {
    accountService.checkExistUser({ 'email.equals': value, 'id.notEquals': formState.id }).then(exist => {
      !exist ? resolve(true) : reject('邮箱已经使用!');
    });
  });
}

const validatorRules: Record<string, Rule[]> = {
  email: [{ required: true, type: 'email', message: '邮箱格式不正确' }, { validator: checkEmail }],
};
const useForm = Form.useForm;
const title = ref<string>('');
const emit = defineEmits(['register', 'success']);
const [registerModal, { setModalProps, closeModal }] = useModalInner(async data => {
  formRef.value.resetFields();
  formRef.value.clearValidate();
  setModalProps({ confirmLoading: false });
  title.value = '修改邮箱';
  //赋值
  data.record.smscode = '';
  Object.assign(formState, data.record);
});

/**
 * 更新邮箱
 */
async function updateEmail() {
  await formRef.value.validateFields();
  var userInfo = userStore.getUserInfo;
  userInfo.email = formState.email;
  accountService
    .updateAccount(userInfo)
    .then(user => {
      createMessage.success('修改邮箱成功');
      userStore.setUserInfo(user);
      emit('success');
      closeModal();
    })
    .catch(err => {
      console.log(err);
      createMessage.warning('修改邮箱失败');
    });
}
</script>

<style scoped></style>
