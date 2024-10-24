import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const systemConfigRoutes: AppRouteRecordRaw = {
  path: 'system-config',
  name: 'systemSystemConfig',
  component: getParentLayout('systemSystemConfig'),
  meta: {
    title: '网站配置',
  },
  redirect: '/settings/system-config/list',
  children: [
    {
      path: 'list',
      name: 'systemSystemConfigList',
      component: () => import('@/views/settings/system-config/system-config-list.vue'),
      meta: { title: '网站配置列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemSystemConfigNew',
      component: () => import('@/views/settings/system-config/system-config-edit.vue'),
      meta: { title: '新建网站配置', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemSystemConfigEdit',
      component: () => import('@/views/settings/system-config/system-config-edit.vue'),
      meta: { title: '编辑网站配置', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemSystemConfigDetail',
      component: () => import('@/views/settings/system-config/system-config-detail.vue'),
      meta: { title: '查看网站配置', hideMenu: true },
    },
  ],
};
