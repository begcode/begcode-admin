import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const commonFieldDataRoutes: AppRouteRecordRaw = {
  path: 'common-field-data',
  name: 'systemCommonFieldData',
  component: getParentLayout('systemCommonFieldData'),
  meta: {
    title: '通用字段数据',
  },
  redirect: '/settings/common-field-data/list',
  children: [
    {
      path: 'list',
      name: 'systemCommonFieldDataList',
      component: () => import('@/views/settings/common-field-data/common-field-data-list.vue'),
      meta: { title: '通用字段数据列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemCommonFieldDataNew',
      component: () => import('@/views/settings/common-field-data/common-field-data-edit.vue'),
      meta: { title: '新建通用字段数据', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemCommonFieldDataEdit',
      component: () => import('@/views/settings/common-field-data/common-field-data-edit.vue'),
      meta: { title: '编辑通用字段数据', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemCommonFieldDataDetail',
      component: () => import('@/views/settings/common-field-data/common-field-data-detail.vue'),
      meta: { title: '查看通用字段数据', hideMenu: true },
    },
  ],
};
