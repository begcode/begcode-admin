export interface IResourceCategory {
  id?: number; //ID
  title?: string | null; //标题
  code?: string | null; //代码
  orderNumber?: number | null; //排序
  parent?: IResourceCategory | null; //上级
  expand?: boolean;
  nzAddLevel?: number;
}

export class ResourceCategory implements IResourceCategory {
  constructor(
    public id?: number,
    public title?: string | null,
    public code?: string | null,
    public orderNumber?: number | null,
    public parent?: IResourceCategory | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {}
}
