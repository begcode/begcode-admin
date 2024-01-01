import { defHttp } from '@/utils/http/axios';
import { LoginParams } from '@/api-service/sys/model/userModel';
import { ErrorMessageMode } from '@/types/axios';
import { User } from '@/models/system/user.model';

const baseApiUrl = '/api';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

export default {
  authenticateWithoutCaptcha(params: LoginParams, mode: ErrorMessageMode = 'modal'): Promise<Object> {
    return defHttp.post({ url: `${baseApiUrl}/authenticate/withoutCaptcha`, params }, { errorMessageMode: mode });
  },
  authenticate(params: LoginParams, mode: ErrorMessageMode = 'modal'): Promise<Object> {
    return defHttp.post({ url: `${baseApiUrl}/authenticate`, params }, { errorMessageMode: mode });
  },
  getAccount(mode: ErrorMessageMode = 'none'): Promise<User> {
    return defHttp.get({ url: `${baseApiUrl}/account` }, { errorMessageMode: mode });
  },
  updateImageUrl(url: string): Promise<Object> {
    return defHttp.put({ url: `${baseApiUrl}/account/imageUrl`, params: `?imageUrl=${url}` }, { errorMessageMode: 'none' });
  },
  updateAccount(userInfo: any): Promise<User> {
    return defHttp.post({ url: `${baseApiUrl}/account`, params: userInfo }, { errorMessageMode: 'none' });
  },
  changePassword(param: any): Promise<Object> {
    return defHttp.post({ url: `${baseApiUrl}/account/change-password`, params: param }, { errorMessageMode: 'none' });
  },
  resetPasswordSmsCode(param: any): Promise<string> {
    return defHttp.get({ url: `${baseApiUrl}/mobile/reset-password/smscode`, params: param }, { errorMessageMode: 'none' });
  },
  resetPasswordFinish(param: any): Promise<boolean> {
    return defHttp.post({ url: `${baseApiUrl}/mobile/reset-password/finish`, params: param }, { errorMessageMode: 'none' });
  },
  checkExistUser(param: any): Promise<boolean> {
    return defHttp.get({ url: `${baseApiUrl}/users/check`, params: param }, { errorMessageMode: 'none' });
  },

  // jhipster-needle-service-add-method - BegCode will add getters and setters here, do not remove
};
