import { pick } from 'lodash-es';

export function getSearchQueryData(searchFormConfig: any) {
  const result: any = {};
  if (searchFormConfig.matchType === 'or') {
    result.useOr = true;
  }
  searchFormConfig.fieldList
    .filter(seachField => seachField.value !== undefined && seachField.value !== null && seachField.value !== '')
    .forEach(seachField => {
      if (seachField.operator) {
        result[seachField.field + '.' + seachField.operator] = seachField.value;
      } else {
        result[seachField.field] = seachField.value;
      }
    });
  return result;
}

export function getFilter(searchValue: string, mapOfFilter: { [key: string]: any }) {
  const result: { [key: string]: any } = {};
  if (searchValue) {
    result['jhiCommonSearchKeywords'] = searchValue;
    return result;
  }
  Object.keys(mapOfFilter).forEach(key => {
    const filterResult: any[] = [];
    if (mapOfFilter[key].type === 'Enum' && mapOfFilter[key].value) {
      mapOfFilter[key].value.forEach(value => {
        filterResult.push(value);
      });
      result[key + '.in'] = filterResult;
    }
    if (mapOfFilter[key].type === 'BOOLEAN' && mapOfFilter[key].value) {
      mapOfFilter[key].value.forEach(value => {
        filterResult.push(value);
      });
      result[key + '.in'] = filterResult;
    }
    if (['one-to-one', 'many-to-many', 'many-to-one', 'one-to-many'].includes(mapOfFilter[key].type)) {
      mapOfFilter[key].value.forEach(value => {
        filterResult.push(value);
      });
      result[key + 'Id.in'] = filterResult;
    }
    if (mapOfFilter[key].type === 'STRING' && mapOfFilter[key].value) {
      result[key + '.contains'] = mapOfFilter[key].value;
    }
    if (
      (mapOfFilter[key].type === 'LONG' ||
        mapOfFilter[key].type === 'INTEGER' ||
        mapOfFilter[key].type === 'FLOAT' ||
        mapOfFilter[key].type === 'DOUBLE') &&
      mapOfFilter[key].value
    ) {
      result[key + '.greaterThanOrEqual'] = mapOfFilter[key].value[0];
      result[key + '.lessThanOrEqual'] = mapOfFilter[key].value[1];
    }
    if (mapOfFilter[key].type === 'ZONED_DATE_TIME' && mapOfFilter[key].value) {
      result[key + '.greaterThanOrEqual'] =
        mapOfFilter[key].value[0] && mapOfFilter[key].value[0].isValid() ? mapOfFilter[key].value[0].toJSON() : null;
      result[key + '.lessThanOrEqual'] =
        mapOfFilter[key].value[1] && mapOfFilter[key].value[1].isValid() ? mapOfFilter[key].value[1].toJSON() : null;
    }
  });
  return result;
}

export function getDataByFormField(formFields: any[], data) {
  const fields = formFields.map(field => {
    return field.key;
  });
  return pick(data, fields);
}

export function idsToIdObjectArray(ids: any): any[] {
  const idArray: any[] = [];
  const result: any[] = [];
  if (typeof ids === 'string') {
    idArray.push(ids.split(','));
  }
  if (Object.prototype.toString.call(ids) === '[object Array]') {
    ids.forEach(id => {
      result.push({ id: id });
    });
  }
  return result;
}

export function idObjectArrayToIdArray(idObjectArray: any[]): any[] {
  const result: any[] = [];
  idObjectArray.forEach(idObject => {
    result.push(idObject.id);
  });
  return result;
}

/**
 * 清除树形结构的空子树
 * 主要用在table标签中，否则可能会显示无意义的+号。
 * @param data
 */
export function removeBlankChildren(data: any[]) {
  return data.reduce((pre, cur) => {
    if (cur.children) {
      if (cur.children.length === 0) {
        delete cur.children;
      } else {
        cur.children = removeBlankChildren(cur.children);
      }
    }
    pre.push(cur);
    return pre;
  }, []);
}
