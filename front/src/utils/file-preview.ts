import dayjs from 'dayjs';

export function getFilePreview(file: any) {
  const result: any = {};
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp'].includes(file.ext || '')) {
    result.imageUrl = file.url;
    result.noPreview = false;
  } else if (file.ext === 'doc') {
    result.imageUrl = '/resource/img/filetype/doc.png';
  } else if (file.ext === 'docx') {
    result.imageUrl = '/resource/img/filetype/docx.png';
  } else if (file.ext === 'xls') {
    result.imageUrl = '/resource/img/filetype/xls.png';
  } else if (file.ext === 'xlsx') {
    result.imageUrl = '/resource/img/filetype/xlsx.png';
  } else if (file.ext === 'ppt') {
    result.imageUrl = '/resource/img/filetype/ppt.png';
  } else if (file.ext === 'pptx') {
    result.imageUrl = '/resource/img/filetype/pptx.png';
  } else if (file.ext === 'pdf') {
    result.imageUrl = '/resource/img/filetype/pdf.png';
  } else if (file.ext === 'txt') {
    result.imageUrl = '/resource/img/filetype/txt.png';
  } else if (file.ext === 'zip') {
    result.imageUrl = '/resource/img/filetype/zip.png';
  } else if (file.ext === 'rar') {
    result.imageUrl = '/resource/img/filetype/rar.png';
  } else {
    result.imageUrl = '/resource/img/filetype/other.png';
  }
  result.showImage = true;
  result.detail = `上传时间:${dayjs(file.createAt).format('YYYY-MM-DD')},文件大小:${((file.fileSize || 0) / 1024).toFixed(1)}KB`;
  return { ...file, ...result };
}
