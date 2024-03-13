import { defineStore } from 'pinia';
import type { LocaleSetting, LocaleType } from '#/config';

import { store } from '@/store';

import { LOCALE_KEY } from '@/enums/cacheEnum';
import { createLocalStorage } from '@/utils/cache';
import { localeSetting } from '@/settings/localeSetting';

const ls = createLocalStorage();

const lsLocaleSetting = (ls.get(LOCALE_KEY) || localeSetting) as LocaleSetting;

interface LocaleState {
  localInfo: LocaleSetting;
  pathTitleMap: object;
  // myapps主题色（低代码应用列表首页）
  appIndexTheme: string;
  // myapps - 跳转前路由地址
  appMainPth: string;
}

export const useLocaleStore = defineStore({
  id: 'app-locale',
  state: (): LocaleState => ({
    localInfo: lsLocaleSetting,
    pathTitleMap: {},
    appIndexTheme: '',
    appMainPth: '',
  }),
  getters: {
    getShowPicker(state): boolean {
      return !!state.localInfo?.showPicker;
    },
    getLocale(state): LocaleType {
      return state.localInfo?.locale ?? 'zh-cn';
    },
    getPathTitle: state => {
      return path => state.pathTitleMap[path];
    },
    getAppIndexTheme(): string {
      return this.appIndexTheme;
    },
    getAppMainPth(): string {
      return this.appMainPth;
    },
  },
  actions: {
    /**
     * Set up multilingual information and cache
     * @param info multilingual info
     */
    setLocaleInfo(info: Partial<LocaleSetting>) {
      this.localInfo = { ...this.localInfo, ...info };
      ls.set(LOCALE_KEY, this.localInfo);
    },
    /**
     * Initialize multilingual information and load the existing configuration from the local cache
     */
    initLocale() {
      this.setLocaleInfo({
        ...localeSetting,
        ...this.localInfo,
      });
    },
    setPathTitle(path, title) {
      this.pathTitleMap[path] = title;
    },
    setAppIndexTheme(theme) {
      this.appIndexTheme = theme;
    },
    setAppMainPth(path) {
      this.appMainPth = path;
    },
  },
});

// Need to be used outside the setup
export function useLocaleStoreWithOut() {
  return useLocaleStore(store);
}
