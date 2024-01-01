import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const resourceCategoryService = apiService.files.resourceCategoryService;
const relationshipApis: any = {
  children: apiService.files.resourceCategoryService.tree,
  parent: apiService.files.resourceCategoryService.tree,
  images: apiService.files.uploadImageService.retrieve,
  files: apiService.files.uploadFileService.retrieve,
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
      label: '标题',
      field: 'title',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入标题', style: 'width: 100%' },
      rules: [{ type: 'string', max: 40 }],
    },
    {
      label: '代码',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入代码', style: 'width: 100%' },
      rules: [{ type: 'string', max: 20 }],
    },
    {
      label: '排序',
      field: 'orderNumber',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', style: 'width: 100%' },
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
        fieldNames: { children: 'children', value: 'id', label: 'title' },
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
