import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const viewPermissionRoutes: AppRouteRecordRaw = {
  path: 'view-permission',
  name: 'systemViewPermission',
  component: getParentLayout('systemViewPermission'),
  meta: {
    title: '可视权限',
  },
  redirect: '/system/view-permission/list',
  children: [
    {
      path: 'list',
      name: 'systemViewPermissionList',
      component: () => import('@/views/system/view-permission/view-permission-list.vue'),
      meta: { title: '可视权限列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemViewPermissionNew',
      component: () => import('@/views/system/view-permission/view-permission-edit.vue'),
      meta: { title: '新建可视权限', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemViewPermissionEdit',
      component: () => import('@/views/system/view-permission/view-permission-edit.vue'),
      meta: { title: '编辑可视权限', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemViewPermissionDetail',
      component: () => import('@/views/system/view-permission/view-permission-detail.vue'),
      meta: { title: '查看可视权限', hideMenu: true },
    },
  ],
};
