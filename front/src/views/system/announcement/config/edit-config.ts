import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import apiService from '@/api-service/index';

const announcementService = apiService.system.announcementService;
const relationshipApis: any = {};

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
      label: '标题',
      field: 'title',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入标题', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '摘要',
      field: 'summary',
      component: 'InputTextArea',
      componentProps: { placeholder: '请输入摘要', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '内容',
      field: 'content',
      component: 'Editor',
      componentProps: { placeholder: '请输入内容', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '开始时间',
      field: 'startTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择开始时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '结束时间',
      field: 'endTime',
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择结束时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '发布人Id',
      field: 'senderId',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入发布人Id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '优先级',
      field: 'priority',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择优先级', options: getEnumDict('PriorityLevel'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '消息类型',
      field: 'category',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择消息类型', options: getEnumDict('AnnoCategory'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '通告对象类型',
      field: 'receiverType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择通告对象类型', options: getEnumDict('ReceiverType'), style: 'width: 100%' };
      },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '发布状态',
      field: 'sendStatus',
      show: false,
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择发布状态', options: getEnumDict('AnnoSendStatus'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '发布时间',
      field: 'sendTime',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择发布时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '撤销时间',
      field: 'cancelTime',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择撤销时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '业务类型',
      field: 'businessType',
      show: false,
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择业务类型', options: getEnumDict('AnnoBusinessType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '业务id',
      field: 'businessId',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入业务id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '打开方式',
      field: 'openType',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择打开方式', options: getEnumDict('AnnoOpenType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '组件/路由 地址',
      field: 'openPage',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入组件/路由 地址', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '指定接收者id',
      field: 'receiverIds',
      show: ({ values }) => {
        return values && values.receiverType !== 'ALL';
      },
      component: 'SelectModal',
      componentProps: ({ formModel }) => {
        let componentName = '';
        if (formModel.receiverType === 'USER') {
          componentName = 'UserRelation';
        } else if (formModel.receiverType === 'DEPARTMENT') {
          componentName = 'DepartmentRelation';
        } else if (formModel.receiverType === 'POSITION') {
          componentName = 'PositionRelation';
        } else if (formModel.receiverType === 'AUTHORITY') {
          componentName = 'AuthorityRelation';
        }
        return {
          placeholder: '请选择指定接收者id',
          style: 'width: 100%',
          componentName,
          updateType: 'emitSelected',
          showComponentName: 'Avatar',
          queryNames: ['id.in'],
          rowIdField: 'value.id',
          valueType: 'splitString',
        };
      },
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
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入创建时间', style: 'width: 100%' },
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
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入修改时间', style: 'width: 100%' },
      rules: [],
    },
  ];
};
export default {
  fields,
};
