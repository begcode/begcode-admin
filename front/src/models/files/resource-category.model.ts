import { IUploadImage } from '@/models/files/upload-image.model';
import { IUploadFile } from '@/models/files/upload-file.model';

export interface IResourceCategory {
  id?: number; //ID
  title?: string | null; //标题
  code?: string | null; //代码
  orderNumber?: number | null; //排序
  children?: IResourceCategory[] | null; //下级列表
  parent?: IResourceCategory | null; //上级
  images?: IUploadImage[] | null; //图片列表
  files?: IUploadFile[] | null; //文件列表
  expand?: boolean;
  nzAddLevel?: number;
}

export class ResourceCategory implements IResourceCategory {
  constructor(
    public id?: number,
    public title?: string | null,
    public code?: string | null,
    public orderNumber?: number | null,
    public children?: IResourceCategory[] | null,
    public parent?: IResourceCategory | null,
    public images?: IUploadImage[] | null,
    public files?: IUploadFile[] | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {}
}
