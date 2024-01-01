import { DescItem } from '@begcode/components';
import dayjs from 'dayjs';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields: DescItem[] = [
  {
    label: 'ID',
    field: 'id',
    show: values => {
      return values && values.id;
    },
  },
  {
    label: '标题',
    field: 'title',
  },
  {
    label: '摘要',
    field: 'summary',
  },
  {
    label: '内容',
    field: 'content',
  },
  {
    label: '开始时间',
    field: 'startTime',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '结束时间',
    field: 'endTime',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '发布人Id',
    field: 'senderId',
  },
  {
    label: '优先级',
    field: 'priority',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('PriorityLevel').find(item => item.value === value) || value;
    },
  },
  {
    label: '消息类型',
    field: 'category',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('AnnoCategory').find(item => item.value === value) || value;
    },
  },
  {
    label: '通告对象类型',
    field: 'receiverType',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('ReceiverType').find(item => item.value === value) || value;
    },
  },
  {
    label: '发布状态',
    field: 'sendStatus',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('AnnoSendStatus').find(item => item.value === value) || value;
    },
  },
  {
    label: '发布时间',
    field: 'sendTime',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '撤销时间',
    field: 'cancelTime',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '业务类型',
    field: 'businessType',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('AnnoBusinessType').find(item => item.value === value) || value;
    },
  },
  {
    label: '业务id',
    field: 'businessId',
  },
  {
    label: '打开方式',
    field: 'openType',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('AnnoOpenType').find(item => item.value === value) || value;
    },
  },
  {
    label: '组件/路由 地址',
    field: 'openPage',
  },
  {
    label: '指定接收者id',
    field: 'receiverIds',
    show: values => {
      return values && values.receiverType !== 'ALL';
    },
  },
  {
    label: '创建者Id',
    field: 'createdBy',
  },
  {
    label: '创建时间',
    field: 'createdDate',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '修改者Id',
    field: 'lastModifiedBy',
  },
  {
    label: '修改时间',
    field: 'lastModifiedDate',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
];

export default {
  fields,
};
