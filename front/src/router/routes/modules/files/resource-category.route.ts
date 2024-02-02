import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const resourceCategoryRoutes: AppRouteRecordRaw = {
  path: 'resource-category',
  name: 'ossResourceCategory',
  component: getParentLayout('ossResourceCategory'),
  meta: {
    title: '资源分类',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'ossResourceCategoryList',
      component: () => import('@/views/files/resource-category/resource-category-list.vue'),
      meta: { title: '资源分类列表' },
    },
    {
      path: 'new',
      name: 'ossResourceCategoryNew',
      component: () => import('@/views/files/resource-category/resource-category-edit.vue'),
      meta: { title: '新建资源分类' },
    },
    {
      path: ':entityId/edit',
      name: 'ossResourceCategoryEdit',
      component: () => import('@/views/files/resource-category/resource-category-edit.vue'),
      meta: { title: '编辑资源分类', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'ossResourceCategoryDetail',
      component: () => import('@/views/files/resource-category/resource-category-detail.vue'),
      meta: { title: '查看资源分类', hideMenu: true },
    },
  ],
};
