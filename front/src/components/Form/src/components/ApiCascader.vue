<template>
  <a-cascader
    v-model:value="state"
    :options="options"
    :load-data="loadData"
    change-on-select
    @change="handleChange"
    :displayRender="handleRenderDisplay"
  >
    <template #suffixIcon v-if="loading">
      <Icon icon="ant-design:loading-outlined" spin />
    </template>
    <template #notFoundContent v-if="loading">
      <span>
        <Icon icon="ant-design:loading-outlined" spin class="mr-1" />
        {{ t('component.form.apiSelectNotFound') }}
      </span>
    </template>
  </a-cascader>
</template>
<script lang="ts" setup>
import { type Recordable } from '#/utils.d';
import type { CascaderProps } from 'ant-design-vue';
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { useI18n } from '@/hooks/web/useI18nOut';

interface Option {
  value?: string;
  label?: string;
  loading?: boolean;
  isLeaf?: boolean;
  children?: Option[];
  [key: string]: any;
}

defineOptions({ name: 'ApiCascader' });

const props = defineProps({
  value: {
    type: Array,
  },
  api: {
    type: Function as PropType<(arg?: any) => Promise<Option[]>>,
    default: null,
  },
  numberToString: {
    type: Boolean,
  },
  resultField: {
    type: String,
    default: '',
  },
  labelField: {
    type: String,
    default: 'label',
  },
  valueField: {
    type: String,
    default: 'value',
  },
  childrenField: {
    type: String,
    default: 'children',
  },
  apiParamKey: {
    type: String,
    default: 'parentCode',
  },
  immediate: {
    type: Boolean,
    default: true,
  },
  // init fetch params
  initFetchParams: {
    type: Object as PropType<Recordable<any>>,
    default: () => ({}),
  },
  // 是否有下级，默认是
  isLeaf: {
    type: Function as PropType<(arg: Recordable<any>) => boolean>,
    default: null,
  },
  displayRenderArray: {
    type: Array,
  },
});

const emit = defineEmits(['change', 'defaultChange']);

const { t } = useI18n();

const apiData = ref<any[]>([]);
const options = ref<Option[]>([]);
const loading = ref<boolean>(false);
const emitData = ref<any[]>([]);
const isFirstLoad = ref(true);
const [state]: any = useRuleFormItem(props, 'value', 'change', emitData);

watch(
  apiData,
  data => {
    const opts = generatorOptions(data);
    options.value = opts;
  },
  { deep: true },
);

function generatorOptions(options: any[]): Option[] {
  const { labelField, valueField, numberToString, childrenField, isLeaf } = props;
  return options.reduce((prev, next: Recordable<any>) => {
    if (next) {
      const value = next[valueField];
      const item = {
        ..._omit(next, [labelField, valueField]),
        label: next[labelField],
        value: numberToString ? `${value}` : value,
        isLeaf: isLeaf && typeof isLeaf === 'function' ? isLeaf(next) : false,
      };
      const children = Reflect.get(next, childrenField);
      if (children) {
        Reflect.set(item, childrenField, generatorOptions(children));
      }
      prev.push(item);
    }
    return prev;
  }, [] as Option[]);
}

async function initialFetch() {
  const api = props.api;
  if (!api || !_isFunction(api)) return;
  apiData.value = [];
  loading.value = true;
  try {
    const res = await api(props.initFetchParams);
    if (Array.isArray(res)) {
      apiData.value = res;
      return;
    }
    if (props.resultField) {
      apiData.value = _get(res, props.resultField) || [];
    }
  } catch (error) {
    console.warn(error);
  } finally {
    loading.value = false;
  }
}

const loadData: CascaderProps['loadData'] = async selectedOptions => {
  const targetOption = selectedOptions[selectedOptions.length - 1];
  targetOption.loading = true;
  const api = props.api;
  if (!api || !_isFunction(api)) return;
  try {
    const res = await api({
      [props.apiParamKey]: Reflect.get(targetOption, 'value'),
    });
    if (Array.isArray(res)) {
      const children = generatorOptions(res);
      targetOption.children = children;
      return;
    }
    if (props.resultField) {
      const children = generatorOptions(_get(res, props.resultField) || []);
      targetOption.children = children;
    }
  } catch (e) {
    console.error(e);
  } finally {
    targetOption.loading = false;
  }
};

watch(
  () => props.immediate,
  () => {
    props.immediate && initialFetch();
  },
  {
    immediate: true,
  },
);

watch(
  () => props.initFetchParams,
  () => {
    !unref(isFirstLoad) && initialFetch();
  },
  { deep: true },
);

function handleChange(keys, args) {
  emitData.value = args;
  emit('defaultChange', keys, args);
}

const handleRenderDisplay: CascaderProps['displayRender'] = ({ labels, selectedOptions }) => {
  if (unref(emitData).length === selectedOptions?.length) {
    return labels.join(' / ');
  }
  if (props.displayRenderArray) {
    return props.displayRenderArray.join(' / ');
  }
  return '';
};
</script>
