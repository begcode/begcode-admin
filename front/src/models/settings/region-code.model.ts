import { RegionCodeLevel } from '@/models/enumerations/region-code-level.model';
export interface IRegionCode {
  id?: number; // ID
  name?: string | null; // 名称
  areaCode?: string | null; // 地区代码
  cityCode?: string | null; // 城市代码
  mergerName?: string | null; // 全名
  shortName?: string | null; // 短名称
  zipCode?: string | null; // 邮政编码
  level?: keyof typeof RegionCodeLevel | null; // 等级
  lng?: number | null; // 经度
  lat?: number | null; // 纬度
  parentId?: number | null;
  parent?: IRegionCode | null; // 上级节点
  expand?: boolean;
  nzAddLevel?: number;
}

export class RegionCode implements IRegionCode {
  constructor(
    public id?: number,
    public name?: string | null,
    public areaCode?: string | null,
    public cityCode?: string | null,
    public mergerName?: string | null,
    public shortName?: string | null,
    public zipCode?: string | null,
    public level?: keyof typeof RegionCodeLevel | null,
    public lng?: number | null,
    public lat?: number | null,
    public parentId?: number | null,
    public parent?: IRegionCode | null,
    public expand?: boolean,
    public nzAddLevel?: number,
  ) {}
}
