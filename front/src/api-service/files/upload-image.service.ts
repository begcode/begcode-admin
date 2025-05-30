import qs from 'qs';
import { AxiosProgressEvent } from 'axios';
import { defHttp } from '@/utils/http/axios';
import buildPaginationQueryOpts from '@/utils/jhipster/sorts';
import { PageRecord } from '@/models/baseModel';
import { useMethods } from '@/hooks/system/useMethods';
import { IUploadImage } from '@/models/files/upload-image.model';
import { UploadFileParams } from '@/types/axios';

const apiUrl = '/api/upload-images';

export default {
  // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

  find(id: number): Promise<IUploadImage> {
    return defHttp.get({ url: `${apiUrl}/${id}` });
  },

  retrieve(paginationQuery?: any): Promise<PageRecord<IUploadImage>> {
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

  update(uploadImage: IUploadImage, batchIds?: number[], batchFields?: String[]): Promise<IUploadImage> {
    let queryParams = '';
    if (batchIds && batchFields) {
      queryParams = qs.stringify({ batchIds, batchFields }, { arrayFormat: 'repeat' });
    }
    return defHttp.put({ url: `${apiUrl}/${uploadImage.id}?${queryParams}`, data: uploadImage });
  },
  copy(uploadImage: IUploadImage, batchIds?: number[], batchFields?: String[]): Promise<IUploadImage> {
    let queryParams = '';
    if (batchIds && batchFields) {
      queryParams = qs.stringify({ batchIds, batchFields }, { arrayFormat: 'repeat' });
    }
    return defHttp.patch({ url: `${apiUrl}/copy/${uploadImage.id}?${queryParams}`, data: uploadImage });
  },
  create(uploadImage: IUploadImage, onUploadProgress?: (progressEvent: AxiosProgressEvent) => void, success?) {
    const params: UploadFileParams = <UploadFileParams>{};
    params.name = 'image';
    params.file = uploadImage['file'] as File;
    if (uploadImage['filename']) {
      params.filename = uploadImage['filename'];
    } else {
      params.filename = uploadImage['file']?.name || 'noFileName';
    }
    const newUploadImage = { ...uploadImage };
    delete newUploadImage['file'];
    params.data = { uploadImageDTO: _pickBy(newUploadImage, value => !!value) };
    return defHttp.uploadFile<IUploadImage>(
      {
        url: `${apiUrl}`,
        onUploadProgress,
      },
      params,
      { isReturnResponse: true, success },
    );
  },
  uploadImageUrl: apiUrl,

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
