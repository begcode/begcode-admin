<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import { PropType, ref, Ref, reactive, watchEffect } from 'vue';
import { useECharts } from '@/hooks/web/useECharts';
import { cloneDeep } from 'lodash-es';
import type { EChartsOption } from 'echarts';

defineOptions({
  name: 'SingleLine',
});

const props = defineProps({
  chartData: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  option: {
    type: Object,
    default: () => ({}),
  },
  width: {
    type: String as PropType<string>,
    default: '100%',
  },
  height: {
    type: String as PropType<string>,
    default: 'calc(100vh - 78px)',
  },
  seriesColor: {
    type: String,
    default: '#1890ff',
  },
});

const chartRef = ref<HTMLDivElement | null>(null);
const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
const option = reactive({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow',
      label: {
        show: true,
        backgroundColor: '#333',
      },
    },
  },
  xAxis: {
    type: 'category',
    data: [] as any[],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      type: 'line',
      showSymbol: false,
      smooth: true,
      areaStyle: {},
      data: [],
      color: props.seriesColor,
    },
  ] as any[],
});

watchEffect(() => {
  props.chartData && initCharts();
});

function initCharts() {
  if (props.option) {
    Object.assign(option, cloneDeep(props.option));
  }
  let seriesData = props.chartData.map(item => {
    return item.value;
  });
  let xAxisData = props.chartData.map(item => {
    return item.name;
  });
  option.series[0].data = seriesData;
  option.series[0].color = props.seriesColor;
  option.xAxis.data = xAxisData;
  setOptions(option as EChartsOption);
}
</script>
