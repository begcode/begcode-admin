import { Switch } from 'ant-design-vue';
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
      label: '字段值',
      field: 'value',
    },
    {
      label: '字段标题',
      field: 'label',
    },
    {
      label: '字段类型',
      field: 'valueType',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('CommonFieldType').find(item => item.value === value) || { value, label: value }).label;
      },
    },
    {
      label: '说明',
      field: 'remark',
    },
    {
      label: '排序',
      field: 'sortValue',
    },
    {
      label: '是否禁用',
      field: 'disabled',
      render: (value, data) => h(Switch, { disabled: true, checked: value }),
    },
    {
      label: '实体名称',
      field: 'ownerEntityName',
    },
    {
      label: '使用实体ID',
      field: 'ownerEntityId',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
