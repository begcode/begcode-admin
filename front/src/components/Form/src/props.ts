import type { RowProps } from 'ant-design-vue/lib/grid/Row';
import dayjs from 'dayjs';
import type { FieldMapToNumber, FieldMapToTime, FormSchema } from './types/form';
import type { ColEx } from './types';
import type { Fn } from '#/types';
import type { Recordable } from '#/utils';
import { ButtonProps } from '@/components/Button';

const form = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 4 },
    xl: { span: 6 },
    xxl: { span: 4 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 18 },
  },
  //表单默认冒号
  colon: true,
};
export const basicProps = {
  model: {
    type: Object as PropType<Recordable>,
    default: () => ({}),
  },
  // 标签宽度  固定宽度
  labelWidth: {
    type: [Number, String] as PropType<number | string>,
    default: 0,
  },
  fieldMapToTime: {
    type: Array as PropType<FieldMapToTime>,
    default: () => [],
  },
  fieldMapToNumber: {
    type: Array as PropType<FieldMapToNumber>,
    default: () => [],
  },
  compact: {
    type: Boolean,
  },
  // 表单配置规则
  schemas: {
    type: Array as PropType<FormSchema[]>,
    default: () => [],
  },
  mergeDynamicData: {
    type: Object as PropType<Recordable>,
    default: null,
  },
  baseRowStyle: {
    type: Object as PropType<CSSProperties>,
  },
  baseColProps: {
    type: Object as PropType<Partial<ColEx>>,
  },
  autoSetPlaceHolder: {
    type: Boolean,
    default: true,
  },
  // 在INPUT组件上单击回车时，是否自动提交
  autoSubmitOnEnter: {
    type: Boolean,
    default: false,
  },
  submitOnReset: {
    type: Boolean,
  },
  submitOnChange: {
    type: Boolean,
  },
  size: {
    type: String as PropType<'default' | 'small' | 'large'>,
    default: 'default',
  },
  // 禁用表单
  disabled: {
    type: Boolean,
  },
  emptySpan: {
    type: [Number, Object] as PropType<number | Recordable>,
    default: 0,
  },
  // 是否显示收起展开按钮
  showAdvancedButton: {
    type: Boolean,
  },
  // 转化时间
  transformDateFunc: {
    type: Function as PropType<Fn>,
    default: (date: any) => {
      return dayjs.isDayjs(date) ? date?.format?.('YYYY-MM-DD HH:mm:ss') : date;
    },
  },
  rulesMessageJoinLabel: {
    type: Boolean,
    default: true,
  },
  // 超过3列自动折叠
  autoAdvancedCol: {
    type: Number,
    default: 3,
  },
  // 超过3行自动折叠
  autoAdvancedLine: {
    type: Number,
    default: 3,
  },
  // 不受折叠影响的行数
  alwaysShowLines: {
    type: Number,
    default: 1,
  },

  // 是否显示操作按钮
  showActionButtonGroup: {
    type: Boolean,
    default: true,
  },
  // 操作列Col配置
  actionColOptions: Object as PropType<Partial<ColEx>>,
  // 显示重置按钮
  showResetButton: {
    type: Boolean,
    default: true,
  },
  // 是否聚焦第一个输入框，只在第一个表单项为input的时候作用
  autoFocusFirstItem: {
    type: Boolean,
  },
  // 重置按钮配置
  resetButtonOptions: Object as PropType<Partial<ButtonProps>>,

  // 显示确认按钮
  showSubmitButton: {
    type: Boolean,
    default: true,
  },
  // 确认按钮配置
  submitButtonOptions: Object as PropType<Partial<ButtonProps>>,

  // 自定义重置函数
  resetFunc: Function as PropType<() => Promise<void>>,
  submitFunc: Function as PropType<() => Promise<void>>,

  // 以下为默认props
  hideRequiredMark: {
    type: Boolean,
  },

  labelCol: {
    type: Object as PropType<Partial<ColEx>>,
    default: form.labelCol,
  },

  layout: {
    type: String as PropType<'horizontal' | 'vertical' | 'inline'>,
    default: 'horizontal',
  },

  tableAction: {
    type: Object,
  },

  wrapperCol: {
    type: Object as PropType<Partial<ColEx>>,
    default: form.wrapperCol,
  },

  colon: {
    type: Boolean,
    default: form.colon,
  },

  labelAlign: {
    type: String,
  },

  rowProps: Object as PropType<RowProps>,

  // 当表单是查询条件的时候 当表单改变后自动查询，不需要点击查询按钮
  autoSearch: {
    type: Boolean,
    default: false,
  },
};
