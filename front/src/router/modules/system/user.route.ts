import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const userRoutes: AppRouteRecordRaw = {
  path: 'user',
  name: 'systemUser',
  component: getParentLayout('systemUser'),
  meta: {
    title: '用户',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemUserList',
      component: () => import('@/views/system/user/user-list.vue'),
      meta: { title: '用户列表' },
    },
    {
      path: 'new',
      name: 'systemUserNew',
      component: () => import('@/views/system/user/user-edit.vue'),
      meta: { title: '新建用户' },
    },
    {
      path: ':entityId/edit',
      name: 'systemUserEdit',
      component: () => import('@/views/system/user/user-edit.vue'),
      meta: { title: '编辑用户', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemUserDetail',
      component: () => import('@/views/system/user/user-detail.vue'),
      meta: { title: '查看用户', hideMenu: true },
    },
  ],
};
