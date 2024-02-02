import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import apiService from '@/api-service/index';

const resourceCategoryService = apiService.files.resourceCategoryService;
const relationshipApis: any = {
  children: apiService.files.resourceCategoryService.tree,
  parent: apiService.files.resourceCategoryService.tree,
  images: apiService.files.uploadImageService.retrieve,
  files: apiService.files.uploadFileService.retrieve,
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
      title: '标题',
      field: 'title',
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
      title: '排序',
      field: 'orderNumber',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '下级列表',
      field: 'children',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.children, style: 'width: 100%', valueField: 'id', labelField: 'title' },
    },
    {
      title: '上级',
      field: 'parent',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.parent, style: 'width: 100%', valueField: 'id', labelField: 'title' },
    },
    {
      title: '图片列表',
      field: 'images',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.images, style: 'width: 100%', valueField: 'id', labelField: 'url' },
    },
    {
      title: '文件列表',
      field: 'files',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.files, style: 'width: 100%', valueField: 'id', labelField: 'url' },
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
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '标题',
      field: 'title',
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
      title: '排序',
      field: 'orderNumber',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '图片列表',
      field: 'images',
      minWidth: 120,
      editRender: {
        name: 'ASelectModal',
        enabled: false,
        props: {
          showComponentName: 'Select',
          container: 'modal',
          componentName: 'UploadImageList',
          multiple: true,
          style: { width: '100%' },
          gridCustomConfig: { hideColumns: ['category'] },
          queryNames: ['categoryId'],
          avatarSlotName: 'default',
          avatarSlotField: 'url',
          avatarTipField: 'url',
          rowIdField: 'row.id',
          source: 'ResourceCategory',
        },
      },
    },
    {
      title: '文件列表',
      field: 'files',
      minWidth: 120,
      editRender: {
        name: 'ASelectModal',
        enabled: false,
        props: {
          showComponentName: 'Select',
          container: 'modal',
          componentName: 'UploadFileList',
          multiple: true,
          style: { width: '100%' },
          gridCustomConfig: { hideColumns: ['category'] },
          queryNames: ['categoryId'],
          avatarSlotName: 'default',
          avatarSlotField: 'url',
          avatarTipField: 'url',
          rowIdField: 'row.id',
          source: 'ResourceCategory',
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

export default {
  searchForm,
  columns,
  baseGridOptions,
};
