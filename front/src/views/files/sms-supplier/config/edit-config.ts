import { useI18n } from '@/hooks/web/useI18n';
import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const smsSupplierService = apiService.files.smsSupplierService;

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
      label: '提供商',
      field: 'provider',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择提供商', options: getEnumDict('SmsProvider'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '配置数据',
      field: 'configData',
      component: 'CodeEditor',
      componentProps: { language: 'json' },
      rules: [],
    },
    {
      label: '短信签名',
      field: 'signName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入短信签名', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '备注',
      field: 'remark',
      component: 'InputTextArea',
      rules: [],
    },
    {
      label: '启用',
      field: 'enabled',
      component: 'Switch',
      rules: [],
    },
  ];
};
export default {
  fields,
};
