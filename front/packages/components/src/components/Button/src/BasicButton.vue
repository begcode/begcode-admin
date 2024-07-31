<template>
  <Button v-bind="getBindValue" :class="getButtonClass" @click="onClick">
    <template #icon>
      <slot name="icon"></slot>
    </template>
    <template #default="data">
      <Icon v-if="preIcon" :icon="preIcon" :size="iconSize" />
      <slot v-bind="data || {}"></slot>
      <Icon :icon="postIcon" v-if="postIcon" :size="iconSize" />
    </template>
  </Button>
</template>
<script lang="ts" setup>
import { Button } from 'ant-design-vue';
import { computed, unref } from 'vue';
import Icon from '@/components/Icon/Icon.vue';
import { buttonProps } from './props';
import { useAttrs } from '@/hooks/vben';

defineOptions({
  name: 'BButton',
  extends: Button,
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
