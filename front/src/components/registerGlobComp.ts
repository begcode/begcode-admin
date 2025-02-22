import { App } from 'vue';
import VXETable from 'vxe-table';
import VXETablePluginExportXLSX from 'vxe-table-plugin-export-xlsx';
import ExcelJS from 'exceljs';
import { VXETablePluginAntd } from './VxeTable';
import { i18n } from '@/i18n/setupI18n';
import 'xe-utils';

export function registerGlobComp(app: App) {
  VXETable.setConfig({
    // 对组件内置的提示语进行国际化翻译
    i18n: (key, args) => {
      return i18n.global.t(key, args);
    },
  });
  app.use(VXETable);
  VXETable.use(VXETablePluginAntd).use(VXETablePluginExportXLSX, { ExcelJS });
}
