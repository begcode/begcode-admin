<template>
  <span :class="`${prefixCls}__extra-fold`" @click="openDrawer(true)">
    <Icon icon="ion:settings-outline" :size="18" />
    <SettingDrawer @register="register" />
  </span>
</template>
<script lang="ts" setup>
import SettingDrawer from '@/layouts/default/setting/SettingDrawer';
import { Icon, useDrawer, useDesign } from '@begcode/components';

defineOptions({ name: 'SettingButton' });

const [register, { openDrawer }] = useDrawer();
const { prefixCls } = useDesign('multiple-tabs-content');
</script>
