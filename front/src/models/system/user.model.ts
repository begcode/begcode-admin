import { IDepartment } from '@/models/settings/department.model';
import { IPosition } from '@/models/settings/position.model';
import { IAuthority } from '@/models/system/authority.model';

export interface IUser {
  id?: number; // 用户ID
  login?: string; // 账户名
  password?: string | null; // 密码
  firstName?: string | null; // 名字
  lastName?: string | null; // 姓氏
  email?: string; // 电子邮箱
  mobile?: string | null; // 手机号码
  birthday?: Date | null; // 出生日期
  activated?: boolean | null; // 激活状态
  langKey?: string | null; // 语言Key
  imageUrl?: string | null; // 头像地址
  activationKey?: string | null; // 激活Key
  resetKey?: string | null; // 重置码
  resetDate?: Date | null; // 重置时间
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
  departmentId?: number | null;
  department?: IDepartment | null; // 部门
  positionId?: number | null;
  position?: IPosition | null; // 岗位
  authorities?: IAuthority[] | null; // 角色列表
}

export class User implements IUser {
  constructor(
    public id?: number,
    public login?: string,
    public password?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string,
    public mobile?: string | null,
    public birthday?: Date | null,
    public activated?: boolean | null,
    public langKey?: string | null,
    public imageUrl?: string | null,
    public activationKey?: string | null,
    public resetKey?: string | null,
    public resetDate?: Date | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public departmentId?: number | null,
    public department?: IDepartment | null,
    public positionId?: number | null,
    public position?: IPosition | null,
    public authorities?: IAuthority[] | null,
  ) {
    this.activated = this.activated ?? false;
  }
}
