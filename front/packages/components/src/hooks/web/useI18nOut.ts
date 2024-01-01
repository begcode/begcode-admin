import { getCurrentInstance } from 'vue';

export function useI18n() {
  const internalInstance = getCurrentInstance();
  const fn = s => s;
  return { t: internalInstance?.appContext.config.globalProperties.$t || fn };
}
