import pkg from '../../package.json';
import { getConfigFileName } from '../../build/getConfigFileName';
import type { GlobEnvConfig } from '#/config';
import { API_ADDRESS } from '@/enums/cacheEnum';

export function getCommonStoragePrefix() {
  const { VITE_GLOB_APP_TITLE } = getAppEnvConfig();
  return `${VITE_GLOB_APP_TITLE.replace(/\s/g, '_')}__${getEnv()}`.toUpperCase();
}

// Generate cache key according to version
export function getStorageShortName() {
  return `${getCommonStoragePrefix()}${`__${pkg.version}`}__`.toUpperCase();
}

const getVariableName = (title: string) => {
  function strToHex(str: string) {
    const result: string[] = [];
    for (let i = 0; i < str.length; ++i) {
      const hex = str.charCodeAt(i).toString(16);
      result.push(`000${hex}`.slice(-4));
    }
    return result.join('').toUpperCase();
  }
  return `__PRODUCTION__${strToHex(title) || '__APP'}__CONF__`.toUpperCase().replace(/\s/g, '');
};

export function getAppEnvConfig() {
  const ENV_NAME = getConfigFileName(import.meta.env);

  // Get the global configuration (the configuration will be extracted independently when packaging)
  const ENV = import.meta.env.DEV ? (import.meta.env as unknown as GlobEnvConfig) : (window[ENV_NAME] as unknown as GlobEnvConfig);

  const {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_UPLOAD_URL,
    VITE_GLOB_MOCK,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_APP_OPEN_QIANKUN,
    VITE_GLOB_APP_CAS_BASE_URL,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_ONLINE_VIEW_URL,
  } = ENV;

  let { VITE_GLOB_API_URL } = ENV;
  if (localStorage.getItem(API_ADDRESS)) {
    const address = JSON.parse(localStorage.getItem(API_ADDRESS) || '{}');
    if (address?.key) VITE_GLOB_API_URL = address?.val;
  }

  return {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_UPLOAD_URL,
    VITE_GLOB_MOCK,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_APP_OPEN_QIANKUN,
    VITE_GLOB_APP_CAS_BASE_URL,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_ONLINE_VIEW_URL,
  };
}

/**
 * @description: Development mode
 */
export const devMode = 'development';

/**
 * @description: Production mode
 */
export const prodMode = 'production';

/**
 * @description: Get environment variables
 * @returns:
 * @example:
 */
export function getEnv(): string {
  return import.meta.env.MODE;
}

/**
 * @description: Is it a development mode
 * @returns:
 * @example:
 */
export function isDevMode(): boolean {
  return import.meta.env.DEV;
}

/**
 * @description: Is it a production mode
 * @returns:
 * @example:
 */
export function isProdMode(): boolean {
  return import.meta.env.PROD;
}

export const frontendAppName = 'monolithMybatisApp';
