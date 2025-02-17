import { ICommonFieldData } from '@/models/settings/common-field-data.model';

export interface ISystemConfig {
  id?: number; // ID
  categoryName?: string; // 分类名称
  categoryKey?: string; // 分类Key
  disabled?: boolean | null; // 是否禁用
  sortValue?: number | null; // 排序
  builtIn?: boolean | null; // 是否内置
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
  items?: ICommonFieldData[] | null; // 配置项列表
}

export class SystemConfig implements ISystemConfig {
  constructor(
    public id?: number,
    public categoryName?: string,
    public categoryKey?: string,
    public disabled?: boolean | null,
    public sortValue?: number | null,
    public builtIn?: boolean | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public items?: ICommonFieldData[] | null,
  ) {
    this.disabled = this.disabled ?? false;
    this.builtIn = this.builtIn ?? false;
  }
}
