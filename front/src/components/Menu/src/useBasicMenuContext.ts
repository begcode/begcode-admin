import type { InjectionKey } from 'vue';
import { createContext, useContext } from '@begcode/components';

export interface BasicRootMenuContextProps {
  menuState: any;
}

const key: InjectionKey<BasicRootMenuContextProps> = Symbol();

export function createBasicRootMenuContext(context: BasicRootMenuContextProps) {
  return createContext<BasicRootMenuContextProps>(context, key, { readonly: false, native: true });
}

export function useBasicRootMenuContext() {
  return useContext<BasicRootMenuContextProps>(key);
}
