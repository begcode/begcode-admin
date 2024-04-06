import { DescItem } from '@begcode/components';

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
    label: '表单Key',
    field: 'formKey',
  },
  {
    label: '名称',
    field: 'formName',
  },
  {
    label: '表单配置',
    field: 'formJson',
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
    label: '业务类别',
    field: 'businessTypeName',
  },
];

export default {
  fields,
};
