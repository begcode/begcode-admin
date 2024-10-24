import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { departmentRoutes } from '@/router/routes/modules/settings/department.route';
import { positionRoutes } from '@/router/routes/modules/settings/position.route';
import { businessTypeRoutes } from '@/router/routes/modules/settings/business-type.route';
import { dictionaryRoutes } from '@/router/routes/modules/settings/dictionary.route';
import { commonFieldDataRoutes } from '@/router/routes/modules/settings/common-field-data.route';
import { regionCodeRoutes } from '@/router/routes/modules/settings/region-code.route';
import { sysFillRuleRoutes } from '@/router/routes/modules/settings/sys-fill-rule.route';
import { fillRuleItemRoutes } from '@/router/routes/modules/settings/fill-rule-item.route';
import { formConfigRoutes } from '@/router/routes/modules/settings/form-config.route';
import { systemConfigRoutes } from '@/router/routes/modules/settings/system-config.route';
import { formSaveDataRoutes } from '@/router/routes/modules/settings/form-save-data.route';
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
    departmentRoutes,
    positionRoutes,
    businessTypeRoutes,
    dictionaryRoutes,
    commonFieldDataRoutes,
    regionCodeRoutes,
    sysFillRuleRoutes,
    fillRuleItemRoutes,
    formConfigRoutes,
    systemConfigRoutes,
    formSaveDataRoutes,
    // jhipster-needle-add-entity-to-client-root-folder-router-children - JHipster will add entities to the client root folder router here
  ],
};
export default settings;
