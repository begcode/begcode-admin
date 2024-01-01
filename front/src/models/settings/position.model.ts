import { IUser } from '@/models/system/user.model';

export interface IPosition {
  id?: number; //ID
  code?: string; //岗位代码
  name?: string; //名称
  sortNo?: number | null; //排序
  description?: string | null; //描述
  users?: IUser[] | null; //员工列表
}

export class Position implements IPosition {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public sortNo?: number | null,
    public description?: string | null,
    public users?: IUser[] | null,
  ) {}
}
