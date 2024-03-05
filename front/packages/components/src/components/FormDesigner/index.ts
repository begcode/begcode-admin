import VFormDesign from './src/components/VFormDesign/index.vue';
import VFormCreate from './src/components/VFormCreate/index.vue';
import VFormPreview from './src/components/VFormPreview/index.vue';
import { WithInstall, withInstall } from '@/utils';

export * from './src/typings/base-type';
export * from './src/typings/v-form-component';
export * from './src/typings/form-type';
export * from './src/hooks/useVFormMethods';

export const FormDesigner: WithInstall<typeof VFormDesign> = withInstall<typeof VFormDesign>(VFormDesign);
export const FormCreate: WithInstall<typeof VFormCreate> = withInstall<typeof VFormCreate>(VFormCreate);
export const FormPreview: WithInstall<typeof VFormPreview> = withInstall<typeof VFormPreview>(VFormPreview);
