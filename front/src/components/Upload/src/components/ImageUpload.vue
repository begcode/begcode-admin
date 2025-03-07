<template>
  <div>
    <a-upload
      v-bind="$attrs"
      v-model:file-list="fileList"
      :list-type="listType"
      :accept="getStringAccept"
      :multiple="multiple"
      :maxCount="maxNumber"
      :before-upload="beforeUpload"
      :custom-request="customRequest"
      @preview="handlePreview"
      @remove="handleRemove"
    >
      <div v-if="fileList && fileList.length < maxNumber">
        <Icon icon="ant-design:plus-outlined" />
        <div style="margin-top: 8px">{{ t('component.upload.upload') }}</div>
      </div>
    </a-upload>
    <a-modal :open="previewOpen" :title="previewTitle" :footer="null" @cancel="handleCancel">
      <img alt="" style="width: 100%" :src="previewImage" />
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import type { UploadFile, UploadProps } from 'ant-design-vue';
import { Upload, message } from 'ant-design-vue';
import { UploadRequestOption } from 'ant-design-vue/lib/vc-upload/interface';
import { isObject } from '@/utils/is';
import { useLog } from '@/hooks/useLog';
import { useI18n } from '@/hooks/web/useI18nOut';
import { useUploadType } from '../hooks/useUpload';
import { uploadContainerProps } from '../props';
import { isImgTypeByName } from '../helper';
import { UploadResultStatus } from '@/components/Upload/src/types/typing';

defineOptions({ name: 'ImageUpload' });

const props = defineProps({
  ...uploadContainerProps,
});

const emit = defineEmits(['change', 'update:value', 'delete']);

const { t } = useI18n();
const { accept, helpText, maxNumber, maxSize } = toRefs(props);
const isInnerOperate = ref<boolean>(false);
const { getStringAccept } = useUploadType({
  acceptRef: accept,
  helpTextRef: helpText,
  maxNumberRef: maxNumber,
  maxSizeRef: maxSize,
});
const previewOpen = ref<boolean>(false);
const previewImage = ref<string>('');
const previewTitle = ref<string>('');

const fileList = ref<UploadProps['fileList']>([]);
const isLtMsg = ref<boolean>(true);
const isActMsg = ref<boolean>(true);

watch(
  () => props.value,
  v => {
    if (isInnerOperate.value) {
      isInnerOperate.value = false;
      return;
    }
    if (v) {
      let value: string[] = [];
      if (_isArray(v)) {
        value = v;
      } else {
        value.push(v);
      }
      fileList.value = value.map((item, i) => {
        if (item && _isString(item)) {
          return {
            uid: -i + '',
            name: item.substring(item.lastIndexOf('/') + 1),
            status: 'done',
            url: item,
          };
        } else if (item && _isObject(item)) {
          if (item.uid === undefined) {
            item.uid = -i + '';
          }
          return item;
        } else {
          return;
        }
      }) as UploadProps['fileList'];
    }
  },
  {
    immediate: true,
  },
);

function getBase64<T extends string | ArrayBuffer | null>(file: File) {
  return new Promise<T>((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      resolve(reader.result as T);
    };
    reader.onerror = error => reject(error);
  });
}

const handlePreview = async (file: UploadFile) => {
  if (!file.url && !file.preview) {
    file.preview = await getBase64<string>(file.originFileObj!);
  }
  previewImage.value = file.url || file.preview || '';
  previewOpen.value = true;
  previewTitle.value = file.name || previewImage.value.substring(previewImage.value.lastIndexOf('/') + 1);
};

const handleRemove = async (file: UploadFile) => {
  if (fileList.value) {
    const index = fileList.value.findIndex(item => item.uid === file.uid);
    index !== -1 && fileList.value.splice(index, 1);
    const value = getValue();
    isInnerOperate.value = true;
    emit('change', value);
    emit('delete', file);
  }
};

const handleCancel = () => {
  previewOpen.value = false;
  previewTitle.value = '';
};

const beforeUpload = (file: File) => {
  const { maxSize, accept } = props;
  const { name } = file;
  const isAct = isImgTypeByName(name);
  if (!isAct) {
    message.error(t('component.upload.acceptUpload', [accept]));
    isActMsg.value = false;
    // 防止弹出多个错误提示
    setTimeout(() => (isActMsg.value = true), 1000);
  }
  const isLt = file.size / 1024 / 1024 > maxSize;
  if (isLt) {
    message.error(t('component.upload.maxSizeMultiple', [maxSize]));
    isLtMsg.value = false;
    // 防止弹出多个错误提示
    setTimeout(() => (isLtMsg.value = true), 1000);
  }
  return (isAct && !isLt) || Upload.LIST_IGNORE;
};

async function customRequest(info: UploadRequestOption<any>) {
  const { api } = props;
  if (!api || !_isFunction(api)) {
    const log = useLog();
    return log.prettyWarn('upload api must exist and be a function');
  }
  try {
    const res = await props.api?.({
      data: {
        ...(props.uploadParams || {}),
      },
      file: info.file,
      name: props.name,
      filename: props.filename,
    });
    info.onSuccess!(res);
    const value = getValue();
    isInnerOperate.value = true;
    emit('change', value);
  } catch (e: any) {
    console.log(e);
    info.onError!(e);
  }
}

function getValue() {
  const list = (fileList.value || [])
    .filter(item => item?.status === UploadResultStatus.DONE)
    .map((item: any) => {
      if (props.fileValueType === 'object') {
        if (item?.response?.url) {
          return item.response;
        } else {
          return item;
        }
      } else {
        return item?.url || item?.response?.url;
      }
    });
  return props.multiple ? list : list.length > 0 ? list[0] : '';
}
</script>

<style>
.ant-upload-select-picture-card i {
  color: #999;
  font-size: 32px;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
