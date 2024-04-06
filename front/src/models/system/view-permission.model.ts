import { IAuthority } from '@/models/system/authority.model';

import { ViewPermissionType } from '@/models/enumerations/view-permission-type.model';
import { TargetType } from '@/models/enumerations/target-type.model';
export interface IViewPermission {
  id?: number; //ID
  text?: string; //权限名称
  type?: keyof typeof ViewPermissionType | null; //权限类型
  i18n?: string | null; //i18n主键
  group?: boolean | null; //显示分组名
  link?: string | null; //路由
  externalLink?: string | null; //外部链接
  target?: keyof typeof TargetType | null; //链接目标
  icon?: string | null; //图标
  disabled?: boolean | null; //禁用菜单
  hide?: boolean | null; //隐藏菜单
  hideInBreadcrumb?: boolean | null; //隐藏面包屑
  shortcut?: boolean | null; //快捷菜单项
  shortcutRoot?: boolean | null; //菜单根节点
  reuse?: boolean | null; //允许复用
  code?: string; //权限代码
  description?: string | null; //权限描述
  order?: number | null; //排序
  apiPermissionCodes?: string | null; //api权限标识串
  componentFile?: string | null; //组件名称
  redirect?: string | null; //重定向路径
  parent?: IViewPermission | null; //上级
  authorities?: IAuthority[] | null; //角色列表
  expand?: boolean;
  nzAddLevel?: number;
}

export class ViewPermission implements IViewPermission {
  constructor(
    public id?: number,
    public text?: string,
    public type?: keyof typeof ViewPermissionType | null,
    public i18n?: string | null,
    public group?: boolean | null,
    public link?: string | null,
    public externalLink?: string | null,
    public target?: keyof typeof TargetType | null,
    public icon?: string | null,
    public disabled?: boolean | null,
    public hide?: boolean | null,
    public hideInBreadcrumb?: boolean | null,
    public shortcut?: boolean | null,
    public shortcutRoot?: boolean | null,
    public reuse?: boolean | null,
    public code?: string,
    public description?: string | null,
    public order?: number | null,
    public apiPermissionCodes?: string | null,
    public componentFile?: string | null,
    public redirect?: string | null,
    public parent?: IViewPermission | null,
    public authorities?: IAuthority[] | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {
    this.group = this.group ?? false;
    this.disabled = this.disabled ?? false;
    this.hide = this.hide ?? false;
    this.hideInBreadcrumb = this.hideInBreadcrumb ?? false;
    this.shortcut = this.shortcut ?? false;
    this.shortcutRoot = this.shortcutRoot ?? false;
    this.reuse = this.reuse ?? false;
  }
}
