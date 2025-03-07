import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const authorityService = apiService.system.authorityService;

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (relationshipApis: any): FormSchema[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: ({ values }) => {
        return values && values.id;
      },
      dynamicDisabled: true,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '角色名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入角色名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '角色代号',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入角色代号', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '信息',
      field: 'info',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入信息', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '排序',
      field: 'order',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '展示',
      field: 'display',
      component: 'Switch',
      rules: [],
    },
    {
      label: '菜单列表',
      field: 'viewPermissions',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.viewPermissions,
        style: 'width: 100%',
        labelInValue: true,
        treeCheckable: true,
        treeCheckStrictly: true,
        showCheckedStrategy: 'SHOW_PARENT',
        fieldNames: { children: 'children', value: 'id', label: 'text' },
        resultField: 'records',
        placeholder: '请选择菜单列表',
      },
      rules: [],
    },
    {
      label: 'Api权限列表',
      field: 'apiPermissions',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.apiPermissions,
        style: 'width: 100%',
        labelInValue: true,
        treeCheckable: true,
        treeCheckStrictly: true,
        showCheckedStrategy: 'SHOW_PARENT',
        fieldNames: { children: 'children', value: 'id', label: 'name' },
        resultField: 'records',
        placeholder: '请选择Api权限列表',
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
