<template>
  <ConfigProvider :locale="getAntdLocale" :theme="themeConfig">
    <AppProvider>
      <RouterView />
    </AppProvider>
  </ConfigProvider>
</template>

<script lang="ts" setup>
import { provide, computed } from 'vue';
import { ConfigProvider } from 'ant-design-vue';
import { AppProvider } from '@/components/Application';
import { useTitle } from '@/hooks/web/useTitle';
import { useLocale } from '@/i18n/useLocale';

import 'dayjs/locale/zh-cn';
import { useDarkModeTheme } from '@/hooks/setting/useDarkModeTheme';
import { useContentHeight } from '@/hooks/web/useContentHeight';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { getViewComponent } from '@/views/getViews';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

// support Multi-language
const { getAntdLocale } = useLocale();
const { isDark, darkTheme } = useDarkModeTheme();

const themeConfig = computed(() =>
  Object.assign(
    {
      token: {
        colorPrimary: '#0960bd',
        colorSuccess: '#55D187',
        colorWarning: '#EFBD47',
        colorError: '#ED6F6F',
        colorInfo: '#0960bd',
      },
    },
    isDark.value ? darkTheme : {},
  ),
);

const { getCalcContentWidth } = useMenuSetting();
provide('CALC_CONTENT_WIDTH', getCalcContentWidth.value);
provide('USE_CONTENT_HEIGHT', useContentHeight);
provide('GET_VIEW_COMPONENT', getViewComponent);

// Listening to page changes and dynamically changing site titles
useTitle();
</script>
<style lang="less">
img {
  display: inline-block;
}
</style>
