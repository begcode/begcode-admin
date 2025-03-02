import type { ModalFuncProps } from 'ant-design-vue/lib/modal/Modal';
import { message as Message, Modal, notification } from 'ant-design-vue';
import { ConfigProps, NotificationArgsProps } from 'ant-design-vue/lib/notification';
import { useI18n } from './useI18n';
import { Icon } from '@/components/Icon';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

export interface NotifyApi {
  info(config: NotificationArgsProps): void;
  success(config: NotificationArgsProps): void;
  error(config: NotificationArgsProps): void;
  warn(config: NotificationArgsProps): void;
  warning(config: NotificationArgsProps): void;
  open(args: NotificationArgsProps): void;
  close(key: String): void;
  config(options: ConfigProps): void;
  destroy(): void;
}

export declare type NotificationPlacement = 'topLeft' | 'topRight' | 'bottomLeft' | 'bottomRight';
export declare type IconType = 'success' | 'info' | 'error' | 'warning';
export interface ModalOptionsEx extends Omit<ModalFuncProps, 'iconType'> {
  iconType: 'warning' | 'success' | 'error' | 'info';
}
export type ModalOptionsPartial = Partial<ModalOptionsEx> & Pick<ModalOptionsEx, 'content'>;

function getIcon(iconType: string) {
  try {
    if (iconType === 'warning') {
      return h(Icon, { class: 'modal-icon-warning', icon: 'ant-design:info-circle-filled' });
    } else if (iconType === 'success') {
      return h(Icon, { class: 'modal-icon-success', icon: 'ant-design:check-circle-filled' });
    } else if (iconType === 'info') {
      return h(Icon, { class: 'modal-icon-info', icon: 'ant-design:info-circle-filled' });
    }
    return h(Icon, { class: 'modal-icon-error', icon: 'ant-design:close-circle-filled' });
  } catch (e) {
    console.log(e);
  }
}

function renderContent({ content }: Pick<ModalOptionsEx, 'content'>) {
  try {
    if (_isString(content)) {
      return h('div', h('div', { innerHTML: content as string }));
    }
    return content;
  } catch (e) {
    console.log(e);
    return content;
  }
}

/**
 * @description: Create confirmation box
 */
function createConfirm(options: ModalOptionsEx) {
  const iconType = options.iconType || 'warning';
  Reflect.deleteProperty(options, 'iconType');
  const opt: ModalFuncProps = {
    centered: true,
    icon: getIcon(iconType),
    ...options,
    content: renderContent(options),
  };
  return Modal.confirm(opt);
}

const getBaseOptions = () => {
  const { t } = useI18n();
  return {
    okText: t('common.okText'),
    centered: true,
  };
};

function createModalOptions(options: ModalOptionsPartial, icon: string): ModalOptionsPartial {
  let titleIcon: any = '';
  if (options.icon) {
    titleIcon = options.icon;
  } else {
    titleIcon = getIcon(icon);
  }
  return {
    ...getBaseOptions(),
    ...options,
    content: renderContent(options),
    icon: titleIcon,
  };
}

function createSuccessModal(options: ModalOptionsPartial) {
  return Modal.success(createModalOptions(options, 'success'));
}

function createErrorModal(options: ModalOptionsPartial) {
  return Modal.error(createModalOptions(options, 'close'));
}

function createInfoModal(options: ModalOptionsPartial) {
  return Modal.info(createModalOptions(options, 'info'));
}

function createWarningModal(options: ModalOptionsPartial) {
  return Modal.warning(createModalOptions(options, 'warning'));
}

interface MOE extends Omit<ModalOptionsEx, 'iconType'> {
  iconType?: ModalOptionsEx['iconType'];
}

// 提示框，无需传入iconType，默认为warning
function createConfirmSync(options: MOE) {
  return new Promise(resolve => {
    createConfirm({
      iconType: 'warning',
      ...options,
      onOk: () => resolve(true),
      onCancel: () => resolve(false),
    });
  });
}

notification.config({
  placement: 'topRight',
  duration: 3,
});

/**
 * @description: message
 */
export function useMessage() {
  return {
    createMessage: Message,
    notification: notification as NotifyApi,
    createConfirm,
    createConfirmSync,
    createSuccessModal,
    createErrorModal,
    createInfoModal,
    createWarningModal,
  };
}
