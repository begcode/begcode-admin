import { h } from 'vue';
import { DescItem } from '@begcode/components';
import { Switch, Select } from 'ant-design-vue';

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
    label: '角色名称',
    field: 'name',
  },
  {
    label: '角色代号',
    field: 'code',
  },
  {
    label: '信息',
    field: 'info',
  },
  {
    label: '排序',
    field: 'order',
  },
  {
    label: '展示',
    field: 'display',
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.display = checked;
        },
      }),
  },
  {
    label: '菜单列表',
    field: 'viewPermissions',
    render: value =>
      h(Select, {
        disabled: true,
        labelInValue: true,
        mode: 'multiple',
        fieldNames: { label: 'id', value: 'text' },
        value: (value || []).map(item => ({ value: item.id, label: item.text })),
      }),
  },
  {
    label: 'Api权限列表',
    field: 'apiPermissions',
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
