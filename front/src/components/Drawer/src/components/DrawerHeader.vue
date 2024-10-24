<template>
  <BasicTitle v-if="!isDetail" :class="[prefixCls, 'is-drawer']">
    <slot name="title"></slot>
    {{ !$slots.title ? title : '' }}
  </BasicTitle>

  <div :class="[prefixCls, `${prefixCls}--detail`]" v-else>
    <span :class="`${prefixCls}__twrap`">
      <span @click="handleClose" v-if="showDetailBack">
        <Icon icon="ant-design:arrow-left-outlined" :class="`${prefixCls}__back`" />
      </span>
      <span v-if="title">{{ title }}</span>
    </span>

    <span :class="`${prefixCls}__toolbar`">
      <slot name="titleToolbar"></slot>
    </span>
  </div>
</template>
<script lang="ts" setup>
import { theme } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';

defineOptions({ name: 'BasicDrawerHeader' });

defineProps({
  isDetail: {
    type: Boolean,
  },
  showDetailBack: {
    type: Boolean,
  },
  title: {
    type: String,
  },
});

const emit = defineEmits(['close']);

const { useToken } = theme;
const { token } = useToken();

const { prefixCls } = useDesign('basic-drawer-header');

function handleClose() {
  emit('close');
}
</script>

<style>
.vben-basic-drawer-header {
  display: flex;
  height: 100%;
  align-items: center;
}
.vben-basic-drawer-header__back {
  padding: 0 12px;
  cursor: pointer;
}

.vben-basic-drawer-header:hover {
  color: v-bind('token["colorPrimary"]');
}

.vben-basic-drawer-header__twrap {
  flex: 1;
}

.vben-basic-drawer-header__toolbar {
  padding-right: 50px;
}
</style>
