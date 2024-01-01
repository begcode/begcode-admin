import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

const settings: AppRouteModule = {
  path: '/system',
  name: 'system',
  component: LAYOUT,
  meta: {
    orderNo: 5000,
    icon: 'tabler:chart-dots',
    title: '系统设置',
  },
  children: [
    // jhipster-needle-add-entity-to-client-root-folder-router-children - JHipster will add entities to the client root folder router here
  ],
};
export default settings;
