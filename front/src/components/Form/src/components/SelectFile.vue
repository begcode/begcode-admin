<template>
  <div class="clearfix">
    <a-upload :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove" :custom-request="requestMethod">
      <a-button>
        <Icon icon="ant-design:upload-outlined" />
        选择文件
      </a-button>
    </a-upload>
    <a-modal :open="previewOpen" :footer="null" @cancel="handleCancel">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import type { UploadProps } from 'ant-design-vue';

defineOptions({
  name: 'SelectFile',
});

const props = defineProps({
  value: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['update:value', 'selectFile']);

const fileList = ref<UploadProps['fileList']>([]);
//预览图
const previewImage = ref<string | undefined>('');
//预览框状态
const previewOpen = ref<boolean>(false);

const handleRemove: UploadProps['onRemove'] = file => {
  const index = fileList.value?.indexOf(file);
  const newFileList = fileList.value?.slice();
  newFileList?.splice(index || 0, 1);
  fileList.value = newFileList;
  emit('update:value', fileList.value ? fileList.value[0] : '');
};

const beforeUpload: UploadProps['beforeUpload'] = file => {
  fileList.value = [...(fileList.value || []), file];
  emit('selectFile', file);
};
const requestMethod: UploadProps['customRequest'] = ({ file }) => {
  console.log('requestMethod', file);
};
const handleCancel = () => {
  previewOpen.value = false;
};
watch(
  () => props.value,
  value => {
    if (value) {
      fileList.value = [{ url: value, name: value, status: 'done', uid: value }];
    }
  },
);
</script>
<style scoped>
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
