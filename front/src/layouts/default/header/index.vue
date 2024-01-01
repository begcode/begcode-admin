<template>
  <Layout.Header :class="getHeaderClass">
    <!-- left start -->
    <div :class="`${prefixCls}-left`">
      <!-- logo -->
      <AppLogo v-if="getShowHeaderLogo || getIsMobile" :class="`${prefixCls}-logo`" :theme="getHeaderTheme" :style="getLogoWidth" />
      <LayoutTrigger
        v-if="(getShowContent && getShowHeaderTrigger && !getSplit && !getIsMixSidebar) || getIsMobile"
        :theme="getHeaderTheme"
        :sider="false"
      />
      <LayoutBreadcrumb v-if="getShowContent && getShowBread" :theme="getHeaderTheme" />
      <!-- 欢迎语 -->
      <span
        v-if="getShowContent && getShowBreadTitle && !getIsMobile"
        :class="[prefixCls, `${prefixCls}--${getHeaderTheme}`, 'headerIntroductionClass']"
      >
        欢迎进入 {{ title }}
      </span>
    </div>
    <!-- left end -->

    <!-- menu start -->
    <div v-if="getShowTopMenu && !getIsMobile" 　:class="`${prefixCls}-menu`">
      <LayoutMenu :isHorizontal="true" :theme="getHeaderTheme" :splitType="getSplitType" :menuMode="getMenuMode" />
    </div>
    <!-- menu-end -->

    <!-- action  -->
    <div :class="`${prefixCls}-action`">
      <AppSearch v-if="getShowSearch" :class="`${prefixCls}-action__item`" />

      <ErrorAction v-if="getUseErrorHandle" :class="`${prefixCls}-action__item error-action`" />

      <Notify v-if="getShowNotice" :class="`${prefixCls}-action__item notify-item`" />

      <FullScreen v-if="getShowFullScreen" :class="`${prefixCls}-action__item fullscreen-item`" />

      <LockScreen v-if="getUseLockPage" />

      <AppLocalePicker v-if="getShowLocalePicker" :reload="true" :showText="false" :class="`${prefixCls}-action__item`" />

      <UserDropDown :theme="getHeaderTheme" />

      <SettingDrawer v-if="getShowSetting" :class="`${prefixCls}-action__item`" />
    </div>
  </Layout.Header>
  <LoginSelect ref="loginSelectRef" @success="loginSelectOk"></LoginSelect>
</template>
<script lang="ts" setup>
import { Layout } from 'ant-design-vue';
import { computed, onMounted, ref, toRaw, unref } from 'vue';

import { AppLocalePicker, AppLogo, AppSearch } from '@/components/Application';
import { SettingButtonPositionEnum } from '@/enums/appEnum';
import { MenuModeEnum, MenuSplitTyeEnum } from '@/enums/menuEnum';
import { useHeaderSetting } from '@/hooks/setting/useHeaderSetting';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { useAppInject } from '@begcode/components';
import { useDesign } from '@begcode/components';
import { useLocale } from '@/i18n/useLocale';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { useGlobSetting } from '@/hooks/setting';
import { propTypes } from '@begcode/components';

import LayoutMenu from '../menu/index.vue';
import LayoutTrigger from '../trigger/index.vue';

import { ErrorAction, FullScreen, LayoutBreadcrumb, Notify, UserDropDown, LockScreen } from './components';

import LoginSelect from '@/views/account/login/LoginSelect.vue';
import { useUserStore } from '@/store/modules/user';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const SettingDrawer = createAsyncComponent(() => import('@/layouts/default/setting/index.vue'), {
  loading: true,
});

const props = defineProps({
  fixed: propTypes.bool,
});

const { prefixCls } = useDesign('layout-header');
const userStore = useUserStore();
const { getShowTopMenu, getShowHeaderTrigger, getSplit, getIsMixMode, getMenuWidth, getIsMixSidebar } = useMenuSetting();

const { getUseErrorHandle, getShowSettingButton, getSettingButtonPosition } = useRootSetting();

const { title } = useGlobSetting();
const {
  getHeaderTheme,
  getShowFullScreen,
  getShowNotice,
  getShowContent,
  getShowBread,
  getShowHeaderLogo,
  getShowHeader,
  getShowSearch,
  getUseLockPage,
  getShowBreadTitle,
} = useHeaderSetting();

const { getShowLocalePicker } = useLocale();

const { getIsMobile } = useAppInject();

const getHeaderClass = computed(() => {
  const theme = unref(getHeaderTheme);
  return [
    prefixCls,
    {
      [`${prefixCls}--fixed`]: props.fixed,
      [`${prefixCls}--mobile`]: unref(getIsMobile),
      [`${prefixCls}--${theme}`]: theme,
    },
  ];
});

const getShowSetting = computed(() => {
  if (!unref(getShowSettingButton)) {
    return false;
  }
  const settingButtonPosition = unref(getSettingButtonPosition);

  if (settingButtonPosition === SettingButtonPositionEnum.AUTO) {
    return unref(getShowHeader);
  }
  return settingButtonPosition === SettingButtonPositionEnum.HEADER;
});

const getLogoWidth = computed(() => {
  if (!unref(getIsMixMode) || unref(getIsMobile)) {
    return {};
  }
  const width = unref(getMenuWidth) < 180 ? 180 : unref(getMenuWidth);
  return { width: `${width}px` };
});

const getSplitType = computed(() => {
  return unref(getSplit) ? MenuSplitTyeEnum.TOP : MenuSplitTyeEnum.NONE;
});

const getMenuMode = computed(() => {
  return unref(getSplit) ? MenuModeEnum.HORIZONTAL : null;
});

/**
 * 首页多租户部门弹窗逻辑
 */
const loginSelectRef = ref();

function showLoginSelect() {
  const loginInfo = toRaw(userStore.getLoginInfo) || {};
  if (!!loginInfo.isLogin) {
    loginSelectRef.value.show(loginInfo);
  }
}

function loginSelectOk() {}

onMounted(() => {
  showLoginSelect();
});
</script>
<style lang="less">
@import url('./index.less');
@prefix-cls: ~'@{namespace}-layout-header';

.@{prefix-cls} {
  display: flex;
  padding: 0 8px !important;
  align-items: center;

  .headerIntroductionClass {
    margin-right: 4px;
    margin-bottom: 2px;
    border-bottom: 0;
    border-left: 0;
  }

  &--light {
    .headerIntroductionClass {
      color: @breadcrumb-item-normal-color;
    }
  }

  &--dark {
    .headerIntroductionClass {
      color: rgba(255, 255, 255, 0.6);
    }

    .anticon {
      color: rgba(255, 255, 255, 0.8);
    }
  }
}
</style>
