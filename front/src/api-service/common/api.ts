import { message } from 'ant-design-vue';
import { defHttp } from '@/utils/http/axios';
import { useGlobSetting } from '@/hooks/setting';
const globSetting = useGlobSetting();
const baseUploadUrl = globSetting.uploadUrl;
enum Api {
  getDictItems = '/sys/dict/getDictItems/',
  getCategoryData = '/sys/category/loadAllData',
}

/**
 * 上传父路径
 */
export const uploadUrl = `${baseUploadUrl}/sys/common/upload`;

/**
 * 根据字典code加载字典text
 */
export const getDictItems = dictCode => {
  return defHttp.get({ url: Api.getDictItems + dictCode }, { joinTime: false });
};
/**
 * 加载全部分类字典数据
 */
export const loadCategoryData = params => {
  return defHttp.get({ url: Api.getCategoryData, params });
};
/**
 * 文件上传
 */
export const uploadFile = (params, success) => {
  return defHttp.uploadFile({ url: uploadUrl }, params, { success });
};

/**
 * 下载文件
 * @param url 文件路径
 * @param fileName 文件名
 * @param parameter
 * @returns {*}
 */
export const downloadFile = (url, fileName?, parameter?) => {
  return getFileblob(url, parameter).then(data => {
    if (!data || data.size === 0) {
      message.warning('文件下载失败');
      return;
    }
    // @ts-ignore
    if (typeof window.navigator.msSaveBlob !== 'undefined') {
      // @ts-ignore
      window.navigator.msSaveBlob(new Blob([data]), fileName);
    } else {
      const url = window.URL.createObjectURL(new Blob([data]));
      const link = document.createElement('a');
      link.style.display = 'none';
      link.href = url;
      link.setAttribute('download', fileName);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link); //下载完成移除元素
      window.URL.revokeObjectURL(url); //释放掉blob对象
    }
  });
};

/**
 * 下载文件 用于excel导出
 * @param url
 * @param parameter
 * @returns {*}
 */
export const getFileblob = (url, parameter) => {
  return defHttp.get(
    {
      url,
      params: parameter,
      responseType: 'blob',
    },
    { isTransformResponse: false },
  );
};
