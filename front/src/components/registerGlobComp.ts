import VXETable from 'vxe-table';
import VXETablePluginExportXLSX from 'vxe-table-plugin-export-xlsx';
import ExcelJS from 'exceljs';
import { VXETablePluginAntd } from './VxeTable';

export function registerGlobComp(app: App) {
  app.use(VXETable);
  VXETable.use(VXETablePluginAntd).use(VXETablePluginExportXLSX, { ExcelJS });
}
