<template>
  <div @click="openDrawer(true)">
    <Icon icon="ion:settings-outline" />
    <SettingDrawer @register="register" />
  </div>
</template>
<script lang="ts" setup>
import SettingDrawer from './SettingDrawer';

import { useDrawer, Icon } from '@begcode/components';

defineOptions({ name: 'SettingButton' });

const [register, { openDrawer }] = useDrawer();
</script>
