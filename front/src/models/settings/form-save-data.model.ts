import { IFormConfig } from '@/models/settings/form-config.model';

export interface IFormSaveData {
  id?: number; //ID
  formData?: string | null; //表单数据
  createdBy?: number | null; //创建者Id
  createdDate?: Date | null; //创建时间
  lastModifiedBy?: number | null; //修改者Id
  lastModifiedDate?: Date | null; //修改时间
  formConfigId?: number | null;
  formConfig?: IFormConfig | null; //表单配置
}

export class FormSaveData implements IFormSaveData {
  constructor(
    public id?: number,
    public formData?: string | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public formConfigId?: number | null,
    public formConfig?: IFormConfig | null,
  ) {}
}
