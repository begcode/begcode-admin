import { h } from 'vue';
import { DescItem } from '@begcode/components';
import { Select } from 'ant-design-vue';

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
    label: '地址',
    field: 'address',
  },
  {
    label: '联系电话',
    field: 'phoneNum',
  },
  {
    label: 'logo地址',
    field: 'logo',
    render: (value, _data) => h('img', { src: value, style: 'width: 100px; height: 100px; object-fit: cover; border-radius: 50%;' }),
  },
  {
    label: '联系人',
    field: 'contact',
  },
  {
    label: '角色列表',
    field: 'authorities',
    render: value =>
      h(Select, {
        disabled: true,
        labelInValue: true,
        mode: 'multiple',
        fieldNames: { label: 'id', value: 'name' },
        value: (value || []).map(item => ({ value: item.id, label: item.name })),
      }),
  },
  {
    label: '上级',
    field: 'parentName',
  },
];

export default {
  fields,
};
