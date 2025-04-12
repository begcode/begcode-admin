<template>
  <Dropdown
    :dropMenuList="getDropMenuList"
    :trigger="getTrigger"
    placement="bottomRight"
    @menu-event="handleMenuEvent"
    :overlayClassName="prefixCls"
  >
    <div :class="`${prefixCls}__info`" @contextmenu="handleContext" v-if="getIsTabs">
      <span class="ml-1">{{ getTitle }}</span>
    </div>
    <span :class="`${prefixCls}__extra-quick`" v-else @click="handleContext">
      <Icon icon="ion:chevron-down" :size="18" />
    </span>
  </Dropdown>
</template>
<script lang="ts" setup>
import type { RouteLocationNormalized } from 'vue-router';
import { Dropdown } from '@/components/Dropdown';
import { useDesign } from '@/hooks/web/useDesign';
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
  if (meta?.title) {
    if (meta.title.includes('.')) {
      return t(meta.title);
    }
    return meta.title;
  } else {
    let title = localeStore.getPathTitle(fullPath);
    if (title) {
      return title;
    } else {
      return name;
    }
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
<style lang="less">
@prefix-cls: ~'@{namespace}-multiple-tabs-content';
.@{prefix-cls} {
  .ant-dropdown-menu-item {
    .ant-dropdown-menu-title-content {
      .anticon {
        font-size: 14px !important;
      }
      span:not(.anticon) {
        margin-left: 6px;
      }
    }
  }
}
</style>
