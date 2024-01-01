import type { RouteLocationNormalized, RouteRecordNormalized } from 'vue-router';
import type { App, Component } from 'vue';

import { unref } from 'vue';
import { intersectionWith, isEqual, mergeWith, unionWith, isArray, isObject } from 'lodash-es';

export const noop = () => {};

/**
 * @description:  Set ui mount node
 */
export function getPopupContainer(node?: HTMLElement): HTMLElement {
  return (node?.parentNode as HTMLElement) ?? document.body;
}

/**
 * Add the object as a parameter to the URL
 * @param baseUrl url
 * @param obj
 * @returns {string}
 * eg:
 *  let obj = {a: '3', b: '4'}
 *  setObjToUrlParams('www.baidu.com', obj)
 *  ==>www.baidu.com?a=3&b=4
 */
export function setObjToUrlParams(baseUrl: string, obj: any): string {
  let parameters = '';
  for (const key in obj) {
    parameters += key + '=' + encodeURIComponent(obj[key]) + '&';
  }
  parameters = parameters.replace(/&$/, '');
  return /\?$/.test(baseUrl) ? baseUrl + parameters : baseUrl.replace(/\/?$/, '?') + parameters;
}

/**
 * Recursively merge two objects.
 * 递归合并两个对象。
 *
 * @param source The source object to merge from. 要合并的源对象。
 * @param target The target object to merge into. 目标对象，合并后结果存放于此。
 * @param mergeArrays How to merge arrays. Default is "replace".
 *        如何合并数组。默认为replace。
 *        - "union": Union the arrays. 对数组执行并集操作。
 *        - "intersection": Intersect the arrays. 对数组执行交集操作。
 *        - "concat": Concatenate the arrays. 连接数组。
 *        - "replace": Replace the source array with the target array. 用目标数组替换源数组。
 * @returns The merged object. 合并后的对象。
 */
export function deepMerge<T extends object | null | undefined, U extends object | null | undefined>(
  source: T,
  target: U,
  mergeArrays: 'union' | 'intersection' | 'concat' | 'replace' = 'replace',
): T & U {
  if (!target) {
    return source as T & U;
  }
  if (!source) {
    return target as T & U;
  }
  return mergeWith({}, source, target, (sourceValue, targetValue) => {
    if (isArray(targetValue) && isArray(sourceValue)) {
      switch (mergeArrays) {
        case 'union':
          return unionWith(sourceValue, targetValue, isEqual);
        case 'intersection':
          return intersectionWith(sourceValue, targetValue, isEqual);
        case 'concat':
          return sourceValue.concat(targetValue);
        case 'replace':
          return targetValue;
        default:
          throw new Error(`Unknown merge array strategy: ${mergeArrays as string}`);
      }
    }
    if (isObject(targetValue) && isObject(sourceValue)) {
      return deepMerge(sourceValue, targetValue, mergeArrays);
    }
    return undefined;
  });
}

export function openWindow(url: string, opt?: { target?: TargetContext | string; noopener?: boolean; noreferrer?: boolean }) {
  const { target = '__blank', noopener = true, noreferrer = true } = opt || {};
  const feature: string[] = [];

  noopener && feature.push('noopener=yes');
  noreferrer && feature.push('noreferrer=yes');

  window.open(url, target, feature.join(','));
}

// dynamic use hook props
export function getDynamicProps<T extends Record<string, unknown>, U>(props: T): Partial<U> {
  const ret: Recordable = {};

  Object.keys(props).map(key => {
    ret[key] = unref((props as Recordable)[key]);
  });

  return ret as Partial<U>;
}

export function getRawRoute(route: RouteLocationNormalized): RouteLocationNormalized {
  if (!route) return route;
  const { matched, ...opt } = route;
  return {
    ...opt,
    matched: (matched
      ? matched.map(item => ({
          meta: item.meta,
          name: item.name,
          path: item.path,
        }))
      : undefined) as RouteRecordNormalized[],
  };
}

// https://github.com/vant-ui/vant/issues/8302
type EventShim = {
  new (...args: any[]): {
    $props: {
      onClick?: (...args: any[]) => void;
    };
  };
};

export type WithInstall<T> = T & {
  install(app: App): void;
} & EventShim;

export type CustomComponent = Component & { displayName?: string };

export const withInstall = <T extends CustomComponent>(component: T, alias?: string) => {
  (component as Record<string, unknown>).install = (app: App) => {
    const compName = component.name || component.displayName;
    if (!compName) return;
    app.component(compName, component);
    if (alias) {
      app.config.globalProperties[alias] = component;
    }
  };
  return component as WithInstall<T>;
};

/**
 * 获取url地址参数
 * @param paraName
 */
export function getUrlParam(paraName) {
  let url = document.location.toString();
  let arrObj = url.split('?');

  if (arrObj.length > 1) {
    let arrPara = arrObj[1].split('&');
    let arr;

    for (let i = 0; i < arrPara.length; i++) {
      arr = arrPara[i].split('=');

      if (arr != null && arr[0] == paraName) {
        return arr[1];
      }
    }
    return '';
  } else {
    return '';
  }
}

/**
 * 获取表单字段值数据类型
 * @param props
 * @param field
 * @updateBy:zyf
 */
export function getValueType(props, field) {
  let formSchema = unref(unref(props)?.schemas);
  let valueType = 'string';
  if (formSchema) {
    let schema = formSchema.filter(item => item.field === field)[0];
    valueType = schema.componentProps && schema.componentProps.valueType ? schema.componentProps.valueType : valueType;
  }
  return valueType;
}

/**
 * 深度克隆对象、数组
 * @param obj 被克隆的对象
 * @return 克隆后的对象
 */
export function cloneObject(obj) {
  return JSON.parse(JSON.stringify(obj));
}
/**
 * 获取随机颜色
 */
export function getRandomColor(index?) {
  const colors = [
    'rgb(100, 181, 246)',
    'rgb(77, 182, 172)',
    'rgb(255, 183, 77)',
    'rgb(229, 115, 115)',
    'rgb(149, 117, 205)',
    'rgb(161, 136, 127)',
    'rgb(144, 164, 174)',
    'rgb(77, 208, 225)',
    'rgb(129, 199, 132)',
    'rgb(255, 138, 101)',
    'rgb(133, 202, 205)',
    'rgb(167, 214, 118)',
    'rgb(254, 225, 89)',
    'rgb(251, 199, 142)',
    'rgb(239, 145, 139)',
    'rgb(169, 181, 255)',
    'rgb(231, 218, 202)',
    'rgb(252, 128, 58)',
    'rgb(254, 161, 172)',
    'rgb(194, 163, 205)',
  ];
  return index && index < 19 ? colors[index] : colors[Math.floor(Math.random() * (colors.length - 1))];
}

export function getRefPromise(componentRef) {
  return new Promise(resolve => {
    (function next() {
      const ref = componentRef.value;
      if (ref) {
        resolve(ref);
      } else {
        setTimeout(() => {
          next();
        }, 100);
      }
    })();
  });
}

/**
 * 2023-09-04
 * liaozhiyang
 * 用new Function替换eval
 */
export function _eval(str: string) {
  return new Function(`return ${str}`)();
}

/**
 * @description: Is it a development mode
 * @returns:
 * @example:
 */
export function isDevMode(): boolean {
  return import.meta.env.DEV;
}

/**
 * @description: Is it a production mode
 * @returns:
 * @example:
 */
export function isProdMode(): boolean {
  return import.meta.env.PROD;
}

/**
 * 简单实现防抖方法
 *
 * 防抖(debounce)函数在第一次触发给定的函数时，不立即执行函数，而是给出一个期限值(delay)，比如100ms。
 * 如果100ms内再次执行函数，就重新开始计时，直到计时结束后再真正执行函数。
 * 这样做的好处是如果短时间内大量触发同一事件，只会执行一次函数。
 *
 * @param fn 要防抖的函数
 * @param delay 防抖的毫秒数
 * @returns {Function}
 */
export function simpleDebounce(fn, delay = 100) {
  let timer: any | null = null;
  return function () {
    let args = arguments;
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      // @ts-ignore
      fn.apply(this, args);
    }, delay);
  };
}

/**
 * 日期格式化
 * @param date 日期
 * @param block 格式化字符串
 */
export function dateFormat(date, block) {
  if (!date) {
    return '';
  }
  let format = block || 'yyyy-MM-dd';
  date = new Date(date);
  const map = {
    M: date.getMonth() + 1, // 月份
    d: date.getDate(), // 日
    h: date.getHours(), // 小时
    m: date.getMinutes(), // 分
    s: date.getSeconds(), // 秒
    q: Math.floor((date.getMonth() + 3) / 3), // 季度
    S: date.getMilliseconds(), // 毫秒
  };
  format = format.replace(/([yMdhmsqS])+/g, (all, t) => {
    let v = map[t];
    if (v !== undefined) {
      if (all.length > 1) {
        v = `0${v}`;
        v = v.substr(v.length - 2);
      }
      return v;
    } else if (t === 'y') {
      return date
        .getFullYear()
        .toString()
        .substr(4 - all.length);
    }
    return all;
  });
  return format;
}
/**
 *  获取文件服务访问路径
 * @param fileUrl 文件路径
 * @param prefix(默认http)  文件路径前缀 http/https
 */
export const getFileAccessHttpUrl = (fileUrl, prefix = 'http', baseApiUrl = '/') => {
  let result = fileUrl;
  try {
    if (fileUrl && fileUrl.length > 0 && !fileUrl.startsWith(prefix)) {
      //判断是否是数组格式
      let isArray = fileUrl.indexOf('[') != -1;
      if (!isArray) {
        let prefix = `${baseApiUrl}`;
        // 判断是否已包含前缀
        if (!fileUrl.startsWith(prefix)) {
          result = `${prefix}${fileUrl}`;
        }
      }
    }
  } catch (err) {}
  return result;
};

/**
 * 获取随机数
 *  @param length 数字位数
 */
export const getRandom = (length: number = 1) => {
  return '-' + parseInt(String(Math.random() * 10000 + 1), length);
};
