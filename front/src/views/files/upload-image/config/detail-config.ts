import { DescItem } from '@begcode/components';
import dayjs from 'dayjs';

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
    label: '完整文件名',
    field: 'fullName',
  },
  {
    label: '文件名',
    field: 'name',
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
    label: 'Web Url地址',
    field: 'url',
  },
  {
    label: '本地路径',
    field: 'path',
  },
  {
    label: '本地存储目录',
    field: 'folder',
  },
  {
    label: '使用实体名称',
    field: 'ownerEntityName',
  },
  {
    label: '使用实体ID',
    field: 'ownerEntityId',
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
    label: '创建时间',
    field: 'createAt',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '文件大小',
    field: 'fileSize',
  },
  {
    label: '小图Url',
    field: 'smartUrl',
  },
  {
    label: '中等图Url',
    field: 'mediumUrl',
  },
  {
    label: '文件被引用次数',
    field: 'referenceCount',
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
  {
    label: '所属分类',
    field: 'categoryTitle',
  },
];

export default {
  fields,
};
