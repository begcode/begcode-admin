import { defHttp } from '@/utils/http/axios';
import { useMessage } from '@/hooks/web/useMessage';
import { useGlobSetting } from '@/hooks/setting';

const { createMessage, createWarningModal } = useMessage();
const glob = useGlobSetting();

/**
 * 导出文件xlsx的mime-type
 */
export const XLSX_MIME_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
/**
 * 导出文件xlsx的文件后缀
 */
export const XLSX_FILE_SUFFIX = '.xlsx';

export function useMethods() {
  /**
   * 导出xls
   * @param name
   * @param url
   */
  async function exportXls(name, url, params, isXlsx = false) {
    const data = await defHttp.get({ url, params, responseType: 'blob', timeout: 60000 }, { isTransformResponse: false });
    if (!data) {
      createMessage.warning('文件下载失败');
      return;
    }
    const reader = new FileReader();
    reader.readAsText(data, 'utf-8');
    reader.onload = async () => {
      if (reader.result) {
        if (reader.result.toString().indexOf('success') != -1) {
          const { success, message } = JSON.parse(reader.result.toString());
          if (!success) {
            createMessage.warning(`导出失败，失败原因：${message}`);
          } else {
            exportExcel(name, isXlsx, data);
          }
        }
      }
    };
  }

  /**
   * 导入xls
   * @param data 导入的数据
   * @param url
   * @param success 成功后的回调
   */
  async function importXls(data, url, success) {
    const isReturn = fileInfo => {
      try {
        if (fileInfo.code === 201) {
          const {
            message,
            result: { msg, fileUrl, fileName },
          } = fileInfo;
          const href = glob.uploadUrl + fileUrl;
          createWarningModal({
            title: message,
            centered: false,
            content: `<div>
                                <span>${msg}</span><br/>
                                <span>具体详情请<a href = ${href} download = ${fileName}> 点击下载 </a> </span>
                              </div>`,
          });
        } else if (fileInfo.code === 500 || fileInfo.code === 510) {
          createMessage.error(fileInfo.message || `${data.file.name} 导入失败`);
        } else {
          createMessage.success(fileInfo.message || `${data.file.name} 文件上传成功`);
        }
      } catch (error) {
        console.log('导入的数据异常', error);
      } finally {
        typeof success === 'function' ? success(fileInfo) : '';
      }
    };
    await defHttp.uploadFile({ url }, { file: data.file }, { success: isReturn });
  }

  return {
    handleExportXls: (name: string, url: string, params?: object) => exportXls(name, url, params),
    handleImportXls: (data, url, success) => importXls(data, url, success),
    handleExportXlsx: (name: string, url: string, params?: object) => exportXls(name, url, params, true),
  };

  /**
   * 导出excel
   * @param name
   * @param isXlsx
   * @param data
   */
  function exportExcel(name, isXlsx, data) {
    if (!name || typeof name !== 'string') {
      name = '导出文件';
    }
    const blobOptions = { type: 'application/vnd.ms-excel' };
    let fileSuffix = '.xls';
    if (isXlsx) {
      blobOptions.type = XLSX_MIME_TYPE;
      fileSuffix = XLSX_FILE_SUFFIX;
    }
    if (typeof window.navigator.msSaveBlob !== 'undefined') {
      window.navigator.msSaveBlob(new Blob([data], blobOptions), name + fileSuffix);
    } else {
      const url = window.URL.createObjectURL(new Blob([data], blobOptions));
      const link = document.createElement('a');
      link.style.display = 'none';
      link.href = url;
      link.setAttribute('download', name + fileSuffix);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link); //下载完成移除元素
      window.URL.revokeObjectURL(url); //释放掉blob对象
    }
  }
}
