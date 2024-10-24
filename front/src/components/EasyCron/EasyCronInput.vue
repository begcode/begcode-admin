<template>
  <div :class="`${prefixCls}`">
    <a-input :placeholder="placeholder" v-model:value="editCronValue" :disabled="disabled">
      <template #addonAfter>
        <a class="open-btn" :disabled="disabled ? 'disabled' : null" @click="showConfigModal">
          <Icon icon="ant-design:setting-outlined" />
          <span>选择</span>
        </a>
      </template>
    </a-input>
    <EasyCronModal
      @register="registerModal"
      v-model:value="editCronValue"
      :exeStartTime="exeStartTime"
      :hideYear="hideYear"
      :remote="remote"
      :hideSecond="hideSecond"
    />
  </div>
</template>

<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign';
import { useModal } from '@/components/Modal';
import EasyCronModal from './EasyCronModal.vue';
import { cronEmits, cronProps } from './easy.cron.data';

const { prefixCls } = useDesign('easy-cron-input');
const emit = defineEmits([...cronEmits]);
const props = defineProps({
  ...cronProps,
  placeholder: {
    type: String,
    default: '请输入cron表达式',
  },
  exeStartTime: {
    type: [Number, String, Object],
    default: 0,
  },
});
const [registerModal, { openModal }] = useModal();
const editCronValue = ref(props.value);

watch(
  () => props.value,
  newVal => {
    if (newVal !== editCronValue.value) {
      editCronValue.value = newVal;
    }
  },
);
watch(editCronValue, newVal => {
  emit('change', newVal);
  emit('update:value', newVal);
});

function showConfigModal() {
  if (!props.disabled) {
    openModal();
  }
}
</script>

<style>
.vben-easy-cron-input a.open-btn {
  cursor: pointer;
}
.vben-easy-cron-input a.open-btn .app-iconify {
  position: relative;
  top: 1px;
  right: 2px;
}
</style>
