import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const positionRoutes: AppRouteRecordRaw = {
  path: 'position',
  name: 'systemPosition',
  component: getParentLayout('systemPosition'),
  meta: {
    title: '岗位',
  },
  redirect: '/settings/position/list',
  children: [
    {
      path: 'list',
      name: 'systemPositionList',
      component: () => import('@/views/settings/position/position-list.vue'),
      meta: { title: '岗位列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemPositionNew',
      component: () => import('@/views/settings/position/position-edit.vue'),
      meta: { title: '新建岗位', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemPositionEdit',
      component: () => import('@/views/settings/position/position-edit.vue'),
      meta: { title: '编辑岗位', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemPositionDetail',
      component: () => import('@/views/settings/position/position-detail.vue'),
      meta: { title: '查看岗位', hideMenu: true },
    },
  ],
};
