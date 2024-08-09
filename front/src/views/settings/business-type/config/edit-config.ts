import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const businessTypeService = apiService.settings.businessTypeService;
const relationshipApis: any = {};

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
      label: '描述',
      field: 'description',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入描述', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '图标',
      field: 'icon',
      component: 'IconPicker',
      componentProps: { placeholder: '请选择图标', style: 'width: 100%' },
      rules: [],
    },
  ];
};
export default {
  fields,
};
