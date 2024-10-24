<template>
  <ScrollContainer ref="wrapperRef" :scrollHeight="realHeight">
    <div ref="spinRef" :style="spinStyle" v-loading="loading" :loading-tip="loadingTip">
      <slot></slot>
    </div>
  </ScrollContainer>
</template>
<script lang="ts" setup>
import { useWindowSizeFn } from '@/hooks/vben';
import { type AnyFunction } from '#/utils.d';
import { ScrollContainer } from '@/components/Container';
import { createModalContext } from '../hooks/useModalContext';
import { useMutationObserver } from '@vueuse/core';

defineOptions({ name: 'ModalWrapper', inheritAttrs: false });

const props = defineProps({
  loading: { type: Boolean },
  useWrapper: { type: Boolean, default: true },
  modalHeaderHeight: { type: Number, default: 57 },
  modalFooterHeight: { type: Number, default: 74 },
  minHeight: { type: Number, default: 200 },
  height: { type: Number },
  footerOffset: { type: Number, default: 0 },
  open: { type: Boolean },
  fullScreen: { type: Boolean },
  loadingTip: { type: String },
});

const emit = defineEmits(['height-change', 'ext-height']);

const wrapperRef = ref(null);
const spinRef = ref(null);
const realHeightRef = ref(0);
const minRealHeightRef = ref(0);
const realHeight = ref(0);

let stopElResizeFn: AnyFunction = () => {};

useWindowSizeFn(setModalHeight.bind(null));

let observer,
  recordCount: any = {};
watch(
  () => props.visible,
  () => {
    if (props.visible && !observer && !(props.maxHeight || props.height)) {
      recordCount = {};
      observer = useMutationObserver(
        spinRef,
        () => {
          setModalHeight({
            source: 'muob',
            callBack: height => {
              const count = recordCount[height];
              if (count) {
                recordCount[height] = ++recordCount[height];
                if (count > 10) {
                  observer.stop();
                  recordCount = {};
                  observer = null;
                }
              } else {
                recordCount = {};
                recordCount[height] = 1;
              }
            },
          });
        },
        {
          attributes: true,
          subtree: true,
        },
      );
    } else {
      if (observer) {
        observer.stop();
        observer = null;
      }
    }
  },
  { immediate: true },
);

createModalContext({
  redoModalHeight: setModalHeight,
});

const spinStyle = computed((): CSSProperties => {
  return {
    minHeight: `${props.minHeight}px`,
    [props.fullScreen ? 'height' : 'maxHeight']: `${unref(realHeightRef)}px`,
  };
});

watchEffect(() => {
  props.useWrapper && setModalHeight();
});

watch(
  () => props.fullScreen,
  v => {
    setModalHeight();
    if (!v) {
      realHeightRef.value = minRealHeightRef.value;
    } else {
      minRealHeightRef.value = realHeightRef.value;
    }
  },
);

onMounted(() => {
  const { modalHeaderHeight, modalFooterHeight } = props;
  emit('ext-height', modalHeaderHeight + modalFooterHeight);
});

onUnmounted(() => {
  stopElResizeFn && stopElResizeFn();
});

async function scrollTop() {
  nextTick(() => {
    const wrapperRefDom = unref(wrapperRef);
    if (!wrapperRefDom) return;
    (wrapperRefDom as any)?.scrollTo?.(0);
  });
}

async function setModalHeight(option?) {
  // 解决在弹窗关闭的时候监听还存在,导致再次打开弹窗没有高度
  // 加上这个,就必须在使用的时候传递父级的open
  console.log('---------性能监控--------setModalHeight----------');
  const options = option || {};
  const source = options.source;
  const callBack = options.callBack;
  if (!props.open) return;
  const wrapperRefDom = unref(wrapperRef);
  if (!wrapperRefDom) return;
  const bodyDom = wrapperRefDom.$el.parentElement?.parentElement?.parentElement;
  if (!bodyDom) return;
  // bodyDom.style.padding = '0';
  await nextTick();
  try {
    const modalDom = bodyDom.parentElement && bodyDom.parentElement.parentElement;
    if (!modalDom) return;
    const modalRect = getComputedStyle(modalDom as Element).top;
    const modalTop = Number.parseInt(modalRect);
    let maxHeight = window.innerHeight - modalTop * 2 + (props.footerOffset! || 0) - props.modalFooterHeight - props.modalHeaderHeight;
    // 距离顶部过进会出现滚动条
    if (modalTop < 40) {
      maxHeight -= 26;
    }
    await nextTick();
    const spinEl: any = unref(spinRef);
    if (!spinEl) return;
    await nextTick();
    // if (!realHeight) {
    realHeight.value = spinEl.scrollHeight;
    // }
    if (props.fullScreen) {
      realHeightRef.value = window.innerHeight - props.modalFooterHeight - props.modalHeaderHeight - 28;
    } else {
      realHeightRef.value = props.height ? props.height : realHeight.value > maxHeight ? maxHeight : realHeight.value;
    }
    if (source == 'muob') {
      callBack(realHeightRef.value);
    }
    emit('height-change', unref(realHeightRef));
  } catch (error) {
    console.log(error);
  }
}
defineExpose({ scrollTop, setModalHeight });
</script>
