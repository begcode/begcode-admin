import { IResourceCategory } from '@/models/files/resource-category.model';

export interface IUploadImage {
  id?: number; //ID
  fullName?: string | null; //完整文件名
  businessTitle?: string | null; //业务标题
  businessDesc?: string | null; //业务自定义描述内容
  businessStatus?: string | null; //业务状态
  url?: string; //Url地址
  name?: string | null; //文件名
  ext?: string | null; //扩展名
  type?: string | null; //文件类型
  path?: string | null; //本地路径
  folder?: string | null; //本地存储目录
  fileSize?: number | null; //文件大小
  ownerEntityName?: string | null; //使用实体名称
  ownerEntityId?: number | null; //使用实体ID
  createAt?: Date | null; //创建时间
  smartUrl?: string | null; //小图Url
  mediumUrl?: string | null; //中等图Url
  referenceCount?: number | null; //文件被引用次数
  createdBy?: number | null; //创建者Id
  createdDate?: Date | null; //创建时间
  lastModifiedBy?: number | null; //修改者Id
  lastModifiedDate?: Date | null; //修改时间
  categoryId?: number | null;
  category?: IResourceCategory | null; //所属分类
}

export class UploadImage implements IUploadImage {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public businessTitle?: string | null,
    public businessDesc?: string | null,
    public businessStatus?: string | null,
    public url?: string,
    public name?: string | null,
    public ext?: string | null,
    public type?: string | null,
    public path?: string | null,
    public folder?: string | null,
    public fileSize?: number | null,
    public ownerEntityName?: string | null,
    public ownerEntityId?: number | null,
    public createAt?: Date | null,
    public smartUrl?: string | null,
    public mediumUrl?: string | null,
    public referenceCount?: number | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public categoryId?: number | null,
    public category?: IResourceCategory | null,
  ) {}
}
