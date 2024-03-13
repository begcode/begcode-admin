import { LoginParams, MobileLoginParams, LoginResultModel, GetUserInfoModel } from './model/userModel';
import { defHttp } from '@/utils/http/axios';

import { ErrorMessageMode } from '#/axios';
import { useMessage } from '@/hooks/web/useMessage';
import { useUserStoreWithOut } from '@/store/modules/user';
import { setAuthCache } from '@/utils/auth';
import { TOKEN_KEY } from '@/enums/cacheEnum';
import { PageEnum } from '@/enums/pageEnum';
import { router } from '@/router';

const { createErrorModal } = useMessage();

enum Api {
  Login = '/sys/login',
  phoneLogin = '/api/authenticate/mobile',
  Logout = '/logout',
  GetUserInfo = '/api/account',
  TestRetry = '/testRetry',
  // 获取系统权限
  // 1、查询用户拥有的按钮/表单访问权限
  // 2、所有权限
  // 3、系统安全模式
  GetPermCode = '/sys/permission/getPermCode',
  //新加的获取图形验证码的接口
  getInputCode = '/api/randomImage',
  //获取短信验证码的接口
  getCaptcha = '/sys/sms',
  //注册接口
  registerApi = '/sys/user/register',
  //校验用户接口
  checkOnlyUser = '/sys/user/checkOnlyUser',
  //SSO登录校验
  validateCasLogin = '/sys/cas/client/validateLogin',
  //校验手机号
  phoneVerify = '/sys/user/phoneVerification',
  //修改密码
  passwordChange = '/sys/user/passwordChange',
  //第三方登录
  thirdLogin = '/sys/thirdLogin/getLoginUser',
  //第三方登录
  getThirdCaptcha = '/sys/thirdSms',
  //获取二维码信息
  getLoginQrcode = '/sys/getLoginQrcode',
  //监控二维码扫描状态
  getQrcodeToken = '/sys/getQrcodeToken',
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginResultModel>(
    {
      url: '/api/authenticate/withoutCaptcha',
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * @description: user phoneLogin api
 */
export function phoneLoginApi(params: MobileLoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginResultModel>(
    {
      url: Api.phoneLogin,
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, {}).catch(e => {
    if (e && (e.message.includes('timeout') || e.message.includes('401'))) {
      //接口不通时跳转到登录界面
      const userStore = useUserStoreWithOut();
      userStore.setToken('');
      setAuthCache(TOKEN_KEY, null);
      router.push({
        path: PageEnum.BASE_LOGIN,
        query: {
          redirect: router.currentRoute.value.fullPath,
        },
      });
    }
  });
}

export function getPermCode() {
  return defHttp.get<string[]>({ url: Api.GetPermCode });
}

export function getImageCaptcha(currdatetime) {
  return defHttp.get({ url: `/api/randomImage/${currdatetime}` });
}

export function getCodeInfo(currdatetime) {
  let url = Api.getInputCode + `/${currdatetime}`;
  return defHttp.get({ url: url });
}

/**
 * @description: 获取短信验证码
 */
export function getSmsCaptcha(params) {
  return defHttp.get({ url: '/api/mobile/smscode', params }, { isTransformResponse: false });
}

/**
 * @description: 用户注册
 */
export function register(params) {
  return defHttp.post({ url: '/api/register', params }, { isTransformResponse: false });
}

/**
 *校验用户是否存在
 * @param params
 */
export const checkOnlyUser = params => defHttp.get({ url: Api.checkOnlyUser, params }, { isTransformResponse: false });

/**
 *校验手机号码
 * @param params
 */
export const phoneVerify = params => defHttp.post({ url: Api.phoneVerify, params }, { isTransformResponse: false });

/**
 *密码修改
 * @param params
 */
export const passwordChange = params => defHttp.get({ url: Api.passwordChange, params }, { isTransformResponse: false });

/**
 * @description: 第三方登录
 */
export function thirdLogin(params, mode: ErrorMessageMode = 'modal') {
  let tenantId = '0';
  if (!params.tenantId) {
    tenantId = params.tenantId;
  }
  return defHttp.get<LoginResultModel>(
    {
      url: `${Api.thirdLogin}/${params.token}/${params.thirdType}/${tenantId}`,
    },
    {
      errorMessageMode: mode,
    },
  );
}
/**
 * @description: 获取第三方短信验证码
 */
export function setThirdCaptcha(params) {
  return new Promise((resolve, reject) => {
    defHttp.post({ url: Api.getThirdCaptcha, params }, { isTransformResponse: false }).then(res => {
      console.log(res);
      if (res.success) {
        resolve(true);
      } else {
        createErrorModal({ title: '错误提示', content: res.message || '未知问题' });
        reject();
      }
    });
  });
}

/**
 * 获取登录二维码信息
 */
export function getLoginQrcode() {
  return defHttp.get({ url: Api.getLoginQrcode });
}

/**
 * 监控扫码状态
 */
export function getQrcodeToken(params) {
  return defHttp.get({ url: Api.getQrcodeToken, params });
}

/**
 * SSO登录校验
 */
export async function validateCasLogin(params) {
  return defHttp.get({ url: Api.validateCasLogin, params });
}

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}

export function testRetry() {
  return defHttp.get(
    { url: Api.TestRetry },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}
