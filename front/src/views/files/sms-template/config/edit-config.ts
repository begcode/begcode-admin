import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import apiService from '@/api-service/index';

const smsTemplateService = apiService.files.smsTemplateService;
const relationshipApis: any = {
  supplier: apiService.files.smsSupplierService.retrieve,
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
      label: '模板标题',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入模板标题', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '模板CODE',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入模板CODE', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '通知类型',
      field: 'sendType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择通知类型', options: getEnumDict('MessageSendType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '模板内容',
      field: 'content',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入模板内容', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '模板测试json',
      field: 'testJson',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入模板测试json', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '模板类型',
      field: 'type',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择模板类型', options: getEnumDict('SmsTemplateType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '备注',
      field: 'remark',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入备注', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '启用',
      field: 'enabled',
      component: 'Switch',
      componentProps: { placeholder: '请选择启用' },
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
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入创建时间', style: 'width: 100%' },
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
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入修改时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '短信服务商',
      field: 'supplier',
      component: 'ApiSelect',
      componentProps: {
        api: relationshipApis.supplier,
        style: 'width: 100%',
        labelInValue: true,
        numberToString: true,
        valueField: 'id',
        labelField: 'signName',
        resultField: 'records',
        placeholder: '请选择短信服务商',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
