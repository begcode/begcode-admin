import { useI18n } from '@/hooks/web/useI18n';
import { FormSchema } from '@/components/Form';
import apiService from '@/api-service/index';

const viewPermissionService = apiService.system.viewPermissionService;
const relationshipApis: any = {
  children: apiService.system.viewPermissionService.tree,
  parent: apiService.system.viewPermissionService.tree,
  authorities: apiService.system.authorityService.tree,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  const { getEnumDict } = useI18n();
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
      label: '权限名称',
      field: 'text',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限名称', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '权限类型',
      field: 'type',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return { options: getEnumDict('ViewPermissionType'), optionType: 'button', style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '多语言Key',
      field: 'localeKey',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入多语言Key', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '显示分组名',
      field: 'group',
      component: 'Switch',
      rules: [],
    },
    {
      label: '路由',
      field: 'link',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入路由', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '外部链接',
      field: 'externalLink',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入外部链接', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '链接目标',
      field: 'target',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择链接目标', options: getEnumDict('TargetType'), showSearch: true, style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '图标',
      field: 'icon',
      component: 'IconPicker',
      componentProps: { placeholder: '请选择图标', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '禁用菜单',
      field: 'disabled',
      component: 'Switch',
      rules: [],
    },
    {
      label: '隐藏菜单',
      field: 'hide',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Switch',
      rules: [],
    },
    {
      label: '隐藏面包屑',
      field: 'hideInBreadcrumb',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Switch',
      rules: [],
    },
    {
      label: '快捷菜单项',
      field: 'shortcut',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Switch',
      rules: [],
    },
    {
      label: '菜单根节点',
      field: 'shortcutRoot',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Switch',
      rules: [],
    },
    {
      label: '允许复用',
      field: 'reuse',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Switch',
      rules: [],
    },
    {
      label: '权限代码',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限代码', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '权限描述',
      field: 'description',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限描述', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '排序',
      field: 'order',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'api权限标识串',
      field: 'apiPermissionCodes',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入api权限标识串', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '组件名称',
      field: 'componentFile',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入组件名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重定向路径',
      field: 'redirect',
      show: ({ values }) => {
        return values && values.type === 'MENU';
      },
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入重定向路径', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '上级',
      field: 'parent',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.parent,
        style: 'width: 100%',
        labelInValue: true,
        fieldNames: { children: 'children', value: 'id', label: 'text' },
        resultField: 'records',
        placeholder: '请选择上级',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
