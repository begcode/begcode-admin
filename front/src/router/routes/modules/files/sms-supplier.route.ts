import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const smsSupplierRoutes: AppRouteRecordRaw = {
  path: 'sms-supplier',
  name: 'systemSmsSupplier',
  component: getParentLayout('systemSmsSupplier'),
  meta: {
    title: '短信服务商配置',
  },
  redirect: '/files/sms-supplier/list',
  children: [
    {
      path: 'list',
      name: 'systemSmsSupplierList',
      component: () => import('@/views/files/sms-supplier/sms-supplier-list.vue'),
      meta: { title: '短信服务商配置列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemSmsSupplierNew',
      component: () => import('@/views/files/sms-supplier/sms-supplier-edit.vue'),
      meta: { title: '新建短信服务商配置', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemSmsSupplierEdit',
      component: () => import('@/views/files/sms-supplier/sms-supplier-edit.vue'),
      meta: { title: '编辑短信服务商配置', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemSmsSupplierDetail',
      component: () => import('@/views/files/sms-supplier/sms-supplier-detail.vue'),
      meta: { title: '查看短信服务商配置', hideMenu: true },
    },
  ],
};
