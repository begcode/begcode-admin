import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const smsTemplateRoutes: AppRouteRecordRaw = {
  path: 'sms-template',
  name: 'systemSmsTemplate',
  component: getParentLayout('systemSmsTemplate'),
  meta: {
    title: '消息模板',
  },
  redirect: '',
  children: [
    {
      path: 'list',
      name: 'systemSmsTemplateList',
      component: () => import('@/views/files/sms-template/sms-template-list.vue'),
      meta: { title: '消息模板列表' },
    },
    {
      path: 'new',
      name: 'systemSmsTemplateNew',
      component: () => import('@/views/files/sms-template/sms-template-edit.vue'),
      meta: { title: '新建消息模板' },
    },
    {
      path: ':entityId/edit',
      name: 'systemSmsTemplateEdit',
      component: () => import('@/views/files/sms-template/sms-template-edit.vue'),
      meta: { title: '编辑消息模板', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemSmsTemplateDetail',
      component: () => import('@/views/files/sms-template/sms-template-detail.vue'),
      meta: { title: '查看消息模板', hideMenu: true },
    },
  ],
};
