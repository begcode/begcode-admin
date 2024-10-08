<template>
  <Layout :class="prefixCls" v-bind="lockEvents">
    <LayoutFeatures />
    <LayoutHeader fixed v-if="getShowFullHeaderRef" />
    <Layout :class="[layoutClass, `${prefixCls}-out`]">
      <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
      <Layout :class="`${prefixCls}-main`">
        <Suspense><LayoutMultipleHeader /></Suspense>
        <LayoutContent />
        <LayoutFooter />
      </Layout>
    </Layout>
  </Layout>
</template>

<script lang="ts" setup>
import { computed, unref } from 'vue';
import { Layout } from 'ant-design-vue';

import LayoutHeader from './header/index.vue';
import LayoutContent from './content/index.vue';
import LayoutSideBar from './sider/index.vue';
import LayoutMultipleHeader from './header/MultipleHeader.vue';

import { useHeaderSetting } from '@/hooks/setting/useHeaderSetting';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useDesign, createAsyncComponent, useAppInject } from '@begcode/components';
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
