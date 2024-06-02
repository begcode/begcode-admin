import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const smsMessageRoutes: AppRouteRecordRaw = {
  path: 'sms-message',
  name: 'systemSmsMessage',
  component: getParentLayout('systemSmsMessage'),
  meta: {
    title: '短信消息',
  },
  redirect: '/system/sms-message/list',
  children: [
    {
      path: 'list',
      name: 'systemSmsMessageList',
      component: () => import('@/views/system/sms-message/sms-message-list.vue'),
      meta: { title: '短信消息列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemSmsMessageNew',
      component: () => import('@/views/system/sms-message/sms-message-edit.vue'),
      meta: { title: '新建短信消息', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemSmsMessageEdit',
      component: () => import('@/views/system/sms-message/sms-message-edit.vue'),
      meta: { title: '编辑短信消息', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemSmsMessageDetail',
      component: () => import('@/views/system/sms-message/sms-message-detail.vue'),
      meta: { title: '查看短信消息', hideMenu: true },
    },
  ],
};
