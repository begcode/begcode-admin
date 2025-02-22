import { useDebounceFn, useTimeoutFn } from '@vueuse/core';
import type { MenuState } from './types';
import type { Menu as MenuType } from '@/router/types';
import { getAllParentPath } from '@/router/helper/menuHelper';

export function useOpenKeys(
  menuState: MenuState,
  menus: Ref<MenuType[]>,
  accordion: Ref<boolean>,
  mixSider: Ref<boolean>,
  collapse: Ref<boolean>,
) {
  const debounceSetOpenKeys = useDebounceFn(setOpenKeys, 50);
  async function setOpenKeys(path: string) {
    const native = !mixSider.value;
    const menuList = toRaw(menus.value);
    const handle = () => {
      if (menuList?.length === 0) {
        menuState.activeSubMenuNames = [];
        menuState.openNames = [];
        return;
      }
      const keys = getAllParentPath(menuList, path);

      if (!unref(accordion)) {
        menuState.openNames = _uniq([...menuState.openNames, ...keys]);
      } else {
        menuState.openNames = keys;
      }
      menuState.activeSubMenuNames = menuState.openNames;
    };
    if (native) {
      handle();
    } else {
      useTimeoutFn(handle, 30);
    }
  }

  const getOpenKeys = computed(() => {
    return unref(collapse) ? [] : menuState.openNames;
  });

  return { setOpenKeys: debounceSetOpenKeys, getOpenKeys };
}
