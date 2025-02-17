import { IResourceCategory } from '@/models/files/resource-category.model';

export interface IUploadFile {
  id?: number; // ID
  fullName?: string | null; // 完整文件名
  businessTitle?: string | null; // 业务标题
  businessDesc?: string | null; // 业务自定义描述内容
  businessStatus?: string | null; // 业务状态
  url?: string; // Url地址
  name?: string | null; // 文件名
  thumb?: string | null; // 缩略图Url地址
  fileSize?: number | null; // 文件大小
  ext?: string | null; // 扩展名
  type?: string | null; // 文件类型
  path?: string | null; // 本地路径
  folder?: string | null; // 存储目录
  ownerEntityName?: string | null; // 实体名称
  ownerEntityId?: number | null; // 使用实体ID
  createAt?: Date | null; // 创建时间
  referenceCount?: number | null; // 被引次数
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
  categoryId?: number | null;
  category?: IResourceCategory | null; // 所属分类
}

export class UploadFile implements IUploadFile {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public businessTitle?: string | null,
    public businessDesc?: string | null,
    public businessStatus?: string | null,
    public url?: string,
    public name?: string | null,
    public thumb?: string | null,
    public fileSize?: number | null,
    public ext?: string | null,
    public type?: string | null,
    public path?: string | null,
    public folder?: string | null,
    public ownerEntityName?: string | null,
    public ownerEntityId?: number | null,
    public createAt?: Date | null,
    public referenceCount?: number | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public categoryId?: number | null,
    public category?: IResourceCategory | null,
  ) {}
}
