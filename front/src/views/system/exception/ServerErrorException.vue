<template>
  <Exception :status="status" />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { Exception } from '@/views/system/exception/index';
import { ExceptionEnum } from '@/enums/exceptionEnum';

const status = ref(ExceptionEnum.ERROR);
</script>
