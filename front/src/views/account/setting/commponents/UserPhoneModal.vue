<template>
  <BasicModal v-bind="$attrs" @register="registerModal" width="500px" :title="title" :showCancelBtn="false" :showOkBtn="false">
    <Form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
      <FormItem name="mobile">
        <Input size="large" v-model:value="formState.mobile" placeholder="请输入手机号" />
      </FormItem>
      <FormItem name="code">
        <CountdownInput size="large" v-model:value="formState.code" placeholder="输入6位验证码" :sendCodeApi="sendCodeApi" />
      </FormItem>
      <FormItem>
        <Button size="large" type="primary" block @click="updatePhone"> 确认 </Button>
      </FormItem>
    </Form>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { Form, FormItem, Button, Input } from 'ant-design-vue';
import { BasicModal, CountdownInput, useModalInner } from '@begcode/components';
import { useUserStore } from '@/store/modules/user';
import { useMessage } from '@/hooks/web/useMessage';
import { getSmsCaptcha } from '@/api-service/sys/user';
import { Rule } from '@begcode/components';
import { updateMobile } from '../UserSetting.api';
import accountService from '@/api-service/account/account.service';

defineOptions({
  name: 'UserReplacePhoneModal',
});

const userStore = useUserStore();
const { createMessage } = useMessage();
const formState = reactive<Record<string, any>>({
  mobile: '',
  code: '',
});
const formRef = ref();
const userData = ref<any>({});

async function checkPhone(_rule, value, _callback) {
  return new Promise((resolve, reject) => {
    accountService.checkExistUser({ 'mobile.equals': value, 'id.notEquals': formState.id }).then(exist => {
      !exist ? resolve(true) : reject('手机号已存在!');
    });
  });
}

const validatorRules: Record<string, Rule[]> = {
  mobile: [{ validator: checkPhone }, { pattern: /^1[3456789]\d{9}$/, message: '手机号码格式有误' }],
  code: [{ required: true, message: '请输入验证码' }],
};
const useForm = Form.useForm;
const title = ref<string>('');
const emit = defineEmits(['register', 'success']);
const [registerModal, { setModalProps, closeModal }] = useModalInner(async data => {
  formRef.value.resetFields();
  formRef.value.clearValidate();
  setModalProps({ confirmLoading: false });
  title.value = '修改手机号';
  //赋值
  data.record.code = '';
  Object.assign(formState, data.record);
  userData.value = data.record;
});

/**
 * 倒计时执行前的函数
 */
function sendCodeApi() {
  return getSmsCaptcha({ mobile: formState.mobile, type: 'UPDATE_MOBILE' });
}

/**
 * 更新手机号
 */
async function updatePhone() {
  await formRef.value.validateFields();
  updateMobile(formState)
    .then(() => {
      createMessage.success('修改手机号成功');
      emit('success');
      closeModal();
    })
    .catch(err => {
      console.log(err);
      createMessage.warning('修改手机号失败');
    });
}
</script>
