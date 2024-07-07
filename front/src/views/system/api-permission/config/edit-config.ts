import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import apiService from '@/api-service/index';

const apiPermissionService = apiService.system.apiPermissionService;
const relationshipApis: any = {
  children: apiService.system.apiPermissionService.tree,
  parent: apiService.system.apiPermissionService.tree,
  authorities: apiService.system.authorityService.tree,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  const { getEnumDict } = useI18n();
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
      label: '服务名称',
      field: 'serviceName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入服务名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '权限名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'Code',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入Code', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '权限描述',
      field: 'description',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限描述', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '类型',
      field: 'type',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择类型', options: getEnumDict('ApiPermissionType'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '请求类型',
      field: 'method',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入请求类型', style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'url 地址',
      field: 'url',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入url 地址', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '状态',
      field: 'status',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择状态', options: getEnumDict('ApiPermissionState'), showSearch: true, style: 'width: 100%' };
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
