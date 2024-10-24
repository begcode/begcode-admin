<template>
  <a-result status="success" title="更改密码成功" :sub-title="getSubTitle">
    <template #extra>
      <a-button key="console" type="primary" @click="finish"> 返回登录 </a-button>
    </template>
  </a-result>
</template>
<script lang="ts" setup>
import { useI18n } from '@/hooks/web/useI18n';
import { useLoginState } from '../login/useLogin';
import { useCountdown } from '@/components/CountDown';
import { useUserStore } from '@/store/modules/user';

defineOptions({
  name: 'step3',
});

const props = defineProps({
  accountInfo: {
    type: Object,
    default: () => ({}),
  },
  count: {
    type: Number,
    default: 5,
  },
});

const emit = defineEmits(['finish']);

const { t } = useI18n();
const { accountInfo } = props;
const userStore = useUserStore();
const { handleBackLogin } = useLoginState();

const { currentCount, start } = useCountdown(props.count);
const getSubTitle = computed(() => {
  return t('sys.login.subTitleText', [unref(currentCount)]);
});
/**
 * 倒计时
 */
watchEffect(() => {
  if (unref(currentCount) === 1) {
    setTimeout(() => {
      finish();
    }, 500);
  }
});

/**
 * 结束回调
 */
function finish() {
  userStore.logout(false);
  handleBackLogin();
  emit('finish');
}

onMounted(() => {
  start();
});
</script>
