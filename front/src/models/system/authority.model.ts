import { IViewPermission } from '@/models/system/view-permission.model';
import { IApiPermission } from '@/models/system/api-permission.model';
import { IUser } from '@/models/system/user.model';
import { IDepartment } from '@/models/settings/department.model';

export interface IAuthority {
  id?: number; //ID
  name?: string | null; //角色名称
  code?: string | null; //角色代号
  info?: string | null; //信息
  order?: number | null; //排序
  display?: boolean | null; //展示
  children?: IAuthority[] | null; //子节点
  viewPermissions?: IViewPermission[] | null; //菜单列表
  apiPermissions?: IApiPermission[] | null; //Api权限列表
  parent?: IAuthority | null; //上级
  users?: IUser[] | null; //用户列表
  departments?: IDepartment[] | null; //Department
  expand?: boolean;
  nzAddLevel?: number;
}

export class Authority implements IAuthority {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public info?: string | null,
    public order?: number | null,
    public display?: boolean | null,
    public children?: IAuthority[] | null,
    public viewPermissions?: IViewPermission[] | null,
    public apiPermissions?: IApiPermission[] | null,
    public parent?: IAuthority | null,
    public users?: IUser[] | null,
    public departments?: IDepartment[] | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {
    this.display = this.display ?? false;
  }
}
