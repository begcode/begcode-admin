import type { ExtractPropTypes } from 'vue';
import button from './src/BasicButton.vue';
import buttonGroup from './src/ButtonGroup.vue';
import uploadButton from './src/UploadButton.vue';
import popConfirmButton from './src/PopConfirmButton.vue';
import { buttonProps } from './src/props';
import { withInstall } from '@/utils';

export const Button = withInstall(button);
export const ButtonGroup = withInstall(buttonGroup);
export const UploadButton = withInstall(uploadButton);
export const PopConfirmButton = withInstall(popConfirmButton);
export * from './button-config';
export declare type ButtonProps = Partial<ExtractPropTypes<typeof buttonProps>>;
