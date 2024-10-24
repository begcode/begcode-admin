import type { RouteLocationRaw, Router } from 'vue-router';

import { useRouter } from 'vue-router';
import { PageEnum } from '@/enums/pageEnum';

import { REDIRECT_NAME } from '@/router/constant';

import { useMultipleTabStore } from '@/store/modules/multipleTab';

export type PathAsPageEnum<T> = T extends { path: string } ? T & { path: PageEnum } : T;
export type RouteLocationRawEx = PathAsPageEnum<RouteLocationRaw>;

function handleError(e: Error) {
  console.error(e);
}

export enum GoType {
  'replace',
  'after',
}

/**
 * page switch
 */
export function useGo(_router?: Router) {
  const { push, replace, currentRoute } = _router || useRouter();
  function go(opt?: RouteLocationRawEx): void;
  function go(opt: RouteLocationRawEx, isReplace: boolean): void;
  function go(opt: RouteLocationRawEx, goType: GoType): void;
  function go(opt: RouteLocationRawEx = PageEnum.BASE_HOME, goTypeOrIsReplace: boolean | GoType = false) {
    if (!opt) {
      return;
    }
    const isReplace = goTypeOrIsReplace === true || goTypeOrIsReplace === GoType.replace;
    const isAfter = goTypeOrIsReplace === GoType.after;

    if (isReplace) {
      replace(opt).catch(handleError);
    } else if (isAfter) {
      const tabStore = useMultipleTabStore();
      const currentName = unref(currentRoute).name;
      // 当前 tab
      const currentIndex = tabStore.getTabList.findIndex(item => item.name === currentName);
      // 当前 tab 数量
      const currentCount = tabStore.getTabList.length;
      push(opt)
        .then(() => {
          if (tabStore.getTabList.length > currentCount) {
            // 产生新 tab
            // 新 tab（也是最后一个）
            const targetIndex = tabStore.getTabList.length - 1;
            // 新 tab 在 当前 tab 的后面
            if (currentIndex > -1 && targetIndex > currentIndex) {
              // 移动 tab
              tabStore.sortTabs(targetIndex, currentIndex + 1).then(() => {});
            }
          }
        })
        .catch(handleError);
    } else {
      push(opt).catch(handleError);
    }
  }
  return go;
}

/**
 * @description: redo current page
 */
export const useRedo = (_router?: Router) => {
  const { replace, currentRoute } = _router || useRouter();
  const { query, params = {}, name, fullPath } = unref(currentRoute.value);
  function redo(): Promise<boolean> {
    return new Promise(resolve => {
      if (name === REDIRECT_NAME) {
        resolve(false);
        return;
      }
      const tabStore = useMultipleTabStore();
      if (name && Object.keys(params).length > 0) {
        tabStore.setRedirectPageParam({
          redirect_type: 'name',
          name: String(name),
          params,
          query,
        });
        params['path'] = String(name);
      } else {
        tabStore.setRedirectPageParam({
          redirect_type: 'path',
          path: fullPath,
          query,
        });
        params['path'] = fullPath;
      }
      replace({ name: REDIRECT_NAME, params, query }).then(() => resolve(true));
    });
  }
  return redo;
};

/**
 * 判断是不是动态路由的跳转
 * @param params
 * @param name
 */
function isDynamicRoute(params, name) {
  let arr = Object.keys(params);
  let flag = false;
  for (let i = 0; i < arr.length; i++) {
    let key = '@' + arr[i];
    if ((name as string).indexOf(key) > 0) {
      flag = true;
      break;
    }
  }
  return flag;
}
