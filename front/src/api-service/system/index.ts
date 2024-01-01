import cacheManagerService from '@/api-service/system/cache-manager.service';
import viewPermissionService from '@/api-service/system/view-permission.service';
import authorityService from '@/api-service/system/authority.service';
import userService from '@/api-service/system/user.service';
import apiPermissionService from '@/api-service/system/api-permission.service';
import smsMessageService from '@/api-service/system/sms-message.service';
import announcementService from '@/api-service/system/announcement.service';
import announcementRecordService from '@/api-service/system/announcement-record.service';
// jhipster-needle-add-entity-service-to-main-import - JHipster will import entities services here

export default {
  cacheManagerService,
  viewPermissionService,
  authorityService,
  userService,
  apiPermissionService,
  smsMessageService,
  announcementService,
  announcementRecordService,
  // jhipster-needle-add-entity-service-to-main-body - JHipster will import entities services here
};
