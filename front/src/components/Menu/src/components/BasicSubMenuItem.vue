<template>
  <BasicMenuItem v-if="!menuHasChildren(item) && getShowMenu" v-bind="$props" />
  <Menu.SubMenu v-if="menuHasChildren(item) && getShowMenu" :class="[theme]" :key="`submenu-${item.path}`" :popupClassName="prefixCls">
    <template #title>
      <MenuItemContent v-bind="$props" :item="item" />
    </template>

    <template v-for="childrenItem in item.children || []" :key="childrenItem.path">
      <BasicSubMenuItem v-bind="$props" :item="childrenItem" />
    </template>
  </Menu.SubMenu>
</template>
<script lang="ts" setup>
import type { Menu as MenuType } from '@/router/types';
import { computed, watch } from 'vue';
import { Menu } from 'ant-design-vue';
import { useDesign } from '@begcode/components';
import { checkChildrenHidden } from '@/utils/common/compUtils';
import { itemProps } from '../props';
import BasicMenuItem from './BasicMenuItem.vue';
import MenuItemContent from './MenuItemContent.vue';
import { useBasicRootMenuContext } from '../useBasicMenuContext';
import { useLocaleStore } from '@/store/modules/locale';
import { getMenus } from '@/router/menus';

defineOptions({ name: 'BasicSubMenuItem', isSubMenu: true });

const props = defineProps(itemProps);

const { prefixCls } = useDesign('basic-subMenu');
const { menuState } = useBasicRootMenuContext();
const localeStore = useLocaleStore();

const getShowMenu = computed(() => !props.item.meta?.hideMenu);

function menuHasChildren(menuTreeItem: MenuType): boolean {
  return (
    !menuTreeItem.meta?.hideChildrenInMenu &&
    Reflect.has(menuTreeItem, 'children') &&
    !!menuTreeItem.children &&
    menuTreeItem.children.length > 0
  );
}

const getMatchingRouterName = (menus, path) => {
  for (let i = 0, len = menus.length; i < len; i++) {
    const item = menus[i];
    if (item.path === path && !item.redirect && !item.paramPath) {
      return item.meta?.title;
    } else if (item.children?.length) {
      const result = getMatchingRouterName(item.children, path);
      if (result) {
        return result;
      }
    }
  }
  return '';
};

watch(
  () => menuState.selectedKeys,
  async value => {
    if (value.length && value.includes(props.item.path)) {
      const menus = await getMenus();
      const getTitle = getMatchingRouterName(menus, props.item.path);
      localeStore.setPathTitle(props.item.path, getTitle ? getTitle : props.item.name);
    }
  },
  { immediate: true },
);
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-basic-subMenu';
html[data-theme='light'] {
  .@{prefix-cls}.ant-menu-dark {
    background-color: var(--header-bg-color);
    color: rgba(255, 255, 255, 0.9);
    &.ant-menu-submenu {
      > .ant-menu {
        background-color: var(--header-bg-color);
      }
    }
    .ant-menu-item-selected {
      background-color: var(--header-active-menu-bg-color);
    }
  }
  .ant-menu-submenu-placement-bottomLeft.ant-menu-dark.ant-menu-submenu-popup {
    background-color: var(--header-bg-color);
    &.ant-menu-dark.ant-menu-submenu > .ant-menu {
      background-color: var(--header-bg-color);
      color: rgba(255, 255, 255, 0.9);
    }
  }
}
html[data-theme='dark'] {
  @bgcolor: #212121;
  .@{prefix-cls}.ant-menu-dark {
    background-color: @bgcolor;
    &.ant-menu-submenu {
      > .ant-menu {
        background-color: @bgcolor;
      }
    }
  }
  .ant-menu-submenu-placement-bottomLeft.ant-menu-dark.ant-menu-submenu-popup {
    background-color: @bgcolor;
    &.ant-menu-dark.ant-menu-submenu > .ant-menu {
      background-color: @bgcolor;
      color: rgba(255, 255, 255, 0.9);
    }
  }
}
</style>
