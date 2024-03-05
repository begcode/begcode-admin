<template>
  <InputGroup>
    <Input :value="beginValue" style="width: calc(50% - 15px)" placeholder="最小值" @change="handleChangeBegin" />
    <Input style="width: 30px; border-left: 0; pointer-events: none; background-color: #fff" placeholder="~" disabled />
    <Input :value="endValue" style="width: calc(50% - 15px); border-left: 0" placeholder="最大值" @change="handleChangeEnd" />
  </InputGroup>
</template>

<script lang="ts" setup>
import { ref, watch, PropType } from 'vue';
import { Form, InputGroup, Input } from 'ant-design-vue';

defineOptions({
  name: 'RangeNumber',
});

const props = defineProps({
  value: {
    type: [String, Array] as PropType<string | string[]>,
  },
});

const emit = defineEmits(['change', 'update:value']);

const beginValue = ref('');
const endValue = ref('');
const formItemContext = Form.useInjectFormItemContext();

function handleChangeBegin(e) {
  beginValue.value = e.target.value;
  emitArray();
}

function handleChangeEnd(e) {
  endValue.value = e.target.value;
  emitArray();
}

function emitArray() {
  let arr: string[] = [];
  let begin = beginValue.value || '';
  let end = endValue.value || '';
  arr.push(begin);
  arr.push(end);
  emit('change', arr);
  emit('update:value', arr);
  formItemContext.onFieldChange();
}

watch(
  () => props.value,
  val => {
    if (val && val.length == 2) {
      beginValue.value = val[0];
      endValue.value = val[1];
    } else {
      beginValue.value = '';
      endValue.value = '';
    }
  },
  { immediate: true },
);
</script>
