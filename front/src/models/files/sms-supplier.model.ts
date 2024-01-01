import { SmsProvider } from '@/models/enumerations/sms-provider.model';
export interface ISmsSupplier {
  id?: number; //ID
  provider?: keyof typeof SmsProvider | null; //提供商
  configData?: string | null; //配置数据
  signName?: string | null; //短信签名
  remark?: string | null; //备注
  enabled?: boolean | null; //启用
}

export class SmsSupplier implements ISmsSupplier {
  constructor(
    public id?: number,
    public provider?: keyof typeof SmsProvider | null,
    public configData?: string | null,
    public signName?: string | null,
    public remark?: string | null,
    public enabled?: boolean | null,
  ) {
    this.enabled = this.enabled ?? false;
  }
}
