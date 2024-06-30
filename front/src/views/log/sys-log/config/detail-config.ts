import { DescItem } from '@begcode/components';
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
    label: '日志类型',
    field: 'logType',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return (getEnumDict('LogType').find(item => item.value === value) || { value: value, label: value }).label;
    },
  },
  {
    label: '日志内容',
    field: 'logContent',
  },
  {
    label: '操作类型',
    field: 'operateType',
    format: (value, _data) => {
      const { getEnumDict } = useI18n();
      return (getEnumDict('OperateType').find(item => item.value === value) || { value: value, label: value }).label;
    },
  },
  {
    label: '操作用户账号',
    field: 'userid',
  },
  {
    label: '操作用户名称',
    field: 'username',
  },
  {
    label: 'IP',
    field: 'ip',
  },
  {
    label: '请求java方法',
    field: 'method',
  },
  {
    label: '请求路径',
    field: 'requestUrl',
  },
  {
    label: '请求参数',
    field: 'requestParam',
  },
  {
    label: '请求类型',
    field: 'requestType',
  },
  {
    label: '耗时',
    field: 'costTime',
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
];

export default {
  fields,
};
