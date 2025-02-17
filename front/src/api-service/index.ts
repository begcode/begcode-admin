import accountService from '@/api-service/account/index';
import systemServices from '@/api-service/system/index';
import settingsServices from '@/api-service/settings/index';
import filesServices from '@/api-service/files/index';
import taskjobServices from '@/api-service/taskjob/index';
import logServices from '@/api-service/log/index';
// jhipster-needle-add-entity-service-to-main-import - BegCode will import entities services here

export default {
  account: accountService,
  system: systemServices,
  settings: settingsServices,
  files: filesServices,
  taskjob: taskjobServices,
  log: logServices,
  // jhipster-needle-add-entity-service-to-main-body - BegCode will import entities services here
};
