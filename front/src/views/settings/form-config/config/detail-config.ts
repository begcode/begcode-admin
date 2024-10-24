import { Switch } from 'ant-design-vue';
import type { DescItem } from '@/components/Descriptions';
import { useI18n } from '@/hooks/web/useI18n';
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
      render: (value, _data) =>
        h(CodeEditor, {
          value,
          options: { mode: 'application/json' },
          onInput: value => {
            _data.formJson = value;
          },
        }),
    },
    {
      label: '表单类型',
      field: 'formType',
      format: (value, _data) => {
        const { getEnumDict } = useI18n();
        return (getEnumDict('FormConfigType').find(item => item.value === value) || { value: value, label: value }).label;
      },
    },
    {
      label: '多条数据',
      field: 'multiItems',
      show: values => {
        return values && values.formType === 'DATA_FORM';
      },
      render: (value, data) =>
        h(Switch, {
          disabled: true,
          checked: value,
          onChange: checked => {
            data.multiItems = checked;
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
      label: '业务类别',
      field: 'businessType.name',
    },
  ].filter(item => !hideColumns.includes(item.field));
};

export default {
  fields,
};
