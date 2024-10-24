<template>
  <span class="thumb">
    <a-image v-if="fileUrl" :src="fileUrl" :width="104" />
  </span>
</template>
<script lang="ts" setup>
defineProps({
  fileUrl: {
    type: String,
    default: '',
  },
  fileName: {
    type: String,
    default: '',
  },
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
