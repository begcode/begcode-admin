import { h } from 'vue';
import { DescItem, Icon } from '@begcode/components';

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
    label: '代码',
    field: 'code',
  },
  {
    label: '描述',
    field: 'description',
  },
  {
    label: '图标',
    field: 'icon',
    render: (value, _data) => h(Icon, { class: value, style: 'font-size: 20px;' }),
  },
];

export default {
  fields,
};
