import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import dayjs from 'dayjs';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->

const searchForm = (relationshipApis): any[] => {
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
      title: '模板标题',
      field: 'name',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '模板CODE',
      field: 'code',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '通知类型',
      field: 'sendType',
      componentType: 'Select',
      value: '',
      span: 8,
      hidden: true,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('MessageSendType'), style: 'width: 100%' };
      },
    },
    {
      title: '模板内容',
      field: 'content',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      hidden: true,
      componentProps: {},
    },
    {
      title: '模板测试json',
      field: 'testJson',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      hidden: true,
      componentProps: {},
    },
    {
      title: '模板类型',
      field: 'type',
      componentType: 'Select',
      value: '',
      span: 8,
      hidden: true,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('SmsTemplateType'), style: 'width: 100%' };
      },
    },
    {
      title: '备注',
      field: 'remark',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      hidden: true,
      componentProps: {},
    },
    {
      title: '启用',
      field: 'enabled',
      componentType: 'RadioGroup',
      value: '',
      operator: '',
      span: 8,
      hidden: true,
      type: 'Boolean',
      componentProps: {
        optionType: 'button',
        buttonStyle: 'solid',
        options: [
          { label: '是', value: true },
          { label: '否', value: false },
        ],
      },
    },
    {
      title: '创建者Id',
      field: 'createdBy',
      componentType: 'ApiSelect',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      hidden: true,
      componentProps: { api: relationshipApis.auditingUser, style: 'width: 100%', valueField: 'id', labelField: 'firstName' },
    },
    {
      title: '创建时间',
      field: 'createdDate',
      componentType: 'DateTimeRange',
      operator: '',
      span: 8,
      hidden: true,
      type: 'Instant',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '修改者Id',
      field: 'lastModifiedBy',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      hidden: true,
      componentProps: {},
    },
    {
      title: '修改时间',
      field: 'lastModifiedDate',
      componentType: 'DateTimeRange',
      operator: '',
      span: 8,
      hidden: true,
      type: 'Instant',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '短信服务商',
      field: 'supplier',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.supplier, style: 'width: 100%', valueField: 'id', labelField: 'signName' },
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
      title: '模板标题',
      field: 'name',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '模板CODE',
      field: 'code',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '通知类型',
      field: 'sendType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('MessageSendType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('MessageSendType') }, enabled: false },
    },
    {
      title: '模板内容',
      field: 'content',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '模板测试json',
      field: 'testJson',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '模板类型',
      field: 'type',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('SmsTemplateType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('SmsTemplateType') }, enabled: false },
    },
    {
      title: '备注',
      field: 'remark',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '启用',
      field: 'enabled',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: true } },
    },
    {
      title: '创建者Id',
      field: 'createdBy',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
    },
    {
      title: '创建时间',
      field: 'createdDate',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'Instant' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '修改者Id',
      field: 'lastModifiedBy',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
    },
    {
      title: '修改时间',
      field: 'lastModifiedDate',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'Instant' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '短信服务商',
      field: 'supplier',
      minWidth: 120,
      formatter: ({ cellValue }) => cellValue?.signName || '',
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

const baseGridOptions = (ajax, toolbarButtons, toolbarTools): VxeGridProps => {
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
    id: 'vxe_grid_sms_template',
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
      autoHidden: false,
      slots: {
        left: 'pagerLeft',
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
  cardSlots: {
    type: Array,
    default: ['title', 'rightExtra'],
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
  parentContainer: {
    type: String,
    default: '',
  },
};

export default {
  searchForm,
  columns,
  baseGridOptions,
  ListProps,
};
