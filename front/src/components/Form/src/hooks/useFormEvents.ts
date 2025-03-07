import type { NamePath, ValidateOptions } from 'ant-design-vue/lib/form/interface';
import type { FormActionType, FormProps, FormSchemaInner as FormSchema } from '../types/form';
import { dateItemType, handleInputNumberValue, defaultValueComponents, isIncludeSimpleComponents } from '../helper';
import type { EmitType, Fn } from '#/types';
import type { Recordable } from '#/utils';

import { isDef, isNil } from '@/utils/is';
import { deepMerge, getValueType } from '@/utils/util';
import { dateUtil } from '@/utils/dateUtil';
import { useLog } from '@/hooks/useLog';

interface UseFormActionContext {
  emit: EmitType;
  getProps: ComputedRef<FormProps>;
  getSchema: ComputedRef<FormSchema[]>;
  formModel: Recordable;
  defaultValueRef: Ref<Recordable>;
  formElRef: Ref<FormActionType>;
  schemaRef: Ref<FormSchema[]>;
  handleFormValues: Fn;
}

function tryConstructArray(field: string, values: Recordable = {}): any[] | undefined {
  const pattern = /^\[(.+)\]$/;
  if (pattern.test(field)) {
    const match = field.match(pattern);
    if (match && match[1]) {
      const keys = match[1].split(',');
      if (!keys.length) {
        return undefined;
      }

      const result = [];
      keys.forEach((k, index) => {
        _set(result, index, values[k.trim()]);
      });

      return result.filter(Boolean).length ? result : undefined;
    }
  }
}

function tryConstructObject(field: string, values: Recordable = {}): Recordable | undefined {
  const pattern = /^\{(.+)\}$/;
  if (pattern.test(field)) {
    const match = field.match(pattern);
    if (match && match[1]) {
      const keys = match[1].split(',');
      if (!keys.length) {
        return;
      }

      const result = {};
      keys.forEach(k => {
        _set(result, k.trim(), values[k.trim()]);
      });

      return Object.values(result).filter(Boolean).length ? result : undefined;
    }
  }
}

export function useFormEvents({
  emit,
  getProps,
  formModel,
  getSchema,
  defaultValueRef,
  formElRef,
  schemaRef,
  handleFormValues,
}: UseFormActionContext) {
  async function resetFields(): Promise<void> {
    const { resetFunc, submitOnReset } = unref(getProps);
    resetFunc && _isFunction(resetFunc) && (await resetFunc());

    const formEl = unref(formElRef);
    if (!formEl) return;

    Object.keys(formModel).forEach(key => {
      const schema = unref(getSchema).find(item => item.field === key);
      const defaultValueObj = schema?.defaultValueObj;
      const fieldKeys = Object.keys(defaultValueObj || {});
      if (fieldKeys.length) {
        fieldKeys.map(field => {
          formModel[field] = defaultValueObj![field];
        });
      }
      formModel[key] = getDefaultValue(schema, defaultValueRef, key);
    });
    nextTick(() => clearValidate());

    emit('reset', toRaw(formModel));
    submitOnReset && handleSubmit();
  }

  // 获取表单fields
  const getAllFields = () =>
    unref(getSchema)
      .map(item => [...(item.fields || []), item.field])
      .flat(1)
      .filter(Boolean);

  /**
   * @description: Set form value
   */
  async function setFieldsValue(values: Recordable): Promise<void> {
    if (Object.keys(values).length === 0) {
      return;
    }
    const fields = getAllFields();

    // key 支持 a.b.c 的嵌套写法
    const delimiter = '.';
    const nestKeyArray = fields.filter(item => String(item).indexOf(delimiter) >= 0);

    const validKeys: string[] = [];
    fields.forEach(key => {
      const schema = unref(getSchema).find(item => item.field === key);
      let value = _get(values, key);
      if (!(values instanceof Object)) {
        return;
      }

      const hasKey = Reflect.has(values, key);

      value = handleInputNumberValue(schema?.component, value);
      const { componentProps } = schema || {};
      let _props = componentProps as any;
      if (typeof componentProps === 'function') {
        _props = _props({
          formModel: unref(formModel),
          formActionType,
        });
      }

      const constructValue = tryConstructArray(key, values) || tryConstructObject(key, values);
      // 0| '' is allow
      if (hasKey || !!constructValue) {
        const fieldValue = constructValue || value;
        // time type
        if (itemIsDateType(key)) {
          if (Array.isArray(fieldValue)) {
            const arr: any[] = [];
            for (const ele of fieldValue) {
              arr.push(ele ? dateUtil(ele) : null);
            }
            unref(formModel)[key] = arr;
          } else {
            unref(formModel)[key] = fieldValue ? (_props?.valueFormat ? fieldValue : dateUtil(fieldValue)) : null;
          }
        } else {
          unref(formModel)[key] = fieldValue;
        }
        if (_props?.onChange) {
          _props?.onChange(fieldValue);
        }
        validKeys.push(key);
      } else {
        nestKeyArray.forEach((nestKey: string) => {
          try {
            const value = nestKey.split('.').reduce((out, item) => out[item], values);
            if (isDef(value)) {
              unref(formModel)[nestKey] = unref(value);
              validKeys.push(nestKey);
            }
          } catch (e) {
            // key not exist
            if (isDef(defaultValueRef.value[nestKey])) {
              unref(formModel)[nestKey] = _cloneDeep(unref(defaultValueRef.value[nestKey]));
            }
          }
        });
      }
    });
    validateFields(validKeys).catch(_ => {});
  }

  async function removeSchemaByField(fields: string | string[]): Promise<void> {
    const schemaList: FormSchema[] = _cloneDeep(unref(getSchema));
    if (!fields) {
      return;
    }

    let fieldList: string[] = _isString(fields) ? [fields] : fields;
    if (_isString(fields)) {
      fieldList = [fields];
    }
    for (const field of fieldList) {
      _removeSchemaByField(field, schemaList);
    }
    schemaRef.value = schemaList;
  }

  function _removeSchemaByField(field: string, schemaList: FormSchema[]): void {
    if (_isString(field)) {
      const index = schemaList.findIndex(schema => schema.field === field);
      if (index !== -1) {
        delete formModel[field];
        schemaList.splice(index, 1);
      }
    }
  }

  async function appendSchemaByField(schema: FormSchema | FormSchema[], prefixField?: string, first = false) {
    const schemaList: FormSchema[] = _cloneDeep(unref(getSchema));
    const addSchemaIds: string[] = Array.isArray(schema) ? schema.map(item => item.field) : [schema.field];
    if (schemaList.find(item => addSchemaIds.includes(item.field))) {
      const log = useLog();
      log.prettyError('There are schemas that have already been added');
      return;
    }

    const index = schemaList.findIndex(schema => schema.field === prefixField);
    const _schemaList = _isObject(schema) ? [schema as FormSchema] : (schema as FormSchema[]);
    const hasInList = schemaList.some(item => item.field === prefixField || (schema as FormSchema).field);
    if (!hasInList) return;

    if (!prefixField || index === -1 || first) {
      first ? schemaList.unshift(..._schemaList) : schemaList.push(..._schemaList);
    } else if (index !== -1) {
      schemaList.splice(index + 1, 0, ..._schemaList);
    }

    schemaRef.value = schemaList;
    _setDefaultValue(schema);
  }

  async function resetSchema(data: Partial<FormSchema> | Partial<FormSchema>[]) {
    let updateData: Partial<FormSchema>[] = [];
    if (_isObject(data)) {
      updateData.push(data as FormSchema);
    }
    if (_isArray(data)) {
      updateData = [...data];
    }

    const hasField = updateData.every(item => isIncludeSimpleComponents(item.component) || (Reflect.has(item, 'field') && item.field));

    if (!hasField) {
      const log = useLog();
      log.prettyError('All children of the form Schema array that need to be updated must contain the `field` field');
      return;
    }
    schemaRef.value = updateData as FormSchema[];
  }

  async function updateSchema(data: Partial<FormSchema> | Partial<FormSchema>[]) {
    let updateData: Partial<FormSchema>[] = [];
    if (_isObject(data)) {
      updateData.push(data as FormSchema);
    }
    if (_isArray(data)) {
      updateData = [...data];
    }

    const hasField = updateData.every(item => isIncludeSimpleComponents(item.component) || (Reflect.has(item, 'field') && item.field));

    if (!hasField) {
      const log = useLog();
      log.prettyError('All children of the form Schema array that need to be updated must contain the `field` field');
      return;
    }
    const schema: FormSchema[] = [];
    const updatedSchema: FormSchema[] = [];
    unref(getSchema).forEach(val => {
      const updatedItem = updateData.find(item => val.field === item.field);
      if (updatedItem) {
        const newSchema = deepMerge(val, updatedItem);
        updatedSchema.push(newSchema as FormSchema);
        schema.push(newSchema as FormSchema);
      } else {
        schema.push(val);
      }
    });
    _setDefaultValue(updatedSchema);

    schemaRef.value = _uniqBy(schema, 'field');
  }

  function _setDefaultValue(data: FormSchema | FormSchema[]) {
    let schemas: FormSchema[] = [];
    if (_isObject(data)) {
      schemas.push(data as FormSchema);
    }
    if (_isArray(data)) {
      schemas = [...data];
    }

    const obj: Recordable = {};
    const currentFieldsValue = getFieldsValue();
    schemas.forEach(item => {
      if (
        !isIncludeSimpleComponents(item.component) &&
        Reflect.has(item, 'field') &&
        item.field &&
        !isNil(item.defaultValue) &&
        (!(item.field in currentFieldsValue) || isNil(currentFieldsValue[item.field]))
      ) {
        obj[item.field] = item.defaultValue;
      }
    });
    setFieldsValue(obj);
  }

  function getFieldsValue(): Recordable {
    const formEl = unref(formElRef);
    if (!formEl) return {};
    return handleFormValues(toRaw(unref(formModel)));
  }

  function itemIsDateType(key: string) {
    return unref(getSchema).some(item => {
      return item.field === key && item.component ? dateItemType.includes(item.component) : false;
    });
  }

  async function validateFields(nameList?: NamePath[] | undefined, options?: ValidateOptions) {
    const values = await unref(formElRef)?.validateFields(nameList, options);
    return handleFormValues(values);
  }

  async function setProps(formProps: Partial<FormProps>): Promise<void> {
    await unref(formElRef)?.setProps(formProps);
  }

  async function validate(nameList?: NamePath[] | false | undefined) {
    let _nameList: any;
    if (nameList === undefined) {
      _nameList = getAllFields();
    } else {
      _nameList = nameList === Array.isArray(nameList) ? nameList : undefined;
    }
    const values = await unref(formElRef)?.validate(_nameList);
    return handleFormValues(values);
  }

  async function clearValidate(name?: string | string[]) {
    await unref(formElRef)?.clearValidate(name);
  }

  async function scrollToField(name: NamePath, options?: ScrollOptions | undefined) {
    await unref(formElRef)?.scrollToField(name, options);
  }

  async function handleSubmit(e?: Event): Promise<void> {
    e && e.preventDefault();
    const { submitFunc } = unref(getProps);
    if (submitFunc && _isFunction(submitFunc)) {
      await submitFunc();
      return;
    }
    const formEl = unref(formElRef);
    if (!formEl) return;
    try {
      const values = await validate();
      for (const key in values) {
        if (values[key] instanceof Array) {
          const valueType = getValueType(getProps, key);
          if (valueType === 'string') {
            values[key] = values[key].join(',');
          }
        }
      }
      emit('submit', values);
    } catch (error: any) {
      if (error?.outOfDate === false && error?.errorFields) {
        return;
      }
      emit('submit', {});
      const log = useLog();
      log.prettyError('query form validate error, please ignore!', error);
      throw new Error(error);
    }
  }

  const formActionType: Partial<FormActionType> = {
    getFieldsValue,
    setFieldsValue,
    resetFields,
    updateSchema,
    resetSchema,
    setProps,
    removeSchemaByField,
    appendSchemaByField,
    clearValidate,
    validateFields,
    validate,
    submit: handleSubmit,
    scrollToField,
  };

  return {
    handleSubmit,
    clearValidate,
    validate,
    validateFields,
    getFieldsValue,
    updateSchema,
    resetSchema,
    appendSchemaByField,
    removeSchemaByField,
    resetFields,
    setFieldsValue,
    scrollToField,
  };
}
function getDefaultValue(schema: FormSchema | undefined, defaultValueRef: UseFormActionContext['defaultValueRef'], key: string) {
  let defaultValue = _cloneDeep(defaultValueRef.value[key]);
  const isInput = checkIsInput(schema);
  if (isInput) {
    return defaultValue || undefined;
  }
  if (!defaultValue && schema && checkIsRangeSlider(schema)) {
    defaultValue = [0, 0];
  }
  if (!defaultValue && schema && schema.component === 'ApiTree') {
    defaultValue = [];
  }
  return defaultValue;
}

function checkIsRangeSlider(schema: FormSchema) {
  if (schema.component === 'Slider' && schema.componentProps && 'range' in schema.componentProps) {
    return true;
  }
}

function checkIsInput(schema?: FormSchema) {
  return schema?.component && defaultValueComponents.includes(schema.component);
}
