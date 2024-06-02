import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const uploadFileRoutes: AppRouteRecordRaw = {
  path: 'upload-file',
  name: 'ossUploadFile',
  component: getParentLayout('ossUploadFile'),
  meta: {
    title: '上传文件',
  },
  redirect: '/files/upload-file/list',
  children: [
    {
      path: 'list',
      name: 'ossUploadFileList',
      component: () => import('@/views/files/upload-file/upload-file-list.vue'),
      meta: { title: '上传文件列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'ossUploadFileNew',
      component: () => import('@/views/files/upload-file/upload-file-edit.vue'),
      meta: { title: '新建上传文件', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'ossUploadFileEdit',
      component: () => import('@/views/files/upload-file/upload-file-edit.vue'),
      meta: { title: '编辑上传文件', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'ossUploadFileDetail',
      component: () => import('@/views/files/upload-file/upload-file-detail.vue'),
      meta: { title: '查看上传文件', hideMenu: true },
    },
  ],
};
