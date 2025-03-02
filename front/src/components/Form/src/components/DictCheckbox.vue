<template>
  <a-checkbox-group v-bind="attrs" v-model:value="checkboxArray" :options="checkOptions" @change="handleChange">
    <template #label="{ label, value }">
      <span :class="[useDicColor && getDicColor(value) ? 'colorText' : '']" :style="{ backgroundColor: `${getDicColor(value)}` }">{{
        label
      }}</span>
    </template>
  </a-checkbox-group>
</template>
<script lang="ts" setup>
defineOptions({ name: 'DictCheckbox' });

const props = defineProps({
  value: {
    type: [Array, String],
  },
  dictCode: {
    type: String,
  },
  useDicColor: {
    type: Boolean,
    default: false,
  },
  options: {
    type: Array,
    default: () => [],
  },
  initDictOptions: {
    type: Function,
    default: null,
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
    checkboxArray.value = (temp as string).split(',');
  }
  props.value && (checkboxArray.value = props.value ? (props.value as string).split(',') : []);
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
    const dictData = await props.initDictOptions(props.dictCode);
    checkOptions.value = dictData.reduce((prev, next) => {
      if (next) {
        const value = next['value'];
        prev.push({
          label: next['text'],
          value: value,
          color: next['color'],
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

const getDicColor = value => {
  if (props.useDicColor) {
    const findItem = checkOptions.value.find(item => item.value == value);
    if (findItem) {
      return findItem.color;
    }
  }
  return null;
};
</script>
<style scoped>
.colorText {
  display: inline-block;
  height: 20px;
  line-height: 20px;
  padding: 0 6px;
  border-radius: 8px;
  background-color: red;
  color: #fff;
  font-size: 12px;
}
</style>
