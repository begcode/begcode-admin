<template>
  <TreeSelect v-bind="getAttrs" @change="handleChange" :load-data="async ? onLoadData : undefined">
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
    <template #suffixIcon v-if="loading">
      <LoadingOutlined spin />
    </template>
  </TreeSelect>
</template>

<script lang="ts" setup>
import { type Recordable } from '#/utils.d';
import { type PropType, computed, watch, ref, onMounted, unref, useAttrs } from 'vue';
import { TreeSelect } from 'ant-design-vue';
import { get, isArray, isFunction } from 'lodash-es';
import { propTypes } from '@/utils/propTypes';
import { LoadingOutlined } from '@ant-design/icons-vue';

defineOptions({ name: 'ApiTreeSelect' });

const props = defineProps({
  value: [Array, Object, String, Number],
  api: { type: Function as PropType<(arg?: Recordable<any>) => Promise<Recordable<any>>> },
  params: { type: Object },
  immediate: { type: Boolean, default: true },
  async: { type: Boolean, default: false },
  resultField: propTypes.string.def(''),
  numberToString: propTypes.bool.def(false),
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
    if (result.fieldNames && result.fieldNames.value) {
      if (Array.isArray(props.value)) {
        props.value.forEach(item => {
          item.value = item[result.fieldNames.value] + '';
        });
      } else if (props.value) {
        props.value['value'] = props.value[result.fieldNames.value] + '';
      }
    }
    if (result.fieldNames && result.fieldNames.label) {
      if (Array.isArray(props.value)) {
        props.value.forEach(item => {
          item.label = item[result.fieldNames.label];
        });
      } else if (props.value) {
        props.value['label'] = props.value[result.fieldNames.label];
      }
    }
  }
  result.value = props.value;
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

function onLoadData(treeNode) {
  return new Promise((resolve: (value?: unknown) => void) => {
    if (isArray(treeNode.children) && treeNode.children.length > 0) {
      resolve();
      return;
    }
    emit('load-data', { treeData, treeNode, resolve });
  });
}

async function fetch() {
  const { api } = props;
  if (!api || !isFunction(api) || loading.value) return;
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
  if (!isArray(result)) {
    result = get(result, props.resultField);
  }
  if (props.numberToString) {
    result = numberToString(result);
  }
  treeData.value = (result as Recordable<any>[]) || [];
  isFirstLoaded.value = true;
  emit('options-change', treeData.value);
}

function numberToString(value) {
  if (Array.isArray(value)) {
    return value.map(item => {
      const valueName = (getAttrs.value.fieldNames as any)?.value ?? 'value';
      const result = { ...item, [valueName]: item[valueName] + '' };
      const childrenName = (getAttrs.value.fieldNames as any)?.children ?? 'children';
      if (result[childrenName] && result[childrenName].length > 0) {
        result[childrenName] = numberToString(item[childrenName]);
      }
      return result;
    });
  } else {
    return value + '';
  }
}
</script>
