<template>
  <PageWrapper>
    <template #headerContent> <WorkbenchHeader /> </template>
    <div class="lg:flex">
      <div class="lg:w-7/10 w-full !mr-4 enter-y">
        <ProjectCard :loading="loading" class="enter-y" />
        <DynamicInfo :loading="loading" class="!my-4 enter-y" />
      </div>
      <div class="lg:w-3/10 w-full enter-y">
        <QuickNav :loading="loading" class="enter-y" />

        <a-card class="!my-4 enter-y" :loading="loading">
          <img class="xl:h-50 h-30 mx-auto" :src="illustratioSvg" />
        </a-card>

        <SaleRadar :loading="loading" class="enter-y" />
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup>
import { PageWrapper } from '@/components/Page';
import WorkbenchHeader from './components/WorkbenchHeader.vue';
import ProjectCard from './components/ProjectCard.vue';
import QuickNav from './components/QuickNav.vue';
import DynamicInfo from './components/DynamicInfo.vue';
import SaleRadar from './components/SaleRadar.vue';
import illustratioSvg from '@/assets/svg/illustration.svg';

const loading = ref(true);

setTimeout(() => {
  loading.value = false;
}, 500);
</script>
