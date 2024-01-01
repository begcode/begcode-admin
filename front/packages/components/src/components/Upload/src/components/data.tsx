import { FileItem, PreviewFileItem, UploadResultStatus } from '../types/typing';
import { isImgTypeByName } from '../helper';
import { Progress, Tag } from 'ant-design-vue';
import ThumbUrl from './ThumbUrl.vue';
import { useI18n } from '@/hooks/web/useI18nOut';

// 文件上传列表
export function createTableColumns(): any[] {
  const { t } = useI18n();
  return [
    {
      dataIndex: 'thumbUrl',
      title: t('component.upload.legend'),
      width: 100,
      customRender: ({ record }) => {
        const { thumbUrl } = (record as FileItem) || {};
        return thumbUrl && <ThumbUrl fileUrl={thumbUrl} />;
      },
    },
    {
      dataIndex: 'name',
      title: t('component.upload.fileName'),
      align: 'left',
      customRender: ({ text, record }) => {
        const { percent, status: uploadStatus } = (record as FileItem) || {};
        let status: 'normal' | 'exception' | 'active' | 'success' = 'normal';
        if (uploadStatus === UploadResultStatus.ERROR) {
          status = 'exception';
        } else if (uploadStatus === UploadResultStatus.UPLOADING) {
          status = 'active';
        } else if (uploadStatus === UploadResultStatus.SUCCESS) {
          status = 'success';
        }
        return (
          <span>
            <p class="truncate mb-1" title={text}>
              {text}
            </p>
            <Progress percent={percent} size="small" status={status} />
          </span>
        );
      },
    },
    {
      dataIndex: 'size',
      title: t('component.upload.fileSize'),
      width: 100,
      customRender: ({ text = 0 }) => {
        return text && (text / 1024).toFixed(2) + 'KB';
      },
    },
    // {
    //   dataIndex: 'type',
    //   title: '文件类型',
    //   width: 100,
    // },
    {
      dataIndex: 'status',
      title: t('component.upload.fileStatue'),
      width: 100,
      customRender: ({ text }) => {
        if (text === UploadResultStatus.SUCCESS) {
          return <Tag color="green">{() => t('component.upload.uploadSuccess')}</Tag>;
        } else if (text === UploadResultStatus.ERROR) {
          return <Tag color="red">{() => t('component.upload.uploadError')}</Tag>;
        } else if (text === UploadResultStatus.UPLOADING) {
          return <Tag color="blue">{() => t('component.upload.uploading')}</Tag>;
        }

        return text;
      },
    },
  ];
}
// 文件预览列表
export function createPreviewColumns(): any[] {
  const { t } = useI18n();
  return [
    {
      dataIndex: 'url',
      title: t('component.upload.legend'),
      width: 100,
      customRender: ({ record }) => {
        const { url } = (record as PreviewFileItem) || {};
        return isImgTypeByName(url) && <ThumbUrl fileUrl={url} />;
      },
    },
    {
      dataIndex: 'name',
      title: t('component.upload.fileName'),
      align: 'left',
    },
  ];
}
