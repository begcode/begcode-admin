import type { RuleObject, Rule as ValidationRule } from 'ant-design-vue/lib/form/interface';
import { ref, computed, unref, Ref } from 'vue';
import { useI18n } from '@/hooks/web/useI18n';
import { OAUTH2_THIRD_LOGIN_TENANT_ID } from '@/enums/cacheEnum';
import { getAuthCache } from '@/utils/auth';
import accountService from '@/api-service/account/account.service';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

export enum LoginStateEnum {
  LOGIN,
  REGISTER,
  RESET_PASSWORD,
  MOBILE,
  QR_CODE,
}

export enum SmsEnum {
  LOGIN = '0',
  REGISTER = '1',
  FORGET_PASSWORD = '2',
}

const currentState = ref(LoginStateEnum.LOGIN);

export function useLoginState() {
  function setLoginState(state: LoginStateEnum) {
    currentState.value = state;
  }

  const getLoginState = computed(() => currentState.value);

  function handleBackLogin() {
    setLoginState(LoginStateEnum.LOGIN);
  }

  return { setLoginState, getLoginState, handleBackLogin };
}

export function useFormValid<T extends Object = any>(formRef: Ref<any>) {
  async function validForm() {
    const form = unref(formRef);
    if (!form) return;
    const data = await form.validate();
    return data as T;
  }

  return { validForm };
}

export function useFormRules(formData?: Recordable) {
  const { t } = useI18n();

  const getAccountFormRule = computed(() => createRule(t('sys.login.accountPlaceholder')));
  const getPasswordFormRule = computed(() => createRule(t('sys.login.passwordPlaceholder')));
  const getSmsFormRule = computed(() => createRule(t('sys.login.smsPlaceholder')));
  const getMobileFormRule = computed(() => createRule(t('sys.login.mobilePlaceholder')));
  const getRegisterAccountRule = computed(() => createRegisterAccountRule('account'));
  const getRegisterMobileRule = computed(() => createRegisterAccountRule('mobile'));

  const validatePolicy = async (_: RuleObject, value: boolean) => {
    return !value ? Promise.reject(t('sys.login.policyPlaceholder')) : Promise.resolve();
  };

  const validateConfirmPassword = (password: string) => {
    return async (_: RuleObject, value: string) => {
      if (!value) {
        return Promise.reject(t('sys.login.passwordPlaceholder'));
      }
      if (value !== password) {
        return Promise.reject(t('sys.login.diffPwd'));
      }
      return Promise.resolve();
    };
  };

  const getFormRules = computed((): { [k: string]: ValidationRule | ValidationRule[] } => {
    const accountFormRule = unref(getAccountFormRule);
    const passwordFormRule = unref(getPasswordFormRule);
    const smsFormRule = unref(getSmsFormRule);
    const mobileFormRule = unref(getMobileFormRule);
    const registerAccountRule = unref(getRegisterAccountRule);
    const registerMobileRule = unref(getRegisterMobileRule);

    const mobileRule = {
      code: smsFormRule,
      mobile: mobileFormRule,
    };
    switch (unref(currentState)) {
      // register form rules
      case LoginStateEnum.REGISTER:
        return {
          login: registerAccountRule,
          password: passwordFormRule,
          // mobile: registerMobileRule,
          // code: smsFormRule,
          confirmPassword: [{ validator: validateConfirmPassword(formData?.password), trigger: 'change' }],
          policy: [{ validator: validatePolicy, trigger: 'change' }],
          ...mobileRule,
          mobile: registerMobileRule,
        };

      // reset password form rules
      case LoginStateEnum.RESET_PASSWORD:
        return {
          account: accountFormRule,
          confirmPassword: [{ validator: validateConfirmPassword(formData?.password), trigger: 'change' }],
          ...mobileRule,
        };

      // mobile form rules
      case LoginStateEnum.MOBILE:
        return mobileRule;

      // login form rules
      default:
        return {
          account: accountFormRule,
          password: passwordFormRule,
        };
    }
  });
  return { getFormRules };
}

function createRule(message: string): ValidationRule[] {
  return [
    {
      required: true,
      message,
      trigger: 'change',
    },
  ];
}
function createRegisterAccountRule(type) {
  return [
    {
      validator: type == 'account' ? checkUsername : checkPhone,
      trigger: 'change',
    },
  ];
}

function checkUsername(_rule, value, _callback) {
  const { t } = useI18n();
  if (!value) {
    return Promise.reject(t('sys.login.accountPlaceholder'));
  } else {
    return new Promise((resolve, reject) => {
      accountService.checkExistUser({ 'login.equals': value }).then(exist => {
        !exist ? resolve(true) : reject('用户名已存在!');
      });
    });
  }
}
async function checkPhone(_rule, value, _callback) {
  const reg = /^1[3456789]\d{9}$/;
  if (!reg.test(value)) {
    return Promise.reject(new Error('请输入正确手机号'));
  } else {
    return new Promise((resolve, reject) => {
      accountService.checkExistUser({ 'mobile.equals': value }).then(exist => {
        !exist ? resolve(true) : reject('手机号已存在!');
      });
    });
  }
}

/**
 * 判断是否是OAuth2APP环境
 */
export function isOAuth2AppEnv() {
  return /wxwork|dingtalk/i.test(navigator.userAgent);
}

/**
 * 后台构造oauth2登录地址
 * @param source
 * @param tenantId
 */
export function sysOAuth2Login(source) {
  let url = `${window._CONFIG['domianURL']}/sys/thirdLogin/oauth2/${source}/login`;
  url += `?state=${encodeURIComponent(window.location.origin)}`;
  let tenantId = getAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID);
  if (tenantId) {
    url += `&tenantId=${tenantId}`;
  }
  window.location.href = url;
}
