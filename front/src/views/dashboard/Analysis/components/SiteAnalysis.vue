<template>
  <a-card :tab-list="tabListTitle" v-bind="$attrs" :active-tab-key="activeKey" @tabChange="onTabChange">
    <p v-if="activeKey === 'tab1'">
      <VisitAnalysis />
    </p>
    <p v-if="activeKey === 'tab2'">
      <VisitAnalysisBar />
    </p>
  </a-card>
</template>
<script lang="ts" setup>
import VisitAnalysis from './VisitAnalysis.vue';
import VisitAnalysisBar from './VisitAnalysisBar.vue';

const activeKey = ref('tab1');

const tabListTitle = [
  {
    key: 'tab1',
    tab: '流量趋势',
  },
  {
    key: 'tab2',
    tab: '访问量',
  },
];

function onTabChange(key) {
  activeKey.value = key;
}
</script>
