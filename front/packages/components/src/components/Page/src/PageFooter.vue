<template>
  <div :class="prefixCls" :style="{ width: getCalcContentWidth }">
    <div :class="`${prefixCls}__left`">
      <slot name="left"></slot>
    </div>
    <slot></slot>
    <div :class="`${prefixCls}__right`">
      <slot name="right"></slot>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { inject } from 'vue';
import { useDesign } from '@/hooks/web/useDesign';
import { theme } from 'ant-design-vue';

const { useToken } = theme;
const token = useToken().token;

const { prefixCls } = useDesign('page-footer');
const getCalcContentWidth = inject('CALC_CONTENT_WIDTH', '100%');
</script>
<style scoped>
.vben-page-footer {
  display: flex;
  position: fixed;
  z-index: 99;
  right: 0;
  bottom: 0;
  align-items: center;
  width: 100%;
  padding: 0 24px;
  transition: width 0.2s;
  border-top: 1px solid #d9d9d9;
  background-color: #ffffff;
  box-shadow:
    0 -6px 16px -8px rgb(0 0 0 / 8%),
    0 -9px 28px 0 rgb(0 0 0 / 5%),
    0 -12px 48px 16px rgb(0 0 0 / 3%);
  line-height: 44px;
}
.vben-page-footer__left {
  flex: 1 1;
}
</style>
