<!--
 * @Author: Vben
 * @Description: logo component
-->
<template>
  <div class="anticon" :class="getAppLogoClass" @click="goHome">
    <img :src="logoPng" />
    <div class="ml-2 truncate md:opacity-100" :class="getTitleClass" v-show="showTitle">
      {{ shortTitle }}
    </div>
  </div>
</template>
<script lang="ts" setup>
import { computed, unref } from 'vue';
import logoPng from '@/assets/images/logo.png';
import { useGlobSetting } from '@/hooks/setting';
import { useGo } from '@/hooks/web/usePage';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useDesign } from '@begcode/components';
import { PageEnum } from '@/enums/pageEnum';
import { useUserStore } from '@/store/modules/user';

const props = defineProps({
  /**
   * The theme of the current parent component
   */
  theme: { type: String, validator: (v: string) => ['light', 'dark'].includes(v) },
  /**
   * Whether to show title
   */
  showTitle: { type: Boolean, default: true },
  /**
   * The title is also displayed when the menu is collapsed
   */
  alwaysShowTitle: { type: Boolean },
});

const { prefixCls } = useDesign('app-logo');
const { getCollapsedShowTitle } = useMenuSetting();
const userStore = useUserStore();
const { title, shortTitle } = useGlobSetting();
const go = useGo();

const getAppLogoClass = computed(() => [prefixCls, props.theme, { 'collapsed-show-title': unref(getCollapsedShowTitle) }]);

const getTitleClass = computed(() => [
  `${prefixCls}__title`,
  {
    'xs:opacity-0': !props.alwaysShowTitle,
  },
]);

function goHome() {
  go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
}
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-app-logo';

.@{prefix-cls} {
  display: flex;
  align-items: center;
  padding-left: 7px;
  cursor: pointer;
  transition: all 0.2s ease;

  &.vben-layout-mix-sider-logo,
  &.vben-layout-menu-logo {
    background: @sider-logo-bg-color;
  }

  &.collapsed-show-title {
    padding-left: 20px;
  }

  &.light &__title {
    color: @primary-color;
  }

  &.dark &__title {
    color: @white;
  }

  &__title {
    font-size: 18px;
    font-weight: 800;
    transition: all 0.5s;
    line-height: normal;
  }
}
</style>
