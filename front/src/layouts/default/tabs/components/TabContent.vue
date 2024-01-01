<template>
  <Dropdown :dropMenuList="getDropMenuList" :trigger="getTrigger" placement="bottomRight" @menu-event="handleMenuEvent">
    <div :class="`${prefixCls}__info`" @contextmenu="handleContext" v-if="getIsTabs">
      <span v-if="showPrefixIcon" :class="`${prefixCls}__prefix-icon`" @click="handleContext">
        <Icon :icon="prefixIconType" />
      </span>
      <span class="ml-1">{{ getTitle }}</span>
    </div>
    <span :class="`${prefixCls}__extra-quick`" v-else @click="handleContext">
      <Icon icon="ion:chevron-down" />
    </span>
  </Dropdown>
</template>
<script lang="ts" setup>
import type { PropType } from 'vue';
import type { RouteLocationNormalized } from 'vue-router';
import { computed, unref } from 'vue';
import { Dropdown, Icon, useDesign } from '@begcode/components';
import { TabContentProps } from '../types';
import { TabsThemeEnum } from '@/enums/appEnum';
import { useI18n } from '@/hooks/web/useI18n';
import { useTabDropdown } from '../useTabDropdown';
import { useMultipleTabSetting } from '@/hooks/setting/useMultipleTabSetting';
import { useLocaleStore } from '@/store/modules/locale';

defineOptions({ name: 'TabContent' });

const props = defineProps({
  tabItem: {
    type: Object as PropType<RouteLocationNormalized>,
    default: null,
  },
  isExtra: Boolean,
});

const { prefixCls } = useDesign('multiple-tabs-content');
const { t } = useI18n();

const localeStore = useLocaleStore();
const getTitle = computed(() => {
  const { tabItem: { meta, fullPath, name } = {} } = props;
  let title = localeStore.getPathTitle(fullPath);
  if (title) {
    return title;
  } else if (meta) {
    title = meta.title;
    return title ? t(title) : '';
  } else {
    return name;
  }
});

const prefixIconType = computed(() => {
  if (props.tabItem.meta.icon) {
    return props.tabItem.meta.icon;
  } else if (props.tabItem.path === '/dashboard/analysis') {
    // 当是首页时返回 home 图标 TODO 此处可能需要动态判断首页路径
    return 'ant-design:home-outlined';
  } else {
    return 'ant-design:code';
  }
});

const getIsTabs = computed(() => !props.isExtra);

const getTrigger = computed((): ('contextmenu' | 'click' | 'hover')[] => (unref(getIsTabs) ? ['contextmenu'] : ['click']));

const { getDropMenuList, handleMenuEvent, handleContextMenu } = useTabDropdown(props as TabContentProps, getIsTabs);

function handleContext(e) {
  props.tabItem && handleContextMenu(props.tabItem)(e);
}

const { getTabsTheme } = useMultipleTabSetting();
// 是否显示图标
const showPrefixIcon = computed(() => unref(getTabsTheme) === TabsThemeEnum.SMOOTH);
</script>
