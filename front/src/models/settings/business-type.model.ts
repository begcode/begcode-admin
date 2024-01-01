export interface IBusinessType {
  id?: number; //ID
  name?: string | null; //名称
  code?: string | null; //代码
  description?: string | null; //描述
  icon?: string | null; //图标
}

export class BusinessType implements IBusinessType {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public description?: string | null,
    public icon?: string | null,
  ) {}
}
