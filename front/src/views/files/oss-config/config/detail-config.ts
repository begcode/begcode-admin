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
      label: '提供商',
      field: 'provider',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('OssProvider').find(item => item.value === value) || { value: value, label: value }).label;
      },
    },
    {
      label: '平台',
      field: 'platform',
    },
    {
      label: '启用',
      field: 'enabled',
      render: (value, data) => h(Switch, { disabled: true, checked: value }),
    },
    {
      label: '备注',
      field: 'remark',
    },
    {
      label: '配置数据',
      field: 'configData',
      render: (value, _data) => h('pre', [h('code', {}, value)]),
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
