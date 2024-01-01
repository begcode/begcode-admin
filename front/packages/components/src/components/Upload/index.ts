import basicUpload from './src/BasicUpload.vue';
import uploadImage from './src/components/ImageUpload.vue';
import { withInstall } from '@/utils';

export const ImageUpload = withInstall(uploadImage);
export const BasicUpload = withInstall(basicUpload);
