<template>
  <a-popover
    trigger="contextmenu"
    v-model:open="visible"
    :overlayClassName="`${prefixCls}-popover`"
    :getPopupContainer="getPopupContainer"
    :placement="position"
  >
    <template #title>
      <span>{{ title }}</span>
      <span style="float: right" title="关闭">
        <Icon icon="ant-design:close-outlined" @click="visible = false" />
      </span>
    </template>
    <template #content>
      <div style="height: 400px; width: 500px">
        <code-editor />
      </div>
    </template>
    <a-input
      :class="`${prefixCls}-input`"
      :value="innerValue"
      :disabled="disabled"
      v-bind="attrs"
      @change="onInputChange"
      :style="textareaStyle"
    >
      <template #suffix>
        <Icon icon="ant-design:fullscreen-outlined" @click.stop="onShowPopup" />
      </template>
    </a-input>
  </a-popover>
</template>

<script lang="ts" setup>
import { useAttrs } from '@/hooks/vben/useAttrs';
import { useDesign } from '@/hooks/web/useDesign';
import { TooltipPlacement } from 'ant-design-vue/es/tooltip';

const { prefixCls } = useDesign('editor-pop');
const props = defineProps({
  // v-model:value
  value: {
    type: String,
    default: '',
  },
  title: {
    type: String,
    default: '',
  },
  // 弹出框显示位置
  position: {
    type: String as PropType<TooltipPlacement>,
    default: 'right',
  },
  width: {
    type: Number,
    default: 300,
  },
  height: {
    type: Number,
    default: 150,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  // 弹出框挂载的元素ID
  popContainer: {
    type: [String, Function] as PropType<string | Function>,
    default: '',
  },
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

<style lang="less">
@prefix-cls: ~'@{namespace}-editor-pop';

.@{prefix-cls} {
  &-popover {
    .ant-popover-title:has(.emptyTitle) {
      border-bottom: none;
    }
  }

  &-input {
    .app-iconify {
      cursor: pointer;
      color: #666666;
      transition: color 0.3s;

      &:hover {
        color: black;
      }
    }
  }
}
</style>
