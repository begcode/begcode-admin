<template>
  <div :class="[prefixCls, getLayoutContentMode]" v-loading="getOpenPageLoading && getPageLoading">
    <div :class="[prefixClsScroll]">
      <PageLayout />
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, provide } from 'vue';
import PageLayout from '@/layouts/page/index.vue';
import { useDesign } from '@begcode/components';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { useTransitionSetting } from '@/hooks/setting/useTransitionSetting';
import { useLayoutHeight } from './useContentViewHeight';
import { useContentViewHeight } from './useContentViewHeight';
import { useGlobSetting } from '@/hooks/setting';

defineOptions({ name: 'LayoutContent' });

provide('USE_LAYOUT_HEIGHT', useLayoutHeight);

const { prefixCls } = useDesign('layout-content');
const { prefixCls: prefixClsScroll } = useDesign('layout-content-scroll');
const { getOpenPageLoading } = useTransitionSetting();
const { getLayoutContentMode, getPageLoading } = useRootSetting();

useContentViewHeight();
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-layout-content';
@prefix-cls-scroll: ~'@{namespace}-layout-content-scroll';

.@{prefix-cls} {
  flex-grow: 1;
  width: 100%;
  height: 0;
  min-height: 0;
  overflow: auto;

  &.fixed {
    width: 1200px;
    margin: 0 auto;
  }

  &-loading {
    position: absolute;
    top: 200px;
    z-index: @page-loading-z-index;
  }
}
</style>
