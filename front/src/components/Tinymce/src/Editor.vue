<template>
  <div ref="editorRootRef" :class="prefixCls" :style="{ width: containerWidth }">
    <Teleport v-if="imgUploadShow" :to="targetElem">
      <ImgUpload
        :fullscreen="fullscreen"
        @uploading="handleImageUploading"
        @loading="handleLoading"
        @done="handleDone"
        v-if="showImageUpload"
        v-show="editorRef"
        :disabled="disabled"
      />
    </Teleport>
    <Editor
      :id="tinymceId"
      ref="elRef"
      :disabled="disabled"
      :init="initOptions"
      :style="{ visibility: 'hidden' }"
      v-if="!initOptions.inline"
    />
    <slot v-else></slot>
    <ProcessMask ref="processMaskRef" :show="showUploadMask" />
  </div>
</template>

<script lang="ts" setup>
import type { RawEditorOptions } from 'tinymce';
import tinymce from 'tinymce/tinymce';
import Editor from '@tinymce/tinymce-vue';
import ProcessMask from './ProcessMask.vue';
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
import 'tinymce/plugins/insertdatetime';
import 'tinymce/plugins/link';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/media';
import 'tinymce/plugins/nonbreaking';
import 'tinymce/plugins/pagebreak';
import 'tinymce/plugins/preview';
import 'tinymce/plugins/save';
import 'tinymce/plugins/searchreplace';
import 'tinymce/plugins/table';
import 'tinymce/plugins/visualblocks';
import 'tinymce/plugins/visualchars';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/image';
import 'tinymce/plugins/table';
import ImgUpload from './ImgUpload.vue';
import { plugins as defaultPlugins, toolbar as defaultToolbar, simplePlugins, simpleToolbar, menubar as defaultMenubar } from './tinymce';
import { buildShortUUID } from '@/utils/uuid';
import { bindHandlers } from './helper';
import { onMountedOrActivated } from '@/hooks/vben';
import { useDesign } from '@/hooks/web/useDesign';
import { useUpload } from '@/hooks/web/useUploadOut';
import { useI18n } from 'vue-i18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

defineOptions({ name: 'Tinymce', inheritAttrs: false });

const props = defineProps({
  options: {
    type: Object as PropType<Partial<RawEditorOptions>>,
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
    type: [Boolean, String],
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
  showUploadMask: {
    type: Boolean,
    default: false,
  },
  autoFocus: {
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
  editorId: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['change', 'update:modelValue', 'inited', 'init-error']);

const { uploadFile } = props;
const { uploadImage } = useUpload();
const uploadApi = uploadFile || uploadImage;
const attrs = useAttrs();
const editorRef = ref<Editor | null>(null);
const processMaskRef = ref<any>(null);
const fullscreen = ref(false);
const tinymceId = ref<string>(props.editorId || buildShortUUID('tiny-vue'));
const elRef = ref<HTMLElement | null>(null);
const editorRootRef = ref<Nullable<HTMLElement>>(null);
const imgUploadShow = ref(false);
const targetElem = ref<null | HTMLDivElement>(null);
const appMarkMode = inject('APP_DARK_MODE', ref('light'));

const { prefixCls } = useDesign('tinymce-container');

const containerWidth = computed(() => {
  const width = props.width;
  if (_isNumber(width)) {
    return `${width}px`;
  }
  return width;
});

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
  let publicPath = import.meta.env.VITE_PUBLIC_PATH || '/';
  if (!publicPath.endsWith('/')) {
    publicPath += '/';
  }
  return {
    selector: `#${unref(tinymceId)}`,
    height: mini ? 200 : height,
    toolbar: mini ? simpleToolbar : toolbar,
    menubar: mini || !menubar ? false : menubar,
    plugins: mini ? simplePlugins : plugins,
    language_url: publicPath + 'resource/tinymce/langs/' + langName.value + '.js',
    language: langName.value,
    branding: false,
    default_link_target: '_blank',
    link_title: false,
    object_resizing: true,
    toolbar_mode: 'sliding',
    auto_focus: props.autoFocus,
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
        // 根据实际情况，如何使用了其他的api可能要修改这里。
        success(res.url);
      };
      uploadApi(params, undefined, uploadSuccess);
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
  if (editor && editor.setMode) {
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
    editor?.setMode?.(attrs?.disabled ? 'readonly' : 'design');
  },
);

onMountedOrActivated(() => {
  if (!initOptions.value.inline) {
    tinymceId.value = props.editorId || buildShortUUID('tiny-vue');
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
  if (el?.style?.visibility) {
    el.style.visibility = '';
  }
  tinymce
    .init(unref(initOptions))
    .then(editor => {
      changeColor();
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

/**
 * 上传进度计算
 * @param file
 * @param fileList
 */
function handleLoading(fileLength, showMask) {
  if (fileLength && fileLength > 0) {
    setTimeout(() => {
      props?.showUploadMask && processMaskRef.value.calcProcess(fileLength);
    }, 100);
  } else {
    props?.showUploadMask && (processMaskRef.value.showMask = showMask);
  }
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

async function handleDone(name: string, url: string) {
  const editor = unref(editorRef);
  if (!editor) {
    return;
  }
  await handleImageUploading(name);
  const content = editor?.getContent() ?? '';
  const val = content?.replace(getUploadingImgName(name), `<img src="${url}"/>`) ?? '';
  setValue(editor, val);
}

function getUploadingImgName(name: string) {
  return `[uploading:${name}]`;
}

let executeCount = 0;

watch(
  () => props.showImageUpload,
  () => {
    mountElem();
  },
);
onMounted(() => {
  mountElem();
});
const mountElem = () => {
  if (executeCount > 20) return;
  setTimeout(() => {
    if (targetElem.value) {
      imgUploadShow.value = props.showImageUpload;
    } else {
      const toxToolbar = editorRootRef.value?.querySelector('.tox-toolbar__group');
      if (toxToolbar) {
        const divElem = document.createElement('div');
        divElem.setAttribute('class', `tox-tbtn`);
        toxToolbar!.appendChild(divElem);
        targetElem.value = divElem;
        imgUploadShow.value = props.showImageUpload;
        executeCount = 0;
      } else {
        mountElem();
      }
    }
    executeCount++;
  }, 100);
};
function changeColor() {
  setTimeout(() => {
    const iframe = editorRootRef.value?.querySelector('iframe');
    const body = iframe?.contentDocument?.querySelector('body');
    if (body) {
      if (appMarkMode.value === 'light') {
        body.style.color = '#000';
      } else {
        body.style.color = '#fff';
      }
    }
  }, 300);
}

watch(
  () => appMarkMode.value,
  () => changeColor(),
);
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-tinymce-container';

.@{prefix-cls} {
  position: relative;
  line-height: normal;

  textarea {
    z-index: -1;
    visibility: hidden;
  }
  .tox:not(.tox-tinymce-inline) .tox-editor-header {
    padding: 0;
  }
  .tox .tox-tbtn--disabled,
  .tox .tox-tbtn--disabled:hover,
  .tox .tox-tbtn:disabled,
  .tox .tox-tbtn:disabled:hover {
    background-image: url("data:image/svg+xml;charset=utf8,%3Csvg height='39px' viewBox='0 0 40 39px' width='40' xmlns='http://www.w3.org/2000/svg'%3E%3Crect x='0' y='38px' width='100' height='1' fill='%23d9d9d9'/%3E%3C/svg%3E");
    background-position: left 0;
  }
}

html[data-theme='dark'] {
  .@{prefix-cls} {
    .tox .tox-edit-area__iframe {
      background-color: #141414;
    }
  }
}
</style>
