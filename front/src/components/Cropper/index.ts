import cropperImage from './src/Cropper.vue';
import avatarCropper from './src/CropperAvatar.vue';
import { withInstall } from '@/utils/util';

export * from './src/typing';
export const CropperImage = withInstall(cropperImage);
export const CropperAvatar = withInstall(avatarCropper);
