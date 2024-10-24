<template>
  <a-layout :class="prefixCls" v-bind="lockEvents">
    <LayoutFeatures />
    <LayoutHeader fixed v-if="getShowFullHeaderRef" />
    <a-layout :class="[layoutClass, `${prefixCls}-out`]">
      <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
      <a-layout :class="`${prefixCls}-main`">
        <Suspense><LayoutMultipleHeader /></Suspense>
        <LayoutContent />
        <LayoutFooter />
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import LayoutHeader from './header/index.vue';
import LayoutContent from './content/index.vue';
import LayoutSideBar from './sider/index.vue';
import LayoutMultipleHeader from './header/MultipleHeader.vue';

import { useHeaderSetting } from '@/hooks/setting/useHeaderSetting';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useAppInject } from '@/hooks/useAppInject';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { useDesign } from '@/hooks/web/useDesign';
import { useLockPage } from '@/hooks/web/useLockPage';

import { useMultipleTabSetting } from '@/hooks/setting/useMultipleTabSetting';

const LayoutFeatures = createAsyncComponent(() => import('@/layouts/default/feature/index.vue'));
const LayoutFooter = createAsyncComponent(() => import('@/layouts/default/footer/index.vue'));

defineOptions({ name: 'DefaultLayout' });

const { prefixCls } = useDesign('default-layout');
const { getIsMobile } = useAppInject();
const { getShowFullHeaderRef } = useHeaderSetting();
const { getShowSidebar, getIsMixSidebar, getShowMenu } = useMenuSetting();
const { getAutoCollapse } = useMultipleTabSetting();

// Create a lock screen monitor
const lockEvents = useLockPage();

const layoutClass = computed(() => {
  let cls: string[] = ['ant-layout'];
  if (unref(getIsMixSidebar) || unref(getShowMenu)) {
    cls.push('ant-layout-has-sider');
  }
  if (!unref(getShowMenu) && unref(getAutoCollapse)) {
    cls.push('ant-layout-auto-collapse-tabs');
  }
  return cls;
});
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-default-layout';

.@{prefix-cls} {
  display: flex;
  width: 100%;
  min-height: 100%;
  background-color: @content-bg;
  flex-direction: column;

  > .ant-layout {
    min-height: 100%;
  }

  &-main {
    width: 100%;
    margin-left: 1px;
  }
}
.@{prefix-cls}-out {
  &.ant-layout-has-sider {
    .@{prefix-cls} {
      &-main {
        margin-left: 1px;
      }
    }
  }
}
</style>
