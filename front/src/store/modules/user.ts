import type { LoginInfo } from '/#/store';
import type { ErrorMessageMode } from '/#/axios';
import { defineStore } from 'pinia';
import { store } from '@/store';
import { RoleEnum } from '@/enums/roleEnum';
import { PageEnum } from '@/enums/pageEnum';
import { ROLES_KEY, TOKEN_KEY, USER_INFO_KEY, LOGIN_INFO_KEY, DB_DICT_DATA_KEY, TENANT_ID } from '@/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '@/utils/auth';
import { LoginParams, ThirdLoginParams } from '@/api-service/sys/model/userModel';
import { loginApi, phoneLoginApi, thirdLogin } from '@/api-service/sys/user';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { router } from '@/router';
import { usePermissionStore } from '@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '@/router/routes/basic';
import { isArray } from 'lodash-es';
import { h } from 'vue';
import { useGlobSetting } from '@/hooks/setting';
import { JDragConfigEnum } from '@/enums/jeecgEnum';
import { useSso } from '@/hooks/web/useSso';
import accountService from '@/api-service/account/account.service';
import { IUser } from '@/models/system/user.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

interface UserState {
  userInfo: Nullable<IUser>;
  token?: string;
  roleList: RoleEnum[];
  dictItems?: [];
  sessionTimeout?: boolean;
  lastUpdateTime: number;
  tenantId?: string | number;
  tenantid?: string | number;
  shareTenantId?: Nullable<string | number>;
  loginInfo?: Nullable<LoginInfo>;
  // 缓存字典项
  sysAllDictItems?: any;
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    // 用户信息
    userInfo: null,
    // token
    token: undefined,
    // roleList
    // 角色列表
    roleList: [],
    // 字典
    dictItems: [],
    // Whether the login expired
    // session过期时间
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
    //租户id
    tenantId: '',
    // 分享租户ID
    // 用于分享页面所属租户与当前用户登录租户不一致的情况
    shareTenantId: null,
    //登录返回信息
    loginInfo: null,
  }),
  getters: {
    getUserInfo(state): IUser {
      return state.userInfo || getAuthCache<IUser>(USER_INFO_KEY) || {};
    },
    getLoginInfo(state): LoginInfo {
      return state.loginInfo || getAuthCache<LoginInfo>(LOGIN_INFO_KEY) || {};
    },
    getToken(state): string {
      return state.token || getAuthCache<string>(TOKEN_KEY);
    },
    getAllDictItems(state): [] {
      return state.dictItems || getAuthCache(DB_DICT_DATA_KEY);
    },
    getRoleList(state): RoleEnum[] {
      return state.roleList.length > 0 ? this.roleList : getAuthCache<RoleEnum[]>(ROLES_KEY);
    },
    getSessionTimeout(state): boolean {
      return !!state.sessionTimeout;
    },
    getLastUpdateTime(state): number {
      return state.lastUpdateTime;
    },
    getTenant(state): string | number {
      return state.tenantId || getAuthCache<string | number>(TENANT_ID);
    },
    // 是否有分享租户id
    hasShareTenantId(): boolean {
      return this.shareTenantId != null && this.shareTenantId !== '';
    },
  },
  actions: {
    setToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(TOKEN_KEY, info);
    },
    setRoleList(roleList: RoleEnum[]) {
      this.roleList = roleList;
      setAuthCache(ROLES_KEY, roleList);
    },
    setUserInfo(info: IUser | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    setLoginInfo(info: LoginInfo | null) {
      this.loginInfo = info;
      setAuthCache(LOGIN_INFO_KEY, info);
    },
    setAllDictItems(dictItems) {
      this.dictItems = dictItems;
      setAuthCache(DB_DICT_DATA_KEY, dictItems);
    },
    setTenant(id) {
      this.tenantId = id;
      setAuthCache(TENANT_ID, id);
    },
    setShareTenantId(id: NonNullable<typeof this.shareTenantId>) {
      this.shareTenantId = id;
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
    },
    resetState() {
      this.userInfo = null;
      this.dictItems = [];
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = false;
    },
    /**
     * @description: login
     * 登录事件
     */
    async login(
      params: LoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      },
    ): Promise<IUser | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        const res = await loginApi(loginParams, mode);
        // save token
        this.setToken(res['id_token']);
        this.setTenant(res['tenant_id']);
        return this.afterLoginAction(goHome);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 登录完成处理
     * @param goHome
     */
    async afterLoginAction(goHome?: boolean, data?: any): Promise<IUser | null> {
      if (!this.getToken) return null;
      // get user info
      //获取用户信息
      const userInfo = await this.getUserInfoAction();

      const sessionTimeout = this.sessionTimeout;
      if (sessionTimeout) {
        this.setSessionTimeout(false);
      } else {
        const permissionStore = usePermissionStore();
        if (!permissionStore.isDynamicAddedRoute) {
          const routes = await permissionStore.buildRoutesAction();
          routes.forEach(route => {
            router.addRoute(route as unknown as RouteRecordRaw);
          });
          router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
          permissionStore.setDynamicAddedRoute(true);
        }
        await this.setLoginInfo({ ...data, isLogin: true });
        localStorage.setItem(JDragConfigEnum.DRAG_BASE_URL, useGlobSetting().domainUrl || '');
        let redirect = router.currentRoute.value?.query?.redirect as string;
        // 判断是否有 redirect 重定向地址
        if (redirect && goHome) {
          // 当前页面打开
          window.open(redirect, '_self');
        }
        goHome && (await router.replace(PageEnum.BASE_HOME));
      }
      return userInfo;
    },
    /**
     * 手机号登录
     * @param params
     */
    async phoneLogin(params: { mobile: string; code: string; goHome?: boolean; mode?: ErrorMessageMode }): Promise<IUser | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        const data = await phoneLoginApi(loginParams, mode);
        const { id_token } = data;
        // save token
        if (id_token) {
          this.setToken(id_token);
          return this.afterLoginAction(goHome, data);
        }
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 扫码登录事件
     */
    async qrCodeLogin(token): Promise<IUser | null> {
      try {
        // save token
        this.setToken(token);
        return this.afterLoginAction(true, {});
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async getUserInfoAction(): Promise<IUser | null> {
      if (!this.getToken) return null;
      const userInfo = await accountService.getAccount();
      const roles = userInfo.authorities || [];
      if (isArray(roles)) {
        const roleList = roles.map(item => item.code) as RoleEnum[];
        this.setRoleList(roleList);
      } else {
        userInfo.authorities = [];
        this.setRoleList([]);
      }
      this.setUserInfo(userInfo);
      /**
       * 添加字典信息到缓存
       * @updateBy:lsq
       * @updateDate:2021-09-08
       */
      // if (sysAllDictItems) {
      //   this.setAllDictItems(sysAllDictItems);
      // }
      return userInfo;
    },
    /**
     * @description: logout
     * 退出登录
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          // await doLogout();
        } catch {
          console.log('注销Token失败');
        }
      }
      this.setToken(undefined);
      setAuthCache(TOKEN_KEY, null);
      this.setSessionTimeout(false);
      this.setUserInfo(null);
      this.setLoginInfo(null);
      this.setTenant(null);
      localStorage.removeItem(JDragConfigEnum.DRAG_BASE_URL);
      // 如果开启单点登录,则跳转到单点统一登录中心
      const openSso = useGlobSetting().openSso;
      if (openSso == 'true') {
        await useSso().ssoLoginOut();
      }
      // 退出登录的时候需要用的应用id
      goLogin &&
        (await router.push({
          path: PageEnum.BASE_LOGIN,
          query: {
            // 传入当前的路由，登录成功后跳转到当前路由
            redirect: router.currentRoute.value.fullPath,
          },
        }));
    },
    /**
     * 登录事件
     */
    async ThirdLogin(
      params: ThirdLoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      },
    ): Promise<any | null> {
      try {
        const { goHome = true, mode, ...ThirdLoginParams } = params;
        const data = await thirdLogin(ThirdLoginParams, mode);
        const { token } = data;
        // save token
        this.setToken(token);
        return this.afterLoginAction(goHome, data);
      } catch (error) {
        return Promise.reject(error);
      }
    },

    /**
     * @description: Confirm before logging out
     * 退出询问
     */
    confirmLoginOut() {
      const { createConfirm } = useMessage();
      const { t } = useI18n();
      createConfirm({
        iconType: 'warning',
        title: () => h('span', t('sys.app.logoutTip')),
        content: () => h('span', t('sys.app.logoutMessage')),
        onOk: async () => {
          await this.logout(true);
        },
      });
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}
