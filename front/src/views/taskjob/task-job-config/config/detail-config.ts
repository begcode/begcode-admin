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
    label: '任务名称',
    field: 'name',
  },
  {
    label: '任务类名',
    field: 'jobClassName',
  },
  {
    label: 'cron表达式',
    field: 'cronExpression',
  },
  {
    label: '参数',
    field: 'parameter',
  },
  {
    label: '描述',
    field: 'description',
  },
  {
    label: '任务状态',
    field: 'jobStatus',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return getEnumDict('JobStatus').find(item => item.value === value) || value;
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
