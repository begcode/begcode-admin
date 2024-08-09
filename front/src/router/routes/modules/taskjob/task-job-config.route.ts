import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const taskJobConfigRoutes: AppRouteRecordRaw = {
  path: 'task-job-config',
  name: 'taskjobTaskJobConfig',
  component: getParentLayout('taskjobTaskJobConfig'),
  meta: {
    title: '任务配置',
  },
  redirect: '/taskjob/task-job-config/list',
  children: [
    {
      path: 'list',
      name: 'taskjobTaskJobConfigList',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-list.vue'),
      meta: { title: '任务配置列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'taskjobTaskJobConfigNew',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-edit.vue'),
      meta: { title: '新建任务配置', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'taskjobTaskJobConfigEdit',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-edit.vue'),
      meta: { title: '编辑任务配置', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'taskjobTaskJobConfigDetail',
      component: () => import('@/views/taskjob/task-job-config/task-job-config-detail.vue'),
      meta: { title: '查看任务配置', hideMenu: true },
    },
  ],
};
