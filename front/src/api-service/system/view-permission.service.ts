import qs from 'qs';
import { defHttp } from '@/utils/http/axios';
import buildPaginationQueryOpts from '@/utils/jhipster/sorts';
import { PageRecord } from '@/models/baseModel';
import { useMethods } from '@/hooks/system/useMethods';
import { IViewPermission } from '@/models/system/view-permission.model';

const apiUrl = '/api/view-permissions';

export default {
  // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

  find(id: number): Promise<IViewPermission> {
    return defHttp.get({ url: `${apiUrl}/${id}` });
  },

  retrieve(paginationQuery?: any): Promise<PageRecord<IViewPermission>> {
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

  updateSortValue(
    id: number,
    type: 'DROP' | 'STEP' | 'VALUE',
    newSortValue: number,
    beforeId: number,
    afterId: number,
    viewPermissionCriteria: any,
  ): Promise<Boolean> {
    return defHttp.put({
      url: `${apiUrl}/sort-value/${id}/${type}?newSortValue=${newSortValue || ''}&beforeId=${beforeId || ''}&afterId=${afterId || ''}`,
      params: viewPermissionCriteria,
    });
  },

  delete(id: number): Promise<any> {
    return defHttp.delete({ url: `${apiUrl}/${id}` });
  },

  deleteByIds(ids: number[]): Promise<any> {
    return defHttp.delete({ url: apiUrl, params: { ids } }, { joinParamsToUrl: true });
  },

  tree(paginationQuery?: any): Promise<PageRecord<IViewPermission[]>> {
    const options = buildPaginationQueryOpts(paginationQuery);
    return defHttp.get({ url: `${apiUrl}/tree`, params: options });
  },

  treeByParentId(parentId: number): Promise<IViewPermission[]> {
    return defHttp.get({ url: `${apiUrl}/${parentId}/tree` });
  },

  create(viewPermission: IViewPermission): Promise<IViewPermission> {
    return defHttp.post({ url: `${apiUrl}`, data: viewPermission });
  },

  update(viewPermission: IViewPermission, batchIds?: number[], batchFields?: String[]): Promise<IViewPermission> {
    let queryParams = '';
    if (batchIds && batchFields) {
      queryParams = qs.stringify({ batchIds, batchFields }, { arrayFormat: 'repeat' });
    }
    return defHttp.put({ url: `${apiUrl}/${viewPermission.id}?${queryParams}`, data: viewPermission });
  },
  // prettier-ignore
  treeByLogin(): Promise<any> {
    return defHttp.get({ url: `${apiUrl}/current-user` });
  },

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
