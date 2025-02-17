<template>
  <div :class="getClass" class="flex grid-items-center h-95%">
    <template v-if="canFullscreen">
      <a-tooltip v-if="fullScreen" :title="t('component.modal.restore')" placement="bottom">
        <FullscreenExitOutlined role="full" @click="handleFullScreen" />
      </a-tooltip>
      <a-tooltip v-else :title="t('component.modal.maximize')" placement="bottom">
        <FullscreenOutlined role="close" @click="handleFullScreen" />
      </a-tooltip>
    </template>
    <a-tooltip :title="t('component.modal.close')" placement="bottom">
      <CloseOutlined @click="handleCancel" />
    </a-tooltip>
  </div>
</template>
<script lang="ts" setup>
import { theme } from 'ant-design-vue';
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
<style lang="less">
@prefix-cls: ~'@{namespace}-basic-modal-close';
.@{prefix-cls} {
  display: flex;
  height: 95%;
  align-items: center;
  margin-top: -2px;
  > span {
    margin-left: 48px;
    font-size: 16px;
  }

  &--can-full {
    > span {
      margin-left: 12px;
    }
  }

  &:not(&--can-full) {
    > span:nth-child(1) {
      &:hover {
        font-weight: 700;
      }
    }
  }

  & span:nth-child(1) {
    display: inline-block;
    padding: 10px;

    &:hover {
      color: v-bind('token.colorPrimary');
    }
  }

  & span:last-child {
    padding: 10px 10px 10px 0;
    &:hover {
      color: v-bind('token.colorError');
    }
  }
}
</style>
