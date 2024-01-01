import collapseContainer from './src/collapse/CollapseContainer.vue';
import scrollContainer from './src/ScrollContainer.vue';
import { withInstall } from '@/utils';

export const CollapseContainer = withInstall(collapseContainer);
export const ScrollContainer = withInstall(scrollContainer);

export * from './src/typing';
