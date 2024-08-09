import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { t } from '@/hooks/web/useI18n';
const IFrame = () => import('@/views/system/iframe/FrameBlank.vue');

const dev: AppRouteModule = {
  path: '/dev',
  name: 'Dev',
  component: LAYOUT,
  redirect: '/dev/doc',
  meta: {
    orderNo: 1000,
    icon: 'ion:tv-outline',
    title: t('routes.dev.title'),
  },

  children: [
    {
      path: 'doc',
      name: 'Doc',
      component: IFrame,
      meta: {
        frameSrc: '/static/swagger-ui/index.html',
        title: t('routes.dev.doc'),
      },
    },
    {
      path: 'icon',
      name: 'IconDemo',
      component: () => import('@/views/dev/feat/icon/index.vue'),
      meta: {
        title: 'ICON选择',
      },
    },
  ],
};

export default dev;
