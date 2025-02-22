import {
  createDefaultFilterRender,
  createDefaultRender,
  createEditRender,
  createFilterRender,
  createFormItemRender,
  createToolbarToolRender,
} from './common';

export default {
  renderDefault: createDefaultRender(),
  renderEdit: createEditRender(),
  renderFilter: createFilterRender(),
  defaultFilterMethod: createDefaultFilterRender(),
  renderItemContent: createFormItemRender(),
  renderToolbarTool: createToolbarToolRender(),
};
