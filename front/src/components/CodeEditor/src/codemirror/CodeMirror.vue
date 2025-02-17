<template>
  <div class="relative !h-full w-full overflow-hidden" ref="el"></div>
</template>

<script lang="ts" setup>
import { useDebounceFn } from '@vueuse/core';
import { useAppStore } from '@/store/modules/app';
import { useWindowSizeFn } from '@/hooks/vben/useWindowSizeFn';
import { CodeMirror } from './codeMirror';

const props = defineProps({
  mode: { type: String, default: 'application/json' },
  value: { type: String, default: '' },
  readonly: { type: Boolean, default: false },
});

const emit = defineEmits(['change']);

const el = ref();
let editor: Nullable<CodeMirror.Editor>;

const debounceRefresh = useDebounceFn(refresh, 100);
const appStore = useAppStore();

watch(
  () => props.value,
  async value => {
    await nextTick();
    const oldValue = editor?.getValue();
    if (value !== oldValue) {
      editor?.setValue(value ? value : '');
    }
  },
  { flush: 'post' },
);

watchEffect(() => {
  editor?.setOption('mode', props.mode);
});

watch(
  () => appStore.getDarkMode,
  async () => {
    setTheme();
  },
  {
    immediate: true,
  },
);

function setTheme() {
  unref(editor)?.setOption('theme', appStore.getDarkMode === 'light' ? 'idea' : 'material-palenight');
}

function refresh() {
  editor?.refresh();
}

async function init() {
  const addonOptions = {
    autoCloseBrackets: true,
    autoCloseTags: true,
    foldGutter: true,
    gutters: ['CodeMirror-linenumbers'],
  };

  editor = CodeMirror(el.value!, {
    value: '',
    mode: props.mode,
    readOnly: props.readonly,
    tabSize: 2,
    theme: 'material-palenight',
    lineWrapping: true,
    lineNumbers: true,
    ...addonOptions,
  });
  editor?.setValue(props.value);
  setTheme();
  editor?.on('change', () => {
    emit('change', editor?.getValue());
  });
}

function getEditor() {
  return editor;
}

defineExpose({ getEditor });

onMounted(async () => {
  await nextTick();
  await init();
  useWindowSizeFn(debounceRefresh);
});

onUnmounted(() => {
  editor = null;
});
</script>
