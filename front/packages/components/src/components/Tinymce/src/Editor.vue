<template>
  <div :class="prefixCls" :style="{ width: containerWidth }">
    <ImgUpload
      :fullscreen="fullscreen"
      @uploading="handleImageUploading"
      @done="handleDone"
      v-if="showImageUpload"
      v-show="editorRef"
      :disabled="disabled"
    />
    <textarea :id="tinymceId" ref="elRef" :style="{ visibility: 'hidden' }" v-if="!initOptions.inline"></textarea>
    <slot v-else></slot>
  </div>
</template>

<script lang="ts" setup>
import type { Editor, RawEditorSettings } from 'tinymce';
import { theme } from 'ant-design-vue';
import tinymce from 'tinymce/tinymce';
import 'tinymce/themes/silver';
import 'tinymce/icons/default/icons';
import 'tinymce/plugins/advlist';
import 'tinymce/plugins/anchor';
import 'tinymce/plugins/autolink';
import 'tinymce/plugins/autosave';
import 'tinymce/plugins/code';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/directionality';
import 'tinymce/plugins/fullscreen';
import 'tinymce/plugins/hr';
import 'tinymce/plugins/insertdatetime';
import 'tinymce/plugins/link';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/media';
import 'tinymce/plugins/nonbreaking';
import 'tinymce/plugins/noneditable';
import 'tinymce/plugins/pagebreak';
import 'tinymce/plugins/paste';
import 'tinymce/plugins/preview';
import 'tinymce/plugins/print';
import 'tinymce/plugins/save';
import 'tinymce/plugins/searchreplace';
import 'tinymce/plugins/spellchecker';
import 'tinymce/plugins/tabfocus';
// import 'tinymce/plugins/table';
import 'tinymce/plugins/template';
import 'tinymce/plugins/textpattern';
import 'tinymce/plugins/visualblocks';
import 'tinymce/plugins/visualchars';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/image';
import 'tinymce/plugins/table';
import 'tinymce/plugins/textcolor';
import 'tinymce/plugins/contextmenu';

import { computed, nextTick, ref, unref, watch, onDeactivated, onBeforeUnmount, PropType, useAttrs } from 'vue';
import ImgUpload from './ImgUpload.vue';
import { plugins as defaultPlugins, toolbar as defaultToolbar, simplePlugins, simpleToolbar, menubar as defaultMenubar } from './tinymce';
import { buildShortUUID } from '@/utils/uuid';
import { bindHandlers } from './helper';
import { onMountedOrActivated } from '@/hooks/vben';
import { useDesign } from '@/hooks/web/useDesign';
import { isNumber } from '@/utils/is';
import { useI18n } from 'vue-i18n';
import { getFileAccessHttpUrl } from '@/utils';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

defineOptions({ name: 'Tinymce', inheritAttrs: false });

const props = defineProps({
  options: {
    type: Object as PropType<Partial<RawEditorSettings>>,
    default: () => ({}),
  },
  value: {
    type: String,
  },
  toolbar: {
    type: [Array, String] as PropType<string[] | string>,
    default: defaultToolbar,
  },
  plugins: {
    type: Array as PropType<string[]>,
    default: defaultPlugins,
  },
  menubar: {
    type: [Object, String],
    default: defaultMenubar,
  },
  modelValue: {
    type: String,
  },
  height: {
    type: [Number, String] as PropType<string | number>,
    required: false,
    default: 400,
  },
  width: {
    type: [Number, String] as PropType<string | number>,
    required: false,
    default: 'auto',
  },
  showImageUpload: {
    type: Boolean,
    default: true,
  },
  mini: {
    type: Boolean,
    default: false,
  },
  uploadFile: {
    type: Function,
    default: null,
  },
});

const emit = defineEmits(['change', 'update:modelValue', 'inited', 'init-error']);

const { uploadFile } = props;
const attrs = useAttrs();
const editorRef = ref<Editor | null>(null);
const fullscreen = ref(false);
const tinymceId = ref<string>(buildShortUUID('tiny-vue'));
const elRef = ref<HTMLElement | null>(null);

const { prefixCls } = useDesign('tinymce-container');

const containerWidth = computed(() => {
  const width = props.width;
  if (isNumber(width)) {
    return `${width}px`;
  }
  return width;
});

const { defaultAlgorithm } = theme;

const skinName = computed(() => {
  return 'jeecg';
});

const { locale } = useI18n();
const langName = computed(() => {
  let lang = locale.value;
  if (lang === 'zh-cn') {
    lang = 'zh_CN';
  }
  return ['zh_CN', 'en'].includes(lang) ? lang : 'zh_CN';
});

const initOptions = computed((): RawEditorSettings => {
  const { height, options, toolbar, plugins, menubar, mini } = props;
  const publicPath = import.meta.env.VITE_PUBLIC_PATH || '/';
  return {
    selector: `#${unref(tinymceId)}`,
    height: mini ? 200 : height,
    toolbar: mini ? simpleToolbar : toolbar,
    menubar: mini ? [] : menubar,
    plugins: mini ? simplePlugins : plugins,
    language_url: publicPath + 'resource/tinymce/langs/' + langName.value + '.js',
    language: langName.value,
    branding: false,
    default_link_target: '_blank',
    link_title: false,
    object_resizing: true,
    toolbar_mode: 'sliding',
    auto_focus: true,
    toolbar_groups: true,
    skin: skinName.value,
    skin_url: publicPath + 'resource/tinymce/skins/ui/' + skinName.value,
    images_upload_handler: (blobInfo, success) => {
      let params = {
        file: blobInfo.blob(),
        filename: blobInfo.filename(),
        data: { biz: 'jeditor', jeditor: '1' },
      };
      const uploadSuccess = res => {
        if (res.success) {
          if (res.message == 'local') {
            const img = 'data:image/jpeg;base64,' + blobInfo.base64();
            success(img);
          } else {
            let img = getFileAccessHttpUrl(res.message);
            success(img);
          }
        }
      };
      uploadFile(params, uploadSuccess);
    },
    content_css: publicPath + 'resource/tinymce/skins/ui/' + skinName.value + '/content.min.css',
    ...options,
    setup: (editor: Editor) => {
      editorRef.value = editor;
      editor.on('init', e => initSetup(e));
    },
  };
});

const disabled = computed(() => {
  const { options } = props;
  const getdDisabled = options && Reflect.get(options, 'readonly');
  const editor = unref(editorRef);
  if (editor) {
    editor.setMode(getdDisabled || attrs.disabled === true ? 'readonly' : 'design');
  }
  if (attrs.disabled === true) {
    return true;
  }
  return getdDisabled ?? false;
});

watch(
  () => attrs.disabled,
  () => {
    const editor = unref(editorRef);
    if (!editor) {
      return;
    }
    editor.setMode(attrs.disabled ? 'readonly' : 'design');
  },
);

onMountedOrActivated(() => {
  if (!initOptions.value.inline) {
    tinymceId.value = buildShortUUID('tiny-vue');
  }
  nextTick(() => {
    setTimeout(() => {
      initEditor();
    }, 30);
  });
});

onBeforeUnmount(() => {
  destory();
});

onDeactivated(() => {
  destory();
});

function destory() {
  if (tinymce !== null) {
    tinymce?.remove?.(unref(initOptions).selector!);
  }
}

function initEditor() {
  const el = unref(elRef);
  if (el) {
    el.style.visibility = '';
  }
  tinymce
    .init(unref(initOptions))
    .then(editor => {
      emit('inited', editor);
    })
    .catch(err => {
      emit('init-error', err);
    });
}

function initSetup(e) {
  const editor = unref(editorRef);
  if (!editor) {
    return;
  }
  const value = props.modelValue || '';
  editor.setContent(value);
  bindModelHandlers(editor);
  bindHandlers(e, attrs, unref(editorRef));
}

function setValue(editor: Record<string, any>, val?: string, prevVal?: string) {
  if (editor && typeof val === 'string' && val !== prevVal && val !== editor.getContent({ format: attrs.outputFormat })) {
    editor.setContent(val);
  }
}
function bindModelHandlers(editor: any) {
  const modelEvents = attrs.modelEvents ? attrs.modelEvents : null;
  const normalizedEvents = Array.isArray(modelEvents) ? modelEvents.join(' ') : modelEvents;
  watch(
    () => props.modelValue,
    (val, prevVal) => {
      setValue(editor, val, prevVal);
    },
  );
  watch(
    () => props.value,
    (val, prevVal) => {
      setValue(editor, val, prevVal);
    },
    {
      immediate: true,
    },
  );
  editor.on(normalizedEvents ? normalizedEvents : 'change keyup undo redo', () => {
    const content = editor.getContent({ format: attrs.outputFormat });
    emit('update:modelValue', content);
    emit('change', content);
  });
  editor.on('FullscreenStateChanged', e => {
    fullscreen.value = e.state;
  });
}
function handleImageUploading(name: string) {
  const editor = unref(editorRef);
  if (!editor) {
    return;
  }
  editor.execCommand('mceInsertContent', false, getUploadingImgName(name));
  const content = editor?.getContent() ?? '';
  setValue(editor, content);
}

function handleDone(name: string, url: string) {
  const editor = unref(editorRef);
  if (!editor) {
    return;
  }
  const content = editor?.getContent() ?? '';
  const val = content?.replace(getUploadingImgName(name), `<img src="${url}"/>`) ?? '';
  setValue(editor, val);
}

function getUploadingImgName(name: string) {
  return `[uploading:${name}]`;
}
</script>
<style>
.vben-tinymce-container {
  position: relative;
  line-height: normal;
}
.vben-tinymce-container textarea {
  z-index: -1;
  visibility: hidden;
}
</style>
