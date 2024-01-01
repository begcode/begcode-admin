import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const apiPermissionRoutes: AppRouteRecordRaw = {
  path: 'api-permission',
  name: 'systemApiPermission',
  component: getParentLayout('systemApiPermission'),
  meta: {
    title: 'API权限',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemApiPermissionList',
      component: () => import('@/views/system/api-permission/api-permission-list.vue'),
      meta: { title: 'API权限列表' },
    },
    {
      path: 'new',
      name: 'systemApiPermissionNew',
      component: () => import('@/views/system/api-permission/api-permission-edit.vue'),
      meta: { title: '新建API权限' },
    },
    {
      path: ':entityId/edit',
      name: 'systemApiPermissionEdit',
      component: () => import('@/views/system/api-permission/api-permission-edit.vue'),
      meta: { title: '编辑API权限', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemApiPermissionDetail',
      component: () => import('@/views/system/api-permission/api-permission-detail.vue'),
      meta: { title: '查看API权限', hideMenu: true },
    },
  ],
};
