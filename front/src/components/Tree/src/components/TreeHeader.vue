<template>
  <div :class="bem()" class="flex mb-1 px-2 py-1.5 items-center">
    <slot name="headerTitle" v-if="slots.headerTitle"></slot>
    <basic-title :helpMessage="helpMessage" v-if="!slots.headerTitle && title">
      {{ title }}
    </basic-title>
    <div class="flex items-center flex-1 cursor-pointer justify-self-stretch" v-if="search || toolbar">
      <div :class="getInputSearchCls" v-if="search">
        <a-input-search :placeholder="t('common.searchText')" size="small" allowClear v-model:value="searchValue" />
      </div>
      <a-dropdown @click.prevent v-if="toolbar">
        <Icon icon="ion:ellipsis-vertical" />
        <template #overlay>
          <a-menu @click="handleMenuClick">
            <template v-for="item in toolbarList" :key="item.value">
              <a-menu-item v-bind="{ key: item.value }">
                {{ item.label }}
              </a-menu-item>
              <a-menu-divider v-if="item.divider" />
            </template>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>
<script lang="ts" setup>
import type { MenuProps } from 'ant-design-vue';
import { useI18n } from '@/hooks/web/useI18nOut';
import { useDebounceFn } from '@vueuse/core';
import { createBEM } from '@/utils/bem';
import { ToolbarEnum } from '../types/tree.d';

const searchValue = ref('');

const [bem] = createBEM('tree-header');

const props = defineProps({
  helpMessage: {
    type: [String, Array] as PropType<string | string[]>,
    default: '',
  },
  title: {
    type: String,
    default: '',
  },
  toolbar: {
    type: Boolean,
    default: false,
  },
  checkable: {
    type: Boolean,
    default: false,
  },
  search: {
    type: Boolean,
    default: false,
  },
  searchText: {
    type: String,
    default: '',
  },
  checkAll: {
    type: Function,
    default: undefined,
  },
  expandAll: {
    type: Function,
    default: undefined,
  },
} as const);
const emit = defineEmits(['strictly-change', 'search']);

const slots = useSlots();
const { t } = useI18n();

const getInputSearchCls = computed(() => {
  const titleExists = slots.headerTitle || props.title;
  return [
    'mr-1',
    'w-full',
    {
      ['ml-5']: titleExists,
    },
  ];
});

const toolbarList = computed(() => {
  const { checkable } = props;
  const defaultToolbarList = [
    { label: t('component.tree.expandAll'), value: ToolbarEnum.EXPAND_ALL },
    {
      label: t('component.tree.unExpandAll'),
      value: ToolbarEnum.UN_EXPAND_ALL,
      divider: checkable,
    },
  ];

  return checkable
    ? [
        { label: t('component.tree.selectAll'), value: ToolbarEnum.SELECT_ALL },
        {
          label: t('component.tree.unSelectAll'),
          value: ToolbarEnum.UN_SELECT_ALL,
          divider: checkable,
        },
        ...defaultToolbarList,
        { label: t('component.tree.checkStrictly'), value: ToolbarEnum.CHECK_STRICTLY },
        { label: t('component.tree.checkUnStrictly'), value: ToolbarEnum.CHECK_UN_STRICTLY },
      ]
    : defaultToolbarList;
});

const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  switch (key) {
    case ToolbarEnum.SELECT_ALL:
      props.checkAll?.(true);
      break;
    case ToolbarEnum.UN_SELECT_ALL:
      props.checkAll?.(false);
      break;
    case ToolbarEnum.EXPAND_ALL:
      props.expandAll?.(true);
      break;
    case ToolbarEnum.UN_EXPAND_ALL:
      props.expandAll?.(false);
      break;
    case ToolbarEnum.CHECK_STRICTLY:
      emit('strictly-change', false);
      break;
    case ToolbarEnum.CHECK_UN_STRICTLY:
      emit('strictly-change', true);
      break;
  }
};

function emitChange(value?: string): void {
  emit('search', value);
}

const debounceEmitChange = useDebounceFn(emitChange, 200);

watch(
  () => searchValue.value,
  v => {
    debounceEmitChange(v);
  },
);

watch(
  () => props.searchText,
  v => {
    if (v !== searchValue.value) {
      searchValue.value = v;
    }
  },
);
</script>
<style>
.mr-1 {
  margin-right: 0.25rem;
}
.mb-1 {
  margin-bottom: 0.25rem;
}
.flex-1 {
  flex: 1 1 0;
}
.justify-self-stretch {
  justify-self: stretch;
}
.py-1\.5 {
  padding-top: 0.375rem;
  padding-bottom: 0.375rem;
}
.px-2 {
  padding-left: 0.5rem;
  padding-right: 0.5rem;
}
</style>
