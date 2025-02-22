import { useI18n } from '@/hooks/web/useI18nOut';

// 文件上传列表
export function createTableColumns(): any[] {
  const { t } = useI18n();
  return [
    {
      field: 'thumbUrl',
      title: t('component.upload.legend'),
      width: 100,
      slots: { default: 'thumbUrl' },
    },
    {
      field: 'name',
      title: t('component.upload.fileName'),
      align: 'left',
      slots: { default: 'name' },
    },
    {
      field: 'size',
      title: t('component.upload.fileSize'),
      width: 100,
      formatter: ({ cellValue = 0 }) => {
        return cellValue && `${(cellValue / 1024).toFixed(2)}KB`;
      },
    },
    // {
    //   dataIndex: 'type',
    //   title: '文件类型',
    //   width: 100,
    // },
    {
      field: 'status',
      title: t('component.upload.fileStatue'),
      width: 100,
      slots: { default: 'status' },
    },
    {
      title: '操作',
      field: 'recordOperation',
      fixed: 'right',
      headerAlign: 'center',
      align: 'right',
      showOverflow: false,
      width: 140,
      slots: { default: 'recordAction' },
    },
  ];
}
// 文件预览列表
export function createPreviewColumns(): any[] {
  const { t } = useI18n();
  return [
    {
      field: 'url',
      title: t('component.upload.legend'),
      width: 100,
      slots: { default: 'url' },
    },
    {
      field: 'name',
      title: t('component.upload.fileName'),
      align: 'left',
    },
    {
      title: '操作',
      field: 'recordOperation',
      fixed: 'right',
      headerAlign: 'center',
      align: 'right',
      showOverflow: false,
      width: 140,
      slots: { default: 'recordAction' },
    },
  ];
}
