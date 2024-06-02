import { ISysFillRule } from '@/models/settings/sys-fill-rule.model';

import { FieldParamType } from '@/models/enumerations/field-param-type.model';
export interface IFillRuleItem {
  id?: number; //ID
  sortValue?: number | null; //排序值
  fieldParamType?: keyof typeof FieldParamType | null; //字段参数类型
  fieldParamValue?: string | null; //字段参数值
  datePattern?: string | null; //日期格式
  seqLength?: number | null; //序列长度
  seqIncrement?: number | null; //序列增量
  seqStartValue?: number | null; //序列起始值
  fillRuleId?: number | null;
  fillRule?: ISysFillRule | null; //填充规则
}

export class FillRuleItem implements IFillRuleItem {
  constructor(
    public id?: number,
    public sortValue?: number | null,
    public fieldParamType?: keyof typeof FieldParamType | null,
    public fieldParamValue?: string | null,
    public datePattern?: string | null,
    public seqLength?: number | null,
    public seqIncrement?: number | null,
    public seqStartValue?: number | null,
    public fillRuleId?: number | null,
    public fillRule?: ISysFillRule | null,
  ) {}
}
