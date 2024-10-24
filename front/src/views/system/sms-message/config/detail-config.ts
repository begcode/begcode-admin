import dayjs from 'dayjs';
import type { DescItem } from '@/components/Descriptions';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (hideColumns: string[] = []): DescItem[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: values => {
        return values && values.id;
      },
    },
    {
      label: '消息标题',
      field: 'title',
    },
    {
      label: '发送方式',
      field: 'sendType',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('MessageSendType').find(item => item.value === value) || { value: value, label: value }).label;
      },
    },
    {
      label: '接收人',
      field: 'receiver',
    },
    {
      label: '发送所需参数',
      field: 'params',
    },
    {
      label: '推送内容',
      field: 'content',
    },
    {
      label: '推送时间',
      field: 'sendTime',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
    {
      label: '推送状态',
      field: 'sendStatus',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('SendStatus').find(item => item.value === value) || { value: value, label: value }).label;
      },
    },
    {
      label: '发送次数',
      field: 'retryNum',
    },
    {
      label: '推送失败原因',
      field: 'failResult',
    },
    {
      label: '备注',
      field: 'remark',
    },
    {
      label: '创建者Id',
      field: 'createdBy',
    },
    {
      label: '创建时间',
      field: 'createdDate',
    },
    {
      label: '修改者Id',
      field: 'lastModifiedBy',
    },
    {
      label: '修改时间',
      field: 'lastModifiedDate',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
