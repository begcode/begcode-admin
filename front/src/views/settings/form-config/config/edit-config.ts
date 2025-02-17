import { useI18n } from '@/hooks/web/useI18n';
import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const formConfigService = apiService.settings.formConfigService;

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (relationshipApis: any): FormSchema[] => {
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
      label: '表单Key',
      field: 'formKey',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入表单Key', style: 'width: 100%' },
      rules: [
        { type: 'string', max: 100 },
        { required: true, message: '必填项' },
      ],
    },
    {
      label: '名称',
      field: 'formName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名称', style: 'width: 100%' },
      rules: [
        { type: 'string', max: 100 },
        { required: true, message: '必填项' },
      ],
    },
    {
      label: '表单配置',
      field: 'formJson',
      show: false,
      component: 'CodeEditor',
      componentProps: { language: 'json' },
      rules: [],
    },
    {
      label: '表单类型',
      field: 'formType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择表单类型', options: getEnumDict('FormConfigType'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '多条数据',
      field: 'multiItems',
      show: ({ values }) => {
        return values && values.formType === 'DATA_FORM';
      },
      component: 'Switch',
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
      label: '业务类别',
      field: 'businessType',
      component: 'ApiSelect',
      componentProps: {
        api: relationshipApis.businessType,
        style: 'width: 100%',
        labelInValue: true,
        fieldNames: { options: 'optionsField', value: 'id', label: 'name' },
        resultField: 'records',
        placeholder: '请选择业务类别',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
