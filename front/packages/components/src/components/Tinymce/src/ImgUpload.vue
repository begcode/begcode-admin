<template>
  <div :class="[prefixCls, { fullscreen }]">
    <Upload
      name="image"
      multiple
      @change="handleChange"
      :action="action"
      :showUploadList="false"
      :headers="headers"
      accept=".jpg,.jpeg,.gif,.png,.webp"
    >
      <Button type="primary" v-bind="{ ...getButtonProps }">
        {{ t('component.upload.imgUpload') }}
      </Button>
    </Upload>
  </div>
</template>
<script lang="ts" setup>
import { computed } from 'vue';
import { Upload, Button } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useI18n } from '@/hooks/web/useI18nOut';
import { useUpload } from '@/hooks/web/useUploadOut';
import { getFileAccessHttpUrl } from '@/utils';

defineOptions({ name: 'TinymceImageUpload' });

const props = defineProps({
  fullscreen: {
    type: Boolean,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  token: {
    type: String,
    default: '',
  },
  uploadUrl: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['uploading', 'done', 'error']);

const { t } = useI18n();
let uploading = false;

const { uploadUrl } = props;
const { uploadImageUrl, getToken } = useUpload();
const action = uploadUrl || uploadImageUrl;

const headers = computed(() => {
  return { Authorization: `Bearer ${getToken}` };
});

const { prefixCls } = useDesign('tinymce-img-upload');

const getButtonProps = computed(() => {
  const { disabled } = props;
  return {
    disabled,
  };
});

function handleChange(info: Record<string, any>) {
  const file = info.file;
  const status = file?.status;
  // const url = file?.response?.url;
  const name = file?.name;

  if (status === 'uploading') {
    if (!uploading) {
      emit('uploading', name);
      uploading = true;
    }
  } else if (status === 'done') {
    let realUrl = getFileAccessHttpUrl(file.response.url);
    emit('done', name, realUrl);
    uploading = false;
  } else if (status === 'error') {
    emit('error');
    uploading = false;
  }
}
</script>
<style scoped>
.vben-tinymce-img-upload {
  position: absolute;
  top: 4px;
  right: 10px;
  z-index: 20;
}
.vben-tinymce-img-upload.fullscreen {
  position: fixed;
  z-index: 10000;
}
</style>
