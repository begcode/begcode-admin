import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const ossConfigRoutes: AppRouteRecordRaw = {
  path: 'oss-config',
  name: 'ossOssConfig',
  component: getParentLayout('ossOssConfig'),
  meta: {
    title: '对象存储配置',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'ossOssConfigList',
      component: () => import('@/views/files/oss-config/oss-config-list.vue'),
      meta: { title: '对象存储配置列表' },
    },
    {
      path: 'new',
      name: 'ossOssConfigNew',
      component: () => import('@/views/files/oss-config/oss-config-edit.vue'),
      meta: { title: '新建对象存储配置' },
    },
    {
      path: ':entityId/edit',
      name: 'ossOssConfigEdit',
      component: () => import('@/views/files/oss-config/oss-config-edit.vue'),
      meta: { title: '编辑对象存储配置', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'ossOssConfigDetail',
      component: () => import('@/views/files/oss-config/oss-config-detail.vue'),
      meta: { title: '查看对象存储配置', hideMenu: true },
    },
  ],
};
