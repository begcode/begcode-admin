import { createDefaultRender, createEditRender, createFormItemRender } from './common';
import XEUtils from 'xe-utils';

export default {
  renderDefault: createDefaultRender(),
  renderEdit: createEditRender(),
  renderCell: createDefaultRender({}, (_, params) => {
    return {
      row: XEUtils.get(params, 'row'),
      column: XEUtils.get(params, 'column'),
      xGrid: XEUtils.get(params, '$grid'),
      showComponentName: 'Avatar',
      disabled: true,
    };
  }),
  renderItemContent: createFormItemRender(),
};
