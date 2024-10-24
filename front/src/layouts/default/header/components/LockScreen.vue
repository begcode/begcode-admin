<template>
  <a-tooltip :title="t('layout.header.tooltipLock')" placement="bottom" :mouseEnterDelay="0.5" @click="handleLock">
    <Icon icon="ant-design:lock-outlined" />
  </a-tooltip>
  <LockModal ref="modalRef" v-if="lockModalVisible" @register="register" />
</template>
<script lang="ts">
import { useModal } from '@/components/Modal';
import { getRefPromise } from '@/utils/util';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { useI18n } from '@/hooks/web/useI18n';

export default defineComponent({
  name: 'LockScreen',
  inheritAttrs: false,
  components: {
    LockOutlined,
    LockModal: createAsyncComponent(() => import('./lock/LockModal.vue')),
  },
  setup() {
    const { t } = useI18n();
    const [register, { openModal }] = useModal();

    const lockModalVisible = ref(false);
    const modalRef = ref(null);
    async function handleLock() {
      lockModalVisible.value = true;
      await getRefPromise(modalRef);
      openModal(true);
    }

    return {
      t,
      register,
      handleLock,
      lockModalVisible,
      modalRef,
    };
  },
});
</script>
