import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import dayjs from 'dayjs';
import apiService from '@/api-service/index';

const relationshipApis: any = {
  children: apiService.settings.departmentService.tree,
  authorities: apiService.system.authorityService.tree,
  parent: apiService.settings.departmentService.tree,
  users: apiService.system.userService.retrieve,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->

const searchForm = (): any[] => {
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
      title: '名称',
      field: 'name',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '代码',
      field: 'code',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '地址',
      field: 'address',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '联系电话',
      field: 'phoneNum',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '联系人',
      field: 'contact',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '创建用户 Id',
      field: 'createUserId',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '创建时间',
      field: 'createTime',
      componentType: 'DateTimeRange',
      operator: '',
      span: 8,
      type: 'Instant',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '下级部门',
      field: 'children',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.children, style: 'width: 100%', valueField: 'id', labelField: 'name' },
    },
    {
      title: '角色列表',
      field: 'authorities',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.authorities, style: 'width: 100%', valueField: 'id', labelField: 'name' },
    },
    {
      title: '上级',
      field: 'parent',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.parent, style: 'width: 100%', valueField: 'id', labelField: 'name' },
    },
    {
      title: '用户列表',
      field: 'users',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.users, style: 'width: 100%', valueField: 'id', labelField: 'firstName' },
    },
  ];
};

const columns = (): VxeGridPropTypes.Columns => {
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
      visible: false,
      treeNode: false,
      params: { type: 'LONG' },
    },
    {
      title: '名称',
      field: 'name',
      minWidth: 160,
      visible: true,
      treeNode: true,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '代码',
      field: 'code',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '地址',
      field: 'address',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '联系电话',
      field: 'phoneNum',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: 'logo地址',
      field: 'logo',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      cellRender: { name: 'AImage' },
    },
    {
      title: '联系人',
      field: 'contact',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '创建用户 Id',
      field: 'createUserId',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
    },
    {
      title: '创建时间',
      field: 'createTime',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'Instant' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '角色列表',
      field: 'authorities',
      minWidth: 120,
      slots: { default: 'authorities_default', edit: 'authorities_default' },
      editRender: { name: 'ASelectModal' },
    },
    {
      title: '用户列表',
      field: 'users',
      minWidth: 120,
      slots: { default: 'users_default' },
      editRender: { name: 'ASelectModal' },
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
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    keepSource: true,
    id: 'vxe_grid_department',
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
      autoHidden: true,
      slots: {
        left: pagerLeft,
      },
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
      props: {
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
    treeConfig: {
      childrenField: 'children',
      indent: 20,
      showLine: false,
      expandAll: false,
      accordion: false,
      trigger: 'default',
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
