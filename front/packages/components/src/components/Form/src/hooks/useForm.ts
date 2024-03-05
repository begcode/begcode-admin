import type { NamePath, ValidateOptions } from 'ant-design-vue/lib/form/interface';
import { ref, onUnmounted, unref, nextTick, watch } from 'vue';
import type { UnwrapRef } from 'vue';
import type { FormProps, FormActionType, UseFormReturnType, FormSchemaInner as FormSchema } from '../types/form';
import { handleRangeValue } from '../utils/formUtils';
import type { DynamicProps, Nullable } from '#/types';
import type { Recordable } from '#/utils';
import { useLog } from '@/hooks/useLog';
import { getDynamicProps, getValueType, isProdMode } from '@/utils';

export declare type ValidateFields = (nameList?: NamePath[], options?: ValidateOptions) => Promise<Recordable>;

type Props = Partial<DynamicProps<FormProps>>;

export function useForm(props?: Props): UseFormReturnType {
  const formRef = ref<Nullable<FormActionType>>(null);
  const loadedRef = ref<Nullable<boolean>>(false);

  async function getForm() {
    const form = unref(formRef);
    if (!form) {
      const log = useLog();
      log.prettyError(
        'The form instance has not been obtained, please make sure that the form has been rendered when performing the form operation!',
      );
    }
    await nextTick();
    return form as FormActionType;
  }

  function register(instance: FormActionType) {
    isProdMode() &&
      onUnmounted(() => {
        formRef.value = null;
        loadedRef.value = null;
      });
    if (unref(loadedRef) && isProdMode() && instance === unref(formRef)) return;

    // formRef.value = (instance as Ref<Nullable<FormActionType>>);
    formRef.value = instance as UnwrapRef<Nullable<FormActionType>>;
    loadedRef.value = true;

    watch(
      () => props,
      () => {
        props && instance.setProps(getDynamicProps(props));
      },
      {
        immediate: true,
        deep: true,
      },
    );
  }

  const methods: FormActionType = {
    submit: async (): Promise<any> => {
      const form = await getForm();
      return form.submit();
    },

    setFieldsValue: async <T extends Recordable<any>>(values: T) => {
      const form = await getForm();
      form.setFieldsValue(values);
    },

    resetFields: async () => {
      getForm().then(async form => {
        await form.resetFields();
      });
    },

    // TODO promisify
    getFieldsValue: <T>() => {
      let values = unref(formRef)?.getFieldsValue() as T;
      if (values) {
        Object.keys(values).map(key => {
          if (values[key] instanceof Array) {
            let isObject = typeof (values[key][0] || '') === 'object';
            if (!isObject) {
              values[key] = values[key].join(',');
            }
          }
        });
      }
      return values;
    },

    scrollToField: async (name: NamePath, options?: ScrollOptions | undefined) => {
      const form = await getForm();
      form.scrollToField(name, options);
    },
    setProps: async (formProps: Partial<FormProps>) => {
      const form = await getForm();
      form.setProps(formProps);
    },

    updateSchema: async (data: Partial<FormSchema> | Partial<FormSchema>[]) => {
      const form = await getForm();
      form.updateSchema(data);
    },

    resetSchema: async (data: Partial<FormSchema> | Partial<FormSchema>[]) => {
      const form = await getForm();
      form.resetSchema(data);
    },

    clearValidate: async (name?: string | string[]) => {
      const form = await getForm();
      form.clearValidate(name);
    },

    removeSchemaByField: async (field: string | string[]) => {
      unref(formRef)?.removeSchemaByField(field);
    },

    appendSchemaByField: async (schema: FormSchema | FormSchema[], prefixField: string | undefined, first?: boolean) => {
      const form = await getForm();
      form.appendSchemaByField(schema, prefixField, first);
    },

    /**
     * 表单验证并返回表单值
     * @update:添加表单值转换逻辑
     */
    validate: async <T = Recordable>(nameList?: NamePath[] | false): Promise<T> => {
      const form = await getForm();
      let getProps = props || form.getProps;
      let values = form.validate(nameList).then(values => {
        for (let key in values) {
          if (values[key] instanceof Array) {
            let valueType = getValueType(getProps, key);
            if (valueType === 'string') {
              values[key] = values[key].join(',');
            }
          }
        }
        return handleRangeValue(getProps, values);
      });
      return values;
    },

    validateFields: async (nameList?: NamePath[], options?: ValidateOptions): Promise<Recordable> => {
      const form = await getForm();
      return form.validateFields(nameList, options);
    },
  };

  return [register, methods];
}
