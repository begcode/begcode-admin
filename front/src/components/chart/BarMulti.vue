<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import type { EChartsOption } from 'echarts';
import { useECharts } from '@/hooks/web/useECharts';

defineOptions({
  name: 'BarMulti',
});

const props = defineProps({
  chartData: {
    type: Array as PropType<any[]>,
    default: () => [],
    required: true,
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
  seriesColor: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(['click']);

const chartRef = ref<HTMLDivElement | null>(null);
const { setOptions, getInstance } = useECharts(chartRef as Ref<HTMLDivElement>);
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
    type: 'category',
    data: [] as any[],
  },
  yAxis: {
    type: 'value',
  },
  series: [] as any[],
});

watchEffect(() => {
  props.chartData && initCharts();
});

function initCharts() {
  if (props.option) {
    Object.assign(option, _cloneDeep(props.option));
  }
  //图例类型
  let typeArr = Array.from(new Set(props.chartData.map(item => item.type)));
  //轴数据
  let xAxisData = Array.from(new Set(props.chartData.map(item => item.name)));
  let seriesData: any[] = [];
  typeArr.forEach(type => {
    let obj: any = { name: type, type: props.type };
    let data: any[] = [];
    xAxisData.forEach(x => {
      let dataArr = props.chartData.filter(item => type === item.type && item.name == x);
      if (dataArr && dataArr.length > 0) {
        data.push(dataArr[0].value);
      } else {
        data.push(null);
      }
    });
    //data数据
    obj['data'] = data;
    if (props.seriesColor?.length) {
      const findItem = props.seriesColor.find((item: any) => item.type === type);
      if (findItem?.color) {
        obj['color'] = findItem.color;
      }
    }
    seriesData.push(obj);
  });
  option.series = seriesData;
  option.xAxis.data = xAxisData;
  setOptions(option as EChartsOption);
  getInstance()?.off('click', onClick);
  getInstance()?.on('click', onClick);
}

function onClick(params) {
  emit('click', params);
}
</script>
