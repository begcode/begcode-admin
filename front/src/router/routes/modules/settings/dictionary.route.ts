import { AppRouteRecordRaw } from '@/router/types';
import { getParentLayout } from '@/router/constant';

export const dictionaryRoutes: AppRouteRecordRaw = {
  path: 'dictionary',
  name: 'systemDictionary',
  component: getParentLayout('systemDictionary'),
  meta: {
    title: '数据字典',
  },
  redirect: '/settings/dictionary/list',
  children: [
    {
      path: 'list',
      name: 'systemDictionaryList',
      component: () => import('@/views/settings/dictionary/dictionary-list.vue'),
      meta: { title: '数据字典列表', hideMenu: true },
    },
    {
      path: 'new',
      name: 'systemDictionaryNew',
      component: () => import('@/views/settings/dictionary/dictionary-edit.vue'),
      meta: { title: '新建数据字典', hideMenu: true },
    },
    {
      path: ':entityId/edit',
      name: 'systemDictionaryEdit',
      component: () => import('@/views/settings/dictionary/dictionary-edit.vue'),
      meta: { title: '编辑数据字典', hideMenu: true },
    },
    {
      path: ':entityId/detail',
      name: 'systemDictionaryDetail',
      component: () => import('@/views/settings/dictionary/dictionary-detail.vue'),
      meta: { title: '查看数据字典', hideMenu: true },
    },
  ],
};
