import { useI18n } from '@/hooks/web/useI18n';
import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const regionCodeService = apiService.settings.regionCodeService;
const relationshipApis: any = {
  children: apiService.settings.regionCodeService.tree,
  parent: apiService.settings.regionCodeService.tree,
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
      label: '名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '地区代码',
      field: 'areaCode',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入地区代码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '城市代码',
      field: 'cityCode',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入城市代码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '全名',
      field: 'mergerName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入全名', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '短名称',
      field: 'shortName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入短名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '邮政编码',
      field: 'zipCode',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入邮政编码', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '等级',
      field: 'level',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择等级', options: getEnumDict('RegionCodeLevel'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '经度',
      field: 'lng',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入经度', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '纬度',
      field: 'lat',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入纬度', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '上级节点',
      field: 'parent',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.parent,
        style: 'width: 100%',
        labelInValue: true,
        fieldNames: { children: 'children', key: 'id', title: 'name' },
        resultField: 'records',
        placeholder: '请选择上级节点',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
