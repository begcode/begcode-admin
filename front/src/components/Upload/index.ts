import basicUpload from './src/BasicUpload.vue';
import uploadImage from './src/components/ImageUpload.vue';
import { withInstall } from '@/utils/util';

export const ImageUpload = withInstall(uploadImage);
export const BasicUpload = withInstall(basicUpload);
