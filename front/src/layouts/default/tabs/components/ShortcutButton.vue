<template>
  <span :class="`${prefixCls}__extra-fold`" @click="button.click" v-for="button in shortcutButtons">
    <a-tooltip :title="button.title" placement="bottomRight" color="#108ee9">
      <Icon :icon="button.icon" :size="18" />
    </a-tooltip>
  </span>
</template>
<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign';

defineOptions({ name: 'ShortcutButton' });
const props = defineProps({
  shortcutButtons: {
    type: Array<any>,
    default: () => [],
  },
});

const { prefixCls } = useDesign('multiple-tabs-content');
</script>
