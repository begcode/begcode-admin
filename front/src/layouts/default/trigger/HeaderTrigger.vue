<template>
  <span :class="[prefixCls, theme]" @click="toggleCollapsed"> <MenuUnfoldOutlined v-if="getCollapsed" /> <MenuFoldOutlined v-else /> </span>
</template>
<script lang="ts" setup>
import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons-vue';
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
