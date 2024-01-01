import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import apiService from '@/api-service/index';

const dictionaryService = apiService.settings.dictionaryService;
const relationshipApis: any = {
  items: apiService.settings.commonFieldDataService.retrieve,
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
      title: '字典名称',
      field: 'dictName',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '字典Key',
      field: 'dictKey',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '是否禁用',
      field: 'disabled',
      componentType: 'Switch',
      value: '',
      operator: '',
      span: 8,
      type: 'Boolean',
      componentProps: {},
    },
    {
      title: '排序',
      field: 'sortValue',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '是否内置',
      field: 'builtIn',
      componentType: 'Switch',
      value: '',
      operator: '',
      span: 8,
      type: 'Boolean',
      componentProps: {},
    },
    {
      title: '更新枚举',
      field: 'syncEnum',
      componentType: 'Switch',
      value: '',
      operator: '',
      span: 8,
      type: 'Boolean',
      componentProps: {},
    },
    {
      title: '字典项列表',
      field: 'items',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.items, style: 'width: 100%', valueField: 'id', labelField: 'name' },
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
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '字典名称',
      field: 'dictName',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '字典Key',
      field: 'dictKey',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '是否禁用',
      field: 'disabled',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: false } },
    },
    {
      title: '排序',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      titlePrefix: { icon: 'vxe-icon-sort', content: '排序操作列' },
      editRender: { name: 'ADragSort', enabled: false, props: { remoteApi: dictionaryService.updateSortValue } },
    },
    {
      title: '是否内置',
      field: 'builtIn',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: false } },
    },
    {
      title: '更新枚举',
      field: 'syncEnum',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: false } },
    },
    {
      title: '字典项列表',
      field: 'items',
      minWidth: 120,
      editRender: {
        name: 'ASelectModal',
        enabled: false,
        props: {
          showComponentName: 'Select',
          container: 'modal',
          componentName: 'CommonFieldDataList',
          multiple: true,
          style: { width: '100%' },
          gridCustomConfig: { hideColumns: ['siteConfig', 'dictionary'] },
          queryNames: ['id.in'],
          avatarSlotName: 'default',
          avatarSlotField: 'name',
          avatarTipField: 'name',
          rowIdField: 'value.id',
          source: 'Dictionary',
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
