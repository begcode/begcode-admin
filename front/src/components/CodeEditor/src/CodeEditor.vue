<template>
  <div class="h-full">
    <CodeMirror ref="codeMirrorRef" :value="getValue" @change="handleValueChange" :mode="mode" :readonly="readonly" />
  </div>
</template>
<script lang="ts" setup>
import CodeMirror from './codemirror/CodeMirror.vue';
import { MODE } from './typing';

const props = defineProps({
  value: { type: [Object, String] as PropType<Record<string, any> | string> },
  mode: { type: String, default: MODE.JSON },
  readonly: { type: Boolean },
  autoFormat: { type: Boolean, default: true },
});

const emit = defineEmits(['change', 'update:value', 'format-error']);

const codeMirrorRef = ref<any>(null);

const getValue = computed(() => {
  const { value, mode, autoFormat } = props;
  if (!autoFormat || mode !== MODE.JSON) {
    return value as string;
  }
  let result = value;
  if (_isString(value)) {
    try {
      result = JSON.parse(value);
    } catch (e) {
      emit('format-error', value);
      return value as string;
    }
  }
  return JSON.stringify(result, null, 2);
});

function getEditor() {
  return codeMirrorRef.value?.getEditor();
}

defineExpose({
  getEditor,
});

function handleValueChange(v) {
  emit('update:value', v);
  emit('change', v);
}
</script>
