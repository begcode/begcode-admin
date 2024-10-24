import type { DescriptionProps, DescInstance, UseDescReturnType } from './typing';
import { isProdMode } from '@/utils/env';
import type { Nullable } from '#/utils';

export function useDescriptions(props?: Partial<DescriptionProps>): UseDescReturnType {
  if (!getCurrentInstance()) {
    throw new Error('useDescriptions() can only be used inside setup() or functional components!');
  }
  const desc = ref<Nullable<DescInstance>>(null);
  const loaded = ref(false);

  function register(instance: DescInstance) {
    if (unref(loaded) && isProdMode()) {
      return;
    }
    desc.value = instance;
    props && instance.setDescProps(props);
    loaded.value = true;
  }

  const methods: DescInstance = {
    setDescProps: (descProps: Partial<DescriptionProps>): void => {
      unref(desc)?.setDescProps(descProps);
    },
  };

  return [register, methods];
}
