import { DemoOptionsItem, selectParams } from './model/optionsModel';
import { defHttp } from '@/utils/http/axios';

enum Api {
  OPTIONS_LIST = '/select/getDemoOptions',
}

/**
 * @description: Get sample options value
 */
export const optionsListApi = (params?: selectParams) => defHttp.get<DemoOptionsItem[]>({ url: Api.OPTIONS_LIST, params });
