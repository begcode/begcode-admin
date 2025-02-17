import type { App } from 'vue';
import type { Pinia } from 'pinia';
import { createPinia } from 'pinia';
import { registerPiniaPersistPlugin } from '@/store/plugin/persist';

let store: Nullable<Pinia> = null;

export function setupStore(app: App<Element>) {
  if (store == null) {
    store = createPinia();
    registerPiniaPersistPlugin(store);
  }
  app.use(store);
}

// 销毁store
export function destroyStore() {
  store = null;
}

export { store };
