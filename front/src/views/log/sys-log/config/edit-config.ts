import { useI18n } from '@/hooks/web/useI18n';
import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const sysLogService = apiService.log.sysLogService;
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
      componentProps: { placeholder: '请输入ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '请求路径',
      field: 'requestUrl',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入请求路径', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '日志类型',
      field: 'logType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择日志类型', options: getEnumDict('LogType'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '日志内容',
      field: 'logContent',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入日志内容', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '操作类型',
      field: 'operateType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择操作类型', options: getEnumDict('OperateType'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '操作用户账号',
      field: 'userid',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入操作用户账号', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '操作用户名称',
      field: 'username',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入操作用户名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'IP',
      field: 'ip',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入IP', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '请求java方法',
      field: 'method',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入请求java方法', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '请求参数',
      field: 'requestParam',
      component: 'InputTextArea',
      componentProps: { placeholder: '请输入请求参数', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '请求类型',
      field: 'requestType',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入请求类型', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '耗时',
      field: 'costTime',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入耗时', controls: false, style: 'width: 100%' },
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
  ];
};
export default {
  fields,
};
