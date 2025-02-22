import { Switch } from 'ant-design-vue';
import dayjs from 'dayjs';
import type { DescItem } from '@/components/Descriptions';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (hideColumns: string[] = []): DescItem[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: values => {
        return values && values.id;
      },
    },
    {
      label: '规则名称',
      field: 'name',
    },
    {
      label: '规则Code',
      field: 'code',
    },
    {
      label: '规则描述',
      field: 'desc',
    },
    {
      label: '是否启用',
      field: 'enabled',
      render: (value, data) => h(Switch, { disabled: true, checked: value }),
    },
    {
      label: '重置频率',
      field: 'resetFrequency',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('ResetFrequency').find(item => item.value === value) || { value, label: value }).label;
      },
    },
    {
      label: '序列值',
      field: 'seqValue',
    },
    {
      label: '生成值',
      field: 'fillValue',
    },
    {
      label: '规则实现类',
      field: 'implClass',
    },
    {
      label: '规则参数',
      field: 'params',
    },
    {
      label: '重置开始日期',
      field: 'resetStartTime',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
    {
      label: '重置结束日期',
      field: 'resetEndTime',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
    {
      label: '重置时间',
      field: 'resetTime',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
  ].filter(item => !hideColumns.includes(item.field));
};
const ruleItemsColumns = (hideColumns: string[] = []) => {
  const { getEnumDict } = useI18n();
  return [
    {
      fixed: 'left',
      type: 'checkbox',
      width: 60,
    },
    {
      title: 'ID',
      field: 'id',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
    },
    {
      title: '排序值',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
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
      editRender: { name: 'ASelect', props: { options: getEnumDict('FieldParamType') }, enabled: false },
    },
    {
      title: '字段参数值',
      field: 'fieldParamValue',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '日期格式',
      field: 'datePattern',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '序列长度',
      field: 'seqLength',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
    },
    {
      title: '序列增量',
      field: 'seqIncrement',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
    },
    {
      title: '序列起始值',
      field: 'seqStartValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
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
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
  ruleItemsColumns,
};
