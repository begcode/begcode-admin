<template>
  <div>
    <IndexDef v-if="indexStyle === 0"></IndexDef>
    <IndexChart v-if="indexStyle === 1"></IndexChart>
    <IndexBdc v-if="indexStyle == 2"></IndexBdc>
    <div style="width: 100%; text-align: right; margin-top: 20px">
      请选择首页样式：
      <RadioGroup v-model:value="indexStyle">
        <Radio :value="0">默认</Radio>
        <Radio :value="1">销量统计</Radio>
        <Radio :value="2">业务统计</Radio>
      </RadioGroup>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';
import { RadioGroup, Radio } from 'ant-design-vue';
import IndexDef from './homePage/IndexDef.vue';
import IndexChart from './homePage/IndexChart.vue';
import IndexBdc from './homePage/IndexBdc.vue';

const indexStyle = ref(0);
</script>
