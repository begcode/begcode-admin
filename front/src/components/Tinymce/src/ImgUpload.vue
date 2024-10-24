<template>
  <div :class="[prefixCls, { fullscreen }]">
    <a-upload
      name="image"
      multiple
      @change="handleChange"
      :action="action"
      :showUploadList="false"
      :headers="headers"
      accept=".jpg,.jpeg,.gif,.png,.webp"
    >
      <Icon icon="ant-design:picture-twotone" :size="24" />
    </a-upload>
  </div>
</template>
<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign';
import { useI18n } from '@/hooks/web/useI18nOut';
import { useUpload } from '@/hooks/web/useUploadOut';
import { getFileAccessHttpUrl } from '@/utils/util';

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
  margin: 0 3px;
}

.vben-tinymce-img-upload .ant-btn {
  padding: 2px 4px;
  font-size: 12px;
  height: 24px;
}
.vben-tinymce-img-upload .ant-btn.is-disabled {
  color: rgba(255, 255, 255, 0.5);
}
.vben-tinymce-img-upload.fullscreen {
  position: fixed;
  z-index: 10000;
}
</style>
