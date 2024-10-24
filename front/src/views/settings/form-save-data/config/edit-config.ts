import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const formSaveDataService = apiService.settings.formSaveDataService;
const relationshipApis: any = {
  formConfig: apiService.settings.formConfigService.retrieve,
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
      componentProps: { placeholder: '请输入ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '表单数据',
      field: 'formData',
      component: 'CodeEditor',
      componentProps: { language: 'json' },
      rules: [],
    },
    {
      label: '创建者Id',
      field: 'createdBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入创建者Id', controls: false, style: 'width: 100%' },
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
      componentProps: { placeholder: '请输入修改者Id', controls: false, style: 'width: 100%' },
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
      label: '表单配置',
      field: 'formConfig',
      component: 'ApiSelect',
      componentProps: {
        api: relationshipApis.formConfig,
        style: 'width: 100%',
        labelInValue: true,
        fieldNames: { options: 'optionsField', value: 'id', label: 'formName' },
        resultField: 'records',
        placeholder: '请选择表单配置',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
