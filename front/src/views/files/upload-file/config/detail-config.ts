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
      label: '完整文件名',
      field: 'fullName',
    },
    {
      label: '业务标题',
      field: 'businessTitle',
    },
    {
      label: '业务自定义描述内容',
      field: 'businessDesc',
    },
    {
      label: '业务状态',
      field: 'businessStatus',
    },
    {
      label: 'Url地址',
      field: 'url',
    },
    {
      label: '文件名',
      field: 'name',
    },
    {
      label: '缩略图Url地址',
      field: 'thumb',
    },
    {
      label: '文件大小',
      field: 'fileSize',
    },
    {
      label: '扩展名',
      field: 'ext',
    },
    {
      label: '文件类型',
      field: 'type',
    },
    {
      label: '本地路径',
      field: 'path',
    },
    {
      label: '存储目录',
      field: 'folder',
    },
    {
      label: '实体名称',
      field: 'ownerEntityName',
    },
    {
      label: '使用实体ID',
      field: 'ownerEntityId',
    },
    {
      label: '创建时间',
      field: 'createAt',
      format: (value, _data) => {
        return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
    {
      label: '被引次数',
      field: 'referenceCount',
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
    {
      label: '所属分类',
      field: 'category.title',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
