<template>
  <Tooltip :title="t('layout.header.tooltipLock')" placement="bottom" :mouseEnterDelay="0.5" @click="handleLock">
    <LockOutlined />
  </Tooltip>
  <LockModal ref="modalRef" v-if="lockModalVisible" @register="register" />
</template>
<script lang="ts">
import { defineComponent, ref } from 'vue';
import { Tooltip } from 'ant-design-vue';
import { LockOutlined } from '@ant-design/icons-vue';
import { Icon, useModal } from '@begcode/components';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { useI18n } from '@/hooks/web/useI18n';
import { getRefPromise } from '@begcode/components';

export default defineComponent({
  name: 'LockScreen',
  inheritAttrs: false,
  components: {
    Icon,
    Tooltip,
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
