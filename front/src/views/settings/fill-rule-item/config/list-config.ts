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
    },
    {
      title: '排序值',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      sortable: true,
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
  ];
};

const baseGridOptions = (ajax, toolbarButtons, toolbarTools, pagerLeft): VxeGridProps => {
  return {
    rowConfig: {
      keyField: 'id',
      isHover: true,
    },
    loading: undefined,
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    keepSource: true,
    id: 'vxe_grid_fill_rule_item',
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
      showIcon: true,
      defaultSort: {
        field: 'sortValue',
        order: 'asc',
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
      slots: {
        left: pagerLeft,
      },
    },
    importConfig: {
      remote: true,
      importMethod: ajax.import,
    },
    exportConfig: {
      columnFilterMethod: ({ column }) => ['radio', 'checkbox'].includes(column.type),
    },
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
    customConfig: {
      storage: {
        visible: true,
        resizable: false,
        sort: true,
        fixed: true,
      },
    },
    proxyConfig: {
      enabled: true,
      autoLoad: true,
      seq: true,
      sort: true,
      filter: true,
      response: {
        result: 'records',
        total: 'total',
      },
      ajax,
    },
    toolbarConfig: {
      custom: false,
      import: false,
      print: false,
      export: false,
      slots: {
        buttons: 'toolbar_buttons',
      },
      buttons: toolbarButtons,
      tools: toolbarTools,
    },
  };
};

const ListProps = {
  query: {
    type: Object,
    default: () => ({}),
  },
  editIn: {
    ype: String,
    default: 'page',
  },
  baseData: {
    type: Object,
    default: () => ({}),
  },
  cardExtra: {
    type: Array,
    default: ['import', 'export', 'print'],
  },
  gridOptions: {
    type: Object,
    default: () => ({}),
  },
  selectType: {
    type: String,
    default: 'checkbox', // checkbox/radio/none/seq
  },
  searchFormOptions: {
    type: Object,
    default: () => ({
      disabled: false,
    }),
  },
  gridCustomConfig: {
    type: Object,
    default: () => ({
      hideOperations: false,
      hideSlots: [],
      hideColumns: [],
    }),
  },
};

export default {
  searchForm,
  columns,
  baseGridOptions,
  ListProps,
};
