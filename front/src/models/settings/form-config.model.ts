import { IBusinessType } from '@/models/settings/business-type.model';

export interface IFormConfig {
  id?: number; //ID
  formKey?: string | null; //表单Key
  formName?: string | null; //名称
  formJson?: string | null; //表单配置
  createdBy?: number | null; //创建者Id
  createdDate?: Date | null; //创建时间
  lastModifiedBy?: number | null; //修改者Id
  lastModifiedDate?: Date | null; //修改时间
  businessType?: IBusinessType | null; //业务类别
}

export class FormConfig implements IFormConfig {
  constructor(
    public id?: number,
    public formKey?: string | null,
    public formName?: string | null,
    public formJson?: string | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public businessType?: IBusinessType | null,
  ) {}
}
