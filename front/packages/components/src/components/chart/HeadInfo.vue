<template>
  <div class="head-info" :class="center && 'center'">
    <span>{{ title }}</span>
    <p :style="{ color: `${iconColor}` }">
      {{ content }}
      <Icon :type="icon" :style="{ fontSize: `24px`, color: `${iconColor}` }" />
    </p>
    <em v-if="bordered" />
  </div>
</template>

<script lang="ts" setup>
import { Icon } from '@/components/Icon';

defineOptions({
  name: 'HeadInfo',
});

defineProps({
  title: {
    type: String,
    default: '',
  },
  content: {
    type: String,
    default: '',
  },
  bordered: {
    type: Boolean,
    default: false,
  },
  center: {
    type: Boolean,
    default: true,
  },
  icon: {
    type: String,
    default: false,
  },
  iconColor: {
    type: String,
    default: '#2b99ff',
  },
});
</script>

<style scoped>
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;
}
.head-info.center {
  text-align: center;
  padding: 0 32px;
}
.head-info span {
  display: inline-block;
  font-size: 14px;
  line-height: 22px;
  margin-bottom: 4px;
}
.head-info p {
  font-size: 24px;
  line-height: 32px;
  margin: 0;
}
.head-info em {
  background-color: #e8e8e8;
  position: absolute;
  height: 56px;
  width: 1px;
  top: 0;
  right: 0;
}
</style>
