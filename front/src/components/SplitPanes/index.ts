import splitPanes from './src/split-panes.vue';
import pane from './src/split-pane.vue';
import { withInstall } from '@/utils/util';

export const SplitPanes = withInstall(splitPanes);
export const SplitPane = withInstall(pane);
