import type { GlobConfig } from '/#/config';

import { getAppEnvConfig } from '@/utils/env';

export const useGlobSetting = (): Readonly<GlobConfig> => {
  const {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_APP_CAS_BASE_URL,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_APP_OPEN_QIANKUN,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_ONLINE_VIEW_URL,
  } = getAppEnvConfig();

  // Take global configuration
  const glob: Readonly<GlobConfig> = {
    title: VITE_GLOB_APP_TITLE,
    domainUrl: VITE_GLOB_DOMAIN_URL,
    apiUrl: VITE_GLOB_API_URL,
    shortName: VITE_GLOB_APP_TITLE.replace(/\s/g, '_').replace(/-/g, '_'),
    openSso: VITE_GLOB_APP_OPEN_SSO,
    openQianKun: VITE_GLOB_APP_OPEN_QIANKUN,
    casBaseUrl: VITE_GLOB_APP_CAS_BASE_URL,
    urlPrefix: VITE_GLOB_API_URL_PREFIX,
    uploadUrl: VITE_GLOB_DOMAIN_URL,
    viewUrl: VITE_GLOB_ONLINE_VIEW_URL,
  };
  window._CONFIG['domianURL'] = VITE_GLOB_DOMAIN_URL;
  return glob as Readonly<GlobConfig>;
};
