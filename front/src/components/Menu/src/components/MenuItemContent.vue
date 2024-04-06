<template>
  <span :class="`${prefixCls}- flex items-center `">
    <img v-if="getImg" :src="getImg" class="w-18px h-18px align-top mr-2" />
    <Icon v-if="getIcon" :icon="getIcon" :size="18" :class="`${prefixCls}-wrapper__icon mr-2`" />
    {{ getI18nName }}
  </span>
</template>
<script lang="ts" setup>
import { computed } from 'vue';

import { Icon } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';
import { useDesign } from '@begcode/components';
import { contentProps } from '../props';

defineOptions({ name: 'MenuItemContent' });

const props = defineProps(contentProps);

console.log('props.item', props.item);

const { t } = useI18n();

const { prefixCls } = useDesign('basic-menu-item-content');

const getI18nName = computed(() => t(props.item?.meta?.title || props.item?.name));
const getIcon = computed(() => (props.item?.img ? undefined : props.item?.icon));
const getImg = computed(() => props.item?.img);

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
</script>
