import type { RouteRecordRaw } from 'vue-router';

import { useTabs } from './useTabs';
import { useAppStore } from '@/store/modules/app';
import { usePermissionStore } from '@/store/modules/permission';
import { useUserStore } from '@/store/modules/user';

import { resetRouter, router } from '@/router';
// import { RootRoute } from '@/router/routes';

import projectSetting from '@/settings/projectSetting';
import { PermissionModeEnum } from '@/enums/appEnum';
import { RoleEnum } from '@/enums/roleEnum';

import { useMultipleTabStore } from '@/store/modules/multipleTab';

// User permissions related operations
export function usePermission() {
  const userStore = useUserStore();
  const appStore = useAppStore();
  const permissionStore = usePermissionStore();
  //动态加载流程节点表单权限
  let formData: any = {};
  function initBpmFormData(_bpmFormData) {
    formData = _bpmFormData;
  }
  const { closeAll } = useTabs(router);

  function hasBpmPermission(code, type) {
    // 禁用-type=2
    // 显示-type=1
    const codeList: string[] = [];
    const permissionList = formData.permissionList;
    if (permissionList && permissionList.length > 0) {
      for (const item of permissionList) {
        if (item.type == type) {
          codeList.push(item.action);
        }
      }
    }
    return codeList.indexOf(code) >= 0;
  }

  /**
   * Change permission mode
   */
  async function togglePermissionMode() {
    appStore.setProjectConfig({
      permissionMode:
        appStore.projectConfig?.permissionMode === PermissionModeEnum.BACK ? PermissionModeEnum.ROUTE_MAPPING : PermissionModeEnum.BACK,
    });
    location.reload();
  }

  /**
   * Reset and regain authority resource information
   * 重置和重新获得权限资源信息
   * @param id
   */
  async function resume() {
    const tabStore = useMultipleTabStore();
    tabStore.clearCacheTabs();
    resetRouter();
    const routes = await permissionStore.buildRoutesAction();
    routes.forEach(route => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });
    permissionStore.setLastBuildMenuTime();
    closeAll();
  }

  /**
   * Determine whether there is permission
   */
  function hasPermission(value?: RoleEnum | RoleEnum[] | string | string[], def = true): boolean {
    // Visible by default
    if (!value) {
      return def;
    }

    const permMode = appStore.getProjectConfig.permissionMode;

    if ([PermissionModeEnum.ROUTE_MAPPING, PermissionModeEnum.ROLE].includes(permMode)) {
      const allCodeList = permissionStore.getPermCodeList as string[];
      if (!_isArray(value)) {
        const splits = ['||', '&&'];
        const splitName = splits.find(item => value.includes(item));
        if (splitName) {
          const splitCodes = value.split(splitName);
          return splitName === splits[0]
            ? _intersection(splitCodes, allCodeList).length > 0
            : _intersection(splitCodes, allCodeList).length === splitCodes.length;
        }
        return userStore.getRoleList?.includes(value as RoleEnum);
      }
      return (_intersection(value, userStore.getRoleList) as RoleEnum[]).length > 0;
    }

    if (PermissionModeEnum.BACK === permMode) {
      const allCodeList = permissionStore.getPermCodeList as string[];
      if (!_isArray(value) && allCodeList && allCodeList.length > 0) {
        if (formData) {
          const code = value as string;
          if (hasBpmPermission(code, '1')) {
            return true;
          }
        }
        return allCodeList.includes(value);
      }
      return (_intersection(value, allCodeList) as string[]).length > 0;
    }
    return true;
  }

  /**
   * 是否禁用组件
   */
  function isDisabledAuth(value?: RoleEnum | RoleEnum[] | string | string[], def = true): boolean {
    if (formData) {
      const code = value as string;
      if (hasBpmPermission(code, '2')) {
        return true;
      }
      if (isCodingButNoConfig(code)) {
        return false;
      }
    }
    return !hasPermission(value);
  }

  /**
   * 判断是不是 代码里写了逻辑但是没有配置权限这种情况
   */
  function isCodingButNoConfig(code) {
    const all = permissionStore.allAuthList;
    if (all && all instanceof Array) {
      const temp = all.filter(item => item.action == code);
      if (temp && temp.length > 0) {
        if (temp[0].status == '0') {
          return true;
        }
      } else {
        return true;
      }
    }
    return false;
  }

  /**
   * Change roles
   * @param roles
   */
  async function changeRole(roles: RoleEnum | RoleEnum[]): Promise<void> {
    if (projectSetting.permissionMode !== PermissionModeEnum.ROUTE_MAPPING) {
      throw new Error('Please switch PermissionModeEnum to ROUTE_MAPPING mode in the configuration to operate!');
    }

    if (!_isArray(roles)) {
      roles = [roles];
    }
    userStore.setRoleList(roles);
    await resume();
  }

  /**
   * refresh menu data
   */
  async function refreshMenu() {
    resume();
  }

  return { changeRole, hasPermission, togglePermissionMode, refreshMenu, isDisabledAuth, initBpmFormData };
}
