import { ajaxGetDictItems, getDictItemsByCode } from './index';

/**
 * 获取字典数组
 * @param dictCode 字典Code
 * @param isTransformResponse 是否转换返回结果
 * @return List<Map>
 */
export async function initDictOptions(dictCode, isTransformResponse = true) {
  if (!dictCode) {
    return '字典Code不能为空!';
  }
  // 优先从缓存中读取字典配置
  if (getDictItemsByCode(dictCode)) {
    const res = {};
    res.result = getDictItemsByCode(dictCode);
    res.success = true;
    if (isTransformResponse) {
      return res.result;
    }
    return res;
  }
  //获取字典数组
  return await ajaxGetDictItems(dictCode, {}, { isTransformResponse });
}

/**
 * 字典值替换文本通用方法
 * @param dictOptions  字典数组
 * @param text  字典值
 * @return String
 */
export function filterDictText(dictOptions, text) {
  if (text != null && Array.isArray(dictOptions)) {
    const result = [];
    // 允许多个逗号分隔，允许传数组对象
    let splitText;
    if (Array.isArray(text)) {
      splitText = text;
    } else {
      splitText = text.toString().trim().split(',');
    }
    for (const txt of splitText) {
      let dictText = txt;
      for (const dictItem of dictOptions) {
        if (txt.toString() === dictItem.value.toString()) {
          dictText = dictItem.text || dictItem.title || dictItem.label;
          break;
        }
      }
      result.push(dictText);
    }
    return result.join(',');
  }
  return text;
}

/**
 * 字典值替换文本通用方法(多选)
 * @param dictOptions  字典数组
 * @param text  字典值
 * @return String
 */
export function filterMultiDictText(dictOptions, text) {
  // js “!text” 认为0为空，所以做提前处理
  if (text === 0 || text === '0') {
    if (dictOptions) {
      for (const dictItem of dictOptions) {
        if (text === dictItem.value) {
          return dictItem.text;
        }
      }
    }
  }

  if (!text || text === 'undefined' || text === 'null' || !dictOptions || dictOptions.length === 0) {
    return '';
  }
  let re = '';
  text = text.toString();
  const arr = text.split(',');
  dictOptions.forEach(function (option) {
    if (option) {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i] === option.value) {
          re += `${option.text},`;
          break;
        }
      }
    }
  });
  if (re === '') {
    return text;
  }
  return re.substring(0, re.length - 1);
}

/**
 * 翻译字段值对应的文本
 * @param dictCode
 * @param key
 * @returns string
 */
export function filterDictTextByCache(dictCode, key) {
  if (key === null || key.length === 0) {
    return;
  }
  if (!dictCode) {
    return '字典Code不能为空!';
  }
  // 优先从缓存中读取字典配置
  if (getDictItemsByCode(dictCode)) {
    const item = getDictItemsByCode(dictCode).filter(t => t.value === key);
    if (item && item.length > 0) {
      return item[0].text;
    }
  }
}

/**
 * 通过code获取字典数组
 *
 */
export async function getDictItems(dictCode, params) {
  // 优先从缓存中读取字典配置
  if (getDictItemsByCode(dictCode)) {
    return getDictItemsByCode(dictCode).map(item => ({ ...item, label: item.text }));
  }

  // 缓存中没有，就请求后台
  return await ajaxGetDictItems(dictCode, params)
    .then(({ success, result }) => {
      if (success) {
        const res = result.map(item => ({ ...item, label: item.text }));
        return Promise.resolve(res);
      }
      console.error('getDictItems error: : ', res);
      return Promise.resolve([]);
    })
    .catch(res => {
      console.error('getDictItems error: ', res);
      return Promise.resolve([]);
    });
}
