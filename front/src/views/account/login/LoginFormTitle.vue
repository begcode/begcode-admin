<template>
  <h2 class="mb-3 text-2xl font-bold text-center xl:text-3xl enter-x xl:text-left" data-cy="loginTitle">
    {{ getFormTitle }}
  </h2>
</template>
<script lang="ts" setup>
import { useI18n } from '@/hooks/web/useI18n';
import { LoginStateEnum, useLoginState } from './useLogin';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const { t } = useI18n();

const { getLoginState } = useLoginState();

const getFormTitle = computed(() => {
  const titleObj = {
    [LoginStateEnum.RESET_PASSWORD]: t('sys.login.forgetFormTitle'),
    [LoginStateEnum.LOGIN]: t('sys.login.signInFormTitle'),
    [LoginStateEnum.REGISTER]: t('sys.login.signUpFormTitle'),
    [LoginStateEnum.MOBILE]: t('sys.login.mobileSignInFormTitle'),
    [LoginStateEnum.QR_CODE]: t('sys.login.qrSignInFormTitle'),
  };
  return titleObj[unref(getLoginState)];
});
</script>
