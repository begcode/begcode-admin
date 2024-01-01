import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const departmentRoutes: AppRouteRecordRaw = {
  path: 'department',
  name: 'systemDepartment',
  component: getParentLayout('systemDepartment'),
  meta: {
    title: '部门',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemDepartmentList',
      component: () => import('@/views/settings/department/department-list.vue'),
      meta: { title: '部门列表' },
    },
    {
      path: 'new',
      name: 'systemDepartmentNew',
      component: () => import('@/views/settings/department/department-edit.vue'),
      meta: { title: '新建部门' },
    },
    {
      path: ':entityId/edit',
      name: 'systemDepartmentEdit',
      component: () => import('@/views/settings/department/department-edit.vue'),
      meta: { title: '编辑部门', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemDepartmentDetail',
      component: () => import('@/views/settings/department/department-detail.vue'),
      meta: { title: '查看部门', hideMenu: true },
    },
  ],
};
