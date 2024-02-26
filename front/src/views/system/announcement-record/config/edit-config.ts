import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const announcementRecordService = apiService.system.announcementRecordService;
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
      label: '通告ID',
      field: 'anntId',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入通告ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '用户id',
      field: 'userId',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入用户id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '是否已读',
      field: 'hasRead',
      component: 'Switch',
      componentProps: { placeholder: '请选择是否已读' },
      rules: [],
    },
    {
      label: '阅读时间',
      field: 'readTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择阅读时间', style: 'width: 100%' },
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
