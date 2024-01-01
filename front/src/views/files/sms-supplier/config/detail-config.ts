import { h } from 'vue';
import { DescItem, CodeEditor } from '@begcode/components';
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
    label: '提供商',
    field: 'provider',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('SmsProvider').find(item => item.value === value) || value;
    },
  },
  {
    label: '配置数据',
    field: 'configData',
    render: (value, _data) => h(CodeEditor, { src: value, style: 'width: 100px; height: 100px; object-fit: cover; border-radius: 50%;' }),
  },
  {
    label: '短信签名',
    field: 'signName',
  },
  {
    label: '备注',
    field: 'remark',
  },
  {
    label: '启用',
    field: 'enabled',
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.enabled = checked;
        },
      }),
  },
];

export default {
  fields,
};
