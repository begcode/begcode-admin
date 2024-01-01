import { ICommonFieldData } from '@/models/settings/common-field-data.model';

export interface IDictionary {
  id?: number; //ID
  dictName?: string; //字典名称
  dictKey?: string; //字典Key
  disabled?: boolean | null; //是否禁用
  sortValue?: number | null; //排序
  builtIn?: boolean | null; //是否内置
  syncEnum?: boolean | null; //更新枚举
  items?: ICommonFieldData[] | null; //字典项列表
}

export class Dictionary implements IDictionary {
  constructor(
    public id?: number,
    public dictName?: string,
    public dictKey?: string,
    public disabled?: boolean | null,
    public sortValue?: number | null,
    public builtIn?: boolean | null,
    public syncEnum?: boolean | null,
    public items?: ICommonFieldData[] | null,
  ) {
    this.disabled = this.disabled ?? false;
    this.builtIn = this.builtIn ?? false;
    this.syncEnum = this.syncEnum ?? false;
  }
}
