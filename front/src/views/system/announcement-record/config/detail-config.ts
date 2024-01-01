import { h } from 'vue';
import { DescItem } from '@begcode/components';
import { Switch } from 'ant-design-vue';
import dayjs from 'dayjs';

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
    label: '通告ID',
    field: 'anntId',
  },
  {
    label: '用户id',
    field: 'userId',
  },
  {
    label: '是否已读',
    field: 'hasRead',
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.hasRead = checked;
        },
      }),
  },
  {
    label: '阅读时间',
    field: 'readTime',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '创建者Id',
    field: 'createdBy',
  },
  {
    label: '创建时间',
    field: 'createdDate',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '修改者Id',
    field: 'lastModifiedBy',
  },
  {
    label: '修改时间',
    field: 'lastModifiedDate',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
];

export default {
  fields,
};
