<template>
  <SiderTrigger v-if="sider" />
  <HeaderTrigger v-else :theme="theme" />
</template>
<script lang="ts" setup>
import { propTypes } from '@begcode/components';
import SiderTrigger from './SiderTrigger.vue';
import HeaderTrigger from './HeaderTrigger.vue';
defineProps({
  sider: propTypes.bool.def(true),
  theme: propTypes.oneOf(['light', 'dark']),
});
</script>
