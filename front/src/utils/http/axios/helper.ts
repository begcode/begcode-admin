import dayjs from 'dayjs';

const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';

export function joinTimestamp<T extends boolean>(join: boolean, restful: T): T extends true ? string : object;

export function joinTimestamp(join: boolean, restful = false): string | object {
  if (!join) {
    return restful ? '' : {};
  }
  const now = new Date().getTime();
  if (restful) {
    return `?_t=${now}`;
  }
  return { _t: now };
}

/**
 * @description: Format request parameter time
 */
export function formatRequestDate(params: Recordable) {
  if (Object.prototype.toString.call(params) !== '[object Object]') {
    return;
  }

  for (const key in params) {
    // 判断是否是dayjs实例
    const format = params[key]?.format ?? null;
    if (format && typeof format === 'function' && dayjs.isDayjs(params[key])) {
      params[key] = params[key].format(DATE_TIME_FORMAT);
    }
    if (_isString(key)) {
      const value = params[key];
      if (value) {
        try {
          params[key] = _isString(value) ? value.trim() : value;
        } catch (error: any) {
          throw new Error(error);
        }
      }
    }
    if (_isObject(params[key])) {
      formatRequestDate(params[key]);
    }
  }
}
