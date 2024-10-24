<template class="inline-block">
  <a-select
    @dropdown-visible-change="handleFetch"
    v-bind="attrs"
    @change="handleChange"
    :options="getOptions"
    :show-search="showSearch"
    v-model:value="state"
  >
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
    <template #suffixIcon v-if="loading">
      <Icon icon="ant-design:loading-outlined" spin />
    </template>
    <template #notFoundContent v-if="loading">
      <span>
        <Icon icon="ant-design:loading-outlined" spin class="mr-1" />
        {{ t('component.form.apiSelectNotFound') }}
      </span>
    </template>
  </a-select>
</template>
<script lang="ts" setup>
import type { SelectValue } from 'ant-design-vue/es/select';
import type { Recordable } from '#/utils';
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { useI18n } from '@/hooks/web/useI18nOut';

type OptionsItem = { label: string; value: string; disabled?: boolean; [name: string]: any };

defineOptions({ name: 'ApiSelect', inheritAttrs: false });

const props = defineProps({
  value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
  api: {
    type: Function as PropType<(arg?: any) => Promise<any[]>>,
    default: null,
  },
  // api params
  params: {
    type: Object,
    default: () => ({}),
  },
  searchParams: {
    type: Object,
    default: () => ({}),
  },
  showSearch: {
    type: Boolean,
    default: false,
  },
  // support xxx.xxx.xx
  resultField: {
    type: String,
    default: '',
  },
  immediate: {
    type: Boolean,
    default: true,
  },
  alwaysLoad: {
    type: Boolean,
    default: false,
  },
  showAdd: {
    type: Boolean,
    default: false,
  },
  options: {
    type: Array,
    default: [],
  },
});

const emit = defineEmits(['options-change', 'change', 'update:value', 'show-add']);

const optionsRef = ref<any[]>([]);

const loading = ref(false);
// 首次是否加载过了
const isFirstLoaded = ref(false);
const emitData = ref<any[]>([]);
const { t } = useI18n();
const attrs = useAttrs();

if (props.showSearch) {
  attrs['onSearch'] = searchHandle;
}

const [state, setState] = useRuleFormItem(props, 'value', 'change', emitData);

const searchKeyword = ref('');
const searchParamValues = computed(() => {
  const res: Recordable = { ...props.searchParams };
  Object.keys(props.searchParams)
    .filter(param => param !== 'useOr')
    .forEach(key => {
      if (!res[key]) {
        res[key] = searchKeyword.value;
      }
    });
  return res;
});

const getOptions = computed(() => {
  const { fieldNames } = attrs;
  const labelField = fieldNames?.label || 'label';
  const valueField = fieldNames?.value || 'value';
  let data = unref(optionsRef).reduce((prev, next: any) => {
    if (next) {
      const value = _get(next, valueField);
      prev.push({
        ...next,
        originOption: next,
        label: _get(next, labelField),
        value: value,
      });
    }
    return prev;
  }, [] as OptionsItem[]);
  return data.length > 0 ? data : props.options;
});

watch(
  () => state.value,
  v => {
    emit('update:value', v);
  },
);

watch(
  () => props.params,
  (value, oldValue) => {
    if (_isEqual(value, oldValue)) return;
    fetch();
  },
  { deep: true, immediate: props.immediate },
);

watch(
  () => searchKeyword.value,
  () => {
    !unref(isFirstLoaded) && fetch();
  },
  { deep: true },
);

async function fetch() {
  const api = props.api;
  if (!api || !_isFunction(api) || loading.value) return;
  optionsRef.value = [];
  try {
    loading.value = true;
    const res = await api({ ...props.params, ...unref(searchParamValues) });
    isFirstLoaded.value = true;
    if (Array.isArray(res)) {
      optionsRef.value = res;
      emitChange();
      return;
    }
    if (props.resultField) {
      optionsRef.value = _get(res, props.resultField) || [];
    }
    emitChange();
  } catch (error) {
    console.warn(error);
    // reset status
    isFirstLoaded.value = false;
  } finally {
    loading.value = false;
    unref(attrs).mode == 'multiple' && !Array.isArray(unref(state)) && setState([]);
    initValue();
  }
}

async function handleFetch(visible: boolean) {
  if (visible) {
    if (props.alwaysLoad) {
      await fetch();
    } else if (!props.immediate && !unref(isFirstLoaded)) {
      await fetch();
    }
  }
}

function emitChange() {
  emit('options-change', unref(getOptions));
}

function searchHandle(value) {
  searchKeyword.value = value;
  fetch();
}

function handleChange(_, ...args) {
  if (unref(attrs).labelInValue) {
    if (_isArray(_)) {
      _.forEach(item => {
        Object.assign(item, item.option);
      });
    } else {
      Object.assign(_, _.option);
    }
  }
  emitData.value = args;
}

function initValue() {
  let value = props.value;
  if (value && typeof value === 'string' && value !== 'null' && value !== 'undefined') {
    state.value = value.split(',');
  }
  if (unref(attrs).labelInValue) {
    const { valueField, labelField } = props;
    if (_isArray(value)) {
      value.forEach(item => {
        item.value = item[valueField];
        item.label = item[labelField];
      });
    } else if (value) {
      value['value'] = value[valueField];
      value['label'] = value[labelField];
    }
  }
}
</script>
