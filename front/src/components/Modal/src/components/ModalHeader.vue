<template>
  <basic-title :helpMessage="helpMessage">
    {{ title }}
  </basic-title>
</template>
<script lang="ts" setup>
defineOptions({ name: 'BasicModalHeader' });

defineProps({
  helpMessage: {
    type: [String, Array] as PropType<string | string[]>,
  },
  title: { type: String },
});
</script>
