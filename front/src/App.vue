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
import { useContentHeight } from '@/hooks/web/useContentHeight';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { getViewComponent } from '@/views/getViews';
import { useAppStore } from '@/store/modules/app';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { ThemeEnum } from '@/enums/appEnum';
import { changeTheme } from '@/logics/theme';
import VXETable from 'vxe-table';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

// support Multi-language
const { getAntdLocale } = useLocale();
const appStore = useAppStore();

const { getCalcContentWidth } = useMenuSetting();
const { getDarkMode } = useRootSetting();
provide('CALC_CONTENT_WIDTH', getCalcContentWidth.value);
provide('USE_CONTENT_HEIGHT', useContentHeight);
provide('GET_VIEW_COMPONENT', getViewComponent);
provide('APP_DARK_MODE', getDarkMode);

// Listening to page changes and dynamically changing site titles
useTitle();
const modeAction = data => {
  if (data.token) {
    if (getDarkMode.value === ThemeEnum.DARK) {
      VXETable.setConfig({ theme: 'dark' });
      Object.assign(data.token, { colorTextBase: '#fff' });
    } else {
      VXETable.setConfig({ theme: 'default' });
      Object.assign(data.token, { colorTextBase: '#333' });
    }
  }
};
const appTheme: any = ref({});
watch(
  () => getDarkMode.value,
  newValue => {
    delete appTheme.value.algorithm;
    if (newValue === ThemeEnum.DARK) {
      appTheme.value.algorithm = theme.darkAlgorithm;
    }
    if (import.meta.env.PROD) {
      changeTheme(appStore.getProjectConfig.themeColor);
    }
    modeAction(appTheme.value);
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
    const result = {
      ...appTheme.value,
      ...{
        token: {
          colorPrimary: primary,
          wireframe: true,
          fontSize: 14,
          colorTextBase: '#333',
          colorSuccess: '#55D187',
          colorInfo: primary,
          borderRadius: 4,
          sizeStep: 4,
          sizeUnit: 4,
          colorWarning: '#EFBD47',
          colorError: '#ED6F6F',
          previewZIndex: 1000,
          fontFamily:
            '-apple-system,BlinkMacSystemFont,Segoe UI,PingFang SC,Hiragino Sans GB,Microsoft YaHei,Helvetica Neue,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol',
        },
      },
    };
    appTheme.value = result;
    modeAction(result);
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
