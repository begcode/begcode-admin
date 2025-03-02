<template>
  <div @click.stop="toggleCollapsed">
    <Icon :icon="getCollapsed ? 'ant-design:double-right-outlined' : 'ant-design:double-left-outlined'" />
  </div>
</template>
<script lang="ts" setup>
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';

const { getCollapsed, toggleCollapsed } = useMenuSetting();
</script>
