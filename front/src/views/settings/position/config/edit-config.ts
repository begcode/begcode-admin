import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const positionService = apiService.settings.positionService;
const relationshipApis: any = {
  users: apiService.system.userService.retrieve,
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
      label: '岗位代码',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入岗位代码', style: 'width: 100%' },
      rules: [
        { type: 'string', max: 50 },
        { required: true, message: '必填项' },
      ],
    },
    {
      label: '名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名称', style: 'width: 100%' },
      rules: [
        { type: 'string', max: 50 },
        { required: true, message: '必填项' },
      ],
    },
    {
      label: '排序',
      field: 'sortNo',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '描述',
      field: 'description',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入描述', style: 'width: 100%' },
      rules: [{ type: 'string', max: 200 }],
    },
  ];
};
export default {
  fields,
};
