<template>
  <a-cascader v-bind="attrs" :value="cascaderValue" :options="getOptions" @change="handleChange" />
</template>
<script lang="ts" setup>
import { provinceAndCityData, regionData, provinceAndCityDataPlus, regionDataPlus } from '../utils/areaDataUtil';
import { useAttrs } from '@/hooks/vben/useAttrs';

defineOptions({
  name: 'AreaLinkage',
  inheritAttrs: false,
});

const props = defineProps({
  value: {
    type: [String, Array, Object],
  },
  //是否显示区县
  showArea: {
    type: Boolean,
    default: true,
  },
  //是否是全部
  showAll: {
    type: Boolean,
    default: false,
  },
  saveCode: {
    type: String as PropType<'province' | 'city' | 'region' | 'all'>,
    default: 'all',
  },
});

const emit = defineEmits(['options-change', 'change']);

const emitData = ref<any[]>([]);
const attrs = useAttrs();
const cascaderValue = ref([]);
const getOptions = computed(() => {
  if (props.showArea && props.showAll) {
    return regionDataPlus;
  }
  if (props.showArea && !props.showAll) {
    return regionData;
  }
  if (!props.showArea && !props.showAll) {
    return provinceAndCityData;
  }
  if (!props.showArea && props.showAll) {
    return provinceAndCityDataPlus;
  }
});
/**
 * 监听value变化
 */
watchEffect(() => {
  if (props.value) {
    initValue();
  } else {
    cascaderValue.value = [];
  }
});

/**
 * 将字符串值转化为数组
 */
function initValue() {
  let value = props.value ? props.value : [];
  if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
    const arr = value.split(',');
    cascaderValue.value = transform(arr);
  } else if (_isArray(value)) {
    if (value.length) {
      cascaderValue.value = transform(value);
    } else {
      cascaderValue.value = [];
    }
  }
}

function transform(arr) {
  let result: any = [];
  if (props.saveCode === 'region') {
    // 81 香港、82 澳门
    const regionCode = arr[0];
    if (['82', '81'].includes(regionCode.substring(0, 2))) {
      result = [`${regionCode.substring(0, 2)}0000`, regionCode];
    } else {
      result = [`${regionCode.substring(0, 2)}0000`, `${regionCode.substring(0, 2)}${regionCode.substring(2, 4)}00`, regionCode];
    }
  } else if (props.saveCode === 'city') {
    const cityCode = arr[0];
    result = [`${cityCode.substring(0, 2)}0000`, cityCode];
  } else if (props.saveCode === 'province') {
    const provinceCode = arr[0];
    result = [provinceCode];
  } else {
    result = arr;
  }
  return result;
}

function handleChange(arr) {
  if (arr?.length) {
    let result: any = [];
    if (props.saveCode === 'region') {
      // 可能只有两位（选择香港时，只有省区）
      result = [arr[arr.length - 1]];
    } else if (props.saveCode === 'city') {
      result = [arr[1]];
    } else if (props.saveCode === 'province') {
      result = [arr[0]];
    } else {
      result = arr;
    }
    emit('change', result);
    emit('update:value', result);
  } else {
    emit('change', arr);
    emit('update:value', arr);
  }
}
</script>
