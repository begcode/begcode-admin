<template>
  <a-upload name="file" :showUploadList="false">
    <a-button :class="getButtonClass" v-bind="getBindValue">
      <template #default="data">
        <Icon :icon="preIcon" v-if="preIcon" :size="iconSize" />
        <slot v-bind="data || {}"></slot>
        <Icon :icon="postIcon" v-if="postIcon" :size="iconSize" />
      </template>
    </a-button>
  </a-upload>
</template>
<script lang="ts" setup>
import { buttonProps } from './props';
import { useAttrs } from '@/hooks/vben/useAttrs';

defineOptions({
  name: 'UploadButton',
  inheritAttrs: false,
});

const props = defineProps(buttonProps);

// get component class
const attrs = useAttrs({ excludeDefaultKeys: false });
const getButtonClass = computed(() => {
  const { color, disabled } = props;
  return [
    {
      [`ant-btn-${color}`]: !!color,
      [`is-disabled`]: disabled,
    },
  ];
});

// get inherit binding value
const getBindValue = computed(() => ({ ...unref(attrs), ...props }));
</script>
