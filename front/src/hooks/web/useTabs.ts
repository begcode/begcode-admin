import type { RouteLocationNormalized, Router } from 'vue-router';

import { useRouter } from 'vue-router';

import { useMultipleTabStore } from '@/store/modules/multipleTab';
import { useAppStore } from '@/store/modules/app';

enum TableActionEnum {
  REFRESH,
  CLOSE_ALL,
  CLOSE_LEFT,
  CLOSE_RIGHT,
  CLOSE_OTHER,
  CLOSE_CURRENT,
  CLOSE,
}

export function useTabs(_router?: Router) {
  const appStore = useAppStore();

  function canIUseTabs(): boolean {
    const { show } = appStore.getMultiTabsSetting;
    if (!show) {
      throw new Error('The multi-tab page is currently not open, please open it in the settings！');
    }
    return !!show;
  }

  const tabStore = useMultipleTabStore();
  const router = _router || useRouter();

  const { currentRoute } = router;

  function getCurrentTab() {
    const route = unref(currentRoute);
    return tabStore.getTabList.find(item => item.path === route.path)!;
  }

  async function updateTabTitle(title: string, tab?: RouteLocationNormalized) {
    const canIUse = canIUseTabs;
    if (!canIUse) {
      return;
    }
    const targetTab = tab || getCurrentTab();
    await tabStore.setTabTitle(title, targetTab);
  }

  async function updateTabPath(path: string, tab?: RouteLocationNormalized) {
    const canIUse = canIUseTabs;
    if (!canIUse) {
      return;
    }
    const targetTab = tab || getCurrentTab();
    await tabStore.updateTabPath(path, targetTab);
  }

  async function handleTabAction(action: TableActionEnum, tab?: RouteLocationNormalized) {
    const canIUse = canIUseTabs;
    if (!canIUse) {
      return;
    }
    const currentTab = getCurrentTab();
    switch (action) {
      case TableActionEnum.REFRESH:
        await tabStore.refreshPage(router);
        break;

      case TableActionEnum.CLOSE_ALL:
        await tabStore.closeAllTab(router);
        break;

      case TableActionEnum.CLOSE_LEFT:
        await tabStore.closeLeftTabs(tab || currentTab, router);
        break;

      case TableActionEnum.CLOSE_RIGHT:
        await tabStore.closeRightTabs(tab || currentTab, router);
        break;

      case TableActionEnum.CLOSE_OTHER:
        await tabStore.closeOtherTabs(tab || currentTab, router);
        break;

      case TableActionEnum.CLOSE_CURRENT:
      case TableActionEnum.CLOSE:
        await tabStore.closeTab(tab || currentTab, router);
        break;
    }
  }

  /**
   * 关闭相同的路由
   * @param path
   */
  function closeSameRoute(path) {
    if (path.indexOf('?') > 0) {
      path = path.split('?')[0];
    }
    const tab = tabStore.getTabList.find(item => item.path.indexOf(path) >= 0)!;
    if (tab) {
      tabStore.closeTab(tab, router);
    }
  }

  return {
    refreshPage: () => handleTabAction(TableActionEnum.REFRESH),
    closeAll: tab => handleTabAction(TableActionEnum.CLOSE_ALL, tab),
    closeLeft: tab => handleTabAction(TableActionEnum.CLOSE_LEFT, tab),
    closeRight: tab => handleTabAction(TableActionEnum.CLOSE_RIGHT, tab),
    closeOther: tab => handleTabAction(TableActionEnum.CLOSE_OTHER, tab),
    closeCurrent: () => handleTabAction(TableActionEnum.CLOSE_CURRENT),
    close: (tab?: RouteLocationNormalized) => handleTabAction(TableActionEnum.CLOSE, tab),
    setTitle: (title: string, tab?: RouteLocationNormalized) => updateTabTitle(title, tab),
    updatePath: (fullPath: string, tab?: RouteLocationNormalized) => updateTabPath(fullPath, tab),
    closeSameRoute,
  };
}
