import { LogType } from '@/models/enumerations/log-type.model';
import { OperateType } from '@/models/enumerations/operate-type.model';
export interface ISysLog {
  id?: number; // ID
  requestUrl?: string | null; // 请求路径
  logType?: keyof typeof LogType | null; // 日志类型
  logContent?: string | null; // 日志内容
  operateType?: keyof typeof OperateType | null; // 操作类型
  userid?: string | null; // 操作用户账号
  username?: string | null; // 操作用户名称
  ip?: string | null; // IP
  method?: string | null; // 请求java方法
  requestParam?: string | null; // 请求参数
  requestType?: string | null; // 请求类型
  costTime?: number | null; // 耗时
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
}

export class SysLog implements ISysLog {
  constructor(
    public id?: number,
    public requestUrl?: string | null,
    public logType?: keyof typeof LogType | null,
    public logContent?: string | null,
    public operateType?: keyof typeof OperateType | null,
    public userid?: string | null,
    public username?: string | null,
    public ip?: string | null,
    public method?: string | null,
    public requestParam?: string | null,
    public requestType?: string | null,
    public costTime?: number | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
  ) {}
}
