<template>
  <div>
    <IndexDef v-if="indexStyle === 0"></IndexDef>
    <IndexChart v-if="indexStyle === 1"></IndexChart>
    <IndexBdc v-if="indexStyle == 2"></IndexBdc>
    <div style="width: 100%; text-align: right; margin-top: 20px">
      请选择首页样式：
      <a-radio-group v-model:value="indexStyle">
        <a-radio :value="0">默认</a-radio>
        <a-radio :value="1">销量统计</a-radio>
        <a-radio :value="2">业务统计</a-radio>
      </a-radio-group>
    </div>
  </div>
</template>
<script lang="ts" setup>
import IndexDef from './homePage/IndexDef.vue';
import IndexChart from './homePage/IndexChart.vue';
import IndexBdc from './homePage/IndexBdc.vue';

const indexStyle = ref(0);
</script>
