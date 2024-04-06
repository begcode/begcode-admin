import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import dayjs from 'dayjs';
import apiService from '@/api-service/index';

const formConfigService = apiService.settings.formConfigService;
const relationshipApis: any = {
  businessType: apiService.settings.businessTypeService.retrieve,
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
      title: '表单Key',
      field: 'formKey',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '名称',
      field: 'formName',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '创建者Id',
      field: 'createdBy',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '创建时间',
      field: 'createdDate',
      componentType: 'DateTime',
      operator: '',
      span: 8,
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
      componentProps: {},
    },
    {
      title: '修改时间',
      field: 'lastModifiedDate',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'Instant',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '业务类别',
      field: 'businessType',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.businessType, style: 'width: 100%', valueField: 'id', labelField: 'name' },
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
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '表单Key',
      field: 'formKey',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '名称',
      field: 'formName',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '创建者Id',
      field: 'createdBy',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
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
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
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
      title: '业务类别',
      field: 'businessType',
      minWidth: 120,
      formatter: ({ cellValue }) => cellValue?.name || '',
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
