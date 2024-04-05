import type { Menu } from '@/router/types';
import { ref, watch, unref } from 'vue';
import { useI18n } from '@/hooks/web/useI18n';
import { useTitle as usePageTitle } from '@vueuse/core';
import { useGlobSetting } from '@/hooks/setting';
import { useRouter } from 'vue-router';
import { useLocaleStore } from '@/store/modules/locale';
import { REDIRECT_NAME } from '@/router/constant';
import { getMenus } from '/@/router/menus';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

/**
 * Listening to page changes and dynamically changing site titles
 */
export function useTitle() {
  const { title } = useGlobSetting();
  const { t } = useI18n();
  const { currentRoute } = useRouter();
  const localeStore = useLocaleStore();

  const pageTitle = usePageTitle();

  const menus = ref<Menu[] | null>(null);

  watch(
    [() => currentRoute.value.path, () => localeStore.getLocale],
    async () => {
      const route = unref(currentRoute);

      if (route.name === REDIRECT_NAME) {
        return;
      }

      if (route.params && Object.keys(route.params).length) {
        if (!menus.value) {
          menus.value = await getMenus();
        }

        const getTitle = getMatchingRouterName(menus.value, route.fullPath);
        let tTitle = '';
        if (getTitle) {
          tTitle = t(getTitle);
        } else {
          tTitle = t(route?.meta?.title as string);
        }
        pageTitle.value = tTitle ? ` ${tTitle} - ${title} ` : `${title}`;
      } else {
        const tTitle = t(route?.meta?.title as string);
        pageTitle.value = tTitle ? ` ${tTitle} - ${title} ` : `${title}`;
      }
    },
    { immediate: true },
  );
}

/**
 获取路由匹配模式的真实页面名字
*/
function getMatchingRouterName(menus, path) {
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
}
