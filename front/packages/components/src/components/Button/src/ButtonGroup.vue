<script setup lang="ts">
import { ref, toRaw } from 'vue';
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

const rawRow = toRaw(props.row);
const showButtons = ref<any>([]);
const dropdownButtons = ref<any>([]);
const allButtons = props.buttons.filter(rowOperation => !rowOperation.disabled && !(rowOperation.hide && rowOperation.hide(rawRow)));
if (allButtons.length > props.showMaxNum) {
  showButtons.value = allButtons.slice(0, props.showMaxNum);
  dropdownButtons.value = allButtons.slice(props.showMaxNum);
} else {
  showButtons.value = allButtons;
}

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
</script>

<template>
  <Space :size="spaceSize" v-if="allButtons.length">
    <Button
      v-for="operation in showButtons"
      :type="operation.type || 'link'"
      :title="getButtonTitle(operation.title, rawRow)"
      :status="operation.primary"
      @click="buttonClick(operation.name, rawRow)"
      class="padding-0"
    >
      <Icon icon="ant-design:save-outlined" #icon v-if="operation.type !== 'link'" />
      <span v-else>{{ getButtonTitle(operation.title, rawRow) }}</span>
    </Button>
    <Dropdown v-if="dropdownButtons && dropdownButtons.length">
      <template #overlay>
        <Menu @click="menuClick($event, rawRow)">
          <MenuItem :key="operation.name" v-for="operation in dropdownButtons">
            <Icon :icon="operation.icon" v-if="operation.icon" />
            <span v-if="operation.type === 'link'">{{ getButtonTitle(operation.title, rawRow) }}</span>
          </MenuItem>
        </Menu>
      </template>
      <a class="ant-dropdown-link" @click.prevent>
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
