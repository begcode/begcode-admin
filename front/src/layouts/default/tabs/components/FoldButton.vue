<template>
  <span :class="`${prefixCls}__extra-fold`" @click="handleFold">
    <Icon :icon="getIcon" :size="18" />
  </span>
</template>
<script lang="ts" setup>
import { unref, computed } from 'vue';
import { Icon } from '@begcode/components';

import { useDesign } from '@begcode/components';
import { useHeaderSetting } from '@/hooks/setting/useHeaderSetting';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { triggerWindowResize } from '@begcode/components';

defineOptions({ name: 'FoldButton' });

const { prefixCls } = useDesign('multiple-tabs-content');
const { getShowMenu, setMenuSetting } = useMenuSetting();
const { getShowHeader, setHeaderSetting } = useHeaderSetting();

const getIsUnFold = computed(() => !unref(getShowMenu) && !unref(getShowHeader));

const getIcon = computed(() => (unref(getIsUnFold) ? 'codicon:screen-normal' : 'codicon:screen-full'));

function handleFold() {
  const isUnFold = unref(getIsUnFold);
  setMenuSetting({
    show: isUnFold,
    hidden: !isUnFold,
  });
  setHeaderSetting({ show: isUnFold });
  triggerWindowResize();
}
</script>
