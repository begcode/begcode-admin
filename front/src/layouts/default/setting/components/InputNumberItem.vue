<template>
  <div :class="prefixCls">
    <span> {{ title }}</span>
    <a-input-number v-bind="$attrs" size="small" :class="`${prefixCls}-input-number`" @change="handleChange" />
  </div>
</template>
<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign';
import { baseHandler } from '../handler';
import { HandlerEnum } from '../enum';

defineOptions({ name: 'InputNumberItem' });

const props = defineProps({
  event: {
    type: Number as PropType<HandlerEnum>,
  },
  title: {
    type: String,
  },
});

const { prefixCls } = useDesign('setting-input-number-item');
function handleChange(e) {
  props.event && baseHandler(props.event, e);
}
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-setting-input-number-item';

.@{prefix-cls} {
  display: flex;
  justify-content: space-between;
  margin: 16px 0;

  &-input-number {
    width: 126px !important;
  }
}
html[data-theme='dark'] {
  .@{prefix-cls} {
    color: rgba(255, 255, 255, 0.8);
  }
}
</style>
