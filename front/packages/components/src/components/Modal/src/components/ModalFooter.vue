<template>
  <div>
    <slot name="insertFooter"></slot>
    <Button v-bind="cancelButtonProps" @click="handleCancel" v-if="showCancelBtn">
      {{ cancelText }}
    </Button>
    <slot name="centerFooter"></slot>
    <Button :type="okType" @click="handleOk" :loading="confirmLoading" v-bind="okButtonProps" v-if="showOkBtn">
      {{ okText }}
    </Button>
    <slot name="appendFooter"></slot>
  </div>
</template>
<script lang="ts" setup>
import { Button } from 'ant-design-vue';
import { basicProps } from '../props';

defineOptions({ name: 'BasicModalFooter' });

defineProps(basicProps);

const emit = defineEmits(['ok', 'cancel']);

function handleOk(e: Event) {
  emit('ok', e);
}

function handleCancel(e: Event) {
  emit('cancel', e);
}
</script>
