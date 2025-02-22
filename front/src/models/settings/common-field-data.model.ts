import { CommonFieldType } from '@/models/enumerations/common-field-type.model';
export interface ICommonFieldData {
  id?: number; // ID
  name?: string | null; // 名称
  value?: string | null; // 字段值
  label?: string | null; // 字段标题
  valueType?: keyof typeof CommonFieldType | null; // 字段类型
  remark?: string | null; // 说明
  sortValue?: number | null; // 排序
  disabled?: boolean | null; // 是否禁用
  ownerEntityName?: string | null; // 实体名称
  ownerEntityId?: number | null; // 使用实体ID
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
    public ownerEntityId?: number | null,
  ) {
    this.disabled = this.disabled ?? false;
  }
}
