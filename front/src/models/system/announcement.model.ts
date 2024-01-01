import { PriorityLevel } from '@/models/enumerations/priority-level.model';
import { AnnoCategory } from '@/models/enumerations/anno-category.model';
import { ReceiverType } from '@/models/enumerations/receiver-type.model';
import { AnnoSendStatus } from '@/models/enumerations/anno-send-status.model';
import { AnnoBusinessType } from '@/models/enumerations/anno-business-type.model';
import { AnnoOpenType } from '@/models/enumerations/anno-open-type.model';
export interface IAnnouncement {
  id?: number; //ID
  title?: string; //标题
  summary?: string | null; //摘要
  content?: string | null; //内容
  startTime?: Date | null; //开始时间
  endTime?: Date | null; //结束时间
  senderId?: number | null; //发布人Id
  priority?: keyof typeof PriorityLevel | null; //优先级
  category?: keyof typeof AnnoCategory | null; //消息类型
  receiverType?: keyof typeof ReceiverType; //通告对象类型
  sendStatus?: keyof typeof AnnoSendStatus | null; //发布状态
  sendTime?: Date | null; //发布时间
  cancelTime?: Date | null; //撤销时间
  businessType?: keyof typeof AnnoBusinessType | null; //业务类型
  businessId?: number | null; //业务id
  openType?: keyof typeof AnnoOpenType | null; //打开方式
  openPage?: string | null; //组件/路由 地址
  receiverIds?: string | null; //指定接收者id
  createdBy?: number | null; //创建者Id
  createdDate?: Date | null; //创建时间
  lastModifiedBy?: number | null; //修改者Id
  lastModifiedDate?: Date | null; //修改时间
}

export class Announcement implements IAnnouncement {
  constructor(
    public id?: number,
    public title?: string,
    public summary?: string | null,
    public content?: string | null,
    public startTime?: Date | null,
    public endTime?: Date | null,
    public senderId?: number | null,
    public priority?: keyof typeof PriorityLevel | null,
    public category?: keyof typeof AnnoCategory | null,
    public receiverType?: keyof typeof ReceiverType,
    public sendStatus?: keyof typeof AnnoSendStatus | null,
    public sendTime?: Date | null,
    public cancelTime?: Date | null,
    public businessType?: keyof typeof AnnoBusinessType | null,
    public businessId?: number | null,
    public openType?: keyof typeof AnnoOpenType | null,
    public openPage?: string | null,
    public receiverIds?: string | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
  ) {}
}
