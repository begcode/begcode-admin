import Sortable from 'sortablejs';

export function useSortable(el: HTMLElement | Ref<HTMLElement | undefined>, options?: Sortable.Options) {
  function initSortable() {
    nextTick(async () => {
      const element = unref(el);
      if (!element) return;
      Sortable.create(element as HTMLElement, {
        animation: 100,
        delay: 400,
        delayOnTouchOnly: true,
        ...options,
      });
    });
  }

  return { initSortable };
}
