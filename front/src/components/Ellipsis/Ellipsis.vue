<template>
  <a-tooltip placement="topLeft">
    <template #title>
      <span>{{ value }}</span>
    </template>
    {{ showText }}
  </a-tooltip>
</template>
<script lang="ts" setup>
const props = defineProps({
  value: {
    type: [String, Array],
  },
  length: {
    type: Number,
    default: 25,
  },
});
//显示的文本
const showText = computed(() =>
  props.value ? (props.value.length > props.length ? props.value.slice(0, props.length) + '...' : props.value) : props.value,
);
</script>
