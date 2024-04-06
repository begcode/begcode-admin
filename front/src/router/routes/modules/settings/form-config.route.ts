import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const formConfigRoutes: AppRouteRecordRaw = {
  path: 'form-config',
  name: 'systemFormConfig',
  component: getParentLayout('systemFormConfig'),
  meta: {
    title: '表单配置',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemFormConfigList',
      component: () => import('@/views/settings/form-config/form-config-list.vue'),
      meta: { title: '表单配置列表' },
    },
    {
      path: 'new',
      name: 'systemFormConfigNew',
      component: () => import('@/views/settings/form-config/form-config-edit.vue'),
      meta: { title: '新建表单配置' },
    },
    {
      path: ':entityId/edit',
      name: 'systemFormConfigEdit',
      component: () => import('@/views/settings/form-config/form-config-edit.vue'),
      meta: { title: '编辑表单配置', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemFormConfigDetail',
      component: () => import('@/views/settings/form-config/form-config-detail.vue'),
      meta: { title: '查看表单配置', hideMenu: true },
    },
  ],
};
