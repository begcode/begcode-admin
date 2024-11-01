import dayjs from 'dayjs';
import { Switch, Tag } from 'ant-design-vue';
import type { DescItem } from '@/components/Descriptions';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (hideColumns: string[] = []): DescItem[] => {
  return [
    {
      label: '用户ID',
      field: 'id',
      show: values => {
        return values && values.id;
      },
    },
    {
      label: '账户名',
      field: 'login',
    },
    {
      label: '密码',
      field: 'password',
    },
    {
      label: '名字',
      field: 'firstName',
    },
    {
      label: '姓氏',
      field: 'lastName',
    },
    {
      label: '电子邮箱',
      field: 'email',
    },
    {
      label: '手机号码',
      field: 'mobile',
    },
    {
      label: '出生日期',
      field: 'birthday',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
    {
      label: '激活状态',
      field: 'activated',
      render: (value, data) => h(Switch, { disabled: true, checked: value }),
    },
    {
      label: '语言Key',
      field: 'langKey',
    },
    {
      label: '头像地址',
      field: 'imageUrl',
      render: (value, _data) => h('img', { src: value, style: 'width: 100px; height: 100px; object-fit: cover; border-radius: 50%;' }),
    },
    {
      label: '激活Key',
      field: 'activationKey',
    },
    {
      label: '重置码',
      field: 'resetKey',
    },
    {
      label: '重置时间',
      field: 'resetDate',
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
      label: '部门',
      field: 'department.name',
    },
    {
      label: '岗位',
      field: 'position.name',
    },
    {
      label: '角色列表',
      field: 'authorities',
      render: value => h('div', () => (value || []).map(item => h(Tag, [item.name]))),
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
