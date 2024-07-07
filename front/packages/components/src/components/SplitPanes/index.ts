import splitPanes from './src/splitpanes.vue';
import pane from './src/pane.vue';
import { withInstall } from '@/utils';

export const SplitPanes = withInstall(splitPanes);
export const Pane = withInstall(pane);
