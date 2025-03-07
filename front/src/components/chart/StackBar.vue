<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import type { EChartsOption } from 'echarts';
import { useECharts } from '@/hooks/web/useECharts';

defineOptions({
  name: 'StackBar',
});

const props = defineProps({
  chartData: {
    type: Array as PropType<any[]>,
    default: () => [],
    required: true,
  },
  size: {
    type: Object,
    default: () => {},
  },
  option: {
    type: Object,
    default: () => ({}),
  },
  type: {
    type: String as PropType<string>,
    default: 'bar',
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
const { setOptions, resize } = useECharts(chartRef as Ref<HTMLDivElement>);
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
  legend: {
    top: 30,
  },
  grid: {
    top: 60,
  },
  xAxis: {
    type: 'value',
  },
  yAxis: {
    type: 'category',
    data: [] as any[],
  },
  series: [] as any[],
});

watchEffect(() => {
  props.chartData && initCharts();
});
/**
 * 监听拖拽大小变化
 */
watch(
  () => props.size,
  () => {
    console.log('props.size', props.size);
    resize();
  },
  {
    immediate: true,
  },
);
function initCharts() {
  if (props.option) {
    Object.assign(option, _cloneDeep(props.option));
  }
  //图例类型
  let typeArr = Array.from(new Set(props.chartData.map(item => item.type)));
  //轴数据
  let yAxisData = Array.from(new Set(props.chartData.map(item => item.name)));
  let seriesData: any[] = [];
  typeArr.forEach(type => {
    let obj = { name: type, type: props.type, stack: 'total' };
    let chartArr = props.chartData.filter(item => type === item.type);
    //data数据
    obj['data'] = chartArr.map(item => item.value);
    seriesData.push(obj);
  });
  option.series = seriesData;
  option.yAxis.data = yAxisData;
  setOptions(option as EChartsOption);
}
</script>
