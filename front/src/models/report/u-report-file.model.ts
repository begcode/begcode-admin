export interface IUReportFile {
  id?: number; //ID
  name?: string; //名称
  content?: string | null; //内容
  createAt?: Date | null; //创建时间
  updateAt?: Date | null; //更新时间
}

export class UReportFile implements IUReportFile {
  constructor(
    public id?: number,
    public name?: string,
    public content?: string | null,
    public createAt?: Date | null,
    public updateAt?: Date | null,
  ) {}
}
