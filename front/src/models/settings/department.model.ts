import { IAuthority } from '@/models/system/authority.model';
import { IUser } from '@/models/system/user.model';

export interface IDepartment {
  id?: number; //ID
  name?: string | null; //名称
  code?: string | null; //代码
  address?: string | null; //地址
  phoneNum?: string | null; //联系电话
  logo?: string | null; //logo地址
  contact?: string | null; //联系人
  createUserId?: number | null; //创建用户 Id
  createTime?: Date | null; //创建时间
  children?: IDepartment[] | null; //下级部门
  authorities?: IAuthority[] | null; //角色列表
  parent?: IDepartment | null; //上级
  users?: IUser[] | null; //用户列表
  expand?: boolean;
  nzAddLevel?: number;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public address?: string | null,
    public phoneNum?: string | null,
    public logo?: string | null,
    public contact?: string | null,
    public createUserId?: number | null,
    public createTime?: Date | null,
    public children?: IDepartment[] | null,
    public authorities?: IAuthority[] | null,
    public parent?: IDepartment | null,
    public users?: IUser[] | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {}
}
