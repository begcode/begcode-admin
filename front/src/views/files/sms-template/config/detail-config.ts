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
    label: '模板标题',
    field: 'name',
  },
  {
    label: '模板CODE',
    field: 'code',
  },
  {
    label: '通知类型',
    field: 'sendType',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('MessageSendType').find(item => item.value === value) || value;
    },
  },
  {
    label: '模板内容',
    field: 'content',
  },
  {
    label: '模板测试json',
    field: 'testJson',
  },
  {
    label: '模板类型',
    field: 'type',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('SmsTemplateType').find(item => item.value === value) || value;
    },
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
  {
    label: '创建者Id',
    field: 'createdBy',
  },
  {
    label: '创建时间',
    field: 'createdDate',
  },
  {
    label: '修改者Id',
    field: 'lastModifiedBy',
  },
  {
    label: '修改时间',
    field: 'lastModifiedDate',
  },
  {
    label: '短信服务商',
    field: 'supplierSignName',
  },
];

export default {
  fields,
};
