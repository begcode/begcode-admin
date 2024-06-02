import { IAuthority } from '@/models/system/authority.model';

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
  authorities?: IAuthority[] | null; //角色列表
  parentId?: number | null;
  parent?: IDepartment | null; //上级
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
    public authorities?: IAuthority[] | null,
    public parentId?: number | null,
    public parent?: IDepartment | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {}
}
