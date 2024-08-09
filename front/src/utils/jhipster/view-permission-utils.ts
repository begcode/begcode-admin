import { AppRouteRecordRaw } from '@/router/types';
import { IViewPermission } from '@/models/system/view-permission.model';

export function toRouteRecords(viewsPermissions: IViewPermission[]): AppRouteRecordRaw[] {
  const result: AppRouteRecordRaw[] = [];
  viewsPermissions = viewsPermissions || [];
  viewsPermissions.forEach(permission => {
    // @ts-ignore
    if (permission.group) {
      result.push(...toRouteRecords(permission.children || []));
    } else {
      const route: AppRouteRecordRaw = {
        meta: {
          icon: permission.icon || '',
          title: permission.text || '',
          hideMenu: !!permission.hide,
          hideBreadcrumb: !!permission.hideInBreadcrumb,
          orderNo: permission.order || 0,
        },
        component: permission.componentFile,
        name: permission.code || '',
        path: permission.link || '',
        redirect: permission.redirect || undefined,
      };
      if (permission.children) {
        route.children = toRouteRecords(permission.children);
      }
      result.push(route);
    }
  });
  return result;
}
