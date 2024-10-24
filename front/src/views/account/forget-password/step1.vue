<template>
  <a-form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
    <a-form-item name="mobile" class="enter-x">
      <a-input size="large" v-model:value="formData.mobile" :placeholder="t('sys.login.mobile')" />
    </a-form-item>
    <a-form-item name="code" class="enter-x">
      <CountdownInput size="large" v-model:value="formData.code" :placeholder="t('sys.login.smsCode')" :sendCodeApi="sendCodeApi" />
    </a-form-item>
    <a-form-item class="enter-x">
      <a-button type="primary" size="large" block @click="handleNext" :loading="loading"> 下一步 </a-button>
      <a-button size="large" block class="mt-4" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </a-button>
    </a-form-item>
  </a-form>
</template>
<script lang="ts">
import { CountdownInput } from '@/components/CountDown';

import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { useLoginState, useFormRules, useFormValid, LoginStateEnum, SmsEnum } from '../login/useLogin';
import { phoneVerify, getSmsCaptcha, phoneLoginApi, getUserInfo } from '@/api-service/sys/user';
import { useUserStore } from '@/store/modules/user';
import accountService from '@/api-service/account/account.service';

export default defineComponent({
  name: 'step1',
  components: {
    CountdownInput,
  },
  emits: ['nextStep'],
  setup(_, { emit }) {
    const { t } = useI18n();
    const userStore = useUserStore();
    const { handleBackLogin } = useLoginState();
    const { notification } = useMessage();

    const formRef = ref();
    const { validForm } = useFormValid(formRef);
    const { getFormRules } = useFormRules();

    const loading = ref(false);
    const formData = reactive({
      mobile: '',
      code: '',
      resetKey: '',
    });

    /**
     * 下一步
     */
    async function handleNext() {
      const data = await validForm();
      if (!data) return;
      const resultInfo = await phoneLoginApi(
        toRaw({
          mobile: data.mobile,
          code: data.code,
        }),
      );
      if (resultInfo.id_token) {
        userStore.setToken(resultInfo.id_token);
        const userInfo = await getUserInfo();
        let accountInfo = {
          login: userInfo.login,
          mobile: data.mobile,
          code: data.code,
          resetKey: formData.resetKey,
        };
        emit('nextStep', accountInfo);
      } else {
        notification.error({
          message: t('sys.api.errorTip'),
          description: resultInfo.message || t('sys.api.networkExceptionMsg'),
          duration: 3,
        });
      }
    }
    //倒计时执行前的函数
    async function sendCodeApi() {
      const resetKey = await accountService.resetPasswordSmsCode({ mobile: formData.mobile });
      if (resetKey) {
        formData.resetKey = resetKey;
      }
      return new Promise(resolve => {
        resolve(!!resetKey);
      });
    }
    return {
      t,
      formRef,
      formData,
      getFormRules,
      handleNext,
      loading,
      handleBackLogin,
      sendCodeApi,
    };
  },
});
</script>
