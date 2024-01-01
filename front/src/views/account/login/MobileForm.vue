<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="mobile" class="enter-x">
        <Input size="large" v-model:value="formData.mobile" :placeholder="t('sys.login.mobile')" class="fix-auto-fill" />
      </FormItem>
      <FormItem name="code" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="formData.code"
          :placeholder="t('sys.login.smsCode')"
          :sendCodeApi="sendCodeApi"
        />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
  </div>
</template>
<script lang="ts" setup>
import { reactive, ref, computed, unref, toRaw } from 'vue';
import { Form, Input, Button } from 'ant-design-vue';
import { CountdownInput } from '@begcode/components';
import LoginFormTitle from './LoginFormTitle.vue';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { useLoginState, useFormRules, useFormValid, LoginStateEnum, SmsEnum } from './useLogin';
import { useUserStore } from '@/store/modules/user'; // todo 没有需要增加
import { getSmsCaptcha } from '@/api-service/sys/user'; // todo 这个统一处理到apiService中。

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const FormItem = Form.Item;
const { t } = useI18n();
const { handleBackLogin, getLoginState } = useLoginState();
const { getFormRules } = useFormRules();
const { notification, createErrorModal } = useMessage();
const userStore = useUserStore();

const formRef = ref();
const loading = ref(false);

const formData = reactive({
  mobile: '',
  code: '',
});

const { validForm } = useFormValid(formRef);

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE);

/**
 * 登录
 */
async function handleLogin() {
  const data = await validForm();
  if (!data) return;
  try {
    loading.value = true;
    const userInfo = await userStore.phoneLogin(
      toRaw({
        mobile: data.mobile,
        code: data.code,
        rememberMe: true,
        imageCode: '',
        mode: 'none', //不要默认的错误提示
      }),
    );
    if (userInfo) {
      notification.success({
        message: t('sys.login.loginSuccessTitle'),
        description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.firstName}`,
        duration: 3,
      });
    }
  } catch (error: any) {
    console.log('error', error);
    notification.error({
      message: t('sys.api.errorTip'),
      description: error.message || t('sys.api.networkExceptionMsg'),
      duration: 3,
    });
  } finally {
    loading.value = false;
  }
}
//倒计时执行前的函数
function sendCodeApi() {
  return getSmsCaptcha({ mobile: formData.mobile, type: 'LOGIN' });
}
</script>
