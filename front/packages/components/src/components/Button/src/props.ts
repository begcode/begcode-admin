import { PropType } from 'vue';

const validColors = ['primary', 'error', 'warning', 'success', ''] as const;
type ButtonColorType = (typeof validColors)[number];

export const buttonProps = {
  color: {
    type: String as PropType<ButtonColorType>,
    validator: v => validColors.includes(v),
    default: '',
  },
  loading: { type: Boolean },
  disabled: { type: Boolean },
  /**
   * Text before icon.
   */
  preIcon: { type: String },
  /**
   * Text after icon.
   */
  postIcon: { type: String },
  type: { type: String },
  /**
   * preIcon and postIcon icon size.
   * @default: 15
   */
  iconSize: { type: Number, default: 15 },
  isUpload: { type: Boolean, default: false },
  onClick: { type: [Function, Array] as PropType<(() => any) | (() => any)[]>, default: null },
};
