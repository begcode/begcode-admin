/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username: string;
  password: string;
}

export interface MobileLoginParams {
  mobile: string;
  code: string;
  rememberMe?: boolean;
  imageCode?: boolean;
}

export interface ThirdLoginParams {
  token: string;
  thirdType: string;
}

export interface RoleInfo {
  roleName: string;
  value: string;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  id?: string | number;
  userId?: string | number;
  token?: string;
  roles?: RoleInfo[];
  userInfo?: any;
  id_token?: string;
}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  roles: RoleInfo[];
  // 用户id
  userId: string | number;
  // 用户名
  username: string;
  // 真实名字
  realname: string;
  // 头像
  avatar: string;
  // 介绍
  desc?: string;
  // 用户信息
  userInfo?: any;
  // 缓存字典项
  sysAllDictItems?: any;
  login?: string;
}
