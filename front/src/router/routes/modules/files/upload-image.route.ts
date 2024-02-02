import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const uploadImageRoutes: AppRouteRecordRaw = {
  path: 'upload-image',
  name: 'ossUploadImage',
  component: getParentLayout('ossUploadImage'),
  meta: {
    title: '上传图片',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'ossUploadImageList',
      component: () => import('@/views/files/upload-image/upload-image-list.vue'),
      meta: { title: '上传图片列表' },
    },
    {
      path: 'new',
      name: 'ossUploadImageNew',
      component: () => import('@/views/files/upload-image/upload-image-edit.vue'),
      meta: { title: '新建上传图片' },
    },
    {
      path: ':entityId/edit',
      name: 'ossUploadImageEdit',
      component: () => import('@/views/files/upload-image/upload-image-edit.vue'),
      meta: { title: '编辑上传图片', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'ossUploadImageDetail',
      component: () => import('@/views/files/upload-image/upload-image-detail.vue'),
      meta: { title: '查看上传图片', hideMenu: true },
    },
  ],
};
