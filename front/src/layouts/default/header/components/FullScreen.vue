<template>
  <a-tooltip :title="getTitle" placement="bottom" :mouseEnterDelay="0.5">
    <span @click="toggle">
      <Icon icon="ant-design:fullscreen-outlined" v-if="!isFullscreen" />
      <Icon icon="ant-design:fullscreen-exit-outlined" v-else />
    </span>
  </a-tooltip>
</template>
<script lang="ts" setup>
import { useFullscreen } from '@vueuse/core';
import { useI18n } from '@/hooks/web/useI18n';

defineOptions({ name: 'FullScreen' });

const { t } = useI18n();
const { toggle, isFullscreen } = useFullscreen();
// 重新检查全屏状态
isFullscreen.value = !!(
  document.fullscreenElement ||
  document.webkitFullscreenElement ||
  document.mozFullScreenElement ||
  document.msFullscreenElement
);

const getTitle = computed(() => {
  return unref(isFullscreen) ? t('layout.header.tooltipExitFull') : t('layout.header.tooltipEntryFull');
});
</script>
