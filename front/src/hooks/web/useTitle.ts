import { watch, unref } from 'vue';
import { useTitle as usePageTitle } from '@vueuse/core';
import { useRouter } from 'vue-router';
import { useI18n } from '@/hooks/web/useI18n';
import { useGlobSetting } from '@/hooks/setting';
import { useLocaleStore } from '@/store/modules/locale';
import { REDIRECT_NAME } from '@/router/constant';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

/**
 * Listening to page changes and dynamically changing site titles
 */
export function useTitle() {
  const { title } = useGlobSetting();
  const { t } = useI18n();
  const { currentRoute } = useRouter();
  const localeStore = useLocaleStore();

  const pageTitle = usePageTitle();

  watch(
    [() => currentRoute.value.path, () => localeStore.getLocale],
    () => {
      const route = unref(currentRoute);

      if (route.name === REDIRECT_NAME) {
        return;
      }

      const tTitle = t(route?.meta?.title as string);
      pageTitle.value = tTitle ? ` ${tTitle} - ${title} ` : `${title}`;
    },
    { immediate: true },
  );
}
