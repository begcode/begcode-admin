import type { RouteLocationRaw, Router } from 'vue-router';

import { unref } from 'vue';

import { useRouter } from 'vue-router';
import { PageEnum } from '@/enums/pageEnum';
import { REDIRECT_NAME } from '@/router/constant';
import { useUserStore } from '@/store/modules/user';

export type PathAsPageEnum<T> = T extends { path: string } ? T & { path: PageEnum } : T;
export type RouteLocationRawEx = PathAsPageEnum<RouteLocationRaw>;

function handleError(e: Error) {
  console.error(e);
}

/**
 * page switch
 */
export function useGo(_router?: Router) {
  const userStore = useUserStore();
  const homePath = userStore.getUserInfo.homePath || PageEnum.BASE_HOME;
  const { push, replace } = _router || useRouter();
  function go(opt: RouteLocationRawEx = homePath, isReplace = false) {
    if (!opt) {
      return;
    }
    isReplace ? replace(opt).catch(handleError) : push(opt).catch(handleError);
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
      if (name && Object.keys(params).length > 0) {
        params['_origin_params'] = JSON.stringify(params ?? {});
        if (isDynamicRoute(params, name)) {
          params['_redirect_type'] = 'path';
          params['path'] = fullPath;
        } else {
          params['_redirect_type'] = 'name';
          params['path'] = String(name);
        }
      } else {
        params['_redirect_type'] = 'path';
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
