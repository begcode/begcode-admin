import type { DescItem } from '@/components/Descriptions';
import { CodeEditor } from '@/components/CodeEditor';

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
      label: '表单数据',
      field: 'formData',
      render: (value, _data) =>
        h(CodeEditor, {
          value,
          options: { mode: 'application/json' },
          onInput: value => {
            _data.formData = value;
          },
        }),
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
      label: '表单配置',
      field: 'formConfig.formName',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
