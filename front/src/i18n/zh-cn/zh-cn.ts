import antdLocale from 'ant-design-vue/es/locale/zh_CN';
import { deepMerge } from '@begcode/components';
import { genMessage } from '../helper';

const modules = import.meta.glob('./*.json', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'zh-cn'),
    antdLocale: {
      ...antdLocale,
      DatePicker: deepMerge(antdLocale.DatePicker, genMessage(modules as Recordable<Recordable>, 'zh-CN').antdLocale.DatePicker),
    },
  },
};
