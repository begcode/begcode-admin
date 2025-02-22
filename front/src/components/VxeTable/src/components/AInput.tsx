import { createDefaultFilterRender, createDefaultRender, createEditRender, createFilterRender, createFormItemRender } from './common';

export default {
  autofocus: 'input.ant-input',
  renderDefault: createDefaultRender(),
  renderEdit: createEditRender(),
  renderFilter: createFilterRender(),
  defaultFilterMethod: createDefaultFilterRender(),
  renderItemContent: createFormItemRender(),
};
