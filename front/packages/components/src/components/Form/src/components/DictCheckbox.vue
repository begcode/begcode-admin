<template>
  <CheckboxGroup v-bind="attrs" v-model:value="checkboxArray" :options="checkOptions" @change="handleChange"></CheckboxGroup>
</template>

<script lang="ts" setup>
import { useAttrs, watchEffect, ref } from 'vue';
import { CheckboxGroup } from 'ant-design-vue';
import { propTypes } from '@/utils/propTypes';
import { initDictOptions } from '@/utils/dict/index';

defineOptions({ name: 'DictCheckbox' });

const props = defineProps({
  value: propTypes.oneOfType([propTypes.string, propTypes.number]),
  dictCode: propTypes.string,
  options: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(['change', 'update:value']);

const attrs = useAttrs();
//checkbox选项
const checkOptions = ref<any[]>([]);
//checkbox数值
const checkboxArray = ref<any[]>([]);
/**
 * 监听value
 */
watchEffect(() => {
  let temp = props.value;
  if (!temp && temp !== 0) {
    checkboxArray.value = [];
  } else {
    temp = temp + '';
    checkboxArray.value = temp.split(',');
  }
  props.value && (checkboxArray.value = props.value ? props.value.split(',') : []);
  if (props.value === '' || props.value === undefined) {
    checkboxArray.value = [];
  }
});
/**
 * 监听字典code
 */
watchEffect(() => {
  props && initOptions();
});

/**
 * 初始化选项
 */
async function initOptions() {
  //根据options, 初始化选项
  if (props.options && props.options.length > 0) {
    checkOptions.value = props.options;
    return;
  }
  //根据字典Code, 初始化选项
  if (props.dictCode) {
    const dictData = await initDictOptions(props.dictCode);
    checkOptions.value = dictData.reduce((prev, next) => {
      if (next) {
        const value = next['value'];
        prev.push({
          label: next['text'],
          value: value,
        });
      }
      return prev;
    }, []);
  }
}

/**
 * change事件
 * @param $event
 */
function handleChange($event) {
  emit('update:value', $event.join(','));
  emit('change', $event.join(','));
}
</script>
