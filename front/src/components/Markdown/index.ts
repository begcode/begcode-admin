import markDown from './src/Markdown.vue';
import markDownViewer from './src/MarkdownViewer.vue';
import { withInstall } from '@/utils/util';

export const MarkDown = withInstall(markDown);
export const MarkdownViewer = withInstall(markDownViewer);
export * from './src/typing';
