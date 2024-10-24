<template>
  <BasicModal
    width="800px"
    :title="t('component.upload.upload')"
    :okText="t('component.upload.upload')"
    v-bind="$attrs"
    @register="register"
    @ok="handleOk"
    :closeFunc="handleCloseFunc"
    :maskClosable="false"
    :keyboard="false"
    class="upload-modal"
    :okButtonProps="getOkButtonProps"
    :cancelButtonProps="{ disabled: isUploadingRef }"
  >
    <template #centerFooter>
      <a-button @click="handleStartUpload" color="success" :disabled="!getIsSelectFile" :loading="isUploadingRef">
        {{ getUploadBtnText }}
      </a-button>
    </template>

    <div class="upload-modal-toolbar">
      <a-alert :message="getHelpText" type="info" banner class="upload-modal-toolbar__text" />

      <a-upload
        :accept="getStringAccept"
        :multiple="multiple"
        :before-upload="beforeUpload"
        :show-upload-list="false"
        class="upload-modal-toolbar__btn"
      >
        <a-button type="primary">
          {{ t('component.upload.choose') }}
        </a-button>
      </a-upload>
    </div>
    <Grid ref="xGrid" :columns="columns" :data="fileListRef">
      <template #thumbUrl="{ row }">
        <ThumbUrl :fileUrl="row.thumbUrl" />
      </template>
      <template #name="{ row }">
        <div>
          <p class="truncate mb-1 max-w-[280px]" :title="getPercent(row).text">
            {{ getPercent(row).text }}
          </p>
          <a-progress :percent="getPercent(row).percent" size="small" :status="getPercent(row).status as any" />
        </div>
      </template>
      <template #status="{ row }">
        <a-tag color="green" v-if="row.status === 'success'">{{ t('component.upload.uploadSuccess') }}</a-tag>
        <a-tag color="red" v-else-if="row.status === 'error'">{{ t('component.upload.uploadError') }}</a-tag>
        <a-tag color="blue" v-else-if="row.status === 'uploading'">{{ t('component.upload.uploading') }}</a-tag>
        <span v-else> {{ row.status || t('component.upload.pending') }}</span>
      </template>
      <template #recordAction="{ row }">
        <a-button :type="'link'" status="primary" @click="handleRemove(row)">
          <span>删除</span>
        </a-button>
        <a-button :type="'link'" status="primary" @click="handlePreview(row)">
          <span>预览</span>
        </a-button>
      </template>
    </Grid>
  </BasicModal>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue';
import { Grid } from 'vxe-table';
import { BasicModal, useModalInner } from '@/components/Modal';
import ThumbUrl from './ThumbUrl.vue';
// hooks
import { useUploadType } from '../hooks/useUpload';
//   types
import { FileItem, UploadResultStatus } from '../types/typing';
import { basicProps } from '../props';
import { createTableColumns } from './data';
// utils
import { checkFileType, checkImgType, getBase64WithFile } from '../helper';
import { buildUUID } from '@/utils/uuid';
import { useLog } from '@/hooks/useLog';
import { useI18n } from '@/hooks/web/useI18nOut';
import { createImgPreview } from '@/components/Preview';
import { useUpload } from '@/hooks/web/useUploadOut';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const props = defineProps({
  ...basicProps,
  previewFileList: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
});

const emit = defineEmits(['change', 'register', 'delete']);

const columns = createTableColumns();

const ctx = getCurrentInstance()?.proxy as any;
const apiService = ctx?.$apiService as any;

// 是否正在上传
const isUploadingRef = ref(false);
const fileListRef = ref<FileItem[]>([]);
const { accept, helpText, maxNumber, maxSize } = toRefs(props);

const { t } = useI18n();
const [register, { closeModal }] = useModalInner();

const { getAccept, getStringAccept, getHelpText } = useUploadType({
  acceptRef: accept,
  helpTextRef: helpText,
  maxNumberRef: maxNumber,
  maxSizeRef: maxSize,
});

const getIsSelectFile = computed(() => {
  return fileListRef.value.length > 0 && !fileListRef.value.every(item => item.status === UploadResultStatus.SUCCESS);
});

const getPercent = (row: FileItem) => {
  const result = {
    text: row.status,
    percent: row.percent,
    status: 'normal',
  };
  if (row.status === 'success') {
    result.status = 'success';
  }
  if (row.status === 'uploading') {
    result.status = 'active';
  }
  if (row.status === 'error') {
    result.status = 'exception';
  }
  return result;
};

const getOkButtonProps = computed(() => {
  const someSuccess = fileListRef.value.some(item => item.status === UploadResultStatus.SUCCESS);
  return {
    disabled: isUploadingRef.value || fileListRef.value.length === 0 || !someSuccess,
  };
});

const getUploadBtnText = computed(() => {
  const someError = fileListRef.value.some(item => item.status === UploadResultStatus.ERROR);
  let result = '';
  if (isUploadingRef.value) {
    result = t('component.upload.uploading');
  } else if (someError) {
    result = t('component.upload.reUploadFailed');
  } else {
    result = t('component.upload.startUpload');
  }
  return result;
});

// 上传前校验
function beforeUpload(file: File) {
  const { size, name } = file;
  const { maxSize } = props;
  const accept = unref(getAccept);
  // 设置最大值，则判断
  if (maxSize && file.size / 1024 / 1024 >= maxSize) {
    message.error(t('component.upload.maxSizeMultiple', { maxSize: maxSize }));
    return false;
  }

  // 设置类型,则判断
  if (accept.length > 0 && !checkFileType(file, accept)) {
    message.error!(t('component.upload.acceptUpload', { acceptFormat: accept.join(',') }));
    return false;
  }

  const commonItem = {
    uuid: buildUUID(),
    file,
    size,
    name,
    percent: 0,
    type: name.split('.').pop(),
  };
  // 生成图片缩略图
  if (checkImgType(file)) {
    // beforeUpload，如果异步会调用自带上传方法
    // file.thumbUrl = await getBase64(file);
    getBase64WithFile(file).then(({ result: thumbUrl }) => {
      fileListRef.value = [
        ...unref(fileListRef),
        {
          thumbUrl,
          ...commonItem,
        },
      ];
    });
  } else {
    fileListRef.value = [...unref(fileListRef), commonItem];
  }
  return false;
}

// 删除
function handleRemove(record: FileItem) {
  const index = fileListRef.value.findIndex(item => item.uuid === record.uuid);
  index !== -1 && fileListRef.value.splice(index, 1);
  emit('delete', record);
}

async function uploadApiByItem(item: FileItem) {
  const { api } = props;
  try {
    let uploadApi;
    if (!api) {
      const log = useLog();
      log.prettyWarn('upload api must exist and be a function');
      const uploadService = useUpload();
      uploadApi = uploadService.uploadFile;
    } else {
      if (_isFunction(api)) {
        uploadApi = api;
      }
      if (_isString(api)) {
        uploadApi = _get(apiService, api);
      }
    }
    item.status = UploadResultStatus.UPLOADING;
    const ret = await uploadApi?.(
      {
        data: {
          ...(props.uploadParams || {}),
        },
        file: item.file,
        name: props.name,
        filename: props.filename,
      },
      function onUploadProgress(progressEvent: ProgressEvent) {
        const complete = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
        item.percent = complete;
      },
    );
    const { data } = ret;
    item.status = UploadResultStatus.SUCCESS;
    if (data) {
      item.response = data;
    } else {
      item.response = ret;
    }
    return {
      success: true,
      error: null,
    };
  } catch (e) {
    console.log(e);
    item.status = UploadResultStatus.ERROR;
    return {
      success: false,
      error: e,
    };
  }
}

// 点击开始上传
async function handleStartUpload() {
  const { maxNumber } = props;
  if (fileListRef.value.length + (props.previewFileList?.length || 0) > maxNumber) {
    return message.warning(t('component.upload.maxNumber', [maxNumber]));
  }
  try {
    isUploadingRef.value = true;
    // 只上传不是成功状态的
    const uploadFileList = fileListRef.value.filter(item => item.status !== UploadResultStatus.SUCCESS) || [];
    const data = await Promise.all(
      uploadFileList.map(item => {
        return uploadApiByItem(item);
      }),
    );
    isUploadingRef.value = false;
    // 生产环境:抛出错误
    const errorList = data.filter((item: any) => !item.success);
    if (errorList.length > 0) throw errorList;
  } catch (e) {
    isUploadingRef.value = false;
    throw e;
  }
}

//   点击保存
function handleOk() {
  const { maxNumber } = props;

  if (fileListRef.value.length > maxNumber) {
    return message.warning(t('component.upload.maxNumber', { maxNumber: maxNumber }));
  }
  if (isUploadingRef.value) {
    return message.warning(t('component.upload.saveWarn'));
  }
  const fileList: string[] = [];

  for (const item of fileListRef.value) {
    const { status, response } = item;
    if (status === UploadResultStatus.SUCCESS && response) {
      fileList.push(response.url);
    }
  }
  // 存在一个上传成功的即可保存
  if (fileList.length <= 0) {
    return message.warning(t('component.upload.saveError'));
  }
  fileListRef.value = [];
  closeModal();
  emit('change', fileList);
}

// 点击关闭：则所有操作不保存，包括上传的
async function handleCloseFunc() {
  if (!isUploadingRef.value) {
    fileListRef.value = [];
    return true;
  } else {
    message.warning(t('component.upload.uploadWait'));
    return false;
  }
}

// 预览
function handlePreview(record: FileItem) {
  const { thumbUrl = '' } = record;
  createImgPreview({
    imageList: [thumbUrl],
  });
}
</script>
<style>
.upload-modal .ant-upload-list {
  display: none;
}
.upload-modal .ant-table-wrapper .ant-spin-nested-loading {
  padding: 0;
}
.upload-modal-toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}
.upload-modal-toolbar__btn {
  margin-left: 8px;
  text-align: right;
  flex: 1;
}
</style>
