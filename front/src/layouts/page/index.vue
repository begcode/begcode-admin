<template>
  <RouterView>
    <template #default="{ Component, route }">
      <keep-alive v-if="openCache" :include="getCaches">
        <suspense>
          <template #default>
            <component :is="Component" :key="route.fullPath" />
          </template>
          <template #fallback>
            <h1>Loading...</h1>
          </template>
        </suspense>
      </keep-alive>
      <component v-else :is="Component" :key="route.fullPath" />
    </template>
  </RouterView>
  <FrameLayout v-if="getCanEmbedIFramePage" />
</template>

<script lang="ts" setup>
import { computed, unref } from 'vue';

import FrameLayout from '@/layouts/iframe/index.vue';

import { useRootSetting } from '@/hooks/setting/useRootSetting';

import { useTransitionSetting } from '@/hooks/setting/useTransitionSetting';
import { useMultipleTabSetting } from '@/hooks/setting/useMultipleTabSetting';
import { getTransitionName } from './transition';

import { useMultipleTabStore } from '@/store/modules/multipleTab';

defineOptions({ name: 'PageLayout' });

const { getShowMultipleTab } = useMultipleTabSetting();
const tabStore = useMultipleTabStore();

const { getOpenKeepAlive, getCanEmbedIFramePage } = useRootSetting();

const { getBasicTransition, getEnableTransition } = useTransitionSetting();

const openCache = computed(() => unref(getOpenKeepAlive) && unref(getShowMultipleTab));

const getCaches = computed((): string[] => {
  if (!unref(getOpenKeepAlive)) {
    return [];
  }
  return tabStore.getCachedTabList;
});
</script>
