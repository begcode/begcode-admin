import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const cacheManageRoutes: AppRouteRecordRaw = {
  path: 'cache-manage',
  name: 'CacheManage',
  component: getParentLayout('cacheManage'),
  redirect: '/system/cache-manage/list',
  meta: {
    title: '缓存信息',
  },
  children: [
    {
      path: 'list',
      name: 'system-cache-manage',
      component: () => import('@/views/system/cache-manage/cache-manage.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表', hideMenu: true },
    },
  ],
};
