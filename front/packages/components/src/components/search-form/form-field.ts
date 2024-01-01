import { FormSchema } from '@/components/Form';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields: FormSchema[] = [
  {
    label: '条件组合关系',
    field: 'useOr',
    show: true,
    component: 'RadioButtonGroup',
    componentProps: {
      style: 'width: 100%',
      options: [
        { value: true, label: '或者' },
        { value: false, label: '并且' },
      ],
    },
  },
];
export default {
  fields,
};
