import antdLocale from 'ant-design-vue/es/locale/zh_CN';
import vxeCn from 'vxe-table/lib/locale/lang/zh-CN';
import { genMessage } from '../helper';
import { deepMerge } from '@/utils/util';

const modules = import.meta.glob('./*.json', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'zh-cn'),
    antdLocale: {
      ...antdLocale,
      DatePicker: deepMerge(antdLocale.DatePicker, genMessage(modules as Recordable<Recordable>, 'zh-CN').antdLocale.DatePicker),
    },
    ...vxeCn,
  },
};
