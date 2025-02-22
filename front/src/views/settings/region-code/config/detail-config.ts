import type { DescItem } from '@/components/Descriptions';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (hideColumns: string[] = []): DescItem[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: values => {
        return values && values.id;
      },
    },
    {
      label: '名称',
      field: 'name',
    },
    {
      label: '地区代码',
      field: 'areaCode',
    },
    {
      label: '城市代码',
      field: 'cityCode',
    },
    {
      label: '全名',
      field: 'mergerName',
    },
    {
      label: '短名称',
      field: 'shortName',
    },
    {
      label: '邮政编码',
      field: 'zipCode',
    },
    {
      label: '等级',
      field: 'level',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('RegionCodeLevel').find(item => item.value === value) || { value, label: value }).label;
      },
    },
    {
      label: '经度',
      field: 'lng',
    },
    {
      label: '纬度',
      field: 'lat',
    },
    {
      label: '上级节点',
      field: 'parent.name',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
