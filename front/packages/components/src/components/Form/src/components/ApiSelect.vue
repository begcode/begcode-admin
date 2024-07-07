<template class="inline-block">
  <Select
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
    <template #suffixIcon v-if="loading || showAdd">
      <LoadingOutlined spin v-if="loading" />
      <plus-outlined @click="emitAdd" v-if="showAdd" />
    </template>
    <template #notFoundContent v-if="loading">
      <span>
        <LoadingOutlined spin class="mr-1" />
        {{ t('component.form.apiSelectNotFound') }}
      </span>
    </template>
  </Select>
</template>
<script lang="ts" setup>
import { PropType, ref, computed, unref, watch, useAttrs } from 'vue';
import { Select } from 'ant-design-vue';
import type { SelectValue } from 'ant-design-vue/es/select';
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { get, isFunction, isEqual, isArray } from 'lodash-es';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { propTypes } from '@/utils/propTypes';
import type { Recordable } from '#/utils';
import { useI18n } from '@/hooks/web/useI18nOut';

type OptionsItem = { label: string; value: string; disabled?: boolean; [name: string]: any };

defineOptions({ name: 'ApiSelect', inheritAttrs: false });

const props = defineProps({
  value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
  api: {
    type: Function as PropType<(arg?: any) => Promise<OptionsItem[]>>,
    default: null,
  },
  // api params
  params: propTypes.any.def({}),
  searchParams: propTypes.any.def({}),
  showSearch: propTypes.bool.def(false),
  // support xxx.xxx.xx
  resultField: propTypes.string.def(''),
  labelField: propTypes.string.def('label'),
  valueField: propTypes.string.def('value'),
  immediate: propTypes.bool.def(true),
  alwaysLoad: propTypes.bool.def(false),
  showAdd: propTypes.bool.def(false),
  options: {
    type: Array<OptionsItem>,
    default: [],
  },
});

const emit = defineEmits(['options-change', 'change', 'update:value', 'show-add']);

const optionsRef = ref<OptionsItem[]>([]);

const loading = ref(false);
// 首次是否加载过了
const isFirstLoaded = ref(false);
const emitData = ref<OptionsItem[]>([]);
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
  const { labelField, valueField } = props;
  let data = unref(optionsRef).reduce((prev, next: any) => {
    if (next) {
      const value = get(next, valueField);
      prev.push({
        ...next,
        label: get(next, labelField),
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
    if (isEqual(value, oldValue)) return;
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
  if (!api || !isFunction(api) || loading.value) return;
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
      optionsRef.value = get(res, props.resultField) || [];
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

const emitAdd = () => {
  emit('show-add');
};
function handleChange(_, ...args) {
  if (unref(attrs).labelInValue) {
    if (isArray(_)) {
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
  if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
    state.value = value.split(',');
  }
  if (unref(attrs).labelInValue) {
    const { valueField, labelField } = props;
    if (isArray(value)) {
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
<style>
.inline-block {
  display: inline-block;
}
</style>
