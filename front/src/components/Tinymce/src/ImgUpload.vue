<template>
  <div :class="[prefixCls, { fullscreen }]">
    <a-upload
      name="image"
      multiple
      @change="handleChange"
      :before-upload="beforeUpload"
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

const emit = defineEmits(['uploading', 'done', 'error', 'loading']);

const { t } = useI18n();

const { uploadUrl } = props;
const { uploadImageUrl, getToken } = useUpload();
const action = uploadUrl || uploadImageUrl;
//文件列表
const uploadFileList = ref<any[]>([]);

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

let uploadLength = 0;
function handleChange({ file, fileList }) {
  // 过滤掉已经存在的文件
  fileList = fileList.filter(file => {
    const existFile = uploadFileList.value.find(({ uid }) => uid === file.uid);
    return !existFile;
  });
  uploadLength === 0 && (uploadLength = fileList.length);
  if (file.status !== 'uploading') {
    emit('loading', uploadLength, true);
  }
  // 处理上传好的文件
  if (file.status !== 'uploading') {
    fileList.forEach(file => {
      if (file.status === 'done') {
        const name = file?.name;
        let realUrl = getFileAccessHttpUrl(file.response.url);
        uploadFileList.value.push(file);
        emit('done', name, realUrl);
      }
    });
  }
}

//上传之前
function beforeUpload() {
  uploadLength = 0;
  emit('loading', null, true);
  setTimeout(() => {
    emit('loading', null, false);
  }, 10000);
}
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-tinymce-img-upload';

.@{prefix-cls} {
  margin: 0 3px;
  &.fullscreen {
    position: fixed;
    z-index: 10000;
  }
  .ant-btn {
    padding: 2px 4px;
    font-size: 12px;
    height: 24px;
    &.is-disabled {
      color: rgba(255, 255, 255, 0.5);
    }
  }
}
</style>
