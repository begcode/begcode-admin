<template>
  <a-layout-header :class="getHeaderClass">
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
      <span
        v-if="getShowContent && getShowBreadTitle && !getIsMobile"
        :class="[prefixCls, `${prefixCls}--${getHeaderTheme}`, 'headerIntroductionClass']"
      >
        {{ t('layout.header.welcomeIn') }} {{ title }}
      </span>
    </div>
    <!-- left end -->

    <!-- menu start -->
    <div v-if="getShowTopMenu && !getIsMobile" :class="`${prefixCls}-menu`">
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
  </a-layout-header>
  <LoginSelect ref="loginSelectRef" @success="loginSelectOk"></LoginSelect>
</template>
<script lang="ts" setup>
import { AppLocalePicker, AppLogo, AppSearch } from '@/components/Application';
import { SettingButtonPositionEnum } from '@/enums/appEnum';
import { MenuModeEnum, MenuSplitTyeEnum } from '@/enums/menuEnum';
import { useHeaderSetting } from '@/hooks/setting/useHeaderSetting';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { useAppInject } from '@/hooks/useAppInject';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { useDesign } from '@/hooks/web/useDesign';
import { useLocale } from '@/i18n/useLocale';
import { useI18n } from '@/hooks/web/useI18n';
import { useGlobSetting } from '@/hooks/setting';

import LayoutMenu from '../menu/index.vue';
import LayoutTrigger from '../trigger/index.vue';

import { ErrorAction, FullScreen, LayoutBreadcrumb, Notify, UserDropDown, LockScreen } from './components';

import LoginSelect from '@/views/account/login/LoginSelect.vue';
import { useUserStore } from '@/store/modules/user';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const { t } = useI18n();

const SettingDrawer = createAsyncComponent(() => import('@/layouts/default/setting/index.vue'), {
  loading: true,
});

const props = defineProps({
  fixed: {
    type: Boolean,
  },
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

.ant-layout .@{prefix-cls} {
  display: flex;
  padding: 0 8px !important;
  height: @header-height;
  align-items: center;

  .headerIntroductionClass {
    margin-right: 4px;
    margin-bottom: 2px;
    border-bottom: 0;
    border-left: 0;
  }

  &--light {
    .headerIntroductionClass {
      color: #000;
    }
  }

  &--dark {
    .headerIntroductionClass {
      color: rgba(255, 255, 255, 1);
    }

    .anticon,
    .truncate {
      color: rgba(255, 255, 255, 1);
    }
  }
}
</style>
