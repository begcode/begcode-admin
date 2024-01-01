import { getCurrentInstance } from 'vue';
import Log from '@/utils/Log';

export function useLog() {
  const internalInstance = getCurrentInstance();
  return internalInstance?.appContext.config.globalProperties.$log || Log;
}
