<template>
  <ConfigProvider :locale="getAntdLocale" :theme="appTheme">
    <AppProvider>
      <RouterView />
    </AppProvider>
  </ConfigProvider>
</template>

<script lang="ts" setup>
import { provide, computed, watch, ref } from 'vue';
import { ConfigProvider, theme } from 'ant-design-vue';
import { AppProvider } from '@/components/Application';
import { useTitle } from '@/hooks/web/useTitle';
import { useLocale } from '@/i18n/useLocale';

import 'dayjs/locale/zh-cn';
import { useDarkModeTheme } from '@/hooks/setting/useDarkModeTheme';
import { useContentHeight } from '@/hooks/web/useContentHeight';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { getViewComponent } from '@/views/getViews';
import { useAppStore } from '/@/store/modules/app';
import { useRootSetting } from '/@/hooks/setting/useRootSetting';
import { ThemeEnum } from '/@/enums/appEnum';
import { changeTheme } from '/@/logics/theme/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

// support Multi-language
const { getAntdLocale } = useLocale();
const appStore = useAppStore();

const { getCalcContentWidth } = useMenuSetting();
provide('CALC_CONTENT_WIDTH', getCalcContentWidth.value);
provide('USE_CONTENT_HEIGHT', useContentHeight);
provide('GET_VIEW_COMPONENT', getViewComponent);

// Listening to page changes and dynamically changing site titles
useTitle();
const appTheme: any = ref({});
const { getDarkMode } = useRootSetting();
watch(
  () => getDarkMode.value,
  newValue => {
    delete appTheme.value.algorithm;
    if (newValue === ThemeEnum.DARK) {
      appTheme.value.algorithm = theme.darkAlgorithm;
    }
    appTheme.value = {
      ...appTheme.value,
    };
  },
  { immediate: true },
);
watch(
  appStore.getProjectConfig,
  newValue => {
    const primary = newValue.themeColor;
    appTheme.value = {
      ...appTheme.value,
      ...{
        token: {
          colorPrimary: primary,
          wireframe: true,
          fontSize: 14,
          colorSuccess: '#55D187',
          colorInfo: primary,
          borderRadius: 2,
          sizeStep: 4,
          sizeUnit: 4,
          colorWarning: '#EFBD47',
          colorError: '#ED6F6F',
        },
      },
    };
  },
  { immediate: true },
);
setTimeout(() => {
  appStore.getProjectConfig?.themeColor && changeTheme(appStore.getProjectConfig.themeColor);
}, 300);
</script>
<style lang="less">
img {
  display: inline-block;
}
</style>
