<template>
  <div class="chart-trend">
    {{ term }}
    <span>{{ rate }}%</span>
    <span :class="['trend-icon', trend]"><Icon :icon="'ant-design:caret-' + trend + '-outlined'" /></span>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import Icon from '@/components/Icon/Icon.vue';

defineOptions({
  name: 'Trend',
});

const props = defineProps({
  // 同title
  term: {
    type: String,
    default: '',
    required: true,
  },
  // 百分比
  percentage: {
    type: Number,
    default: null,
  },
  type: {
    type: Boolean,
    default: null,
  },
  target: {
    type: Number,
    default: 0,
  },
  value: {
    type: Number,
    default: 0,
  },
  fixed: {
    type: Number,
    default: 2,
  },
});

const trend = computed(() => {
  let type = props.type === null ? props.value >= props.target : props.type;
  return type ? 'up' : 'down';
});
const rate = computed(() =>
  (props.percentage === null ? (Math.abs(props.value - props.target) * 100) / props.target : props.percentage).toFixed(props.fixed),
);
</script>

<style scoped>
.chart-trend {
  display: inline-block;
  font-size: 14px;
  line-height: 22px;
}
.chart-trend .trend-icon {
  font-size: 12px;
}
.chart-trend .trend-icon.up,
.chart-trend .trend-icon.down {
  margin-left: 4px;
  position: relative;
  top: 1px;
}
.chart-trend .trend-icon.up i,
.chart-trend .trend-icon.down i {
  font-size: 12px;
  transform: scale(0.83);
}
.chart-trend .trend-icon.up {
  color: #f5222d;
}
.chart-trend .trend-icon.down {
  color: #52c41a;
  top: -1px;
}
</style>
