import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const taskJobConfigRoutes: AppRouteRecordRaw = {
  path: 'task-job-config',
  name: 'taskjobTaskJobConfig',
  component: getParentLayout('taskjobTaskJobConfig'),
  meta: {
    title: '定时任务',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'taskjobTaskJobConfigList',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-list.vue'),
      meta: { title: '定时任务列表' },
    },
    {
      path: 'new',
      name: 'taskjobTaskJobConfigNew',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-edit.vue'),
      meta: { title: '新建定时任务' },
    },
    {
      path: ':entityId/edit',
      name: 'taskjobTaskJobConfigEdit',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-edit.vue'),
      meta: { title: '编辑定时任务', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'taskjobTaskJobConfigDetail',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-detail.vue'),
      meta: { title: '查看定时任务', hideMenu: true },
    },
  ],
};
