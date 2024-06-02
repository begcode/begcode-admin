import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const siteConfigRoutes: AppRouteRecordRaw = {
  path: 'site-config',
  name: 'systemSiteConfig',
  component: getParentLayout('systemSiteConfig'),
  meta: {
    title: '网站配置',
  },
  redirect: '/settings/site-config/list',
  children: [
    {
      path: 'list',
      name: 'systemSiteConfigList',
      component: () => import('@/views/settings/site-config/site-config-list.vue'),
      meta: { title: '网站配置列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemSiteConfigNew',
      component: () => import('@/views/settings/site-config/site-config-edit.vue'),
      meta: { title: '新建网站配置', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemSiteConfigEdit',
      component: () => import('@/views/settings/site-config/site-config-edit.vue'),
      meta: { title: '编辑网站配置', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemSiteConfigDetail',
      component: () => import('@/views/settings/site-config/site-config-detail.vue'),
      meta: { title: '查看网站配置', hideMenu: true },
    },
  ],
};
