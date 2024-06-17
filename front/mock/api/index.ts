import { setViewPermissionMock } from 'mock/api/system/view-permission.mock';
import { setAuthorityMock } from 'mock/api/system/authority.mock';
import { setUserMock } from 'mock/api/system/user.mock';
import { setApiPermissionMock } from 'mock/api/system/api-permission.mock';
import { setDepartmentMock } from 'mock/api/settings/department.mock';
import { setPositionMock } from 'mock/api/settings/position.mock';
import { setBusinessTypeMock } from 'mock/api/settings/business-type.mock';
import { setSmsTemplateMock } from 'mock/api/files/sms-template.mock';
import { setSmsSupplierMock } from 'mock/api/files/sms-supplier.mock';
import { setResourceCategoryMock } from 'mock/api/files/resource-category.mock';
import { setUploadFileMock } from 'mock/api/files/upload-file.mock';
import { setUploadImageMock } from 'mock/api/files/upload-image.mock';
import { setOssConfigMock } from 'mock/api/files/oss-config.mock';
import { setDictionaryMock } from 'mock/api/settings/dictionary.mock';
import { setSiteConfigMock } from 'mock/api/settings/site-config.mock';
import { setCommonFieldDataMock } from 'mock/api/settings/common-field-data.mock';
import { setRegionCodeMock } from 'mock/api/settings/region-code.mock';
import { setSysFillRuleMock } from 'mock/api/settings/sys-fill-rule.mock';
import { setFillRuleItemMock } from 'mock/api/settings/fill-rule-item.mock';
import { setUReportFileMock } from 'mock/api/report/u-report-file.mock';
import { setSmsMessageMock } from 'mock/api/system/sms-message.mock';
import { setAnnouncementMock } from 'mock/api/system/announcement.mock';
import { setAnnouncementRecordMock } from 'mock/api/system/announcement-record.mock';
import { setTaskJobConfigMock } from 'mock/api/taskjob/task-job-config.mock';
import { setSysLogMock } from 'mock/api/log/sys-log.mock';
import { setFormConfigMock } from 'mock/api/settings/form-config.mock';
import { setAuthMock } from './authenticate.mock';
// jhipster-needle-add-import-mock JHipster will add import statement, do not remove

export function setupMockServer(mock) {
  setAuthMock(mock);
  setViewPermissionMock(mock);
  setAuthorityMock(mock);
  setUserMock(mock);
  setApiPermissionMock(mock);
  setDepartmentMock(mock);
  setPositionMock(mock);
  setBusinessTypeMock(mock);
  setSmsTemplateMock(mock);
  setSmsSupplierMock(mock);
  setResourceCategoryMock(mock);
  setUploadFileMock(mock);
  setUploadImageMock(mock);
  setOssConfigMock(mock);
  setDictionaryMock(mock);
  setSiteConfigMock(mock);
  setCommonFieldDataMock(mock);
  setRegionCodeMock(mock);
  setSysFillRuleMock(mock);
  setFillRuleItemMock(mock);
  setUReportFileMock(mock);
  setSmsMessageMock(mock);
  setAnnouncementMock(mock);
  setAnnouncementRecordMock(mock);
  setTaskJobConfigMock(mock);
  setSysLogMock(mock);
  setFormConfigMock(mock);
  // jhipster-needle-add-set-mock JHipster will add import statement, do not remove
}
