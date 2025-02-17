import { MessageSendType } from '@/models/enumerations/message-send-type.model';
import { SendStatus } from '@/models/enumerations/send-status.model';
export interface ISmsMessage {
  id?: number; // ID
  title?: string | null; // 消息标题
  sendType?: keyof typeof MessageSendType | null; // 发送方式
  receiver?: string | null; // 接收人
  params?: string | null; // 发送所需参数
  content?: string | null; // 推送内容
  sendTime?: Date | null; // 推送时间
  sendStatus?: keyof typeof SendStatus | null; // 推送状态
  retryNum?: number | null; // 发送次数
  failResult?: string | null; // 推送失败原因
  remark?: string | null; // 备注
  createdBy?: number | null; // 创建者Id
  createdDate?: Date | null; // 创建时间
  lastModifiedBy?: number | null; // 修改者Id
  lastModifiedDate?: Date | null; // 修改时间
}

export class SmsMessage implements ISmsMessage {
  constructor(
    public id?: number,
    public title?: string | null,
    public sendType?: keyof typeof MessageSendType | null,
    public receiver?: string | null,
    public params?: string | null,
    public content?: string | null,
    public sendTime?: Date | null,
    public sendStatus?: keyof typeof SendStatus | null,
    public retryNum?: number | null,
    public failResult?: string | null,
    public remark?: string | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
  ) {}
}
