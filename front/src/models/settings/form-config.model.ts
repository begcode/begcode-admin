import { IBusinessType } from '@/models/settings/business-type.model';

import { FormConfigType } from '@/models/enumerations/form-config-type.model';
export interface IFormConfig {
  id?: number; // ID
  formKey?: string; // 表单Key
  formName?: string; // 名称
  formJson?: string | null; // 表单配置
  formType?: keyof typeof FormConfigType | null; // 表单类型
  multiItems?: boolean | null; // 多条数据
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
  businessTypeId?: number | null;
  businessType?: IBusinessType | null; // 业务类别
}

export class FormConfig implements IFormConfig {
  constructor(
    public id?: number,
    public formKey?: string,
    public formName?: string,
    public formJson?: string | null,
    public formType?: keyof typeof FormConfigType | null,
    public multiItems?: boolean | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public businessTypeId?: number | null,
    public businessType?: IBusinessType | null,
  ) {
    this.multiItems = this.multiItems ?? false;
  }
}
