import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const uReportFileRoutes: AppRouteRecordRaw = {
  path: 'u-report-file',
  name: 'reportUReportFile',
  component: getParentLayout('reportUReportFile'),
  meta: {
    title: '报表存储',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'reportUReportFileList',
      component: () => import('@/views/report/u-report-file/u-report-file-list.vue'),
      meta: { title: '报表存储列表' },
    },
    {
      path: 'new',
      name: 'reportUReportFileNew',
      component: () => import('@/views/report/u-report-file/u-report-file-edit.vue'),
      meta: { title: '新建报表存储' },
    },
    {
      path: ':entityId/edit',
      name: 'reportUReportFileEdit',
      component: () => import('@/views/report/u-report-file/u-report-file-edit.vue'),
      meta: { title: '编辑报表存储', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'reportUReportFileDetail',
      component: () => import('@/views/report/u-report-file/u-report-file-detail.vue'),
      meta: { title: '查看报表存储', hideMenu: true },
    },
  ],
};
