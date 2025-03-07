<template>
  <a-input
    :style="{ width }"
    :placeholder="t('component.icon.placeholder')"
    :class="prefixCls"
    v-model:value="currentSelect"
    @click="triggerPopover"
    :allowClear="props.allowClear"
    :readonly="props.readonly"
  >
    <template #addonAfter>
      <a-popover placement="bottomLeft" trigger="click" v-model="visible" :overlayClassName="`${prefixCls}-popover`">
        <template #title>
          <div class="flex justify-between">
            <a-input
              :placeholder="t('component.icon.search')"
              v-model:value="searchIconValue"
              @change="debounceHandleSearchChange"
              allowClear
            />
          </div>
        </template>

        <template #content>
          <div v-if="getPaginationList.length">
            <ScrollContainer class="border border-solid border-t-0">
              <ul class="flex flex-wrap px-2 pr-0">
                <li
                  v-for="icon in getPaginationList"
                  :key="icon"
                  :class="currentSelect === icon ? 'border border-primary' : ''"
                  class="p-2 w-1/8 cursor-pointer mr-1 mt-1 flex justify-center items-center border border-solid hover:border-primary"
                  @click="handleClick(icon)"
                  :title="icon"
                >
                  <!-- <Icon :icon="icon" :prefix="prefix" /> -->
                  <SvgIcon v-if="isSvgMode" :name="icon" />
                  <Icon :icon="icon" v-else />
                </li>
              </ul>
            </ScrollContainer>
            <div class="flex py-2 items-center justify-center" v-if="getTotal >= pageSize">
              <a-pagination
                v-model:current="current"
                :page-size-options="pageSizeOptions"
                :pageSize="pageSize"
                :total="getTotal"
                @change="handlePageChange"
                showLessItems
                size="small"
              />
            </div>
          </div>
          <template v-else>
            <div class="p-5"><a-empty /></div>
          </template>
        </template>

        <div ref="trigger">
          <span v-if="isSvgMode && currentSelect" class="cursor-pointer px-2 py-1 flex items-center">
            <SvgIcon :name="currentSelect" />
          </span>
          <Icon v-else :icon="currentSelect || 'ion:apps-outline'" class="cursor-pointer px-2 py-1" />
        </div>
      </a-popover>
    </template>
  </a-input>
</template>
<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign';
import { ScrollContainer } from '@/components/Container';
import Icon from '../Icon.vue';
import SvgIcon from './SvgIcon.vue';

import iconsData from '../data/icons.data';
import { usePagination } from '@/hooks/web/usePagination';
import { useDebounceFn } from '@vueuse/core';
// @ts-ignore
import svgIcons from 'virtual:svg-icons-names';
import { copyText } from '@/utils/copyTextToClipboard';
import { useI18n } from '@/hooks/web/useI18nOut';

// Don't inherit FormItem disabled、placeholder...
defineOptions({
  name: 'IconPicker',
  inheritAttrs: false,
});

const { t } = useI18n();

function getIcons() {
  const prefix = iconsData.prefix;
  return iconsData.icons.map(icon => `${prefix}:${icon}`);
}

function getSvgIcons() {
  return svgIcons.map((icon: string) => icon.replace('icon-', ''));
}

export interface Props {
  value?: string;
  width?: string;
  pageSize?: number;
  copy?: boolean;
  mode?: 'svg' | 'iconify';
  disabled?: boolean;
  clearSelect?: boolean;
  allowClear?: boolean;
  readonly?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  value: '',
  width: '100%',
  pageSize: 140,
  copy: false,
  mode: 'iconify',
  disabled: true,
  clearSelect: false,
  allowClear: true,
  readonly: false,
});

const emit = defineEmits(['change', 'update:value']);

const isSvgMode = props.mode === 'svg';
const icons = isSvgMode ? getSvgIcons() : getIcons();

const currentSelect = ref('');
const visible = ref(false);
const currentList = ref(icons);
const trigger = ref<HTMLDivElement>();
const triggerPopover = () => {
  if (trigger.value) {
    trigger.value.click();
  }
};
const { prefixCls } = useDesign('icon-picker');

const debounceHandleSearchChange = useDebounceFn(handleSearchChange, 100);
//页数
const current = ref<number>(1);
//每页条数
const pageSize = ref<number>(140);
//下拉分页显示
const pageSizeOptions = ref<any>(['10', '20', '50', '100', '140']);
//下拉搜索值
const searchIconValue = ref<string>('');

const { getPaginationList, getTotal, setCurrentPage, setPageSize } = usePagination(currentList, pageSize.value);

watchEffect(() => {
  currentSelect.value = props.value;
});

watch(
  () => currentSelect.value,
  v => {
    emit('update:value', v);
    emit('change', v);
  },
);

function handlePageChange(page: number, size: number) {
  current.value = page;
  pageSize.value = size;
  setPageSize(size);
  setCurrentPage(page);
}

function handleClick(icon: string) {
  if (props.clearSelect) {
    if (currentSelect.value === icon) {
      currentSelect.value = '';
    } else {
      currentSelect.value = icon;
    }
  } else {
    currentSelect.value = icon;
    if (props.copy) {
      copyText(icon, t('component.icon.copy'));
    }
  }
}

function handleSearchChange(e: Event) {
  const value = (e.target as any).value;
  setCurrentPage(1);
  current.value = 1;
  if (!value) {
    currentList.value = icons;
    return;
  }
  currentList.value = icons.filter(item => item.includes(value));
}
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-icon-picker';

.@{prefix-cls} {
  // 输入框手势图标
  .ant-input:not([disabled]) {
    cursor: pointer;
  }

  .ant-input-group-addon {
    padding: 0;
  }

  &-popover {
    width: 400px;

    .ant-popover-inner-content {
      padding: 0;
    }

    .scrollbar {
      height: 220px;
    }
  }
}
</style>
