<template>
  <a-menu-item :key="itemKey">
    <span class="flex items-center">
      <Icon :icon="icon" class="mr-1" />
      <span>{{ text }}</span>
    </span>
  </a-menu-item>
</template>
<script lang="ts" setup>
defineOptions({ name: 'DropdownMenuItem' });

const props = defineProps({
  text: {
    type: String,
  },
  icon: {
    type: String,
  },
});

const instance = getCurrentInstance();
const itemKey = computed(() => instance?.vnode?.props?.key);
</script>
