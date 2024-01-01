import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const cacheManageRoutes: AppRouteRecordRaw = {
  path: 'cache-manage',
  name: 'CacheManage',
  component: getParentLayout('cacheManage'),
  meta: {
    title: '缓存信息',
  },
  children: [
    {
      path: '',
      name: 'system-cache-manage',
      component: () => import('@/views/system/cache-manage/cache-manage.vue'),
      meta: { authorities: ['ROLE_USER'], title: '列表' },
    },
  ],
};
