<template>
  <basic-modal
    width="800px"
    :title="t('component.upload.preview')"
    class="upload-preview-modal"
    v-bind="$attrs"
    @register="register"
    :showOkBtn="false"
  >
    <vxe-grid ref="xGrid" :columns="columns" :data="fileListRef">
      <template #url="{ row }">
        <ThumbUrl v-if="isImgTypeByName(row.url)" :fileUrl="row.url" />
        <span v-else>{{ row.url }}</span>
      </template>
      <template #recordAction="{ row }">
        <a-button :type="'link'" status="primary" @click="handleRemove(row)">
          <span>删除</span>
        </a-button>
        <a-button :type="'link'" status="primary" @click="handlePreview(row)">
          <span>预览</span>
        </a-button>
        <a-button :type="'link'" status="primary" @click="handleDownload(row)">
          <span>下载</span>
        </a-button>
      </template>
    </vxe-grid>
  </basic-modal>
</template>
<script lang="ts" setup>
import { BasicModal, useModalInner } from '@/components/Modal';
import { previewProps } from '../props';
import { PreviewFileItem } from '../types/typing';
import { downloadByUrl } from '@/utils/file/download';
import { createPreviewColumns } from './data';
import { useI18n } from '@/hooks/web/useI18nOut';
import { createImgPreview } from '@/components/Preview';
import { isImgTypeByName } from '../helper';
import ThumbUrl from './ThumbUrl.vue';

const props = defineProps(previewProps);

const emit = defineEmits(['list-change', 'register', 'delete']);

const columns = createPreviewColumns() as any[];

const [register] = useModalInner();

const { t } = useI18n();

const fileListRef = ref<PreviewFileItem[]>([]);

watch(
  () => props.value,
  value => {
    if (!_isArray(value)) value = [];
    fileListRef.value = value
      .filter(item => !!item)
      .map(item => {
        return {
          url: item,
          type: item.split('.').pop() || '',
          name: item.split('/').pop() || '',
        };
      });
  },
  { immediate: true },
);

// 删除
function handleRemove(record: PreviewFileItem) {
  const index = fileListRef.value.findIndex(item => item.url === record.url);
  if (index !== -1) {
    const removed = fileListRef.value.splice(index, 1);
    emit('delete', removed[0].url);
    emit(
      'list-change',
      fileListRef.value.map(item => item.url),
    );
  }
}

// 预览
function handlePreview(record: PreviewFileItem) {
  const { url = '' } = record;
  createImgPreview({
    imageList: [url],
  });
}

// 下载
function handleDownload(record: PreviewFileItem) {
  const { url = '' } = record;
  downloadByUrl({ url });
}
</script>
<style>
.upload-preview-modal .ant-upload-list {
  display: none;
}
.upload-preview-modal .ant-table-wrapper .ant-spin-nested-loading {
  padding: 0;
}
</style>
