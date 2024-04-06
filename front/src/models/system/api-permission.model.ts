import { IAuthority } from '@/models/system/authority.model';

import { ApiPermissionType } from '@/models/enumerations/api-permission-type.model';
import { ApiPermissionState } from '@/models/enumerations/api-permission-state.model';
export interface IApiPermission {
  id?: number; //ID
  serviceName?: string | null; //服务名称
  name?: string | null; //权限名称
  code?: string | null; //Code
  description?: string | null; //权限描述
  type?: keyof typeof ApiPermissionType | null; //类型
  method?: string | null; //请求类型
  url?: string | null; //url 地址
  status?: keyof typeof ApiPermissionState | null; //状态
  parent?: IApiPermission | null; //上级
  authorities?: IAuthority[] | null; //角色列表
  expand?: boolean;
  nzAddLevel?: number;
}

export class ApiPermission implements IApiPermission {
  constructor(
    public id?: number,
    public serviceName?: string | null,
    public name?: string | null,
    public code?: string | null,
    public description?: string | null,
    public type?: keyof typeof ApiPermissionType | null,
    public method?: string | null,
    public url?: string | null,
    public status?: keyof typeof ApiPermissionState | null,
    public parent?: IApiPermission | null,
    public authorities?: IAuthority[] | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {}
}
