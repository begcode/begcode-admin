import { FormSchema } from '@/components/Form';

export const TypeOperator = {
  text: [
    {
      title: '包含',
      value: 'contains',
    },
    {
      title: '不包含',
      value: 'doesNotContain',
    },
    {
      title: '等于',
      value: 'equals',
    },
    {
      title: '不等于',
      value: 'notEquals',
    },
    {
      title: '非空',
      value: 'specified',
    },
    {
      title: '包括',
      value: 'in',
    },
    {
      title: '不包括',
      value: 'notIn',
    },
  ],
  range: [
    {
      title: '大于',
      value: 'greaterThan',
    },
    {
      title: '小于',
      value: 'lessThan',
    },
    {
      title: '大于等于',
      value: 'greaterThanOrEqual',
    },
    {
      title: '小于等于',
      value: 'lessThanOrEqual',
    },
    {
      title: '等于',
      value: 'equals',
    },
    {
      title: '不等于',
      value: 'notEquals',
    },
    {
      title: '非空',
      value: 'specified',
    },
    {
      title: '包括',
      value: 'in',
    },
    {
      title: '不包括',
      value: 'notIn',
    },
  ],
  dateTime: [
    {
      title: '大于',
      value: 'greaterThan',
    },
    {
      title: '小于',
      value: 'lessThan',
    },
    {
      title: '大于等于',
      value: 'greaterThanOrEqual',
    },
    {
      title: '小于等于',
      value: 'lessThanOrEqual',
    },
    {
      title: '等于',
      value: 'equals',
    },
    {
      title: '不等于',
      value: 'notEquals',
    },
    {
      title: '非空',
      value: 'specified',
    },
  ],
  common: [
    {
      title: '等于',
      value: 'equals',
    },
    {
      title: '不等于',
      value: 'notEquals',
    },
    {
      title: '非空',
      value: 'specified',
    },
    {
      title: '包括',
      value: 'in',
    },
    {
      title: '不包括',
      value: 'notIn',
    },
  ],
  null: [
    {
      title: '非空',
      value: 'specified',
    },
  ],
};

export const fields: FormSchema[] = [
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

export const clearSearchFieldValue = field => {
  switch (field.type) {
    case 'Boolean':
      if (field.operator === 'in' || field.operator === 'notIn') {
        field.value = [];
      } else {
        field.value = null;
      }
      break;
    case 'Enum':
      field.props = { multiple: false };
      if (field.operator === 'in' || field.operator === 'notIn') {
        field.value = [];
      } else {
        field.value = null;
      }
      break;
    case 'UUID':
    case 'String':
      field.componentType = 'Text';
      if (field.operator === 'in' || field.operator === 'notIn') {
        field.value = [];
      } else {
        field.value = null;
      }
      break;
    case 'Integer':
    case 'Long':
    case 'Float':
    case 'Double':
    case 'BigDecimal':
      if (field.operator === 'in' || field.operator === 'notIn') {
        field.value = [];
      } else {
        field.value = null;
      }
      break;
    case 'LocalDate':
    case 'Date':
      if (field.operator === 'in' || field.operator === 'notIn') {
        field.value = [];
      } else {
        field.value = null;
      }
      break;
    case 'ZonedDateTime':
    case 'Instant':
    case 'Duration':
      if (field.operator === 'specified') {
        field.value = null;
      } else {
        field.value = [];
      }
      break;
    case 'Blob':
    case 'AnyBlob':
    case 'ImageBlob':
    case 'TextBlob':
    case 'ByteBuffer':
      field.value = null;
      break;
    case 'RelationId':
      if (field.operator === 'in') {
        field.value = [];
      } else {
        field.value = null;
      }
      break;
    default:
      field.value = null;
  }
};
