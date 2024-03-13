import { merge } from 'lodash-es';
import type { LocaleType } from '#/config';

export const loadLocalePool: LocaleType[] = [];

export function setHtmlPageLang(locale: LocaleType) {
  document.querySelector('html')?.setAttribute('lang', locale);
}

export function setLoadLocalePool(cb: (loadLocalePool: LocaleType[]) => void) {
  cb(loadLocalePool);
}

export function genMessage(langs: Record<string, Record<string, any>>, _prefix = 'lang') {
  const obj: Recordable = {};

  Object.keys(langs).forEach(key => {
    merge(obj, langs[key].default);
  });
  return obj;
}
