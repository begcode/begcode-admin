import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import apiService from '@/api-service/index';
import { useI18n } from '@/hooks/web/useI18n';

const regionCodeService = apiService.settings.regionCodeService;
const relationshipApis: any = {
  children: apiService.settings.regionCodeService.tree,
  parent: apiService.settings.regionCodeService.tree,
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
      title: '地区代码',
      field: 'areaCode',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '城市代码',
      field: 'cityCode',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '全名',
      field: 'mergerName',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '短名称',
      field: 'shortName',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '邮政编码',
      field: 'zipCode',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '等级',
      field: 'level',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('RegionCodeLevel'), style: 'width: 100%' };
      },
    },
    {
      title: '经度',
      field: 'lng',
      componentType: 'Text',
      value: '',
      type: 'Double',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '纬度',
      field: 'lat',
      componentType: 'Text',
      value: '',
      type: 'Double',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '子节点',
      field: 'children',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.children, style: 'width: 100%', valueField: 'id', labelField: 'name' },
    },
    {
      title: '上级节点',
      field: 'parent',
      componentType: 'ApiSelect',
      value: '',
      operator: '',
      span: 8,
      componentProps: { api: relationshipApis.parent, style: 'width: 100%', valueField: 'id', labelField: 'name' },
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
      visible: false,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
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
      title: '地区代码',
      field: 'areaCode',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '城市代码',
      field: 'cityCode',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '全名',
      field: 'mergerName',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '短名称',
      field: 'shortName',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '邮政编码',
      field: 'zipCode',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '等级',
      field: 'level',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('RegionCodeLevel').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('RegionCodeLevel') }, enabled: false },
    },
    {
      title: '经度',
      field: 'lng',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'DOUBLE' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '纬度',
      field: 'lat',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'DOUBLE' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
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
