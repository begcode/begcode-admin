import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import apiService from '@/api-service/index';

const sysFillRuleService = apiService.settings.sysFillRuleService;
const fillRuleItemService = apiService.settings.fillRuleItemService;
const relationshipApis: any = {
  ruleItems: apiService.settings.fillRuleItemService.retrieve,
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
      label: '规则名称',
      field: 'name',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入规则名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '规则Code',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入规则Code', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '规则描述',
      field: 'desc',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入规则描述', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '是否启用',
      field: 'enabled',
      component: 'Switch',
      rules: [],
    },
    {
      label: '重置频率',
      field: 'resetFrequency',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择重置频率', options: getEnumDict('ResetFrequency'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '序列值',
      field: 'seqValue',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入序列值', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '生成值',
      field: 'fillValue',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入生成值', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '规则实现类',
      field: 'implClass',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入规则实现类', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '规则参数',
      field: 'params',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入规则参数', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重置开始日期',
      field: 'resetStartTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择重置开始日期', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重置结束日期',
      field: 'resetEndTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择重置结束日期', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重置时间',
      field: 'resetTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择重置时间', style: 'width: 100%' },
      rules: [],
    },
  ];
};
const ruleItemsColumns = () => {
  const { getEnumDict } = useI18n();
  return [
    {
      fixed: 'left',
      type: 'checkbox',
      width: 60,
    },
    {
      title: '排序值',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      sortable: true,
      titlePrefix: { icon: 'vxe-icon-sort', content: '排序操作列' },
      editRender: { name: 'ADragSort', enabled: true, props: { remoteApi: fillRuleItemService.updateSortValue } },
    },
    {
      title: '字段参数类型',
      field: 'fieldParamType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('FieldParamType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('FieldParamType') }, enabled: true },
    },
    {
      title: '字段参数值',
      field: 'fieldParamValue',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '日期格式',
      field: 'datePattern',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '序列长度',
      field: 'seqLength',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: true, props: { controls: false } },
    },
    {
      title: '序列增量',
      field: 'seqIncrement',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: true, props: { controls: false } },
    },
    {
      title: '序列起始值',
      field: 'seqStartValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: true, props: { controls: false } },
    },
    {
      title: '操作',
      field: 'recordOperation',
      fixed: 'right',
      headerAlign: 'center',
      align: 'right',
      showOverflow: false,
      width: 120,
      slots: { default: 'recordAction' },
    },
  ];
};
export default {
  fields,
  ruleItemsColumns,
};
