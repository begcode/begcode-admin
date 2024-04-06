import { ResetFrequency } from '@/models/enumerations/reset-frequency.model';
export interface ISysFillRule {
  id?: number; //ID
  name?: string | null; //规则名称
  code?: string | null; //规则Code
  desc?: string | null; //规则描述
  enabled?: boolean | null; //是否启用
  resetFrequency?: keyof typeof ResetFrequency | null; //重置频率
  seqValue?: number | null; //序列值
  fillValue?: string | null; //生成值
  implClass?: string | null; //规则实现类
  params?: string | null; //规则参数
  resetStartTime?: Date | null; //重置开始日期
  resetEndTime?: Date | null; //重置结束日期
  resetTime?: Date | null; //重置时间
}

export class SysFillRule implements ISysFillRule {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public desc?: string | null,
    public enabled?: boolean | null,
    public resetFrequency?: keyof typeof ResetFrequency | null,
    public seqValue?: number | null,
    public fillValue?: string | null,
    public implClass?: string | null,
    public params?: string | null,
    public resetStartTime?: Date | null,
    public resetEndTime?: Date | null,
    public resetTime?: Date | null,
  ) {
    this.enabled = this.enabled ?? false;
  }
}
