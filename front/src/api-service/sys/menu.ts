import { getMenuListResultModel } from './model/menuModel';
import { defHttp } from '@/utils/http/axios';

enum Api {
  GetMenuList = '/getMenuList',
}

/**
 * @description: Get user menu based on id
 */

export const getMenuList = () => {
  return new Promise(resolve => {
    //为了兼容mock和接口数据
    defHttp.get<getMenuListResultModel>({ url: Api.GetMenuList }).then(res => {
      if (Array.isArray(res)) {
        resolve(res);
      } else {
        resolve(res['menu']);
      }
    });
  });
};
