<template>
  <span class="thumb">
    <Image v-if="fileUrl" :src="fileUrl" :width="104" />
  </span>
</template>
<script lang="ts" setup>
import { propTypes } from '@/utils/propTypes';
import { Image } from 'ant-design-vue';

defineProps({
  fileUrl: propTypes.string.def(''),
  fileName: propTypes.string.def(''),
});
</script>
<style>
.thumb img {
  position: static;
  display: block;
  cursor: zoom-in;
  border-radius: 4px;
  object-fit: cover;
}
.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
