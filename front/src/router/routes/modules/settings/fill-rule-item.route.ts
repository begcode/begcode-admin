import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const fillRuleItemRoutes: AppRouteRecordRaw = {
  path: 'fill-rule-item',
  name: 'systemFillRuleItem',
  component: getParentLayout('systemFillRuleItem'),
  meta: {
    title: '填充规则条目',
  },
  redirect: '/settings/fill-rule-item/list',
  children: [
    {
      path: 'list',
      name: 'systemFillRuleItemList',
      component: () => import('@/views/settings/fill-rule-item/fill-rule-item-list.vue'),
      meta: { title: '填充规则条目列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemFillRuleItemNew',
      component: () => import('@/views/settings/fill-rule-item/fill-rule-item-edit.vue'),
      meta: { title: '新建填充规则条目', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemFillRuleItemEdit',
      component: () => import('@/views/settings/fill-rule-item/fill-rule-item-edit.vue'),
      meta: { title: '编辑填充规则条目', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemFillRuleItemDetail',
      component: () => import('@/views/settings/fill-rule-item/fill-rule-item-detail.vue'),
      meta: { title: '查看填充规则条目', hideMenu: true },
    },
  ],
};
