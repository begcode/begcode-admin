<template>
  <div :class="prefixCls">
    <span> {{ title }}</span>
    <a-select
      v-bind="getBindValue"
      :class="`${prefixCls}-select`"
      @change="handleChange"
      :disabled="disabled"
      size="small"
      :options="options"
    />
  </div>
</template>
<script lang="ts" setup>
import type { SelectProps } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';
import { baseHandler } from '../handler';
import { HandlerEnum } from '../enum';

defineOptions({ name: 'SelectItem' });

const props = defineProps({
  event: {
    type: Number as PropType<HandlerEnum>,
  },
  disabled: {
    type: Boolean,
  },
  title: {
    type: String,
  },
  def: {
    type: [String, Number] as PropType<string | number>,
  },
  initValue: {
    type: [String, Number] as PropType<string | number>,
  },
  options: {
    type: Array as PropType<LabelValueOptions>,
    default: () => [],
  },
});

const { prefixCls } = useDesign('setting-select-item');
const getBindValue = computed(() => {
  return props.def ? { value: props.def, defaultValue: props.initValue || props.def } : {};
});

const handleChange: SelectProps['onChange'] = val => {
  props.event && baseHandler(props.event, val);
};
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-setting-select-item';

.@{prefix-cls} {
  display: flex;
  justify-content: space-between;
  margin: 16px 0;

  &-select {
    width: 126px;
  }
}
html[data-theme='dark'] {
  .@{prefix-cls} {
    color: rgba(255, 255, 255, 0.8);
  }
}
</style>
