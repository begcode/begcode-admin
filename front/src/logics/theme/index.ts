import {
  replaceStyleVariables,
  loadDarkThemeCss,
  replaceCssColors,
  darkCssIsReady,
  linkID,
  styleTagId,
  appendCssToDom,
  getStyleDom,
} from '@rys-fe/vite-plugin-theme/es/client';
import { mixLighten, mixDarken, tinycolor } from '@rys-fe/vite-plugin-theme/es/colorUtils';
import { getThemeColors, generateColors } from '../../../build/config/themeConfig';
import { useAppStore } from '@/store/modules/app';
import { defHttp } from '@/utils/http/axios';

let cssText = '';

export async function changeTheme(color: string) {
  const appStore = useAppStore();
  appStore.setProjectConfig({ themeColor: color });
  const colors = generateColors({
    mixDarken,
    mixLighten,
    tinycolor,
    color,
  });

  if (import.meta.env.PROD && appStore.getDarkMode === 'dark') {
    if (!darkCssIsReady && !cssText) {
      await loadDarkThemeCss();
    }
    const el: HTMLLinkElement = document.getElementById(linkID) as HTMLLinkElement;
    if (el?.href) {
      // cssText = await fetchCss(el.href) as string;
      !cssText && (cssText = await defHttp.get({ url: el.href }, { isTransformResponse: false }));
      const colorVariables = [...getThemeColors(color), ...colors];
      const processCss = await replaceCssColors(cssText, colorVariables);
      appendCssToDom(getStyleDom(styleTagId) as HTMLStyleElement, processCss);
    }
  } else {
    let res = await replaceStyleVariables({
      colorVariables: [...getThemeColors(color), ...colors],
    });
    fixDark();
  }
}

async function fixDark() {
  const el = document.getElementById(styleTagId);
  if (el) {
    el.innerHTML = el.innerHTML.replace(/\\["']dark\\["']/g, `'dark'`);
  }
}
