import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const announcementRecordRoutes: AppRouteRecordRaw = {
  path: 'announcement-record',
  name: 'systemAnnouncementRecord',
  component: getParentLayout('systemAnnouncementRecord'),
  meta: {
    title: '通告阅读记录',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemAnnouncementRecordList',
      component: () => import('@/views/system/announcement-record/announcement-record-list.vue'),
      meta: { title: '通告阅读记录列表' },
    },
    {
      path: 'new',
      name: 'systemAnnouncementRecordNew',
      component: () => import('@/views/system/announcement-record/announcement-record-edit.vue'),
      meta: { title: '新建通告阅读记录' },
    },
    {
      path: ':entityId/edit',
      name: 'systemAnnouncementRecordEdit',
      component: () => import('@/views/system/announcement-record/announcement-record-edit.vue'),
      meta: { title: '编辑通告阅读记录', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemAnnouncementRecordDetail',
      component: () => import('@/views/system/announcement-record/announcement-record-detail.vue'),
      meta: { title: '查看通告阅读记录', hideMenu: true },
    },
  ],
};
