<template>
  <span :class="[prefixCls, theme]" @click="toggleCollapsed">
    <Icon :icon="getCollapsed ? 'ant-design:menu-unfold-outlined' : 'ant-design:menu-fold-outlined'" />
  </span>
</template>
<script lang="ts" setup>
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useDesign } from '@/hooks/web/useDesign';

defineProps({
  theme: {
    type: String as PropType<'light' | 'dark'>,
  },
});

const { getCollapsed, toggleCollapsed } = useMenuSetting();
const { prefixCls } = useDesign('layout-header-trigger');
</script>
