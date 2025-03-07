<template>
  <div :class="getClass" :style="getStyle">
    <div :class="`${prefixCls}-image-wrapper`" :style="getImageWrapperStyle" @click="openModal">
      <div :class="`${prefixCls}-image-mask`" :style="getImageWrapperStyle">
        <Icon icon="ant-design:cloud-upload-outlined" :size="getIconWidth" :style="getImageWrapperStyle" color="#d6d6d6" />
      </div>
      <img :src="sourceValue" v-if="sourceValue" alt="avatar" />
    </div>
    <basic-button :class="`${prefixCls}-upload-btn`" @click="openModal" v-if="showBtn" v-bind="btnProps">
      {{ btnText ? btnText : t('component.cropper.selectImage') }}
    </basic-button>

    <CropperModal @register="register" @upload-success="handleUploadSuccess" :uploadApi="uploadApi" :src="sourceValue" :size="size" />
  </div>
</template>
<script lang="ts" setup>
import type { CSSProperties } from 'vue';
import type { ButtonProps } from '@/components/Button';
import { useModal } from '@/components/Modal';
import CropperModal from './CropperModal.vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useMessage } from '@/hooks/web/useMessage';
import { useI18n } from '@/hooks/web/useI18n';

defineOptions({ name: 'CropperAvatar' });

const props = defineProps({
  width: { type: [String, Number], default: '200px' },
  value: { type: String },
  showBtn: { type: Boolean, default: true },
  btnProps: { type: Object as PropType<ButtonProps> },
  btnText: { type: String, default: '' },
  uploadApi: {
    type: Function as PropType<({ file, name }: { file: Blob; name: string }) => Promise<void>>,
  },
  size: { type: Number, default: 5 },
  disableChangeEvent: { type: Boolean, default: false },
});

const emit = defineEmits(['update:value', 'change']);

const { t } = useI18n();

const sourceValue = ref(props.value || '');
const { prefixCls } = useDesign('cropper-avatar');
const [register, { openModal, closeModal }] = useModal();
const { createMessage } = useMessage();
const getClass = computed(() => [prefixCls]);
const getWidth = computed(() => `${props.width}`.replace(/px/, '') + 'px');
const getIconWidth = computed(() => parseInt(`${props.width}`.replace(/px/, '')) / 2 + 'px');
const getStyle = computed((): CSSProperties => ({ width: unref(getWidth) }));
const getImageWrapperStyle = computed((): CSSProperties => ({ width: unref(getWidth), height: unref(getWidth) }));

watchEffect(() => {
  sourceValue.value = props.value || '';
});
watch(
  () => sourceValue.value,
  (v: string) => {
    emit('update:value', v);
  },
);
function handleUploadSuccess({ source, data }) {
  sourceValue.value = source;
  if (!props.disableChangeEvent) {
    emit('change', { source, data });
  }
  createMessage.success(t('component.cropper.uploadSuccess'));
}

defineExpose({ openModal: openModal.bind(null, true), closeModal });
</script>

<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-cropper-avatar';

.@{prefix-cls} {
  display: inline-block;
  text-align: center;

  &-image-wrapper {
    overflow: hidden;
    cursor: pointer;
    background: @component-background;
    border: 1px solid @border-color-base;
    border-radius: 50%;

    img {
      width: 100%;
    }
  }

  &-image-mask {
    opacity: 0;
    position: absolute;
    width: inherit;
    height: inherit;
    border-radius: inherit;
    border: inherit;
    background: rgb(0 0 0 / 40%);
    cursor: pointer;
    transition: opacity 0.4s;

    :deep(svg) {
      margin: auto;
    }
  }

  &-image-mask:hover {
    opacity: 40%;
  }

  &-upload-btn {
    margin: 10px auto;
  }
}
</style>
