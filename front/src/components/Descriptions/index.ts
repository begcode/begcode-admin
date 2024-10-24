import descriptions from './src/Descriptions.vue';
import { withInstall } from '@/utils/util';

export * from './src/typing';
export { useDescriptions } from './src/useDescriptions';
export const Descriptions = withInstall(descriptions);
