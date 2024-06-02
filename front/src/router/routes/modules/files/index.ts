import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { smsTemplateRoutes } from '@/router/routes/modules/files/sms-template.route';
import { smsSupplierRoutes } from '@/router/routes/modules/files/sms-supplier.route';
import { resourceCategoryRoutes } from '@/router/routes/modules/files/resource-category.route';
import { uploadFileRoutes } from '@/router/routes/modules/files/upload-file.route';
import { uploadImageRoutes } from '@/router/routes/modules/files/upload-image.route';
import { ossConfigRoutes } from '@/router/routes/modules/files/oss-config.route';
// jhipster-needle-add-entity-to-client-root-folder-router-import - JHipster will import entities to the client root folder router here

const files: AppRouteModule = {
  path: '/system',
  name: 'system',
  component: LAYOUT,
  meta: {
    orderNo: 5000,
    icon: 'tabler:chart-dots',
    title: '文件管理',
  },
  children: [
    smsTemplateRoutes,
    smsSupplierRoutes,
    resourceCategoryRoutes,
    uploadFileRoutes,
    uploadImageRoutes,
    ossConfigRoutes,
    // jhipster-needle-add-entity-to-client-root-folder-router-children - JHipster will add entities to the client root folder router here
  ],
};
export default files;
