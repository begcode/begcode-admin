import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { cacheManageRoutes } from '@/router/routes/modules/system/cache-manage.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

const system: AppRouteModule = {
  path: '/system',
  name: 'system',
  component: LAYOUT,
  meta: {
    orderNo: 5000,
    icon: 'tabler:chart-dots',
    title: '系统设置',
  },
  children: [
    cacheManageRoutes,
    // jhipster-needle-add-entity-to-client-root-folder-router - JHipster will add entities to the client root folder router here
  ],
};
export default system;
