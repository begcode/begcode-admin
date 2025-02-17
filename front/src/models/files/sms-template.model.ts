import { ISmsSupplier } from '@/models/files/sms-supplier.model';

import { MessageSendType } from '@/models/enumerations/message-send-type.model';
import { SmsTemplateType } from '@/models/enumerations/sms-template-type.model';
export interface ISmsTemplate {
  id?: number; // ID
  name?: string | null; // 模板标题
  code?: string | null; // 模板CODE
  sendType?: keyof typeof MessageSendType | null; // 通知类型
  content?: string | null; // 模板内容
  testJson?: string | null; // 模板测试json
  type?: keyof typeof SmsTemplateType | null; // 模板类型
  remark?: string | null; // 备注
  enabled?: boolean | null; // 启用
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
  supplierId?: number | null;
  supplier?: ISmsSupplier | null; // 短信服务商
}

export class SmsTemplate implements ISmsTemplate {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public sendType?: keyof typeof MessageSendType | null,
    public content?: string | null,
    public testJson?: string | null,
    public type?: keyof typeof SmsTemplateType | null,
    public remark?: string | null,
    public enabled?: boolean | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
    public supplierId?: number | null,
    public supplier?: ISmsSupplier | null,
  ) {
    this.enabled = this.enabled ?? false;
  }
}
