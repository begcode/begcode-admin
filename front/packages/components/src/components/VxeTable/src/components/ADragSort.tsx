import { createDefaultRender, createEditRender, createFormItemRender } from './common';

export default {
  renderDefault: createDefaultRender(),
  renderEdit: createEditRender(),
  renderCell: createDefaultRender({}, (_, params) => {
    return {
      params,
      disabled: true,
    };
  }),
  renderItemContent: createFormItemRender(),
};
