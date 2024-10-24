<template>
  <a-radio-group v-bind="attrs" v-model:value="state" button-style="solid">
    <template v-for="item in getOptions" :key="`${item.value}`">
      <a-radio-button v-if="props.isBtn" :value="item.value" :disabled="item.disabled" @click="handleClick(item)">
        {{ item.label }}
      </a-radio-button>
      <a-radio v-else :value="item.value" :disabled="item.disabled" @click="handleClick(item)">
        {{ item.label }}
      </a-radio>
    </template>
  </a-radio-group>
</template>
<script lang="ts" setup>
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { useAttrs } from '@/hooks/vben';

type OptionsItem = {
  label?: string;
  value?: string | number | boolean;
  disabled?: boolean;
  [key: string]: any;
};

defineOptions({ name: 'ApiRadioGroup' });

const props = defineProps({
  api: {
    type: Function as PropType<(arg?: any) => Promise<OptionsItem[]>>,
    default: null,
  },
  params: {
    type: [Object, String] as PropType<any | string>,
    default: () => ({}),
  },
  value: {
    type: [String, Number, Boolean] as PropType<string | number | boolean>,
  },
  isBtn: {
    type: [Boolean] as PropType<boolean>,
    default: false,
  },
  numberToString: {
    type: Boolean,
    default: false,
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
  immediate: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(['options-change', 'change', 'update:value']);

const options = ref<OptionsItem[]>([]);
const loading = ref(false);
const emitData = ref<any[]>([]);
const attrs = useAttrs();

// Embedded in the form, just use the hook binding to perform form verification
const [state] = useRuleFormItem(props, 'value', 'change', emitData);

// Processing options value
const getOptions = computed(() => {
  const { labelField, valueField, numberToString } = props;
  return unref(options).reduce((prev, next: any) => {
    if (next) {
      const value = next[valueField];
      prev.push({
        label: next[labelField],
        value: numberToString ? `${value}` : value,
        ..._omit(next, [labelField, valueField]),
      });
    }
    return prev;
  }, [] as OptionsItem[]);
});

watch(
  () => props.params,
  (value, oldValue) => {
    if (_isEqual(value, oldValue)) return;
    fetch();
  },
  { deep: true, immediate: props.immediate },
);

async function fetch() {
  const api = props.api;
  if (!api || !_isFunction(api)) return;
  options.value = [];
  try {
    loading.value = true;
    const res = await api(props.params);
    if (Array.isArray(res)) {
      options.value = res;
      emitChange();
      return;
    }
    if (props.resultField) {
      options.value = _get(res, props.resultField) || [];
    }
    emitChange();
  } catch (error) {
    console.warn(error);
  } finally {
    loading.value = false;
  }
}

function emitChange() {
  emit('options-change', unref(getOptions));
}
function handleClick(...args) {
  emitData.value = args;
}
</script>
