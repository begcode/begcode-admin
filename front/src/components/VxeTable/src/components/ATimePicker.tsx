import { getDatePickerCellValue } from './ADatePicker';
import { createCellRender, createEditRender, createExportMethod, createFormItemRender } from './common';

export default {
  renderEdit: createEditRender(),
  renderCell: createCellRender(getDatePickerCellValue, () => {
    return ['HH:mm:ss'];
  }),
  renderItemContent: createFormItemRender(),
  exportMethod: createExportMethod(getDatePickerCellValue, () => {
    return ['HH:mm:ss'];
  }),
};
