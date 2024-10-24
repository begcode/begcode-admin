import dayjs from 'dayjs';
import type { FormProps, FormSchemaInner as FormSchema } from '../types/form';
import type { Recordable } from '#/utils';

import { isNil, isEmpty } from '@/utils/is';
import { dateUtil } from '@/utils/dateUtil';

interface UseFormValuesContext {
  defaultValueRef: Ref<any>;
  getSchema: ComputedRef<FormSchema[]>;
  getProps: ComputedRef<FormProps>;
  formModel: Recordable;
}

/**
 * @desription deconstruct array-link key. This method will mutate the target.
 */
function tryDeconstructArray(key: string, value: any, target: Recordable) {
  const pattern = /^\[(.+)\]$/;
  if (pattern.test(key)) {
    const match = key.match(pattern);
    if (match && match[1]) {
      const keys = match[1].split(',');
      value = Array.isArray(value) ? value : [value];
      keys.forEach((k, index) => {
        _set(target, k.trim(), value[index]);
      });
      return true;
    }
  }
}

/**
 * @desription deconstruct object-link key. This method will mutate the target.
 */
function tryDeconstructObject(key: string, value: any, target: Recordable) {
  const pattern = /^\{(.+)\}$/;
  if (pattern.test(key)) {
    const match = key.match(pattern);
    if (match && match[1]) {
      const keys = match[1].split(',');
      value = _isObject(value) ? value : {};
      keys.forEach(k => {
        _set(target, k.trim(), value[k.trim()]);
      });
      return true;
    }
  }
}

export function useFormValues({ defaultValueRef, getSchema, formModel, getProps }: UseFormValuesContext) {
  // Processing form values
  function handleFormValues(values: Recordable) {
    if (!_isObject(values)) {
      return {};
    }
    const res: Recordable = {};
    for (const item of Object.entries(values)) {
      let [, value] = item;
      const [key] = item;
      if (!key || (_isArray(value) && value.length === 0) || _isFunction(value)) {
        continue;
      }
      const transformDateFunc = unref(getProps).transformDateFunc;
      if (_isObject(value)) {
        value = transformDateFunc?.(value);
      }
      // 判断是否是dayjs实例
      // @ts-ignore
      if (_isArray(value) && dayjs.isDayjs(value[0]) && dayjs.isDayjs(value[1]) && value[0]?.format && value[1]?.format) {
        value = value.map(item => transformDateFunc?.(item));
      }
      // Remove spaces
      if (_isString(value)) {
        value = value.trim();
      }
      if (!tryDeconstructArray(key, value, res) && !tryDeconstructObject(key, value, res)) {
        // 没有解构成功的，按原样赋值
        _set(res, key, value);
      }
    }
    return handleRangeTimeValue(res);
  }

  /**
   * @description: Processing time interval parameters
   */
  function handleRangeTimeValue(values: Recordable) {
    const fieldMapToTime = unref(getProps).fieldMapToTime;

    if (!fieldMapToTime || !Array.isArray(fieldMapToTime)) {
      return values;
    }

    for (const [field, [startTimeKey, endTimeKey], format = 'YYYY-MM-DD'] of fieldMapToTime) {
      if (!field || !startTimeKey || !endTimeKey) {
        continue;
      }
      // If the value to be converted is empty, remove the field
      if (!_get(values, field)) {
        _unset(values, field);
        continue;
      }

      const [startTime, endTime]: string[] = _get(values, field);

      const [startTimeFormat, endTimeFormat] = Array.isArray(format) ? format : [format, format];

      if (!isNil(startTime) && !isEmpty(startTime)) {
        _set(values, startTimeKey, formatTime(startTime, startTimeFormat));
      }
      if (!isNil(startTime) && !isEmpty(endTime)) {
        _set(values, endTimeKey, formatTime(endTime, endTimeFormat));
      }
      _unset(values, field);
    }

    return values;
  }

  function formatTime(time: string, format: string) {
    if (format === 'timestamp') {
      return dateUtil(time).unix();
    } else if (format === 'timestampStartDay') {
      return dateUtil(time).startOf('day').unix();
    }
    return dateUtil(time).format(format);
  }

  function initDefault() {
    const schemas = unref(getSchema);
    const obj: Recordable = {};
    schemas.forEach(item => {
      const { defaultValue, defaultValueObj } = item;
      const fieldKeys = Object.keys(defaultValueObj || {});
      if (fieldKeys.length) {
        fieldKeys.map(field => {
          obj[field] = defaultValueObj![field];
          if (formModel[field] === undefined) {
            formModel[field] = defaultValueObj![field];
          }
        });
      }
      if (!isNil(defaultValue)) {
        obj[item.field] = defaultValue;

        if (formModel[item.field] === undefined) {
          formModel[item.field] = defaultValue;
        }
      }
    });
    defaultValueRef.value = _cloneDeep(obj);
  }

  return { handleFormValues, initDefault };
}
