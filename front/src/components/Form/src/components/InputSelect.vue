<template>
  <div class="InputSelect">
    <a-input-group compact>
      <a-select
        v-bind="$attrs"
        :placeholder="selectPlaceholder"
        v-if="selectLocation === 'left'"
        v-model:value="selectVal"
        @change="handleSelectChange"
      >
        <a-select-option v-for="item in options" :key="item.value">{{ item.label }}</a-select-option>
      </a-select>
      <a-input v-bind="$attrs" :placeholder="inputPlaceholder" v-model:value="inputVal" @change="handleInputChange" />
      <a-select
        v-bind="$attrs"
        :placeholder="selectPlaceholder"
        v-if="selectLocation === 'right'"
        v-model:value="selectVal"
        @change="handleSelectChange"
      >
        <a-select-option v-for="item in options" :key="item.value">{{ item.label }}</a-select-option>
      </a-select>
    </a-input-group>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'InputSelect',
});
const props = defineProps({
  value: {
    type: String,
    default: '',
  },
  options: {
    type: Array,
    default: () => [],
  },
  selectLocation: {
    type: String as PropType<'left' | 'right'>,
    default: 'right',
  },
  selectPlaceholder: {
    type: String,
    default: '',
  },
  inputPlaceholder: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['update:value', 'change']);
const selectVal = ref<string>();
const inputVal = ref<string>();

const handleInputChange = e => {
  const val = e.target.value;
  setSelectValByInputVal(val);
  emits(val);
};
const handleSelectChange = val => {
  inputVal.value = val;
  emits(val);
};
const setSelectValByInputVal = val => {
  const findItem = props.options.find(item => item.value === val);
  if (findItem) {
    selectVal.value = val;
  } else {
    selectVal.value = undefined;
  }
};
watchEffect(() => {
  inputVal.value = props.value;
  setSelectValByInputVal(props.value);
});
const emits = val => {
  emit('update:value', val);
  emit('change', val);
};
</script>

<style lang="less" scoped>
.InputSelect {
  .ant-input-group {
    display: flex;
  }
}
</style>
