<template>
  <div :class="getClass" ref="wrapperRef">
    <a-page-header
      :ghost="ghost"
      :title="title"
      v-bind="_omit($attrs, 'class')"
      :style="getHeaderStyle"
      ref="headerRef"
      v-if="getShowHeader"
    >
      <template #default>
        <template v-if="content">
          {{ content }}
        </template>
        <slot name="headerContent" v-else></slot>
      </template>
      <template #[item]="data" v-for="item in getHeaderSlots">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </a-page-header>

    <div class="overflow-hidden" :class="getContentClass" :style="getContentStyle" ref="contentRef">
      <slot></slot>
    </div>

    <PageFooter v-if="getShowFooter" ref="footerRef">
      <template #left>
        <slot name="leftFooter"></slot>
      </template>
      <template #right>
        <slot name="rightFooter"></slot>
      </template>
    </PageFooter>
  </div>
</template>
<script lang="ts" setup>
import type { CSSProperties } from 'vue';
import { theme } from 'ant-design-vue';
import { omit as _omit } from 'lodash-es';
import PageFooter from './PageFooter.vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useContentHeight } from '@/hooks/web/useContentHeight';
import { PageWrapperFixedHeightKey } from '../injectionKey';

defineOptions({
  name: 'PageWrapper',
});
const props = defineProps({
  title: String,
  dense: Boolean,
  ghost: Boolean,
  headerSticky: Boolean,
  headerStyle: Object as PropType<CSSProperties>,
  content: String,
  contentStyle: Object as PropType<CSSProperties>,
  contentBackground: Boolean,
  contentFullHeight: {
    type: Boolean,
    default: false,
  },
  contentClass: String,
  fixedHeight: Boolean,
  upwardSpace: {
    type: [Number, String],
    default: 0,
  },
});

const attrs = useAttrs();
const slots = useSlots();

const wrapperRef = ref(null);
const headerRef = ref(null);
const contentRef = ref(null);
const footerRef = ref(null);
const { prefixCls } = useDesign('page-wrapper');

provide(
  PageWrapperFixedHeightKey,
  computed(() => props.fixedHeight),
);

const getIsContentFullHeight = computed(() => {
  return props.contentFullHeight;
});

const getUpwardSpace = computed(() => props.upwardSpace);
const { redoHeight, setCompensation, contentHeight } = useContentHeight(
  getIsContentFullHeight,
  wrapperRef,
  [headerRef, footerRef],
  [contentRef],
  getUpwardSpace,
);
setCompensation({ useLayoutFooter: true, elements: [footerRef] });

const getClass = computed(() => {
  return [
    prefixCls,
    {
      [`${prefixCls}--dense`]: props.dense,
    },
    attrs.class ?? {},
  ];
});
const useLayoutHeight = inject('USE_LAYOUT_HEIGHT') as any;
const { headerHeightRef } = useLayoutHeight();
const getHeaderStyle = computed((): CSSProperties => {
  const { headerSticky } = props;
  if (!headerSticky) {
    return {};
  }

  return {
    position: 'sticky',
    top: `${unref(headerHeightRef)}px`,
    ...props.headerStyle,
  };
});

const getShowHeader = computed(() => props.content || slots?.headerContent || props.title || getHeaderSlots.value.length);

const getShowFooter = computed(() => slots?.leftFooter || slots?.rightFooter);

const getHeaderSlots = computed(() => {
  return Object.keys(_omit(slots, 'default', 'leftFooter', 'rightFooter', 'headerContent'));
});

const getContentStyle = computed((): CSSProperties => {
  const { contentFullHeight, contentStyle, fixedHeight } = props;
  if (!contentFullHeight) {
    return { ...contentStyle };
  }

  const height = `${unref(contentHeight)}px`;
  return {
    ...contentStyle,
    minHeight: height,
    ...(fixedHeight ? { height } : {}),
  };
});

const getContentClass = computed(() => {
  const { contentBackground, contentClass } = props;
  return [
    `${prefixCls}-content`,
    contentClass,
    {
      [`${prefixCls}-content-bg`]: contentBackground,
    },
  ];
});

watch(
  () => [getShowFooter.value],
  () => {
    redoHeight();
  },
  {
    flush: 'post',
    immediate: true,
  },
);

const { useToken } = theme;
const { token } = useToken();
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-page-wrapper';

.@{prefix-cls} {
  position: relative;

  .@{prefix-cls}-content {
    margin: 16px;
  }

  .ant-page-header {
    &:empty {
      padding: 0;
    }
  }

  &-content-bg {
    background-color: v-bind('token["component-background"]');
  }

  &--dense {
    .@{prefix-cls}-content {
      margin: 0;
    }
  }
}
</style>
