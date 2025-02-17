import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const departmentService = apiService.settings.departmentService;

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
      componentProps: { api: apiService.files.uploadImageService.create },
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
