import { ISiteConfig } from '@/models/settings/site-config.model';
import { IDictionary } from '@/models/settings/dictionary.model';

import { CommonFieldType } from '@/models/enumerations/common-field-type.model';
export interface ICommonFieldData {
  id?: number; //ID
  name?: string | null; //名称
  value?: string | null; //字段值
  label?: string | null; //字段标题
  valueType?: keyof typeof CommonFieldType | null; //字段类型
  remark?: string | null; //说明
  sortValue?: number | null; //排序
  disabled?: boolean | null; //是否禁用
  ownerEntityName?: string | null; //实体名称
  ownerEntityId?: string | null; //使用实体ID
  siteConfig?: ISiteConfig | null; //Site Config
  dictionary?: IDictionary | null; //Dictionary
}

export class CommonFieldData implements ICommonFieldData {
  constructor(
    public id?: number,
    public name?: string | null,
    public value?: string | null,
    public label?: string | null,
    public valueType?: keyof typeof CommonFieldType | null,
    public remark?: string | null,
    public sortValue?: number | null,
    public disabled?: boolean | null,
    public ownerEntityName?: string | null,
    public ownerEntityId?: string | null,
    public siteConfig?: ISiteConfig | null,
    public dictionary?: IDictionary | null,
  ) {
    this.disabled = this.disabled ?? false;
  }
}
