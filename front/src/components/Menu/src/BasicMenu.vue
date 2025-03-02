<template>
  <a-menu
    :selectedKeys="menuState.selectedKeys"
    :defaultSelectedKeys="menuState.defaultSelectedKeys"
    :mode="mode"
    :openKeys="getOpenKeys"
    :inlineIndent="inlineIndent"
    :theme="theme"
    @open-change="handleOpenChange"
    :class="getMenuClass"
    @click="handleMenuClick"
    :subMenuOpenDelay="0.2"
    v-bind="getInlineCollapseOptions"
  >
    <template v-for="item in items" :key="item.path">
      <BasicSubMenuItem :item="item" :theme="theme" :isHorizontal="isHorizontal" />
    </template>
  </a-menu>
</template>
<script lang="ts" setup>
import type { MenuState } from './types';
import { MenuProps } from 'ant-design-vue';
import BasicSubMenuItem from './components/BasicSubMenuItem.vue';
import { MenuModeEnum, MenuTypeEnum } from '@/enums/menuEnum';
import { useOpenKeys } from './useOpenKeys';
import { RouteLocationNormalizedLoaded, useRouter } from 'vue-router';
import { basicProps } from './props';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { REDIRECT_NAME } from '@/router/constant';
import { isUrl } from '@/utils/is';
import { useDesign } from '@/hooks/web/useDesign';
import { getCurrentParentPath } from '@/router/menus';
import { listenerRouteChange } from '@/logics/mitt/routeChange';
import { getAllParentPath } from '@/router/helper/menuHelper';
import { createBasicRootMenuContext } from './useBasicMenuContext';
import { getMenus } from '@/router/menus';

defineOptions({ name: 'BasicMenu' });

const props = defineProps(basicProps);

const emit = defineEmits(['menuClick']);

const isClickGo = ref(false);
const currentActiveMenu = ref('');

const menuState = reactive<MenuState>({
  defaultSelectedKeys: [],
  openKeys: [],
  selectedKeys: [],
  collapsedOpenKeys: [],
});

createBasicRootMenuContext({ menuState: menuState });

const { prefixCls } = useDesign('basic-menu');
const { items, mode, accordion } = toRefs(props);

const { getCollapsed, getTopMenuAlign, getSplit } = useMenuSetting();

const { currentRoute } = useRouter();

const { handleOpenChange, setOpenKeys, getOpenKeys } = useOpenKeys(menuState, items, mode as any, accordion);

const getIsTopMenu = computed(() => {
  const { type, mode } = props;
  return (type === MenuTypeEnum.TOP_MENU && mode === MenuModeEnum.HORIZONTAL) || (props.isHorizontal && unref(getSplit));
});

const getMenuClass = computed(() => {
  const align = props.isHorizontal && unref(getSplit) ? 'start' : unref(getTopMenuAlign);
  return [
    prefixCls,
    `justify-${align}`,
    {
      [`${prefixCls}__second`]: !props.isHorizontal && unref(getSplit),
      [`${prefixCls}__sidebar-hor`]: unref(getIsTopMenu),
    },
  ];
});

const getInlineCollapseOptions = computed(() => {
  const isInline = props.mode === MenuModeEnum.INLINE;
  const inlineCollapseOptions: { inlineCollapsed?: boolean } = {};
  if (isInline) {
    inlineCollapseOptions.inlineCollapsed = props.mixSider ? false : unref(getCollapsed);
  }
  return inlineCollapseOptions;
});

listenerRouteChange(route => {
  if (route.name === REDIRECT_NAME) return;
  handleMenuChange(route);
  currentActiveMenu.value = route.meta?.currentActiveMenu as string;
  if (unref(currentActiveMenu)) {
    menuState.selectedKeys = [unref(currentActiveMenu)];
    setOpenKeys(unref(currentActiveMenu));
  }
});

!props.mixSider &&
  watch(
    () => props.items,
    () => {
      handleMenuChange();
    },
  );

const handleMenuClick: MenuProps['onClick'] = async ({ item, key }) => {
  const { beforeClickFn } = props;
  if (isUrl(key)) {
    window.open(key);
    return;
  }
  if (beforeClickFn && _isFunction(beforeClickFn)) {
    const flag = await beforeClickFn(key);
    if (!flag) return;
  }
  if (props.type === MenuTypeEnum.MIX) {
    const menus = await getMenus();
    const menuItem = getMatchingPath(menus, key);
    if (menuItem && !menuItem.redirect && menuItem.children?.length) {
      const subMenuItem = getSubMenu(menuItem.children);
      if (subMenuItem?.path) {
        const path = subMenuItem.redirect ?? subMenuItem.path;
        let _key = path;
        if (isUrl(path)) {
          window.open(path);
          // 外部打开emit出去的key不能是url，否则左侧菜单出不来
          _key = key;
        }
        emit('menuClick', _key, { title: subMenuItem.title });
      } else {
        emit('menuClick', key, item);
      }
    } else {
      emit('menuClick', key, item);
    }
  } else {
    emit('menuClick', key, item);
  }
  isClickGo.value = true;
  menuState.selectedKeys = [key];
};

async function handleMenuChange(route?: RouteLocationNormalizedLoaded) {
  if (unref(isClickGo)) {
    isClickGo.value = false;
    return;
  }
  const path = (route || unref(currentRoute)).meta?.currentActiveMenu || (route || unref(currentRoute)).path;
  setOpenKeys(path);
  if (unref(currentActiveMenu)) return;
  if (props.isHorizontal && unref(getSplit)) {
    const parentPath = await getCurrentParentPath(path);
    menuState.selectedKeys = [parentPath];
  } else {
    menuState.selectedKeys = await getAllParentPath(props.items, path);
  }
}

/**
 * 获取指定菜单下的第一个菜单
 */
function getSubMenu(menus) {
  for (let i = 0, len = menus.length; i < len; i++) {
    const item = menus[i];
    if (item.path && !item.children?.length) {
      return item;
    } else if (item.children?.length) {
      const result = getSubMenu(item.children);
      if (result) {
        return result;
      }
    }
  }
  return null;
}

/**
 * 获取匹配path的菜单
 */
function getMatchingPath(menus, path) {
  for (let i = 0, len = menus.length; i < len; i++) {
    const item = menus[i];
    if (item.path === path) {
      return item;
    } else if (item.children?.length) {
      const result = getMatchingPath(item.children, path);
      if (result) {
        return result;
      }
    }
  }
  return null;
}
</script>
<style lang="less">
@import url('./index.less');
</style>
