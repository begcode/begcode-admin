<template>
  <span :class="getClass">
    <slot></slot>
    <basic-help :class="`${prefixCls}-help`" v-if="helpMessage" :text="helpMessage" />
  </span>
</template>
<script lang="ts" setup>
import { theme } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';

const props = defineProps({
  /**
   * Help text list or string
   * @default: ''
   */
  helpMessage: {
    type: [String, Array] as PropType<string | string[]>,
    default: '',
  },
  /**
   * Whether the color block on the left side of the title
   * @default: false
   */
  span: { type: Boolean },
  /**
   * Whether to default the text, that is, not bold
   * @default: false
   */
  normal: { type: Boolean },
});

const { useToken } = theme;
const { token } = useToken();

const { prefixCls } = useDesign('basic-title');
const slots = useSlots();
const getClass = computed(() => [
  prefixCls,
  { [`${prefixCls}-show-span`]: props.span && slots.default },
  { [`${prefixCls}-normal`]: props.normal },
]);
</script>

<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-basic-title';

.@{prefix-cls} {
  position: relative;
  display: flex;
  padding-left: 7px;
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  color: v-bind('token["colorText"]');
  cursor: move;
  user-select: none;

  &.is-drawer {
    cursor: default;
  }

  &-normal {
    font-size: 14px;
    font-weight: 500;
  }

  &-show-span::before {
    position: absolute;
    top: 4px;
    left: 0;
    width: 3px;
    height: 16px;
    margin-right: 4px;
    background-color: v-bind('token.colorPrimary');
    content: '';
  }

  &-help {
    margin-left: 10px;
  }
}
</style>
