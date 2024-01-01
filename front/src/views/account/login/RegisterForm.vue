<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="login" class="enter-x">
        <Input class="fix-auto-fill" size="large" v-model:value="formData.login" :placeholder="t('sys.login.userName')" />
      </FormItem>
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
      <FormItem name="password" class="enter-x">
        <StrengthMeter size="large" v-model:value="formData.password" :placeholder="t('sys.login.password')" />
      </FormItem>
      <FormItem name="confirmPassword" class="enter-x">
        <InputPassword
          size="large"
          visibilityToggle
          v-model:value="formData.confirmPassword"
          :placeholder="t('sys.login.confirmPassword')"
        />
      </FormItem>

      <FormItem class="enter-x" name="policy">
        <!-- No logic, you need to deal with it yourself -->
        <Checkbox v-model:checked="formData.policy" size="small">
          {{ t('sys.login.policy') }}
        </Checkbox>
      </FormItem>

      <Button type="primary" class="enter-x" size="large" block @click="handleRegister" :loading="loading">
        {{ t('sys.login.registerButton') }}
      </Button>
      <Button size="large" block class="mt-4 enter-x" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </Form>
  </div>
</template>
<script lang="ts" setup>
import { reactive, ref, unref, computed, toRaw } from 'vue';
import LoginFormTitle from './LoginFormTitle.vue';
import { Form, Input, Button, Checkbox } from 'ant-design-vue';
import { StrengthMeter, CountdownInput } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';
import { getSmsCaptcha, register } from '@/api-service/sys/user';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const FormItem = Form.Item;
const InputPassword = Input.Password;
const { t } = useI18n();
const { handleBackLogin, getLoginState } = useLoginState();
const { notification, createErrorModal } = useMessage();

const formRef = ref();
const loading = ref(false);

const formData = reactive({
  login: '',
  password: '',
  confirmPassword: '',
  mobile: '',
  code: '',
  policy: false,
});

const { getFormRules } = useFormRules(formData);
const { validForm } = useFormValid(formRef);

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.REGISTER);

/**
 * 注册
 */
async function handleRegister() {
  const data = await validForm();
  if (!data) return;
  try {
    loading.value = true;
    await register(
      toRaw({
        login: data.login,
        password: data.password,
        mobile: data.mobile,
        code: data.code,
      }),
    ).catch(err => {
      notification.warning({
        message: '错误提示',
        description: err.message || '网络异常，请检查您的网络连接是否正常!',
        duration: 3,
      });
    });
    notification.success({
      message: '注册成功',
      description: '注册成功,请返回登录页面登录系统',
      duration: 3,
    });
    handleBackLogin();
  } catch (error: any) {
    console.log('error', error);
    notification.error({
      message: '错误提示',
      description: error.message || '网络异常，请检查您的网络连接是否正常!',
      duration: 3,
    });
  } finally {
    loading.value = false;
  }
}
// 发送验证码的函数
function sendCodeApi() {
  if (!formData.mobile) {
    return new Promise<any>((_resolve, reject) => {
      reject('请输入手机号码');
    });
  }
  return getSmsCaptcha({ mobile: formData.mobile, type: 'SIGN_UP' });
}
</script>
