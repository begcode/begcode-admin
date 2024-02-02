import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const sysFillRuleRoutes: AppRouteRecordRaw = {
  path: 'sys-fill-rule',
  name: 'systemSysFillRule',
  component: getParentLayout('systemSysFillRule'),
  meta: {
    title: '填充规则',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemSysFillRuleList',
      component: () => import('@/views/settings/sys-fill-rule/sys-fill-rule-list.vue'),
      meta: { title: '填充规则列表' },
    },
    {
      path: 'new',
      name: 'systemSysFillRuleNew',
      component: () => import('@/views/settings/sys-fill-rule/sys-fill-rule-edit.vue'),
      meta: { title: '新建填充规则' },
    },
    {
      path: ':entityId/edit',
      name: 'systemSysFillRuleEdit',
      component: () => import('@/views/settings/sys-fill-rule/sys-fill-rule-edit.vue'),
      meta: { title: '编辑填充规则', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemSysFillRuleDetail',
      component: () => import('@/views/settings/sys-fill-rule/sys-fill-rule-detail.vue'),
      meta: { title: '查看填充规则', hideMenu: true },
    },
  ],
};
