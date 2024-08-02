<template>
  <Dropdown v-if="syncToApp && syncToLocal">
    <Button type="primary" preIcon="ant-design:sync-outlined">同步{{ name }}</Button>
    <template #overlay>
      <Menu @click="handleMenuClick">
        <MenuItem v-if="syncToApp" key="to-app">同步到{{ name }}</MenuItem>
        <MenuItem v-if="syncToLocal" key="to-local">同步到本地</MenuItem>
      </Menu>
    </template>
  </Dropdown>
  <Button v-else-if="syncToApp" type="primary" preIcon="ant-design:sync-outlined" @click="handleMenuClick({ key: 'to-app' })"
    >同步{{ name }}</Button
  >
  <Button v-else type="primary" preIcon="ant-design:sync-outlined" @click="handleMenuClick({ key: 'to-local' })"
    >同步{{ name }}到本地</Button
  >
</template>

<script lang="ts" setup>
import { Dropdown, Button, Menu, MenuItem } from 'ant-design-vue';
/* JThirdAppButton 的子组件，不可单独使用 */

const props = defineProps({
  type: String,
  name: String,
  syncToApp: Boolean,
  syncToLocal: Boolean,
});
// 声明Emits
const emit = defineEmits(['to-app', 'to-local']);

function handleMenuClick(event) {
  emit(event.key, { type: props.type });
}
</script>

<style scoped></style>
