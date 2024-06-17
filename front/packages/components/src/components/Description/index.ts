import descriptions from './src/Description.vue';
import { withInstall } from '@/utils';

export * from './src/typing';
export { useDescription } from './src/useDescription';
export const Descriptions = withInstall(descriptions);
