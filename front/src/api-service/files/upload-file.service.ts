import qs from 'qs';
import { defHttp } from '@/utils/http/axios';
import buildPaginationQueryOpts from '@/utils/jhipster/sorts';
import { PageRecord } from '@/models/baseModel';
import { useMethods } from '@/hooks/system/useMethods';
import { IUploadFile } from '@/models/files/upload-file.model';
import { UploadFileParams } from '@/types/axios';

const apiUrl = '/api/upload-files';

export default {
  // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

  find(id: number): Promise<IUploadFile> {
    return defHttp.get({ url: `${apiUrl}/${id}` });
  },

  retrieve(paginationQuery?: any): Promise<PageRecord<IUploadFile>> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return defHttp.get({ url: apiUrl, params: options });
  },

  stats(queryParams?: any): Promise<any> {
    const params = buildPaginationQueryOpts(queryParams);
    return defHttp.get({ url: `${apiUrl}/stats`, params });
  },

  exist(queryParams?: any): Promise<Boolean> {
    if (!queryParams.hasOwnProperty('id.aggregate.count') || !_get(queryParams, 'id.aggregate.count')) {
      queryParams['id.aggregate.count'] = true;
    }
    const options = buildPaginationQueryOpts(queryParams);
    return new Promise((resolve, reject) => {
      defHttp
        .get({ url: `${apiUrl}/stats?`, params: options })
        .then(res => {
          resolve(res && res[0] && res[0].id_count > 0);
        })
        .catch(err => reject(err));
    });
  },

  delete(id: number): Promise<any> {
    return defHttp.delete({ url: `${apiUrl}/${id}` });
  },

  deleteByIds(ids: number[]): Promise<any> {
    return defHttp.delete({ url: apiUrl, params: { ids } }, { joinParamsToUrl: true });
  },

  update(uploadFile: IUploadFile, batchIds?: number[], batchFields?: String[]): Promise<IUploadFile> {
    let queryParams = '';
    if (batchIds && batchFields) {
      queryParams = qs.stringify({ batchIds, batchFields }, { arrayFormat: 'repeat' });
    }
    return defHttp.put({ url: `${apiUrl}/${uploadFile.id}?${queryParams}`, data: uploadFile });
  },
  copy(uploadFile: IUploadFile, batchIds?: number[], batchFields?: String[]): Promise<IUploadFile> {
    let queryParams = '';
    if (batchIds && batchFields) {
      queryParams = qs.stringify({ batchIds, batchFields }, { arrayFormat: 'repeat' });
    }
    return defHttp.patch({ url: `${apiUrl}/copy/${uploadFile.id}?${queryParams}`, data: uploadFile });
  },
  create(uploadFile: IUploadFile, onUploadProgress?: (progressEvent: ProgressEvent) => void, success?) {
    const params: UploadFileParams = <UploadFileParams>{};
    params.name = 'file';
    params.file = uploadFile.file as File;
    params.filename = uploadFile!.file!.name;
    const newUploadFile = { ...uploadFile };
    delete newUploadFile.file;
    params.data = { uploadFileDTO: _pickBy(newUploadFile, value => !!value) };
    return defHttp.uploadFile<IUploadFile>(
      {
        url: `${apiUrl}`,
        onUploadProgress,
      },
      params,
      { isReturnResponse: true, success },
    );
  },
  uploadFileUrl: apiUrl,

  /**
   * 导出xls
   * @param fileName 文件名
   * @param paginationQuery 分页参数
   * @param isXlsx 是否导出xlsx
   */
  exportExcel(fileName, paginationQuery?: any, isXlsx = false) {
    const { handleExportXls, handleExportXlsx } = useMethods();
    const options = buildPaginationQueryOpts(paginationQuery);
    if (isXlsx) {
      return handleExportXlsx(fileName, `${apiUrl}/export`, options);
    }
    return handleExportXls(fileName, `${apiUrl}/export`, options);
  },

  /**
   * 导入xls
   * @param file 导入的文件
   * @param success 成功回调
   */
  importExcel(file, success) {
    const { handleImportXls } = useMethods();
    return handleImportXls(file, `${apiUrl}/import`, success);
  },
  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
};
