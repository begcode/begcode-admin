import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import apiService from '@/api-service/index';
import { useI18n } from '@/hooks/web/useI18n';

const fillRuleItemService = apiService.settings.fillRuleItemService;
const relationshipApis: any = {
  fillRule: apiService.settings.sysFillRuleService.retrieve,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->

const searchForm = (): any[] => {
  const { getEnumDict } = useI18n();
  return [
    {
      title: 'ID',
      field: 'id',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '排序值',
      field: 'sortValue',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '字段参数类型',
      field: 'fieldParamType',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('FieldParamType'), style: 'width: 100%' };
      },
    },
    {
      title: '字段参数值',
      field: 'fieldParamValue',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '日期格式',
      field: 'datePattern',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '序列长度',
      field: 'seqLength',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '序列增量',
      field: 'seqIncrement',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '序列起始值',
      field: 'seqStartValue',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '填充规则',
      field: 'fillRule',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.fillRule, style: 'width: 100%', valueField: 'id', labelField: 'name' },
    },
  ];
};

const columns = (): VxeGridPropTypes.Columns => {
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
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '排序值',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      titlePrefix: { icon: 'vxe-icon-sort', content: '排序操作列' },
      editRender: { name: 'ADragSort', enabled: false, props: { remoteApi: fillRuleItemService.updateSortValue } },
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
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '序列增量',
      field: 'seqIncrement',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '序列起始值',
      field: 'seqStartValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '填充规则',
      field: 'fillRule',
      minWidth: 120,
      editRender: {
        name: 'ASelectModal',
        enabled: false,
        props: {
          showComponentName: 'Select',
          container: 'modal',
          componentName: 'SysFillRuleDetail',
          multiple: false,
          style: { width: '100%' },
          queryNames: ['id'],
          avatarSlotName: 'default',
          avatarSlotField: 'name',
          avatarTipField: 'name',
          rowIdField: 'value.id',
          source: 'FillRuleItem',
        },
      },
    },
    {
      title: '操作',
      field: 'operation',
      fixed: 'right',
      headerAlign: 'center',
      align: 'right',
      showOverflow: false,
      width: 170,
      slots: { default: 'recordAction' },
    },
  ];
};

const baseGridOptions = (): VxeGridProps => {
  return {
    rowConfig: {
      keyField: 'id',
      isHover: true,
    },
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    keepSource: true,
    id: 'full_edit_1',
    height: 600,
    printConfig: {
      columns: [
        // { field: 'name' },
      ],
    },
    filterConfig: {
      remote: true,
    },
    columnConfig: {
      resizable: true,
    },
    sortConfig: {
      trigger: 'cell',
      remote: true,
      orders: ['asc', 'desc', null],
      multiple: true,
      defaultSort: {
        field: 'id',
        order: 'desc',
      },
    },
    pagerConfig: {
      layouts: ['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total'],
      pageSize: 15,
      pageSizes: [5, 10, 15, 20, 30, 50],
      total: 0,
      pagerCount: 5,
      currentPage: 1,
      autoHidden: true,
    },
    importConfig: {},
    exportConfig: {},
    checkboxConfig: {
      // labelField: 'id',
      reserve: true,
      highlight: true,
    },
    editRules: {},
    editConfig: {
      trigger: 'click',
      mode: 'cell',
      showStatus: true,
    },
  };
};

export default {
  searchForm,
  columns,
  baseGridOptions,
};
