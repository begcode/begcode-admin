<template>
  <Popover
    trigger="contextmenu"
    v-model:open="visible"
    :overlayClassName="`${prefixCls}-popover`"
    :getPopupContainer="getPopupContainer"
    :placement="position"
  >
    <template #title>
      <span :class="title ? 'title' : 'emptyTitle'">{{ title }}</span>
      <span style="float: right" title="关闭">
        <Icon icon="ant-design:close-outlined" @click="visible = false" />
      </span>
    </template>
    <template #content>
      <Textarea ref="textareaRef" :value="innerValue" :disabled="disabled" :style="textareaStyle" v-bind="attrs" @input="onInputChange" />
    </template>
    <Input :class="`${prefixCls}-input`" :value="innerValue" :disabled="disabled" v-bind="attrs" @change="onInputChange">
      <template #suffix>
        <Icon icon="ant-design:fullscreen-outlined" @click.stop="onShowPopup" />
      </template>
    </Input>
  </Popover>
</template>

<script lang="ts" setup>
import type { TooltipPlacement } from 'ant-design-vue/es/tooltip';
import { computed, nextTick, ref, watch, PropType } from 'vue';
import { Input, Popover, Textarea } from 'ant-design-vue';
import Icon from '@/components/Icon/Icon.vue';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { propTypes } from '@/utils/propTypes';
import { useDesign } from '@/hooks/web/useDesign';

const { prefixCls } = useDesign('j-input-popup');
const props = defineProps({
  // v-model:value
  value: propTypes.string.def(''),
  title: propTypes.string.def(''),
  // 弹出框显示位置
  position: {
    type: String as PropType<TooltipPlacement>,
    default: 'right',
  },
  width: propTypes.number.def(300),
  height: propTypes.number.def(150),
  disabled: propTypes.bool.def(false),
  // 弹出框挂载的元素ID
  popContainer: propTypes.oneOfType([propTypes.string, propTypes.func]).def(''),
});
const attrs = useAttrs();
const emit = defineEmits(['change', 'update:value']);

const visible = ref<boolean>(false);
const innerValue = ref<string>('');
// textarea ref对象
const textareaRef = ref();
// textarea 样式
const textareaStyle = computed(() => ({
  height: `${props.height}px`,
  width: `${props.width}px`,
}));

watch(
  () => props.value,
  value => {
    if (value && value.length > 0) {
      innerValue.value = value;
    }
  },
  { immediate: true },
);

function onInputChange(event) {
  innerValue.value = event.target.value;
  emitValue(innerValue.value);
}

async function onShowPopup() {
  visible.value = true;
  await nextTick();
  textareaRef.value?.focus();
}

// 获取弹出框挂载的元素
function getPopupContainer(node) {
  if (!props.popContainer) {
    return node.parentNode;
  } else if (typeof props.popContainer === 'function') {
    return props.popContainer(node);
  } else {
    return document.getElementById(props.popContainer);
  }
}

function emitValue(value) {
  emit('change', value);
  emit('update:value', value);
}
</script>

<style>
.vben-j-input-popup-input-popover .ant-popover-title:has(.emptyTitle) {
  border-bottom: none;
}
.vben-j-input-popup-input .app-iconify {
  cursor: pointer;
  color: #666666;
  transition: color 0.3s;
}
.vben-j-input-popup-input .app-iconify:hover {
  color: black;
}
</style>
