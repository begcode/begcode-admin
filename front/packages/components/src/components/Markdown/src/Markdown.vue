<template>
  <div ref="wrapRef"></div>
</template>
<script lang="ts" setup>
import type { Ref } from 'vue';
import { ref, unref, nextTick, computed, watch, onBeforeUnmount, onDeactivated, useAttrs } from 'vue';
import Vditor from 'vditor';
import 'vditor/dist/index.css';
import { useLocale } from '@/i18n/useLocale';
import { useModalContext } from '../../Modal';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { onMountedOrActivated } from '@/hooks/vben';
import { getTheme } from './getTheme';
import { getTenantId, getToken } from '@/utils/auth';
import { getFileAccessHttpUrl } from '@/utils';

type Lang = 'zh_CN' | 'en_US' | 'ja_JP' | 'ko_KR' | undefined;

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({ inheritAttrs: false });

const props = defineProps({
  height: { type: Number, default: 360 },
  value: { type: String, default: '' },
});

const emit = defineEmits(['change', 'get', 'update:value']);

const attrs = useAttrs();

const wrapRef = ref(null);
const vditorRef = ref(null) as Ref<Vditor | null>;
const initedRef = ref(false);

const modalFn = useModalContext();

const { getLocale } = useLocale();
const { getDarkMode } = useRootSetting();
const valueRef = ref(props.value || '');

watch(
  [() => getDarkMode.value, () => initedRef.value],
  ([val, inited]) => {
    if (!inited) {
      return;
    }
    instance.getVditor()?.setTheme(getTheme(val) as any, getTheme(val, 'content'), getTheme(val, 'code'));
  },
  {
    immediate: true,
    flush: 'post',
  },
);

watch(
  () => props.value,
  v => {
    if (v !== valueRef.value) {
      instance.getVditor()?.setValue(v);
    }
    valueRef.value = v;
  },
);

const getCurrentLang = computed((): 'zh_CN' | 'en_US' | 'ja_JP' | 'ko_KR' => {
  let lang: Lang;
  switch (unref(getLocale)) {
    case 'en':
      lang = 'en_US';
      break;
    case 'ja':
      lang = 'ja_JP';
      break;
    case 'ko':
      lang = 'ko_KR';
      break;
    default:
      lang = 'zh_CN';
  }
  return lang;
});
const uploadUrl = `${window._CONFIG['domianURL']}/sys/common/upload`;
const token = getToken();
const tenantId = getTenantId() ? getTenantId() : '0';

function formatResult(files, responseText): string {
  let data: any = JSON.parse(responseText);
  // {"success":true,"message":"markdown/aa_1653391146501.png","code":0,"result":null,"timestamp":1653391146501}'
  let filename = files[0].name as string;
  let result = {
    msg: '',
    code: 0,
    data: {
      errFiles: [''],
      succMap: {},
    },
  };
  if (data.success) {
    result.data.errFiles = [];
    result.data.succMap = {
      [data.message]: getFileAccessHttpUrl(data.message),
    };
  } else {
    result.code = 1;
    result.msg = data.message;
    result.data.errFiles = [filename];
  }
  return JSON.stringify(result);
}

function init() {
  const wrapEl = unref(wrapRef) as HTMLElement;
  if (!wrapEl) return;
  const bindValue = { ...attrs, ...props };
  const insEditor = new Vditor(wrapEl, {
    // 设置外观主题
    theme: getTheme(getDarkMode.value) as any,
    lang: unref(getCurrentLang),
    mode: 'sv',
    fullscreen: {
      index: 520,
    },
    preview: {
      theme: {
        // 设置内容主题
        current: getTheme(getDarkMode.value, 'content'),
      },
      hljs: {
        // 设置代码块主题
        style: getTheme(getDarkMode.value, 'code'),
      },
      actions: [],
    },
    upload: {
      accept: 'image/*',
      url: uploadUrl,
      fieldName: 'file',
      extraData: { biz: 'markdown' },
      setHeaders() {
        return {
          'X-Access-Token': token as string,
          'X-Tenant-Id': tenantId,
        };
      },
      format(files, response) {
        return formatResult(files, response);
      },
    },
    input: v => {
      valueRef.value = v;
      emit('update:value', v);
      emit('change', v);
    },
    after: () => {
      nextTick(() => {
        modalFn?.redoModalHeight?.();
        insEditor.setValue(valueRef.value);
        vditorRef.value = insEditor;
        initedRef.value = true;
        emit('get', instance);
      });
    },
    blur: () => {
      //unref(vditorRef)?.setValue(props.value);
    },
    ...bindValue,
    cache: {
      enable: false,
    },
  });
}

const instance = {
  getVditor: (): Vditor => vditorRef.value!,
};

function destroy() {
  const vditorInstance = unref(vditorRef);
  if (!vditorInstance) return;
  try {
    vditorInstance?.destroy?.();
  } catch (error) {
    //
  }
  vditorRef.value = null;
  initedRef.value = false;
}

onMountedOrActivated(init);
onBeforeUnmount(destroy);
onDeactivated(destroy);
</script>
