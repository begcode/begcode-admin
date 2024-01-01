import { JobStatus } from '@/models/enumerations/job-status.model';
export interface ITaskJobConfig {
  id?: number; //ID
  name?: string | null; //任务名称
  jobClassName?: string | null; //任务类名
  cronExpression?: string | null; //cron表达式
  parameter?: string | null; //参数
  description?: string | null; //描述
  jobStatus?: keyof typeof JobStatus | null; //任务状态
  createdBy?: number | null; //创建者Id
  createdDate?: Date | null; //创建时间
  lastModifiedBy?: number | null; //修改者Id
  lastModifiedDate?: Date | null; //修改时间
}

export class TaskJobConfig implements ITaskJobConfig {
  constructor(
    public id?: number,
    public name?: string | null,
    public jobClassName?: string | null,
    public cronExpression?: string | null,
    public parameter?: string | null,
    public description?: string | null,
    public jobStatus?: keyof typeof JobStatus | null,
    public createdBy?: number | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: number | null,
    public lastModifiedDate?: Date | null,
  ) {}
}
