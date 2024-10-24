import type { ButtonType } from 'ant-design-vue/es/button/buttonTypes';
import { useI18n } from '@/hooks/web/useI18nOut';

export const footerProps = {
  confirmLoading: { type: Boolean },
  /**
   * @description: Show close button
   */
  showCancelBtn: { type: Boolean, default: true },
  cancelButtonProps: Object as PropType<Record<string, any>>,
  cancelText: {
    type: String,
    default: () => {
      const { t } = useI18n();
      return t('common.cancelText');
    },
  },
  /**
   * @description: Show confirmation button
   */
  showOkBtn: { type: Boolean, default: true },
  okButtonProps: Object as PropType<Record<string, any>>,
  okText: {
    type: String,
    default: () => {
      const { t } = useI18n();
      return t('common.okText');
    },
  },
  okType: { type: String as PropType<ButtonType>, default: 'primary' },
  showFooter: { type: Boolean },
  footerHeight: {
    type: [String, Number] as PropType<string | number>,
    default: 60,
  },
};
export const basicProps = {
  class: { type: [String, Object, Array] },
  isDetail: { type: Boolean },
  title: { type: String, default: '' },
  loadingText: { type: String },
  showDetailBack: { type: Boolean, default: true },
  open: { type: Boolean },
  loading: { type: Boolean },
  maskClosable: { type: Boolean, default: true },
  getContainer: {
    type: [Object, String] as PropType<any>,
  },
  closeFunc: {
    type: [Function, Object] as PropType<any>,
    default: null,
  },
  destroyOnClose: { type: Boolean },
  ...footerProps,
};
