import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const authorityRoutes: AppRouteRecordRaw = {
  path: 'authority',
  name: 'systemAuthority',
  component: getParentLayout('systemAuthority'),
  meta: {
    title: '角色',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemAuthorityList',
      component: () => import('@/views/system/authority/authority-list.vue'),
      meta: { title: '角色列表' },
    },
    {
      path: 'new',
      name: 'systemAuthorityNew',
      component: () => import('@/views/system/authority/authority-edit.vue'),
      meta: { title: '新建角色' },
    },
    {
      path: ':entityId/edit',
      name: 'systemAuthorityEdit',
      component: () => import('@/views/system/authority/authority-edit.vue'),
      meta: { title: '编辑角色', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemAuthorityDetail',
      component: () => import('@/views/system/authority/authority-detail.vue'),
      meta: { title: '查看角色', hideMenu: true },
    },
  ],
};
