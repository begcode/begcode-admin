import type { VxeGridPropTypes, VxeGridProps } from 'vxe-table/types/grid';
import dayjs from 'dayjs';
import apiService from '@/api-service/index';
import { useI18n } from '@/hooks/web/useI18n';

const announcementService = apiService.system.announcementService;
const relationshipApis: any = {};

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
      title: '开始时间',
      field: 'startTime',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'ZonedDateTime',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '结束时间',
      field: 'endTime',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'ZonedDateTime',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '发布人Id',
      field: 'senderId',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '优先级',
      field: 'priority',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('PriorityLevel'), style: 'width: 100%' };
      },
    },
    {
      title: '消息类型',
      field: 'category',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('AnnoCategory'), style: 'width: 100%' };
      },
    },
    {
      title: '通告对象类型',
      field: 'receiverType',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('ReceiverType'), style: 'width: 100%' };
      },
    },
    {
      title: '发布状态',
      field: 'sendStatus',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('AnnoSendStatus'), style: 'width: 100%' };
      },
    },
    {
      title: '发布时间',
      field: 'sendTime',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'ZonedDateTime',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '撤销时间',
      field: 'cancelTime',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'ZonedDateTime',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '业务类型',
      field: 'businessType',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('AnnoBusinessType'), style: 'width: 100%' };
      },
    },
    {
      title: '业务id',
      field: 'businessId',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '打开方式',
      field: 'openType',
      componentType: 'Select',
      value: '',
      span: 8,
      operator: '',
      type: 'Enum',
      componentProps: () => {
        return { options: getEnumDict('AnnoOpenType'), style: 'width: 100%' };
      },
    },
    {
      title: '组件/路由 地址',
      field: 'openPage',
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
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '标题',
      field: 'title',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '开始时间',
      field: 'startTime',
      minWidth: 140,
      visible: true,
      treeNode: false,
      params: { type: 'ZONED_DATE_TIME' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '结束时间',
      field: 'endTime',
      minWidth: 140,
      visible: true,
      treeNode: false,
      params: { type: 'ZONED_DATE_TIME' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '发布人Id',
      field: 'senderId',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '优先级',
      field: 'priority',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('PriorityLevel').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('PriorityLevel') }, enabled: false },
    },
    {
      title: '消息类型',
      field: 'category',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('AnnoCategory').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('AnnoCategory') }, enabled: false },
    },
    {
      title: '通告对象类型',
      field: 'receiverType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('ReceiverType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('ReceiverType') }, enabled: false },
    },
    {
      title: '发布状态',
      field: 'sendStatus',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('AnnoSendStatus').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('AnnoSendStatus') }, enabled: false },
    },
    {
      title: '发布时间',
      field: 'sendTime',
      minWidth: 140,
      visible: true,
      treeNode: false,
      params: { type: 'ZONED_DATE_TIME' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '撤销时间',
      field: 'cancelTime',
      minWidth: 140,
      visible: true,
      treeNode: false,
      params: { type: 'ZONED_DATE_TIME' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '业务类型',
      field: 'businessType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('AnnoBusinessType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('AnnoBusinessType') }, enabled: false },
    },
    {
      title: '业务id',
      field: 'businessId',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
    },
    {
      title: '打开方式',
      field: 'openType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('AnnoOpenType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('AnnoOpenType') }, enabled: false },
    },
    {
      title: '组件/路由 地址',
      field: 'openPage',
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
