<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import { useECharts } from '@/hooks/web/useECharts';
import { basicProps } from './props';
import { useRootSetting } from '@/hooks/setting/useRootSetting';

defineProps({
  ...basicProps,
});

const chartRef = ref<HTMLDivElement | null>(null);
const { getThemeColor } = useRootSetting();
const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
const init = () => {
  setOptions({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        lineStyle: {
          width: 1,
          color: getThemeColor.value,
        },
      },
    },
    grid: { left: '1%', right: '1%', top: '2  %', bottom: 0, containLabel: true },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    },
    yAxis: {
      type: 'value',
      max: 8000,
      splitNumber: 4,
    },
    series: [
      {
        data: [3000, 2000, 3333, 5000, 3200, 4200, 3200, 2100, 3000, 5100, 6000, 3200, 4800],
        type: 'bar',
        barMaxWidth: 80,
        color: getThemeColor.value,
      },
    ],
  });
};

watchEffect(() => {
  init();
});
</script>
