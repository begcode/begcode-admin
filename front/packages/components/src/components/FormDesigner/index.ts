import VFormDesign from './src/components/VFormDesign/index.vue';
import VFormCreate from './src/components/VFormCreate/index.vue';
import VFormPreview from './src/components/VFormPreview/index.vue';
import { withInstall } from '@/utils';

export * from './src/typings/base-type';
export * from './src/typings/v-form-component';
export * from './src/typings/form-type';
export * from './src/hooks/useVFormMethods';

export const FormDesigner = withInstall(VFormDesign);
export const FormCreate = withInstall(VFormCreate);
export const FormPreview = withInstall(VFormPreview);
