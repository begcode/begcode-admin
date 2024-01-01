import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const departmentService = apiService.settings.departmentService;
const relationshipApis: any = {
  children: apiService.settings.departmentService.tree,
  authorities: apiService.system.authorityService.tree,
  parent: apiService.settings.departmentService.tree,
  users: apiService.system.userService.retrieve,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: ({ values }) => {
        return values && values.id;
      },
      dynamicDisabled: true,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入ID', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '代码',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入代码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '地址',
      field: 'address',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入地址', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '联系电话',
      field: 'phoneNum',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入联系电话', style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'logo地址',
      field: 'logo',
      component: 'ImageUpload',
      rules: [],
    },
    {
      label: '联系人',
      field: 'contact',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入联系人', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建用户 Id',
      field: 'createUserId',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入创建用户 Id', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建时间',
      field: 'createTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择创建时间', style: 'width: 100%' },
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
    {
      label: '上级',
      field: 'parent',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.parent,
        style: 'width: 100%',
        labelInValue: true,
        numberToString: true,
        fieldNames: { children: 'children', value: 'id', label: 'name' },
        resultField: 'records',
        placeholder: '请选择上级',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
