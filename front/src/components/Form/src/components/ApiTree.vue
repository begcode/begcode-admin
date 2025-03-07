<template>
  <a-tree v-bind="getAttrs" v-model:selectedKeys="state">
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </a-tree>
</template>
<script lang="ts" setup>
import type { Recordable, AnyFunction } from '#/utils.d';
import type { TreeProps } from 'ant-design-vue';
import type { DataNode } from 'ant-design-vue/es/tree';

import { useRuleFormItem } from '@/hooks/component/useFormItem';

defineOptions({ name: 'ApiTree' });

const props = defineProps({
  api: { type: Function as PropType<(arg?: any) => Promise<Recordable<any>>> },
  params: { type: Object },
  immediate: { type: Boolean, default: true },
  resultField: { type: String, default: '' },
  afterFetch: { type: Function as PropType<AnyFunction> },
  value: {
    type: Array as PropType<TreeProps['selectedKeys']>,
  },
});

const emit = defineEmits(['options-change', 'change', 'update:value']);

const attrs = useAttrs();

const treeData = ref<DataNode[]>([]);
const isFirstLoaded = ref<Boolean>(false);
const loading = ref(false);
const emitData = ref<any[]>([]);
const [state] = useRuleFormItem(props, 'value', 'change', emitData);
const getAttrs = computed(() => {
  return {
    ...(props.api ? { treeData: unref(treeData) } : {}),
    ...attrs,
  };
});

watch(
  () => state.value,
  v => {
    emit('update:value', v);
  },
);

watch(
  () => props.params,
  () => {
    !unref(isFirstLoaded) && fetch();
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

async function fetch() {
  const { api, afterFetch } = props;
  if (!api || !_isFunction(api)) return;
  loading.value = true;
  treeData.value = [];
  let result;
  try {
    result = await api(props.params);
  } catch (e) {
    console.error(e);
  }
  if (afterFetch && _isFunction(afterFetch)) {
    result = afterFetch(result);
  }
  loading.value = false;
  if (!result) return;
  if (!_isArray(result)) {
    result = _get(result, props.resultField);
  }
  treeData.value = (result as (Recordable & { key: string | number })[]) || [];
  isFirstLoaded.value = true;
  emit('options-change', treeData.value);
}
</script>
