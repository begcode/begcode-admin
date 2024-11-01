import { Switch, Tag } from 'ant-design-vue';
import type { DescItem } from '@/components/Descriptions';

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
      render: (value, data) => h(Switch, { disabled: true, checked: value }),
    },
    {
      label: '菜单列表',
      field: 'viewPermissions',
      render: value => h('div', () => (value || []).map(item => h(Tag, [item.text]))),
    },
    {
      label: 'Api权限列表',
      field: 'apiPermissions',
      render: value => h('div', () => (value || []).map(item => h(Tag, [item.name]))),
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
