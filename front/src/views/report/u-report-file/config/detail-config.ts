import dayjs from 'dayjs';
import type { DescItem } from '@/components/Descriptions';

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
      label: '名称',
      field: 'name',
    },
    {
      label: '内容',
      field: 'content',
    },
    {
      label: '创建时间',
      field: 'createAt',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
    {
      label: '更新时间',
      field: 'updateAt',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
