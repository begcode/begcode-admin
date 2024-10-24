<template>
  <a-button v-bind="$attrs" :disabled="isStart" @click="handleStart" :loading="loading">
    {{ getButtonText }}
  </a-button>
  <CaptchaModal @register="captchaRegisterModal" @ok="handleStart" />
</template>
<script lang="ts" setup>
import { useCountdown } from './useCountdown';
import { useModal } from '@/components/Modal';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
const CaptchaModal = createAsyncComponent(() => import('@/components/Captcha/src/CaptchaModal.vue'));
const [captchaRegisterModal, { openModal: openCaptchaModal }] = useModal();
import { useI18n } from '@/hooks/web/useI18nOut';

defineOptions({ name: 'CountButton' });

const props = defineProps({
  value: { type: [Object, Number, String, Array] },
  count: { type: Number, default: 60 },
  beforeStartFunc: {
    type: Function as PropType<() => Promise<boolean>>,
    default: null,
  },
  openCaptchaModalCode: {
    type: Number,
    default: 'NEED_CAPTCHA',
  },
});

const { t } = useI18n();
const loading = ref(false);

const { currentCount, isStart, start, reset } = useCountdown(props.count);

const getButtonText = computed(() => {
  return !unref(isStart) ? t('component.countdown.normalText') : t('component.countdown.sendText', [unref(currentCount)]);
});

watchEffect(() => {
  props.value === undefined && reset();
});

/**
 * @description: Judge whether there is an external function before execution, and decide whether to start after execution
 */
async function handleStart() {
  const { beforeStartFunc } = props;
  if (beforeStartFunc && _isFunction(beforeStartFunc)) {
    loading.value = true;
    try {
      const canStart = await beforeStartFunc().catch(res => {
        if (res.code === props.openCaptchaModalCode) {
          openCaptchaModal(true, {});
        }
      });
      canStart && start();
    } finally {
      loading.value = false;
    }
  } else {
    start();
  }
}
// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
</script>
