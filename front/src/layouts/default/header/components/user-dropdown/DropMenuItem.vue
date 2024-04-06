<template>
  <Menu.Item :key="itemKey">
    <span class="flex items-center">
      <Icon :icon="icon" class="mr-1" />
      <span>{{ text }}</span>
    </span>
  </Menu.Item>
</template>
<script lang="ts" setup>
import { computed, getCurrentInstance } from 'vue';
import { Menu } from 'ant-design-vue';
import { Icon, propTypes } from '@begcode/components';

defineOptions({ name: 'DropdownMenuItem' });

const props = defineProps({
  text: propTypes.string,
  icon: propTypes.string,
});

const instance = getCurrentInstance();
const itemKey = computed(() => instance?.vnode?.props?.key);
</script>
