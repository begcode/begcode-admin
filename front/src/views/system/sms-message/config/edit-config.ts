import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import apiService from '@/api-service/index';

const smsMessageService = apiService.system.smsMessageService;
const relationshipApis: any = {};

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
      componentProps: { placeholder: '请输入ID', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '消息标题',
      field: 'title',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入消息标题', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '发送方式',
      field: 'sendType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择发送方式', options: getEnumDict('MessageSendType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '接收人',
      field: 'receiver',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入接收人', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '发送所需参数',
      field: 'params',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入发送所需参数', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '推送内容',
      field: 'content',
      component: 'Editor',
      componentProps: { placeholder: '请输入推送内容', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '推送时间',
      field: 'sendTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择推送时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '推送状态',
      field: 'sendStatus',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择推送状态', options: getEnumDict('SendStatus'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '发送次数 超过5次不再发送',
      field: 'retryNum',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入发送次数 超过5次不再发送', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '推送失败原因',
      field: 'failResult',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入推送失败原因', style: 'width: 100%' },
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
      label: '创建者Id',
      field: 'createdBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入创建者Id', style: 'width: 100%' },
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
      componentProps: { placeholder: '请输入修改者Id', style: 'width: 100%' },
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
  ];
};
export default {
  fields,
};
