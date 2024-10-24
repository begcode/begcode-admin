<template>
  <a-menu-item :key="item.path" :title="item.title">
    <MenuItemContent v-bind="$props" :item="item" />
  </a-menu-item>
</template>
<script lang="ts" setup>
import { itemProps } from '../props';
import MenuItemContent from './MenuItemContent.vue';

defineOptions({ name: 'BasicMenuItem' });

defineProps(itemProps);
</script>
