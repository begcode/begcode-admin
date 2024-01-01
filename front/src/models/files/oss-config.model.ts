import { OssProvider } from '@/models/enumerations/oss-provider.model';
export interface IOssConfig {
  id?: number; //ID
  provider?: keyof typeof OssProvider; //提供商
  platform?: string; //平台
  enabled?: boolean | null; //启用
  remark?: string | null; //备注
  configData?: string | null; //配置数据
}

export class OssConfig implements IOssConfig {
  constructor(
    public id?: number,
    public provider?: keyof typeof OssProvider,
    public platform?: string,
    public enabled?: boolean | null,
    public remark?: string | null,
    public configData?: string | null,
  ) {
    this.enabled = this.enabled ?? false;
  }
}
