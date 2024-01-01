import { VxeTableDefines } from 'vxe-table';

export interface TableColumnOptions extends VxeTableDefines.ColumnOptions {
  rules?: VxeTableDefines.ValidatorRule[];
}
