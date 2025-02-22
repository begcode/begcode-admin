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
      label: '服务名称',
      field: 'serviceName',
    },
    {
      label: '权限名称',
      field: 'name',
    },
    {
      label: 'Code',
      field: 'code',
    },
    {
      label: '权限描述',
      field: 'description',
    },
    {
      label: '类型',
      field: 'type',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('ApiPermissionType').find(item => item.value === value) || { value, label: value }).label;
      },
    },
    {
      label: '请求类型',
      field: 'method',
    },
    {
      label: 'url 地址',
      field: 'url',
    },
    {
      label: '状态',
      field: 'status',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('ApiPermissionState').find(item => item.value === value) || { value, label: value }).label;
      },
    },
    {
      label: '上级',
      field: 'parent.name',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
