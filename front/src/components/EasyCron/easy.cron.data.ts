export const cronEmits = ['change', 'update:value'];
export const cronProps = {
  value: {
    type: String,
    default: '',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  hideSecond: {
    type: Boolean,
    default: false,
  },
  hideYear: {
    type: Boolean,
    default: false,
  },
  remote: {
    type: Function,
    default: false,
  },
};
