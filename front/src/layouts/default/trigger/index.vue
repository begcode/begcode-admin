<template>
  <SiderTrigger v-if="sider" />
  <HeaderTrigger v-else :theme="theme" />
</template>
<script lang="ts" setup>
import SiderTrigger from './SiderTrigger.vue';
import HeaderTrigger from './HeaderTrigger.vue';
defineProps({
  sider: {
    type: Boolean,
    default: true,
  },
  theme: {
    type: String as PropType<'light' | 'dark'>,
  },
});
</script>
