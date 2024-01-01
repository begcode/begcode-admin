import { getActivePinia } from 'pinia';

export function getCollapsed(): any {
  const pinia = getActivePinia();
  console.log('pinia', pinia);
  return pinia && pinia.rootState.value;
}
