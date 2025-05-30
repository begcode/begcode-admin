<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts" setup>
import type { EChartsOption } from 'echarts';
import { useECharts } from '@/hooks/web/useECharts';

defineOptions({
  name: 'Radar',
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
});

const chartRef = ref<HTMLDivElement | null>(null);
const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
const option = reactive({
  title: {
    text: '基础雷达图',
  },
  legend: {
    data: ['文综'],
  },
  radar: {
    indicator: [{ name: '历史' }, { name: '地理' }, { name: '生物' }, { name: '化学' }, { name: '物理' }, { name: '政治' }] as any[],
  } as any,
  series: [
    {
      type: 'radar' as 'custom',
      data: [
        {
          value: [82, 70, 60, 55, 90, 66],
          name: '文综',
        },
      ],
    },
  ] as any[],
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
  //雷达数据
  let indicator = Array.from(
    new Set(
      props.chartData.map(item => {
        let { name, max } = item;
        return { name, max };
      }),
    ),
  );

  let data: any[] = [];
  typeArr.forEach(type => {
    let obj = { name: type };
    let chartArr = props.chartData.filter(item => type === item.type);
    obj['value'] = chartArr.map(item => item.value);
    //data数据
    data.push(obj);
  });
  option.radar.axisName = indicator;
  option.series[0]['data'] = data;
  setOptions(option as EChartsOption);
}
</script>
