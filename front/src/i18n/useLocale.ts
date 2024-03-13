/**
 * Multi-language related operations
 */
import { unref, computed, getCurrentInstance } from 'vue';
import { Locale } from 'ant-design-vue/es/locale';
import { i18n } from './setupI18n';
import { loadLocalePool, setHtmlPageLang } from './helper';
import type { LocaleType } from '#/config';

import { useLocaleStoreWithOut } from '@/store/modules/locale';

interface LangModule {
  message: Recordable;
  dateLocale: Recordable;
  dateLocaleName: string;
}

function setI18nLanguage(locale: LocaleType) {
  const localeStore = useLocaleStoreWithOut();

  if (!i18n) {
    const internalInstance = getCurrentInstance();
    return internalInstance?.appContext.config.globalProperties.$i18n || {};
  }

  if (i18n.mode === 'legacy') {
    i18n.global.locale = locale;
  } else {
    (i18n.global.locale as any).value = locale;
  }
  localeStore.setLocaleInfo({ locale });
  setHtmlPageLang(locale);
}

export function useLocale() {
  const localeStore = useLocaleStoreWithOut();
  const getLocale = computed(() => localeStore.getLocale);
  const getShowLocalePicker = computed(() => localeStore.getShowPicker);

  const getAntdLocale = computed((): any => {
    if (!i18n) {
      const internalInstance = getCurrentInstance();
      return internalInstance?.appContext.config.globalProperties.$i18n || {};
    }
    // @ts-ignore
    const localeMessage = i18n.global.getLocaleMessage<{ antdLocale: Locale }>(unref(getLocale));
    return localeMessage?.antdLocale ?? {};
  });

  // Switching the language will change the locale of useI18n
  // And submit to configuration modification
  async function changeLocale(locale: LocaleType) {
    if (!i18n) {
      const internalInstance = getCurrentInstance();
      return internalInstance?.appContext.config.globalProperties.$i18n || {};
    }
    const globalI18n = i18n.global;
    const currentLocale = unref(globalI18n.locale);
    if (currentLocale === locale) {
      return locale;
    }

    if (loadLocalePool.includes(locale)) {
      setI18nLanguage(locale);
      return locale;
    }
    const langModule = ((await import(`./${locale}/${locale}.ts`)) as any).default as LangModule;
    if (!langModule) return;

    const { message } = langModule;

    globalI18n.setLocaleMessage(locale, message);
    loadLocalePool.push(locale);

    setI18nLanguage(locale);
    return locale;
  }

  return {
    getLocale,
    getShowLocalePicker,
    changeLocale,
    getAntdLocale,
  };
}
