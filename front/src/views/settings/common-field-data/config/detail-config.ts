import { h } from 'vue';
import { DescItem } from '@begcode/components';
import { Switch } from 'ant-design-vue';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields: DescItem[] = [
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
      return (getEnumDict('CommonFieldType').find(item => item.value === value) || { value: value, label: value }).label;
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
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.disabled = checked;
        },
      }),
  },
  {
    label: '实体名称',
    field: 'ownerEntityName',
  },
  {
    label: '使用实体ID',
    field: 'ownerEntityId',
  },
  {
    label: 'Site Config',
    field: 'siteConfigId',
  },
  {
    label: 'Dictionary',
    field: 'dictionaryId',
  },
];

export default {
  fields,
};
