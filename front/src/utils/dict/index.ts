import { getAuthCache } from '@/utils/auth';
import { DB_DICT_DATA_KEY } from '@/enums/cacheEnum';
import { defHttp } from '@/utils/http/axios';

/**
 * 从缓存中获取字典配置
 * @param code
 */
export const getDictItemsByCode = code => {
  if (getAuthCache(DB_DICT_DATA_KEY) && getAuthCache(DB_DICT_DATA_KEY)[code]) {
    return getAuthCache(DB_DICT_DATA_KEY)[code];
  }
};
/**
 * 获取字典数组
 * @param code 字典Code
 * @return List<Map>
 */
export const initDictOptions = code => {
  //1.优先从缓存中读取字典配置
  if (getDictItemsByCode(code)) {
    return new Promise((resolve, _reject) => {
      resolve(getDictItemsByCode(code));
    });
  }
  //2.获取字典数组
  if (code.indexOf(',') > 0 && code.indexOf(' ') > 0) {
    // 编码后类似sys_user%20where%20username%20like%20xxx' 是不包含空格的,这里判断如果有空格和逗号说明需要编码处理
    code = encodeURI(code);
  }
  return defHttp.get({ url: `/sys/dict/getDictItems/${code}` });
};
/**
 * 获取字典数组
 * @param code 字典Code
 * @param params 查询参数
 * @param options 查询配置
 * @return List<Map>
 */
export const ajaxGetDictItems = (code, params, options?) => defHttp.get({ url: `/sys/dict/getDictItems/${code}`, params }, options);
