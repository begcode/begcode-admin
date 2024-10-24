<template>
  <a-tree-select v-bind="getAttrs" @change="handleChange" :load-data="async ? onLoadData : undefined">
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
    <template #suffixIcon v-if="loading">
      <Icon icon="ant-design:loading-outlined" spin />
    </template>
  </a-tree-select>
</template>

<script lang="ts" setup>
import { type Recordable } from '#/utils.d';

defineOptions({ name: 'ApiTreeSelect' });

const props = defineProps({
  api: { type: Function as PropType<(arg?: any) => Promise<Recordable<any>>> },
  params: { type: Object },
  immediate: { type: Boolean, default: true },
  async: { type: Boolean, default: false },
  resultField: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['options-change', 'change', 'load-data']);

const attrs = useAttrs();
const treeData = ref<Recordable<any>[]>([]);
const isFirstLoaded = ref<Boolean>(false);
const loading = ref(false);
const getAttrs = computed(() => {
  const result: any = {
    ...(props.api ? { treeData: unref(treeData) } : {}),
    ...attrs,
  };
  if (result.labelInValue) {
    if (!result.value) {
      result.value = {};
    } else {
      const { value = 'value', label = 'label' } = result.fieldNames || {};
      if (Array.isArray(result.value)) {
        result.value.forEach(item => {
          value !== 'value' && (item.value = item[value]);
          label !== 'label' && (item.label = item[label]);
        });
      } else if (result.value) {
        value !== 'value' && (result.value['value'] = result.value[value]);
        label !== 'label' && (result.value['label'] = result.value[label]);
      }
    }
  }
  return result;
});

function handleChange(...args) {
  // 需要还原为原始值
  const { value = 'value', label = 'label' } = getAttrs.value.fieldNames || {};
  if (getAttrs.value?.labelInValue) {
    if (args[0]) {
      if (value !== 'value') {
        if (Array.isArray(args[0])) {
          args[0].forEach(argItem => {
            argItem[value] = argItem.value;
          });
        } else {
          args[0][value] = args[0].value;
        }
      }
      if (label !== 'label') {
        if (Array.isArray(args[0])) {
          args[0].forEach(argItem => {
            argItem[label] = argItem.label;
          });
        } else {
          args[0][label] = args[0].label;
        }
      }
    }
  }
  emit('change', ...args);
}

watch(
  () => props.params,
  () => {
    unref(isFirstLoaded) && fetch();
  },
  { deep: true },
);

watch(
  () => props.immediate,
  v => {
    v && !isFirstLoaded.value && fetch();
  },
);

onMounted(() => {
  props.immediate && fetch();
});

function onLoadData(treeNode) {
  return new Promise((resolve: (value?: unknown) => void) => {
    if (_isArray(treeNode.children) && treeNode.children.length > 0) {
      resolve();
      return;
    }
    emit('load-data', { treeData, treeNode, resolve });
  });
}

async function fetch() {
  const { api } = props;
  if (!api || !_isFunction(api) || loading.value) return;
  loading.value = true;
  treeData.value = [];
  let result;
  try {
    result = await api(props.params);
  } catch (e) {
    console.error(e);
  }
  loading.value = false;
  if (!result) return;
  if (!_isArray(result)) {
    result = _get(result, props.resultField);
  }
  treeData.value = (result as Recordable<any>[]) || [];
  isFirstLoaded.value = true;
  emit('options-change', treeData.value);
}
</script>
