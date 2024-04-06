<template>
  <div v-if="getMenuFixed && !getIsMobile" v-show="showClassSideBarRef" :style="getHiddenDomStyle"></div>
  <Layout.Sider
    v-show="showClassSideBarRef"
    ref="sideRef"
    :breakpoint="getTrigger === TriggerEnum.FOOTER ? 'md' : 'lg'"
    collapsible
    :class="getSiderClass"
    :width="getMenuWidth"
    :collapsed="getCollapsed"
    :collapsedWidth="getCollapsedWidth"
    :theme="getMenuTheme"
    :trigger="triggerVNode"
    v-bind="getTriggerAttr"
    @breakpoint="onBreakpointChange"
  >
    <template v-if="getShowTrigger" #trigger>
      <LayoutTrigger />
    </template>
    <LayoutMenu :theme="getMenuTheme" :menuMode="getMode" :splitType="getSplitType" />
    <DragBar ref="dragBarRef" />
  </Layout.Sider>
</template>
<script lang="ts" setup>
import { Layout, theme } from 'ant-design-vue';
import { computed, ref, unref, CSSProperties, h } from 'vue';

import { MenuModeEnum, MenuSplitTyeEnum, TriggerEnum } from '@/enums/menuEnum';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useAppInject } from '@begcode/components';
import { useDesign } from '@begcode/components';
import LayoutTrigger from '@/layouts/default/trigger/index.vue';

import LayoutMenu from '../menu/index.vue';
import DragBar from './DragBar.vue';
import { useDragLine, useSiderEvent, useTrigger } from './useLayoutSider';

const dragBarRef = ref(null);
const sideRef = ref(null);

const { getCollapsed, getMenuWidth, getSplit, getMenuTheme, getRealWidth, getMenuHidden, getMenuFixed, getIsMixMode, getTrigger } =
  useMenuSetting();

const { prefixCls } = useDesign('layout-sideBar');

const { getIsMobile } = useAppInject();

const { getTriggerAttr, getShowTrigger } = useTrigger(getIsMobile);

useDragLine(sideRef, dragBarRef);

const { getCollapsedWidth, onBreakpointChange } = useSiderEvent();

const getMode = computed(() => {
  return unref(getSplit) ? MenuModeEnum.INLINE : null;
});

const getSplitType = computed(() => {
  return unref(getSplit) ? MenuSplitTyeEnum.LEFT : MenuSplitTyeEnum.NONE;
});

const showClassSideBarRef = computed(() => {
  return unref(getSplit) ? !unref(getMenuHidden) : true;
});

const getSiderClass = computed(() => {
  return [
    prefixCls,
    {
      [`${prefixCls}--fixed`]: unref(getMenuFixed),
      [`${prefixCls}--mix`]: unref(getIsMixMode) && !unref(getIsMobile),
    },
  ];
});

const getHiddenDomStyle = computed((): CSSProperties => {
  const width = `${unref(getRealWidth)}px`;
  return {
    width,
    overflow: 'hidden',
    flex: `0 0 ${width}`,
    maxWidth: width,
    minWidth: width,
    transition: 'all 0.2s',
  };
});

// 在此处使用计算量可能会导致sider异常
// andv 更新后，如果trigger插槽可用，则此处代码可废弃
const triggerVNode = h(LayoutTrigger);

const { useToken } = theme;
const { token } = useToken();
</script>
<style lang="less">
.vben-layout-sideBar {
  z-index: @layout-sider-fixed-z-index;

  &--fixed {
    position: fixed !important;
    top: 0;
    left: 0;
    height: 100%;
  }

  &--mix {
    top: @header-height;
    height: calc(100% - @header-height);
  }

  &.ant-layout-sider-dark {
    background-color: @sider-dark-bg-color;

    .ant-layout-sider-trigger {
      color: darken(@white, 25%);
      background-color: @trigger-dark-bg-color;

      &:hover {
        color: v-bind('token.colorWhite');
        background-color: @trigger-dark-hover-bg-color;
      }
    }
  }

  &:not(.ant-layout-sider-dark) {
    // box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);

    .ant-layout-sider-trigger {
      color: @text-color-base;
      border-top: 1px solid v-bind('token.colorBorder');
    }
  }

  .ant-layout-sider-zero-width-trigger {
    top: 40%;
    z-index: 10;
  }

  & .ant-layout-sider-trigger {
    height: 36px;
    line-height: 36px;
  }
}
</style>
