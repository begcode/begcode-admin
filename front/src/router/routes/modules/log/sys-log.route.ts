import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const sysLogRoutes: AppRouteRecordRaw = {
  path: 'sys-log',
  name: 'logSysLog',
  component: getParentLayout('logSysLog'),
  meta: {
    title: '系统日志',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'logSysLogList',
      component: () => import('@/views/log/sys-log/sys-log-list.vue'),
      meta: { title: '系统日志列表' },
    },
    {
      path: 'new',
      name: 'logSysLogNew',
      component: () => import('@/views/log/sys-log/sys-log-edit.vue'),
      meta: { title: '新建系统日志' },
    },
    {
      path: ':entityId/edit',
      name: 'logSysLogEdit',
      component: () => import('@/views/log/sys-log/sys-log-edit.vue'),
      meta: { title: '编辑系统日志', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'logSysLogDetail',
      component: () => import('@/views/log/sys-log/sys-log-detail.vue'),
      meta: { title: '查看系统日志', hideMenu: true },
    },
  ],
};
