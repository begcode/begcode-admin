<template>
  <div class="clearfix">
    <Upload :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove" :custom-request="requestMethod">
      <Button>
        <UploadOutlined />
        选择文件
      </Button>
    </Upload>
    <Modal :open="previewOpen" :footer="null" @cancel="handleCancel">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </Modal>
  </div>
</template>
<script lang="ts" setup>
import type { UploadProps } from 'ant-design-vue';
import { ref, watch } from 'vue';
import { UploadOutlined } from '@ant-design/icons-vue';
import { Upload, Button, Modal } from 'ant-design-vue';

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
