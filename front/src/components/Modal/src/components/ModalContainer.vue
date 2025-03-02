<script lang="tsx">
import { useModalDragMove } from '../hooks/useModalDrag';
import type { Recordable } from '#/utils';
import { extendSlots } from '@/utils/helper/tsxHelper';
import { basicProps } from '../props';

export default defineComponent({
  name: 'ModalContainer',
  inheritAttrs: false,
  props: basicProps,
  emits: ['cancel'],
  setup(props, { slots, emit, attrs }) {
    const { open, draggable, destroyOnClose } = toRefs(props);
    useModalDragMove({
      open,
      destroyOnClose,
      draggable,
    });

    const onCancel = (e: Event) => {
      emit('cancel', e);
    };

    return () => {
      const propsData = { ...unref(attrs), ...props, onCancel } as Recordable;
      return <a-modal {...propsData}>{extendSlots(slots)}</a-modal>;
    };
  },
});
</script>
