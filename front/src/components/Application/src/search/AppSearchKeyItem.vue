<template>
  <span :class="$attrs.class">
    <Icon :icon="icon" />
  </span>
</template>
<script lang="ts" setup>
import { Icon } from '@begcode/components';
defineProps({
  icon: String,
});
</script>
