import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { t } from '@/hooks/web/useI18n';

const account: AppRouteModule = {
  path: '/account',
  name: 'Account',
  component: LAYOUT,
  redirect: '/account/settings',
  meta: {
    hideChildrenInMenu: true,
    icon: 'simple-icons:aboutdotme',
    title: t('routes.dashboard.account'),
    orderNo: 100000,
    hideMenu: true,
  },
  children: [
    {
      path: 'settings',
      name: 'AboutPage',
      component: () => import('@/views/account/setting/index.vue'),
      meta: {
        title: t('routes.dashboard.account'),
        icon: 'simple-icons:aboutdotme',
        hideMenu: true,
      },
    },
  ],
};

export default account;
