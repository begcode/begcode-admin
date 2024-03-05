<template>
  <div class="h-full min-editor">
    <VueMonaco
      ref="editor"
      class="source-code-content"
      :value="value"
      :options="options"
      @change="handleValueChange"
      @editorDidMount="editorDidMount"
    ></VueMonaco>
    <div v-if="showErrorMsg" class="error-msg">{{ editorState.errorMsg }}</div>
  </div>
</template>
<script lang="ts" setup>
import type { PropType } from 'vue';
import { reactive, ref, watchEffect } from 'vue';
import VueMonaco from './VueMonaco.vue';

const props = defineProps({
  value: { type: [Object, String] as PropType<Record<string, any> | string> },
  language: {
    type: String,
    default: 'javascript',
  },
  showErrorMsg: {
    type: Boolean,
    default: true,
  },
  readonly: { type: Boolean },
  autoFormat: { type: Boolean, default: true },
  bordered: { type: Boolean, default: false },
});

const emit = defineEmits(['change', 'update:value', 'format-error']);

const value = ref('');
const editor = ref<any>(null);

watchEffect(() => {
  const { value: val } = props;
  const newValue = val || '';
  value.value = typeof newValue === 'string' ? newValue : JSON.stringify(newValue, null, 2);
});

const options = reactive({
  theme: 'vs',
  tabSize: 2,
  language: props.language,
  autoIndent: true,
  formatOnPaste: true,
  automaticLayout: true,
  roundedSelection: true,
  minimap: {
    enabled: false,
  },
});

const editorState = reactive({
  show: false,
  created: false,
  errorMsg: '',
  showEditorDemo: false,
});

const parseContent = (content = editor.value?.getEditor()?.getValue()) => {
  let jsonData;
  if (props.language === 'json' && content) {
    try {
      jsonData = JSON.parse(content);
      editorState.errorMsg = '';
    } catch (error) {
      editorState.errorMsg = error + '';
    }
  }
  return jsonData;
};

const editorDidMount = monacoInstance => {
  monacoInstance.onDidChangeModelContent(() => {
    const newValue = monacoInstance.getValue();
    parseContent(newValue);
  });
};

function handleValueChange(v) {
  if (editorState.errorMsg) {
  } else {
    emit('update:value', v);
    emit('change', v);
  }
}
</script>
<style scoped>
.min-editor {
  min-height: 200px;
  min-width: 300px;
  border: #d4d9e1 1px solid;
}
.source-code-content {
  min-height: 180px;
  overflow-y: auto;
  flex: 1;
  border: 1px solid transparent;
}
</style>
