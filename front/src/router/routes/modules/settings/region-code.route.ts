import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const regionCodeRoutes: AppRouteRecordRaw = {
  path: 'region-code',
  name: 'systemRegionCode',
  component: getParentLayout('systemRegionCode'),
  meta: {
    title: '行政区划码',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemRegionCodeList',
      component: () => import('@/views/settings/region-code/region-code-list.vue'),
      meta: { title: '行政区划码列表' },
    },
    {
      path: 'new',
      name: 'systemRegionCodeNew',
      component: () => import('@/views/settings/region-code/region-code-edit.vue'),
      meta: { title: '新建行政区划码' },
    },
    {
      path: ':entityId/edit',
      name: 'systemRegionCodeEdit',
      component: () => import('@/views/settings/region-code/region-code-edit.vue'),
      meta: { title: '编辑行政区划码', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemRegionCodeDetail',
      component: () => import('@/views/settings/region-code/region-code-detail.vue'),
      meta: { title: '查看行政区划码', hideMenu: true },
    },
  ],
};
