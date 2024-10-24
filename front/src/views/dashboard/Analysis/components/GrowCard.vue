<template>
  <div class="md:flex">
    <template v-for="(item, index) in growCardList" :key="item.title">
      <a-card
        size="small"
        :loading="loading"
        :title="item.title"
        class="md:w-1/4 w-full !md:mt-0 !mt-4"
        :class="[index + 1 < 4 && '!md:mr-4']"
        :canExpand="false"
      >
        <template #extra>
          <a-tag :color="item.color">{{ item.action }}</a-tag>
        </template>

        <div class="py-4 px-4 flex justify-between">
          <CountTo prefix="$" :startVal="1" :endVal="item.value" class="text-2xl" />
          <Icon :icon="item.icon" :size="40" />
        </div>

        <div class="p-2 px-4 flex justify-between">
          <span>总{{ item.title }}</span>
          <CountTo prefix="$" :startVal="1" :endVal="item.total" />
        </div>
      </a-card>
    </template>
  </div>
</template>
<script lang="ts" setup>
import { growCardList } from '../data';

defineProps({
  loading: {
    type: Boolean,
  },
});
</script>
