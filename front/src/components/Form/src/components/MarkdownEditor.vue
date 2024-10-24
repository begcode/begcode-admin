<template>
  <MarkDown v-bind="bindProps" @change="onChange" @get="onGetVditor" />
</template>

<script lang="ts" setup>
import { Form } from 'ant-design-vue';

defineOptions({
  name: 'MarkdownEditor',
  inheritAttrs: false,
});

const props = defineProps({
  value: {
    type: String,
    default: '',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['change', 'update:value']);

// markdown 组件实例
let mdRef: any = null;
// vditor 组件实例
let vditorRef: any = null;

const attrs = useAttrs();
// 合并 props 和 attrs
const bindProps = computed(() => Object.assign({}, props, attrs));
const formItemContext = Form.useInjectFormItemContext();
// 相当于 onMounted
function onGetVditor(instance) {
  mdRef = instance;
  vditorRef = mdRef.getVditor();

  // 监听禁用，切换编辑器禁用状态
  watch(
    () => props.disabled,
    disabled => (disabled ? vditorRef.disabled() : vditorRef.enable()),
    { immediate: true },
  );
}

// value change 事件
function onChange(value) {
  emit('change', value);
  emit('update:value', value);
  nextTick(() => {
    formItemContext?.onFieldChange();
  });
}
</script>

<style lang="less" scoped></style>
