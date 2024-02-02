import { IResourceCategory } from '@/models/files/resource-category.model';

export interface IUploadImage {
  id?: number; //ID
  url?: string; //Url地址
  fullName?: string | null; //完整文件名
  name?: string | null; //文件名
  ext?: string | null; //扩展名
  type?: string | null; //文件类型
  path?: string | null; //本地路径
  folder?: string | null; //本地存储目录
  ownerEntityName?: string | null; //使用实体名称
  ownerEntityId?: number | null; //使用实体ID
  businessTitle?: string | null; //业务标题
  businessDesc?: string | null; //业务自定义描述内容
  businessStatus?: string | null; //业务状态
  createAt?: Date | null; //创建时间
  fileSize?: number | null; //文件大小
  smartUrl?: string | null; //小图Url
  mediumUrl?: string | null; //中等图Url
  referenceCount?: number | null; //文件被引用次数
  createdBy?: number | null; //创建者Id
  createdDate?: Date | null; //创建时间
  lastModifiedBy?: number | null; //修改者Id
  lastModifiedDate?: Date | null; //修改时间
  category?: IResourceCategory | null; //所属分类
}

export class UploadImage implements IUploadImage {
  constructor(
    public id?: number,
    public url?: string,
    public fullName?: string | null,
    public name?: string | null,
    public ext?: string | null,
    public type?: string | null,
    public path?: string | null,
    public folder?: string | null,
    public ownerEntityName?: string | null,
    public ownerEntityId?: number | null,
    public businessTitle?: string | null,
    public businessDesc?: string | null,
    public businessStatus?: string | null,
    public createAt?: Date | null,
    public fileSize?: number | null,
    public smartUrl?: string | null,
    public mediumUrl?: string | null,
    public referenceCount?: number | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public category?: IResourceCategory | null,
  ) {}
}
