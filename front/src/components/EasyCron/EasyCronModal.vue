<template>
  <basic-modal @register="registerModal" title="Cron表达式" width="1000px" @ok="onOk">
    <EasyCron v-bind="attrs" />
  </basic-modal>
</template>

<script lang="ts" setup>
import { useAttrs } from '@/hooks/vben/useAttrs';
import { BasicModal, useModalInner } from '@/components/Modal';
import EasyCron from './EasyCronInner.vue';

defineOptions({
  name: 'EasyCronModal',
  inheritAttrs: false,
});
const attrs = useAttrs();
const [registerModal, { closeModal }] = useModalInner();

function onOk() {
  closeModal();
}
</script>
