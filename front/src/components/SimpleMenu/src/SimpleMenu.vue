<template>
  <Menu
    v-bind="getBindValues"
    :activeName="menuState.activeName"
    :openNames="getOpenKeys"
    :class="`${prefixCls} ${isThemeBright ? 'bright' : ''}`"
    :activeSubMenuNames="menuState.activeSubMenuNames"
    @select="handleSelect"
  >
    <template v-for="item in items" :key="item.path">
      <SimpleSubMenu
        :item="item"
        :parent="true"
        :collapsedShowTitle="collapsedShowTitle"
        :collapse="collapse"
        :isThemeBright="isThemeBright"
      />
    </template>
  </Menu>
</template>
<script lang="ts" setup>
import type { MenuState } from './types';
import type { Menu as MenuType } from '@/router/types';
import type { RouteLocationNormalizedLoaded } from 'vue-router';
import { computed, ref, unref, reactive, toRefs, watch, PropType, useAttrs } from 'vue';
import { useDesign } from '@begcode/components';
import Menu from './components/Menu.vue';
import SimpleSubMenu from './SimpleSubMenu.vue';
import { listenerRouteChange } from '@/logics/mitt/routeChange';
import { propTypes, isHttpUrl, openWindow } from '@begcode/components';
import { REDIRECT_NAME } from '@/router/constant';
import { useRouter } from 'vue-router';
import { isFunction } from 'lodash-es';
import { useOpenKeys } from './useOpenKeys';
import { useAppStore } from '@/store/modules/app';

defineOptions({ name: 'SimpleMenu', inheritAttrs: false });

const props = defineProps({
  items: {
    type: Array as PropType<MenuType[]>,
    default: () => [],
  },
  collapse: propTypes.bool,
  mixSider: propTypes.bool,
  theme: propTypes.string,
  accordion: propTypes.bool.def(true),
  collapsedShowTitle: propTypes.bool,
  beforeClickFn: {
    type: Function as PropType<(key: string) => Promise<boolean>>,
  },
  isSplitMenu: propTypes.bool,
});

const emit = defineEmits(['menuClick']);

const attrs = useAttrs();
const currentActiveMenu = ref('');
const isClickGo = ref(false);
const appStore = useAppStore();
const isThemeBright = ref(false);
const menuState = reactive<MenuState>({
  activeName: '',
  openNames: [],
  activeSubMenuNames: [],
});

const { currentRoute } = useRouter();
const { prefixCls } = useDesign('simple-menu');
const { items, accordion, mixSider, collapse } = toRefs(props);

const { setOpenKeys, getOpenKeys } = useOpenKeys(menuState, items, accordion, mixSider as any, collapse as any);

const getBindValues = computed(() => ({ ...attrs, ...props }));

watch(
  () => props.collapse,
  collapse => {
    if (collapse) {
      menuState.openNames = [];
    } else {
      setOpenKeys(currentRoute.value.path);
    }
  },
  { immediate: true },
);

watch(
  () => props.items,
  () => {
    if (!props.isSplitMenu) {
      return;
    }
    setOpenKeys(currentRoute.value.path);
  },
  { flush: 'post' },
);

watch(
  () => appStore.getProjectConfig.menuSetting,
  menuSetting => {
    isThemeBright.value = !!menuSetting?.isThemeBright;
  },
  { immediate: true, deep: true },
);

listenerRouteChange(route => {
  if (route.name === REDIRECT_NAME) return;
  currentActiveMenu.value = route.meta?.currentActiveMenu as string;
  handleMenuChange(route);
  if (unref(currentActiveMenu)) {
    menuState.activeName = unref(currentActiveMenu);
    setOpenKeys(unref(currentActiveMenu));
  }
});

async function handleMenuChange(route?: RouteLocationNormalizedLoaded) {
  if (unref(isClickGo)) {
    isClickGo.value = false;
    return;
  }
  const path = (route || unref(currentRoute)).path;
  menuState.activeName = path;
  setOpenKeys(path);
}

async function handleSelect(key: string) {
  if (isHttpUrl(key)) {
    openWindow(key);
    return;
  }
  const findItem = getMatchingMenu(props.items, key);
  if (findItem?.internalOrExternal == true) {
    window.open(location.origin + key);
    return;
  }
  const { beforeClickFn } = props;
  if (beforeClickFn && isFunction(beforeClickFn)) {
    const flag = await beforeClickFn(key);
    if (!flag) return;
  }
  emit('menuClick', key);
  isClickGo.value = true;
  setOpenKeys(key);
  menuState.activeName = key;
}

const getMatchingMenu = (menus, path) => {
  for (let i = 0, len = menus.length; i < len; i++) {
    const item = menus[i];
    if (item.path === path && !item.redirect && !item.paramPath) {
      return item;
    } else if (item.children?.length) {
      const result = getMatchingMenu(item.children, path);
      if (result) {
        return result;
      }
    }
  }
  return '';
};
</script>
<style lang="less">
@import url('./index.less');
</style>
