import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { cacheManageRoutes } from '@/router/routes/modules/system/cache-manage.route';
import { viewPermissionRoutes } from '@/router/routes/modules/system/view-permission.route';
import { authorityRoutes } from '@/router/routes/modules/system/authority.route';
import { userRoutes } from '@/router/routes/modules/system/user.route';
import { apiPermissionRoutes } from '@/router/routes/modules/system/api-permission.route';
import { smsMessageRoutes } from '@/router/routes/modules/system/sms-message.route';
import { announcementRoutes } from '@/router/routes/modules/system/announcement.route';
import { announcementRecordRoutes } from '@/router/routes/modules/system/announcement-record.route';
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
    viewPermissionRoutes,
    authorityRoutes,
    userRoutes,
    apiPermissionRoutes,
    smsMessageRoutes,
    announcementRoutes,
    announcementRecordRoutes,
    // jhipster-needle-add-entity-to-client-root-folder-router-children - JHipster will add entities to the client root folder router here
  ],
};
export default system;
