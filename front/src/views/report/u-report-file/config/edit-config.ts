import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const uReportFileService = apiService.report.uReportFileService;
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
      componentProps: { placeholder: '请输入ID', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名称', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '内容',
      field: 'content',
      component: 'Editor',
      componentProps: { placeholder: '请输入内容', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建时间',
      field: 'createAt',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择创建时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '更新时间',
      field: 'updateAt',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择更新时间', style: 'width: 100%' },
      rules: [],
    },
  ];
};
export default {
  fields,
};
