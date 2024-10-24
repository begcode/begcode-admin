import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const formSaveDataRoutes: AppRouteRecordRaw = {
  path: 'form-save-data',
  name: 'systemFormSaveData',
  component: getParentLayout('systemFormSaveData'),
  meta: {
    title: '表单数据',
  },
  redirect: '/settings/form-save-data/list',
  children: [
    {
      path: 'list',
      name: 'systemFormSaveDataList',
      component: () => import('@/views/settings/form-save-data/form-save-data-list.vue'),
      meta: { title: '表单数据列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemFormSaveDataNew',
      component: () => import('@/views/settings/form-save-data/form-save-data-edit.vue'),
      meta: { title: '新建表单数据', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemFormSaveDataEdit',
      component: () => import('@/views/settings/form-save-data/form-save-data-edit.vue'),
      meta: { title: '编辑表单数据', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemFormSaveDataDetail',
      component: () => import('@/views/settings/form-save-data/form-save-data-detail.vue'),
      meta: { title: '查看表单数据', hideMenu: true },
    },
  ],
};
