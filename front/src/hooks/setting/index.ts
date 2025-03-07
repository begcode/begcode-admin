import type { GlobConfig } from '#/config';

import { getAppEnvConfig } from '@/utils/env';

export const useGlobSetting = (): Readonly<GlobConfig> => {
  const {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_MOCK,
  } = getAppEnvConfig();

  // Take global configuration
  const shortTitle = VITE_GLOB_APP_SHORT_NAME.replace(/_/g, ' ');
  const glob: Readonly<GlobConfig> = {
    title: VITE_GLOB_APP_TITLE,
    domainUrl: VITE_GLOB_DOMAIN_URL,
    apiUrl: VITE_GLOB_API_URL,
    shortName: VITE_GLOB_APP_TITLE.replace(/\s/g, '_').replace(/-/g, '_'),
    shortTitle,
    openSso: VITE_GLOB_APP_OPEN_SSO,
    urlPrefix: VITE_GLOB_API_URL_PREFIX,
    uploadUrl: VITE_GLOB_DOMAIN_URL,
    useMock: VITE_GLOB_MOCK === 'true' || VITE_GLOB_MOCK === true,
  };
  return glob as Readonly<GlobConfig>;
};
