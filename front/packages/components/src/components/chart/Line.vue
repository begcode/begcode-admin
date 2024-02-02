<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import { defineComponent, PropType, ref, Ref, reactive, watchEffect } from 'vue';
import { useECharts } from '@/hooks/web/useECharts';

defineOptions({
  name: 'Line',
});

const props = defineProps({
  chartData: {
    type: Array,
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
});

const chartRef = ref<HTMLDivElement | null>(null);
const { setOptions, echarts } = useECharts(chartRef as Ref<HTMLDivElement>);
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
    data: [],
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
    },
  ],
});

watchEffect(() => {
  props.chartData && initCharts();
});

function initCharts() {
  if (props.option) {
    Object.assign(option, props.option);
  }
  let seriesData = props.chartData.map(item => {
    return item.value;
  });
  let xAxisData = props.chartData.map(item => {
    return item.name;
  });
  option.series[0].data = seriesData;
  option.xAxis.data = xAxisData;
  setOptions(option);
}
</script>
