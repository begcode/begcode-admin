import antdLocale from 'ant-design-vue/es/locale/en_US';
import { genMessage } from '../helper';
import vxeEn from 'vxe-table/lib/locale/lang/en-US';

const modules = import.meta.glob('./*.json', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'en'),
    antdLocale,
    ...vxeEn,
  },
  dateLocale: null,
  dateLocaleName: 'en',
};
