<template>
  <span :class="getClass">
    <slot></slot>
    <BasicHelp :class="`${prefixCls}-help`" v-if="helpMessage" :text="helpMessage" />
  </span>
</template>
<script lang="ts" setup>
import { theme } from 'ant-design-vue';
import BasicHelp from './BasicHelp.vue';
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
<style scoped>
.vben-basic-title {
  display: flex;
  position: relative;
  padding-left: 7px;
  color: v-bind('token["colorText"]');
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  cursor: pointer;
  user-select: none;
}
.vben-basic-title-normal {
  font-size: 14px;
  font-weight: 500;
}
.vben-basic-title.is-drawer {
  cursor: default;
}
.vben-basic-title-show-span::before {
  content: '';
  position: absolute;
  top: 4px;
  left: 0;
  width: 3px;
  height: 16px;
  margin-right: 4px;
  background-color: v-bind('token.colorPrimary');
}
.vben-basic-title-help {
  margin-left: 10px;
}
</style>
