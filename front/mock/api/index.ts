import { setViewPermissionMock } from 'mock/system/view-permission.mock';
import { setAuthorityMock } from 'mock/system/authority.mock';
import { setUserMock } from 'mock/system/user.mock';
import { setApiPermissionMock } from 'mock/system/api-permission.mock';
import { setDepartmentMock } from 'mock/settings/department.mock';
import { setPositionMock } from 'mock/settings/position.mock';
import { setBusinessTypeMock } from 'mock/settings/business-type.mock';
import { setSmsTemplateMock } from 'mock/files/sms-template.mock';
import { setSmsSupplierMock } from 'mock/files/sms-supplier.mock';
import { setResourceCategoryMock } from 'mock/files/resource-category.mock';
import { setUploadFileMock } from 'mock/files/upload-file.mock';
import { setUploadImageMock } from 'mock/files/upload-image.mock';
import { setOssConfigMock } from 'mock/files/oss-config.mock';
import { setDictionaryMock } from 'mock/settings/dictionary.mock';
import { setSiteConfigMock } from 'mock/settings/site-config.mock';
import { setCommonFieldDataMock } from 'mock/settings/common-field-data.mock';
import { setRegionCodeMock } from 'mock/settings/region-code.mock';
import { setSysFillRuleMock } from 'mock/settings/sys-fill-rule.mock';
import { setFillRuleItemMock } from 'mock/settings/fill-rule-item.mock';
import { setUReportFileMock } from 'mock/report/u-report-file.mock';
import { setSmsMessageMock } from 'mock/system/sms-message.mock';
import { setAnnouncementMock } from 'mock/system/announcement.mock';
import { setAnnouncementRecordMock } from 'mock/system/announcement-record.mock';
import { setTaskJobConfigMock } from 'mock/taskjob/task-job-config.mock';
import { setSysLogMock } from 'mock/log/sys-log.mock';
import { setFormConfigMock } from 'mock/settings/form-config.mock';
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
