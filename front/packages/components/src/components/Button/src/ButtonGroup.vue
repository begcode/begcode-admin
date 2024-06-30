<script setup lang="ts">
import { ref, toRaw, watchEffect } from 'vue';
import { Space, Dropdown, Menu, MenuItem } from 'ant-design-vue';
import Icon from '@/components/Icon/Icon.vue';
import Button from './BasicButton.vue';

defineOptions({
  name: 'BButtonGroup',
  inheritAttrs: false,
});
const props = defineProps({
  showMaxNum: {
    type: Number,
    default: 3,
  },
  buttons: {
    type: Array<any>,
    default: () => [],
  },
  row: {
    type: Object,
    default: () => ({}),
  },
  spaceSize: {
    type: Number,
    default: 4,
  },
});
const emit = defineEmits(['click']);

const showButtons = ref<any>([]);
const dropdownButtons = ref<any>([]);
watchEffect(() => {
  const rawRow = toRaw(props.row);
  const allButtons = props.buttons.filter(rowOperation => !rowOperation.disabled && !(rowOperation.hide && rowOperation.hide(rawRow)));
  if (allButtons.length > props.showMaxNum) {
    showButtons.value = allButtons.slice(0, props.showMaxNum);
    dropdownButtons.value = allButtons.slice(props.showMaxNum);
  } else {
    showButtons.value = allButtons;
  }
});

const getButtonTitle = (title: any, row: any) => {
  if (typeof title === 'function') {
    return title(row);
  }
  return title;
};

const buttonClick = (buttonName: any, row: any) => {
  emit('click', { name: buttonName, data: row });
};
const menuClick = ({ key }, row) => {
  buttonClick(key, row);
};
const getElementCount = () => {
  return showButtons.value.length + (dropdownButtons.value.length ? 1 : 0);
};

defineExpose({
  getElementCount,
});
</script>

<template>
  <Space :size="spaceSize" v-if="showButtons.length">
    <Button
      v-for="operation in showButtons"
      :type="operation.type || 'link'"
      :title="getButtonTitle(operation.title, row)"
      :status="operation.primary"
      @click="buttonClick(operation.name, row)"
      class="padding-0"
      v-bind="operation.attrs"
    >
      <Icon icon="ant-design:save-outlined" #icon v-if="operation.icon" />
      <span v-else>{{ getButtonTitle(operation.title, row) }}</span>
    </Button>
    <Dropdown v-if="dropdownButtons && dropdownButtons.length">
      <template #overlay>
        <Menu @click="menuClick($event, row)">
          <MenuItem :key="operation.name" v-for="operation in dropdownButtons" v-bind="operation.attrs">
            <Icon :icon="operation.icon" v-if="operation.icon" />
            <span v-if="operation.type === 'link'">{{ getButtonTitle(operation.title, row) }}</span>
          </MenuItem>
        </Menu>
      </template>
      <a class="ant-dropdown-link" @click.prevent data-cy="buttonGroupDropdown">
        &nbsp;
        <Icon icon="ant-design:down-outlined" />
      </a>
    </Dropdown>
  </Space>
</template>
<style scoped>
.padding-0 {
  padding: 0;
}
</style>
