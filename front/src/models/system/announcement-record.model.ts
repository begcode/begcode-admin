export interface IAnnouncementRecord {
  id?: number; // ID
  anntId?: number | null; // 通告ID
  userId?: number | null; // 用户id
  hasRead?: boolean | null; // 是否已读
  readTime?: Date | null; // 阅读时间
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
}

export class AnnouncementRecord implements IAnnouncementRecord {
  constructor(
    public id?: number,
    public anntId?: number | null,
    public userId?: number | null,
    public hasRead?: boolean | null,
    public readTime?: Date | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
  ) {
    this.hasRead = this.hasRead ?? false;
  }
}
