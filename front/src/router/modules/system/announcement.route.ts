import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const announcementRoutes: AppRouteRecordRaw = {
  path: 'announcement',
  name: 'systemAnnouncement',
  component: getParentLayout('systemAnnouncement'),
  meta: {
    title: '系统通告',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemAnnouncementList',
      component: () => import('@/views/system/announcement/announcement-list.vue'),
      meta: { title: '系统通告列表' },
    },
    {
      path: 'new',
      name: 'systemAnnouncementNew',
      component: () => import('@/views/system/announcement/announcement-edit.vue'),
      meta: { title: '新建系统通告' },
    },
    {
      path: ':entityId/edit',
      name: 'systemAnnouncementEdit',
      component: () => import('@/views/system/announcement/announcement-edit.vue'),
      meta: { title: '编辑系统通告', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemAnnouncementDetail',
      component: () => import('@/views/system/announcement/announcement-detail.vue'),
      meta: { title: '查看系统通告', hideMenu: true },
    },
  ],
};
