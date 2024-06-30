<template>
  <span :class="`${prefixCls}__extra-fold`" @click="button.click" v-for="button in shortcutButtons">
    <Tooltip :title="button.title" placement="bottomRight" color="#108ee9">
      <Icon :icon="button.icon" :size="18" />
    </Tooltip>
  </span>
</template>
<script lang="ts" setup>
import { Icon } from '@begcode/components';
import { Tooltip } from 'ant-design-vue';

import { useDesign } from '@begcode/components';

defineOptions({ name: 'ShortcutButton' });
const props = defineProps({
  shortcutButtons: {
    type: Array<any>,
    default: () => [],
  },
});

const { prefixCls } = useDesign('multiple-tabs-content');
</script>
