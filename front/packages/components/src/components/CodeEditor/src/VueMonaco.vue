<template>
  <div ref="monacoRef"></div>
</template>
<script lang="ts" setup>
import * as monacoEditor from 'monaco-editor';
import { watch, onMounted, nextTick, onBeforeUnmount, ref } from 'vue';

defineOptions({
  name: 'MonacoEditor',
});

const props = defineProps({
  original: {
    type: String,
  },
  value: {
    type: String,
    required: true,
  },
  theme: {
    type: String,
    default: 'vs',
  },
  language: {
    type: String,
  },
  options: {
    type: Object,
  },
  amdRequire: {
    type: Function,
  },
  diffEditor: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:value', 'change', 'editorWillMount', 'editorDidMount']);

const vueMonaco = {
  editor: null as any,
  monaco: null as any,
};
const monacoRef = ref(null);

const getModifiedEditor = () => (props.diffEditor ? vueMonaco.editor?.getModifiedEditor() : vueMonaco.editor);

const getOriginalEditor = () => (props.diffEditor ? vueMonaco.editor?.getOriginalEditor() : vueMonaco.editor);

const initMonaco = monaco => {
  emit('editorWillMount', vueMonaco.monaco);

  const options = { value: props.value, theme: props.theme, language: props.language, ...props.options };

  if (props.diffEditor) {
    vueMonaco.editor = monaco.editor?.createDiffEditor(monacoRef.value, options);
    const originalModel = monaco.editor?.createModel(props.original, props.language);
    const modifiedModel = monaco.editor?.createModel(props.value, props.language);

    vueMonaco.editor?.setModel({
      original: originalModel,
      modified: modifiedModel,
    });
  } else {
    vueMonaco.editor = monaco.editor?.create(monacoRef.value, options);
  }

  const editor2 = getModifiedEditor();

  editor2.onDidChangeModelContent(event => {
    const value = editor2.getValue();

    if (props.value !== value) {
      emit('update:value', value, event);
      emit('change', value, event);
    }
  });

  emit('editorDidMount', vueMonaco.editor);
};

onMounted(() => {
  if (props.amdRequire) {
    props.amdRequire(['vs/editor/editor.main'], () => {
      vueMonaco.monaco = (window as any).monaco;
      nextTick(() => {
        initMonaco((window as any).monaco);
      });
    });
  } else {
    vueMonaco.monaco = monacoEditor;
    nextTick(() => {
      initMonaco(vueMonaco.monaco);
    });
  }
});

onBeforeUnmount(() => {
  vueMonaco.editor && vueMonaco.editor?.dispose();
});

watch(
  () => props.options,
  options => {
    if (vueMonaco.editor) {
      const editor2 = getModifiedEditor();

      editor2.updateOptions(options);
    }
  },
  {
    deep: true,
  },
);

watch(
  () => props.value,
  newValue => {
    if (vueMonaco.editor) {
      const editor = getModifiedEditor();

      if (newValue !== editor.getValue()) {
        editor.setValue(newValue);
      }
    }
  },
);

watch(
  () => props.original,
  newValue => {
    if (vueMonaco.editor && props.diffEditor) {
      const editor = getOriginalEditor();

      if (newValue !== editor.getValue()) {
        editor.setValue(newValue);
      }
    }
  },
);

watch(
  () => props.language,
  newVal => {
    if (vueMonaco.editor) {
      const editor = getModifiedEditor();
      vueMonaco.monaco?.editor?.setModelLanguage(editor.getModel(), newVal);
    }
  },
);

watch(
  () => props.theme,
  newVal => {
    if (vueMonaco.editor) {
      vueMonaco.monaco?.editor?.setTheme(newVal);
    }
  },
);
</script>
