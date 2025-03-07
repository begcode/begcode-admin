<template>
  <SvgIcon :size="size" :name="getSvgIcon" v-if="isSvgIcon" :class="[$attrs.class, 'anticon']" :spin="spin" />
  <icon-font :class="[$attrs.class || 'item-icon']" :type="getIconFont" v-else-if="isIconFont" />
  <span v-else ref="elRef" :class="[$attrs.class, 'app-iconify anticon', spin && 'app-iconify-spin']" :style="getWrapStyle"></span>
</template>
<script lang="ts" setup>
import Iconify from '@purge-icons/generated';
import { createFromIconfontCN } from '@ant-design/icons-vue';
import SvgIcon from './src/SvgIcon.vue';

const SVG_END_WITH_FLAG = '|svg';
const FONT_END_WITH_FLAG = '|font';
const IconFont = createFromIconfontCN({
  scriptUrl: '//at.alicdn.com/t/font_2316098_umqusozousr.js',
});

defineOptions({ name: 'BIcon' });

const props = defineProps({
  // icon name
  icon: {
    type: String,
  },
  // icon color
  color: {
    type: String,
  },
  // icon size
  size: {
    type: [String, Number] as PropType<string | number>,
    default: 16,
  },
  spin: {
    type: Boolean,
    default: false,
  },
  prefix: {
    type: String,
    default: '',
  },
});

const elRef = ref(null);

const isSvgIcon = computed(() => props.icon?.endsWith(SVG_END_WITH_FLAG));
const isIconFont = computed(() => props.icon?.endsWith(FONT_END_WITH_FLAG));
const getSvgIcon = computed(() => props.icon.replace(SVG_END_WITH_FLAG, ''));
const getIconFont = computed(() => props.icon.replace(FONT_END_WITH_FLAG, ''));
const getIconRef = computed(() => `${props.prefix ? props.prefix + ':' : ''}${props.icon}`);

const update = async () => {
  if (unref(isSvgIcon)) return;
  const el: any = unref(elRef);
  if (!el) return;
  await nextTick();
  const icon = unref(getIconRef);
  if (!icon) return;
  const svg = Iconify.renderSVG(icon, {});
  if (svg) {
    el.textContent = '';
    el.appendChild(svg);
  } else {
    const span = document.createElement('span');
    span.className = 'iconify';
    span.dataset.icon = icon;
    el.textContent = '';
    el.appendChild(span);
  }
};

const getWrapStyle = computed((): CSSProperties => {
  const { size, color } = props;
  let fs = size;
  if (_isString(size)) {
    fs = parseInt(size, 10);
  }
  return {
    fontSize: `${fs}px`,
    color: color,
    display: 'inline-flex',
  };
});

watch(() => props.icon, update, { flush: 'post' });

onMounted(update);
</script>
<style>
.app-iconify {
  display: inline-block;
}
.app-iconify-spin svg {
  animation: loadingCircle 1s infinite linear;
}
span.iconify {
  display: block;
  min-width: 1em;
  min-height: 1em;
  border-radius: 100%;
}
</style>
