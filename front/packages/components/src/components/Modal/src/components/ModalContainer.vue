<script lang="tsx">
import { Modal } from 'ant-design-vue';
import { defineComponent, toRefs, unref } from 'vue';
import { useModalDragMove } from '../hooks/useModalDrag';
import { useAttrs } from '@/hooks/vben';
import type { Recordable } from '#/utils';
import { extendSlots } from '@/utils/helper/tsxHelper';
import { basicProps } from '../props';

export default defineComponent({
  name: 'ModalContainer',
  inheritAttrs: false,
  props: basicProps,
  emits: ['cancel'],
  setup(props, { slots, emit }) {
    const { open, draggable, destroyOnClose } = toRefs(props);
    const attrs = useAttrs();
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
      return <Modal {...propsData}>{extendSlots(slots)}</Modal>;
    };
  },
});
</script>
