<template>
  <div :class="getClass" class="flex grid-items-center h-95%">
    <template v-if="canFullscreen">
      <Tooltip v-if="fullScreen" :title="t('component.modal.restore')" placement="bottom">
        <FullscreenExitOutlined role="full" @click="handleFullScreen" />
      </Tooltip>
      <Tooltip v-else :title="t('component.modal.maximize')" placement="bottom">
        <FullscreenOutlined role="close" @click="handleFullScreen" />
      </Tooltip>
    </template>
    <Tooltip :title="t('component.modal.close')" placement="bottom">
      <CloseOutlined @click="handleCancel" />
    </Tooltip>
  </div>
</template>
<script lang="ts" setup>
import { computed } from 'vue';
import { Tooltip, theme } from 'ant-design-vue';
import { FullscreenExitOutlined, FullscreenOutlined, CloseOutlined } from '@ant-design/icons-vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useI18n } from '@/hooks/web/useI18nOut';

defineOptions({ name: 'ModalClose' });

const props = defineProps({
  canFullscreen: { type: Boolean, default: true },
  fullScreen: { type: Boolean },
});

const emit = defineEmits(['cancel', 'fullscreen']);

const { prefixCls } = useDesign('basic-modal-close');
const { t } = useI18n();
const { useToken } = theme;
const { token } = useToken();

const getClass = computed(() => {
  return [
    prefixCls,
    `${prefixCls}--custom`,
    {
      [`${prefixCls}--can-full`]: props.canFullscreen,
    },
  ];
});

function handleCancel(e: Event) {
  emit('cancel', e);
}

function handleFullScreen(e: Event) {
  e?.stopPropagation();
  e?.preventDefault();
  emit('fullscreen');
}
</script>
<style>
.vben-basic-modal-close {
  display: flex;
  align-items: center;
  height: 95%;
}
.vben-basic-modal-close > span {
  margin-left: 48px;
  font-size: 16px;
}
.vben-basic-modal-close--can-full > span {
  margin-left: 12px;
}
.vben-basic-modal-close:not(.vben-basic-modal-close--can-full) > span:nth-child(1):hover {
  font-weight: 700;
}
.vben-basic-modal-close span:nth-child(1) {
  display: inline-block;
  padding: 10px;
}
.vben-basic-modal-close span:nth-child(1):hover {
  color: v-bind('token.colorPrimary');
}
.vben-basic-modal-close span:last-child:hover {
  color: v-bind('token.colorError');
}
.grid-items-center {
  align-items: center;
}
.h-95\% {
  height: 95%;
}
</style>
