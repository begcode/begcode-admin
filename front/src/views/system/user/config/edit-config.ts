import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const userService = apiService.system.userService;
const relationshipApis: any = {
  department: apiService.settings.departmentService.tree,
  position: apiService.settings.positionService.retrieve,
  authorities: apiService.system.authorityService.tree,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  return [
    {
      label: '用户ID',
      field: 'id',
      show: ({ values }) => {
        return values && values.id;
      },
      dynamicDisabled: true,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入用户ID', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '账户名',
      field: 'login',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入账户名', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '密码',
      field: 'password',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入密码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '名字',
      field: 'firstName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名字', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '姓氏',
      field: 'lastName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入姓氏', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '电子邮箱',
      field: 'email',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入电子邮箱', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '手机号码',
      field: 'mobile',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入手机号码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '出生日期',
      field: 'birthday',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择出生日期', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '激活状态',
      field: 'activated',
      component: 'Switch',
      componentProps: { placeholder: '请选择激活状态' },
      rules: [],
    },
    {
      label: '语言Key',
      field: 'langKey',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入语言Key', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '头像地址',
      field: 'imageUrl',
      component: 'Avatar',
      rules: [],
    },
    {
      label: '激活Key',
      field: 'activationKey',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入激活Key', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重置码',
      field: 'resetKey',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入重置码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重置时间',
      field: 'resetDate',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入重置时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建者Id',
      field: 'createdBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入创建者Id', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建时间',
      field: 'createdDate',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择创建时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '修改者Id',
      field: 'lastModifiedBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入修改者Id', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '修改时间',
      field: 'lastModifiedDate',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择修改时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '部门',
      field: 'department',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.department,
        style: 'width: 100%',
        labelInValue: true,
        numberToString: true,
        fieldNames: { children: 'children', value: 'id', label: 'name' },
        resultField: 'records',
        placeholder: '请选择部门',
      },
      rules: [],
    },
    {
      label: '岗位',
      field: 'position',
      component: 'ApiSelect',
      componentProps: {
        api: relationshipApis.position,
        style: 'width: 100%',
        labelInValue: true,
        numberToString: true,
        valueField: 'id',
        labelField: 'name',
        resultField: 'records',
        placeholder: '请选择岗位',
      },
      rules: [],
    },
    {
      label: '角色列表',
      field: 'authorities',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.authorities,
        style: 'width: 100%',
        labelInValue: true,
        treeCheckable: true,
        treeCheckStrictly: true,
        showCheckedStrategy: 'SHOW_PARENT',
        numberToString: true,
        fieldNames: { children: 'children', value: 'id', label: 'name' },
        resultField: 'records',
        placeholder: '请选择角色列表',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
