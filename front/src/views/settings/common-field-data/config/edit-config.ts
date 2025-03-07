import { useI18n } from '@/hooks/web/useI18n';
import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const commonFieldDataService = apiService.settings.commonFieldDataService;

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
      label: '名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '字段值',
      field: 'value',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入字段值', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '字段标题',
      field: 'label',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入字段标题', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '字段类型',
      field: 'valueType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择字段类型', options: getEnumDict('CommonFieldType'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '说明',
      field: 'remark',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入说明', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '排序',
      field: 'sortValue',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '是否禁用',
      field: 'disabled',
      component: 'Switch',
      rules: [],
    },
    {
      label: '实体名称',
      field: 'ownerEntityName',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入实体名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '使用实体ID',
      field: 'ownerEntityId',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入使用实体ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
  ];
};
export default {
  fields,
};
