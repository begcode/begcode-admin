import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';
import { useI18n } from '@/hooks/web/useI18n';

const siteConfigService = apiService.settings.siteConfigService;
const commonFieldDataService = apiService.settings.commonFieldDataService;
const relationshipApis: any = {
  items: apiService.settings.commonFieldDataService.retrieve,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: ({ values }) => {
        return values && values.id;
      },
      dynamicDisabled: true,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '分类名称',
      field: 'categoryName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入分类名称', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '分类Key',
      field: 'categoryKey',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入分类Key', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '是否禁用',
      field: 'disabled',
      component: 'Switch',
      componentProps: { placeholder: '请选择是否禁用' },
      rules: [],
    },
    {
      label: '排序',
      field: 'sortValue',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '是否内置',
      field: 'builtIn',
      component: 'Switch',
      componentProps: { placeholder: '请选择是否内置' },
      rules: [],
    },
    {
      label: '创建者Id',
      field: 'createdBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入创建者Id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建时间',
      field: 'createdDate',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择创建时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '修改者Id',
      field: 'lastModifiedBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入修改者Id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '修改时间',
      field: 'lastModifiedDate',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择修改时间', style: 'width: 100%' },
      rules: [],
    },
  ];
};
const itemsColumns = () => {
  const { getEnumDict } = useI18n();
  return [
    {
      fixed: 'left',
      type: 'checkbox',
      width: 60,
    },
    {
      title: '名称',
      field: 'name',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '字段值',
      field: 'value',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '字段标题',
      field: 'label',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '字段类型',
      field: 'valueType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('CommonFieldType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('CommonFieldType') }, enabled: true },
    },
    {
      title: '说明',
      field: 'remark',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '排序',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      titlePrefix: { icon: 'vxe-icon-sort', content: '排序操作列' },
      editRender: { name: 'ADragSort', enabled: true, props: { remoteApi: commonFieldDataService.updateSortValue } },
    },
    {
      title: '是否禁用',
      field: 'disabled',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: true } },
    },
    {
      title: '实体名称',
      field: 'ownerEntityName',
      minWidth: 160,
      visible: false,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '使用实体ID',
      field: 'ownerEntityId',
      minWidth: 80,
      visible: false,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: true, props: { controls: false } },
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
export default {
  fields,
  itemsColumns,
};
