import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const businessTypeRoutes: AppRouteRecordRaw = {
  path: 'business-type',
  name: 'systemBusinessType',
  component: getParentLayout('systemBusinessType'),
  meta: {
    title: '业务类型',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemBusinessTypeList',
      component: () => import('@/views/settings/business-type/business-type-list.vue'),
      meta: { title: '业务类型列表' },
    },
    {
      path: 'new',
      name: 'systemBusinessTypeNew',
      component: () => import('@/views/settings/business-type/business-type-edit.vue'),
      meta: { title: '新建业务类型' },
    },
    {
      path: ':entityId/edit',
      name: 'systemBusinessTypeEdit',
      component: () => import('@/views/settings/business-type/business-type-edit.vue'),
      meta: { title: '编辑业务类型', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemBusinessTypeDetail',
      component: () => import('@/views/settings/business-type/business-type-detail.vue'),
      meta: { title: '查看业务类型', hideMenu: true },
    },
  ],
};
