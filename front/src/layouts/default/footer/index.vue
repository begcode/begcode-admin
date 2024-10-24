<template>
  <a-layout-footer :class="prefixCls" v-if="getShowLayoutFooter" ref="footerRef">
    <div :class="`${prefixCls}__links`">
      <a @click="openWindow(SITE_URL)">{{ t('layout.footer.onlinePreview') }}</a>
      <Icon icon="ant-design:github-filled" @click="openWindow(GITHUB_URL)" :class="`${prefixCls}__github`" />
      <a @click="openWindow(DOC_URL)">{{ t('layout.footer.onlineDocument') }}</a>
    </div>
    <div>Copyright &copy;2024 BegCode</div>
  </a-layout-footer>
</template>

<script lang="ts" setup>
import { DOC_URL, GITHUB_URL, SITE_URL } from '@/settings/siteSetting';
import { openWindow } from '@/utils/util';
import { useI18n } from '@/hooks/web/useI18n';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { useRouter } from 'vue-router';
import { useDesign } from '@/hooks/web/useDesign';
import { useLayoutHeight } from '../content/useContentViewHeight';
import { ThemeEnum } from '@/enums/appEnum';

defineOptions({ name: 'LayoutFooter' });

const { t } = useI18n();
const { getShowFooter } = useRootSetting();
const { currentRoute } = useRouter();
const { prefixCls } = useDesign('layout-footer');

const footerRef = ref<ComponentRef>(null);
const { setFooterHeight } = useLayoutHeight();

const getShowLayoutFooter = computed(() => {
  if (unref(getShowFooter)) {
    const footerEl = unref(footerRef)?.$el;
    setFooterHeight(footerEl?.offsetHeight || 0);
  } else {
    setFooterHeight(0);
  }
  return unref(getShowFooter) && !unref(currentRoute).meta?.hiddenFooter;
});

//当前主题
const { getDarkMode } = useRootSetting();
const isDark = computed(() => getDarkMode.value === ThemeEnum.DARK);

//鼠标移入的颜色设置
const hoverColor = computed(() => {
  return unref(isDark) ? 'rgba(255, 255, 255, 1)' : 'rgba(0, 0, 0, 0.85)';
});
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-layout-footer';

@normal-color: rgba(0, 0, 0, 0.45);

@hover-color: v-bind(hoverColor);

.@{prefix-cls} {
  color: @normal-color;
  text-align: center;

  &__links {
    margin-bottom: 8px;

    a {
      color: @normal-color;

      &:hover {
        color: @hover-color;
      }
    }
  }

  &__github {
    margin: 0 30px;

    &:hover {
      color: @hover-color;
    }
  }
}
</style>
