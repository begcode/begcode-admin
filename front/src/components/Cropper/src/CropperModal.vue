<template>
  <BasicModal
    v-bind="$attrs"
    :canFullscreen="false"
    :okText="t('component.cropper.okText')"
    :title="t('component.cropper.modalTitle')"
    width="800px"
    @register="register"
    @ok="handleOk"
  >
    <div :class="prefixCls">
      <div :class="`${prefixCls}-left`">
        <div :class="`${prefixCls}-cropper`">
          <CropperImage v-if="src" :src="src" height="300px" :circled="circled" @cropend="handleCropend" @ready="handleReady" />
        </div>

        <div :class="`${prefixCls}-toolbar`">
          <a-upload :fileList="[]" accept="image/*" :beforeUpload="handleBeforeUpload">
            <a-tooltip :title="t('component.cropper.selectImage')" placement="bottom">
              <BasicButton size="small" preIcon="ant-design:upload-outlined" type="primary" />
            </a-tooltip>
          </a-upload>
          <a-space>
            <a-tooltip :title="t('component.cropper.btn_reset')" placement="bottom">
              <BasicButton
                type="primary"
                preIcon="ant-design:reload-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('reset')"
              />
            </a-tooltip>
            <a-tooltip :title="t('component.cropper.btn_rotate_left')" placement="bottom">
              <BasicButton
                type="primary"
                preIcon="ant-design:rotate-left-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('rotate', -45)"
              />
            </a-tooltip>
            <a-tooltip :title="t('component.cropper.btn_rotate_right')" placement="bottom">
              <BasicButton
                type="primary"
                preIcon="ant-design:rotate-right-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('rotate', 45)"
              />
            </a-tooltip>
            <a-tooltip :title="t('component.cropper.btn_scale_x')" placement="bottom">
              <BasicButton type="primary" preIcon="vaadin:arrows-long-h" size="small" :disabled="!src" @click="handlerToolbar('scaleX')" />
            </a-tooltip>
            <a-tooltip :title="t('component.cropper.btn_scale_y')" placement="bottom">
              <BasicButton type="primary" preIcon="vaadin:arrows-long-v" size="small" :disabled="!src" @click="handlerToolbar('scaleY')" />
            </a-tooltip>
            <a-tooltip :title="t('component.cropper.btn_zoom_in')" placement="bottom">
              <BasicButton
                type="primary"
                preIcon="ant-design:zoom-in-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('zoom', 0.1)"
              />
            </a-tooltip>
            <a-tooltip :title="t('component.cropper.btn_zoom_out')" placement="bottom">
              <BasicButton
                type="primary"
                preIcon="ant-design:zoom-out-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('zoom', -0.1)"
              />
            </a-tooltip>
          </a-space>
        </div>
      </div>
      <div :class="`${prefixCls}-right`">
        <div :class="`${prefixCls}-preview`">
          <img :src="previewSource" v-if="previewSource" :alt="t('component.cropper.preview')" />
        </div>
        <template v-if="previewSource">
          <div :class="`${prefixCls}-group`">
            <a-avatar :src="previewSource" size="large" />
            <a-avatar :src="previewSource" :size="48" />
            <a-avatar :src="previewSource" :size="64" />
            <a-avatar :src="previewSource" :size="80" />
          </div>
        </template>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts" setup>
import type { CropendResult, Cropper } from './typing';

import CropperImage from './Cropper.vue';
import { useDesign } from '@/hooks/web/useDesign';
import { BasicModal, useModalInner } from '@/components/Modal';
import { dataURLtoBlob } from '@/utils/file/base64Conver';
import { useI18n } from '@/hooks/web/useI18n';

type apiFunParams = { file: Blob; name: string; filename: string };

defineOptions({ name: 'CropperModal' });

const props = defineProps({
  circled: { type: Boolean, default: true },
  uploadApi: {
    type: Function as PropType<(params: apiFunParams) => Promise<any>>,
  },
  src: { type: String },
  size: { type: Number },
});

const emit = defineEmits(['uploadSuccess', 'uploadError', 'register']);

let filename = '';
const src = ref(props.src || '');
const previewSource = ref('');
const cropper = ref<Cropper>();
let scaleX = 1;
let scaleY = 1;

const { prefixCls } = useDesign('cropper-am');
const [register, { closeModal, setModalProps }] = useModalInner();

const { t } = useI18n();

// Block upload
function handleBeforeUpload(file: File) {
  if (props.size && file.size > 1024 * 1024 * props.size) {
    emit('uploadError', { msg: t('component.cropper.imageTooBig') });
    return false;
  }
  const reader = new FileReader();
  reader.readAsDataURL(file);
  src.value = '';
  previewSource.value = '';
  reader.onload = function (e) {
    src.value = (e.target?.result as string) ?? '';
    filename = file.name;
    if (!filename.endsWith('.png')) {
      filename = filename.replace(/\.[^/.]+$/, '') + '.png';
      if (!filename.endsWith('.png')) {
        filename = filename + '.png';
      }
    }
  };
  return false;
}

function handleCropend({ imgBase64 }: CropendResult) {
  previewSource.value = imgBase64;
}

function handleReady(cropperInstance: Cropper) {
  cropper.value = cropperInstance;
}

function handlerToolbar(event: string, arg?: number) {
  if (event === 'scaleX') {
    scaleX = arg = scaleX === -1 ? 1 : -1;
  }
  if (event === 'scaleY') {
    scaleY = arg = scaleY === -1 ? 1 : -1;
  }
  cropper?.value?.[event]?.(arg);
}

async function handleOk() {
  const uploadApi = props.uploadApi;
  if (uploadApi && _isFunction(uploadApi)) {
    const blob = dataURLtoBlob(previewSource.value);
    try {
      setModalProps({ confirmLoading: true });
      const result = await uploadApi({ name: 'file', file: blob, filename });
      emit('uploadSuccess', { source: previewSource.value, data: result.url });
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
}
</script>

<style lang="less">
@prefix-cls: ~'@{namespace}-cropper-am';
.@{prefix-cls} {
  display: flex;
  &-left,
  &-right {
    height: 340px;
  }
  &-left {
    width: 55%;
  }
  &-right {
    width: 45%;
  }
  &-cropper {
    height: 300px;
    background: #eee;
    background-image: linear-gradient(45deg, rgb(0 0 0 / 25%) 25%, transparent 0, transparent 75%, rgb(0 0 0 / 25%) 0),
      linear-gradient(45deg, rgb(0 0 0 / 25%) 25%, transparent 0, transparent 75%, rgb(0 0 0 / 25%) 0);
    background-position:
      0 0,
      12px 12px;
    background-size: 24px 24px;
  }
  &-toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 10px;
  }
  &-preview {
    width: 220px;
    height: 220px;
    margin: 0 auto;
    overflow: hidden;
    border: 1px solid @border-color-base;
    border-radius: 50%;
    img {
      width: 100%;
      height: 100%;
    }
  }
  &-group {
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-top: 8px;
    padding-top: 8px;
    border-top: 1px solid @border-color-base;
  }
}
</style>
