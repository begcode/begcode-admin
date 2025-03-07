<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <a-form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef" v-show="getShow" @keypress.enter="handleLogin">
    <a-form-item name="account" class="enter-x">
      <a-input
        size="large"
        v-model:value="formData.account"
        :placeholder="t('sys.login.userName')"
        class="fix-auto-fill"
        data-cy="username"
      />
    </a-form-item>
    <a-form-item name="password" class="enter-x">
      <a-input-password
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="t('sys.login.password')"
        data-cy="password"
      />
    </a-form-item>
    <!--验证码-->
    <a-row class="enter-x">
      <a-col :span="12">
        <a-form-item name="inputCode" class="enter-x">
          <a-input size="large" v-model:value="formData.inputCode" :placeholder="t('sys.login.inputCode')" style="min-width: 100px" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item :style="{ 'text-align': 'right', 'margin-left': '20px' }" class="enter-x">
          <img
            v-if="randCodeData.requestCodeSuccess"
            style="margin-top: 2px; max-width: initial"
            :src="randCodeData.randCodeImage"
            @click="handleChangeCheckCode"
          />
          <img v-else style="margin-top: 2px; max-width: initial" :src="checkcodePng" @click="handleChangeCheckCode" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="enter-x">
      <a-col :span="12">
        <a-form-item>
          <!-- No logic, you need to deal with it yourself -->
          <a-checkbox v-model:checked="rememberMe" size="small">
            {{ t('sys.login.rememberMe') }}
          </a-checkbox>
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <a-button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ t('sys.login.forgetPassword') }}
          </a-button>
        </a-form-item>
      </a-col>
    </a-row>

    <a-form-item class="enter-x">
      <a-button type="primary" size="large" block @click="handleLogin" :loading="loading" data-cy="submit">
        {{ t('sys.login.loginButton') }}
      </a-button>
      <!-- <a-button size="large" class="mt-4 enter-x" block @click="handleRegister">
        {{ t('sys.login.registerButton') }}
      </a-button> -->
    </a-form-item>
    <a-row class="enter-x" :gutter="[16, 16]">
      <a-col :md="8" :xs="24">
        <a-button block @click="setLoginState(LoginStateEnum.MOBILE)">
          {{ t('sys.login.mobileSignInFormTitle') }}
        </a-button>
      </a-col>
      <a-col :md="8" :xs="24">
        <a-button block @click="setLoginState(LoginStateEnum.QR_CODE)">
          {{ t('sys.login.qrSignInFormTitle') }}
        </a-button>
      </a-col>
      <a-col :md="8" :xs="24">
        <a-button block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </a-button>
      </a-col>
    </a-row>

    <a-divider class="enter-x">{{ t('sys.login.otherSignIn') }}</a-divider>

    <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <a @click="onThirdLogin('github')" title="github"><Icon icon="ant-design:github-filled" /></a>
      <a @click="onThirdLogin('wechat_enterprise')" title="企业微信"><Icon class="item-icon" icon="icon-qiyeweixin3|font" /></a>
      <a @click="onThirdLogin('dingtalk')" title="钉钉"><Icon icon="ant-design:dingtalk-circle-filled" /></a>
      <a @click="onThirdLogin('wechat_open')" title="微信"><Icon icon="ant-design:wechat-filled" /></a>
    </div>
  </a-form>
  <!-- 第三方登录相关弹框 -->
  <ThirdModal ref="thirdModalRef"></ThirdModal>
</template>
<script lang="ts" setup>
import LoginFormTitle from './LoginFormTitle.vue';
import ThirdModal from './ThirdModal.vue';

import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';

import { useUserStore } from '@/store/modules/user';
import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
import { useDesign } from '@/hooks/web/useDesign';
import { getCodeInfo } from '@/api-service/sys/user';
import checkcodePng from '@/assets/images/checkcode.png';
//import { onKeyStroke } from '@vueuse/core';
// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const { t } = useI18n();
const { notification, createErrorModal } = useMessage();
const { prefixCls } = useDesign('login');
const userStore = useUserStore();

const { setLoginState, getLoginState } = useLoginState();
const { getFormRules } = useFormRules();

const formRef = ref();
const thirdModalRef = ref();
const loading = ref(false);
const rememberMe = ref(false);

const formData = reactive({
  account: 'admin',
  password: 'admin',
  inputCode: '',
});
const randCodeData = reactive({
  randCodeImage: '',
  requestCodeSuccess: false,
  checkKey: null,
});

const { validForm } = useFormValid(formRef);

//onKeyStroke('Enter', handleLogin);

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

async function handleLogin() {
  const data = await validForm();
  if (!data) return;
  try {
    loading.value = true;
    const userInfo = await userStore.login(
      toRaw({
        password: data.password,
        username: data.account,
        captcha: data.inputCode,
        checkKey: randCodeData.checkKey,
        mode: 'none', // 不要默认的错误提示
      }),
    );
    if (userInfo) {
      notification.success({
        message: t('sys.login.loginSuccessTitle'),
        description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.firstName}`,
        duration: 3,
      });
    }
  } catch (error) {
    console.log('error', error);
    notification.error({
      message: t('sys.api.errorTip'),
      description: error.message || t('sys.api.networkExceptionMsg'),
      duration: 3,
    });
  } finally {
    loading.value = false;
    handleChangeCheckCode();
  }
}

function handleChangeCheckCode() {
  formData.inputCode = '';
  //TODO 兼容mock和接口，暂时这样处理
  randCodeData.checkKey = new Date().getTime();
  getCodeInfo(randCodeData.checkKey).then(data => {
    randCodeData.randCodeImage = data;
    randCodeData.requestCodeSuccess = true;
  });
}

/**
 * 第三方登录
 * @param type
 */
function onThirdLogin(type) {
  thirdModalRef.value.onThirdLogin(type);
}
//初始化验证码
onMounted(() => {
  handleChangeCheckCode();
});
</script>
