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
      const route: AppRouteRecordRaw = { meta: { icon: '' }, name: '', path: '', redirect: undefined };
      route.meta = { icon: permission.icon };
      route.meta.title = permission.text;
      route.meta.hideMenu = !!permission.hide;
      route.meta.hideBreadcrumb = !!permission.hideInBreadcrumb;
      route.meta.orderNo = permission.order || 0;
      route.component = permission.componentFile;
      route.name = permission.code || '';
      route.path = permission.link || '';
      route.redirect = permission.redirect || '';
      if (permission.children) {
        route.children = toRouteRecords(permission.children);
      }
      result.push(route);
    }
  });
  return result;
}
