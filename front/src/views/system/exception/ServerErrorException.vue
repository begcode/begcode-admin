<template>
  <Exception :status="status" />
</template>

<script lang="ts" setup>
import { Exception } from '@/views/system/exception/index';
import { ExceptionEnum } from '@/enums/exceptionEnum';

const status = ref(ExceptionEnum.ERROR);
</script>
