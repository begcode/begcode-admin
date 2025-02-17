import type { Router, RouteRecordRaw } from 'vue-router';

import { usePermissionStoreWithOut } from '@/store/modules/permission';

import { PageEnum } from '@/enums/pageEnum';
import { useUserStoreWithOut } from '@/store/modules/user';

import { PAGE_NOT_FOUND_ROUTE } from '@/router/routes/basic';

import { RootRoute } from '@/router/routes';
import { isOAuth2AppEnv, isOAuth2DingAppEnv } from '@/views/account/login/useLogin';
import { OAUTH2_THIRD_LOGIN_TENANT_ID } from '@/enums/cacheEnum';
import { setAuthCache } from '@/utils/auth';
import { PAGE_NOT_FOUND_NAME_404 } from '/@/router/constant';

const LOGIN_PATH = PageEnum.BASE_LOGIN;

const TOKEN_LOGIN = PageEnum.TOKEN_LOGIN;

const ROOT_PATH = RootRoute.path;

const whitePathList: PageEnum[] = [LOGIN_PATH, TOKEN_LOGIN];

export function createPermissionGuard(router: Router) {
  const userStore = useUserStoreWithOut();
  const permissionStore = usePermissionStoreWithOut();
  router.beforeEach(async (to, from, next) => {
    if (
      from.path === ROOT_PATH &&
      to.path === PageEnum.BASE_HOME &&
      userStore.getUserInfo.homePath &&
      userStore.getUserInfo.homePath !== PageEnum.BASE_HOME
    ) {
      next(userStore.getUserInfo.homePath);
      return;
    }

    const token = userStore.getToken;

    // Whitelist can be directly entered
    if (whitePathList.includes(to.path as PageEnum)) {
      if (to.path === LOGIN_PATH && token) {
        const isSessionTimeout = userStore.getSessionTimeout;
        try {
          await userStore.afterLoginAction();
          if (!isSessionTimeout) {
            next((to.query?.redirect as string) || '/');
            return;
          }
        } catch {
          //
        }
      } else if (to.path === LOGIN_PATH && isOAuth2AppEnv() && !token) {
        if (to.query.tenantId) {
          setAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID, to.query.tenantId);
        }
        next({ path: OAUTH2_LOGIN_PAGE_PATH });
        return;
      }
      next();
      return;
    }

    // token or user does not exist
    if (!token) {
      // You can access without permission. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
        next();
        return;
      }

      let path = LOGIN_PATH;
      if (whitePathList.includes(to.path as PageEnum)) {
        // 在免登录白名单，如果进入的页面是login页面并且当前是OAuth2app环境，就进入OAuth2登录页面
        if (to.path === LOGIN_PATH && isOAuth2AppEnv()) {
          next({ path: OAUTH2_LOGIN_PAGE_PATH });
        } else {
          //在免登录白名单，直接进入
          next();
        }
      } else {
        let href = window.location.href;
        if (isOAuth2AppEnv() && href.indexOf('/tenantId/') != -1) {
          let params = to.params;
          if (params && params.path && params.path.length > 0) {
            setAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID, params.path[params.path.length - 1]);
          }
        }
        path = isOAuth2AppEnv() ? OAUTH2_LOGIN_PAGE_PATH : LOGIN_PATH;
      }

      // redirect login page
      const redirectData: { path: string; replace: boolean; query?: Recordable<string> } = {
        path: LOGIN_PATH,
        replace: true,
      };
      if (to.fullPath) {
        let getFullPath = to.fullPath;
        if (
          getFullPath == '/' ||
          getFullPath == '/500' ||
          getFullPath == '/400' ||
          getFullPath == '/login?redirect=/' ||
          getFullPath == '/login?redirect=/login?redirect=/'
        ) {
          return;
        }
        redirectData.query = {
          ...redirectData.query,
          redirect: to.fullPath,
        };
      }
      next(redirectData);
      return;
    }

    if (isOAuth2AppEnv() && to.path.indexOf('/tenantId/') != -1) {
      if (isOAuth2DingAppEnv()) {
        next(OAUTH2_LOGIN_PAGE_PATH);
      } else {
        next(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
      }
      return;
    }
    // Jump to the 404 page after processing the login
    if (
      from.path === LOGIN_PATH &&
      to.name === PAGE_NOT_FOUND_NAME_404 &&
      to.fullPath !== (userStore.getUserInfo.homePath || PageEnum.BASE_HOME)
    ) {
      next(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
      return;
    }

    if (permissionStore.getIsDynamicAddedRoute) {
      next();
      return;
    }

    const routes = await permissionStore.buildRoutesAction();

    routes.forEach(route => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });

    router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);

    permissionStore.setDynamicAddedRoute(true);

    if (to.name === PAGE_NOT_FOUND_NAME_404) {
      // 动态添加路由后，此处应当重定向到fullPath，否则会加载404页面内容
      next({ path: to.fullPath, replace: true, query: to.query });
    } else {
      const redirectPath = (from.query.redirect || to.path) as string;
      const redirect = decodeURIComponent(redirectPath);
      const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect };
      next(nextData);
    }
  });
}
