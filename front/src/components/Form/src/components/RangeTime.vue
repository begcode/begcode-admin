<template>
  <a-time-range-picker
    v-model:value="rangeValue"
    @change="handleChange"
    :placeholder="placeholder"
    :valueFormat="format"
    :format="format"
  />
</template>

<script lang="ts" setup>
import { Form } from 'ant-design-vue';
import type { RangeValue } from 'ant-design-vue/es/vc-picker/interface';

defineOptions({ name: 'RangeTime' });

const props = defineProps({
  value: {
    type: String,
    default: '',
  },
  format: {
    type: String,
    default: 'HH:mm:ss',
  },
  placeholder: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['change', 'update:value']);

const placeholder: [string, string] = ['开始时间', '结束时间'];

const rangeValue = ref<RangeValue<string>>(null);
const formItemContext = Form.useInjectFormItemContext();

watch(
  () => props.value,
  val => {
    if (val) {
      rangeValue.value = val.split(',') as [string, string];
    } else {
      rangeValue.value = null;
    }
  },
  { immediate: true },
);

function handleChange(arr) {
  let str = '';
  if (arr && arr.length > 0) {
    if (arr[1] && arr[0]) {
      str = arr.join(',');
    }
  }
  emit('change', str);
  emit('update:value', str);
  formItemContext.onFieldChange();
}
</script>
