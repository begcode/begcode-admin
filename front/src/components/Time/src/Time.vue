<template>
  <span>{{ date }}</span>
</template>
<script lang="ts" setup>
import { useIntervalFn } from '@vueuse/shared';
import { formatToDateTime, formatToDate, dateUtil } from '@/utils/dateUtil';
import { useI18n } from '@/hooks/web/useI18nOut';

defineOptions({ name: 'Time' });

const props = defineProps({
  value: {
    type: [Number, Date, String],
    required: true,
  },
  step: {
    type: Number,
    default: 60,
  },
  mode: {
    type: String as PropType<'date' | 'datetime' | 'relative'>,
    default: 'relative',
  },
});

const ONE_SECONDS = 1000;
const ONE_MINUTES = ONE_SECONDS * 60;
const ONE_HOUR = ONE_MINUTES * 60;
const ONE_DAY = ONE_HOUR * 24;

const date = ref('');

const { t } = useI18n();

useIntervalFn(setTime, props.step * ONE_SECONDS);

watch(
  () => props.value,
  () => {
    setTime();
  },
  { immediate: true },
);

function getTime() {
  const { value } = props;
  let time = 0;
  if (_isNumber(value)) {
    const timestamp = value.toString().length > 10 ? value : value * 1000;
    time = new Date(timestamp).getTime();
  } else if (_isString(value)) {
    time = new Date(value).getTime();
  } else if (_isObject(value)) {
    time = value.getTime();
  }
  return time;
}

function setTime() {
  const { mode, value } = props;
  const time = getTime();
  if (mode === 'relative') {
    date.value = getRelativeTime(time);
  } else {
    if (mode === 'datetime') {
      date.value = formatToDateTime(value);
    } else if (mode === 'date') {
      date.value = formatToDate(value);
    }
  }
}

function getRelativeTime(timeStamp: number) {
  const currentTime = new Date().getTime();

  // Determine whether the incoming timestamp is earlier than the current timestamp
  const isBefore = dateUtil(timeStamp).isBefore(currentTime);

  let diff = currentTime - timeStamp;
  if (!isBefore) {
    diff = -diff;
  }

  let resStr = '';
  let dirStr = isBefore ? t('component.time.before') : t('component.time.after');

  if (diff < ONE_SECONDS) {
    resStr = t('component.time.just');
    // Less than or equal to 59 seconds
  } else if (diff < ONE_MINUTES) {
    resStr = parseInt((diff / ONE_SECONDS).toString()) + t('component.time.seconds') + dirStr;
    // More than 59 seconds, less than or equal to 59 minutes and 59 seconds
  } else if (diff >= ONE_MINUTES && diff < ONE_HOUR) {
    resStr = Math.floor(diff / ONE_MINUTES) + t('component.time.minutes') + dirStr;
    // More than 59 minutes and 59 seconds, less than or equal to 23 hours, 59 minutes and 59 seconds
  } else if (diff >= ONE_HOUR && diff < ONE_DAY) {
    resStr = Math.floor(diff / ONE_HOUR) + t('component.time.hours') + dirStr;
    // More than 23 hours, 59 minutes and 59 seconds, less than or equal to 29 days, 59 minutes and 59 seconds
  } else if (diff >= ONE_DAY && diff < 2623860000) {
    resStr = Math.floor(diff / ONE_DAY) + t('component.time.days') + dirStr;
    // More than 29 days, 59 minutes, 59 seconds, less than 364 days, 23 hours, 59 minutes, 59 seconds, and the incoming timestamp is earlier than the current
  } else if (diff >= 2623860000 && diff <= 31567860000 && isBefore) {
    resStr = dateUtil(timeStamp).format('MM-DD-HH-mm');
  } else {
    resStr = dateUtil(timeStamp).format('YYYY');
  }
  return resStr;
}
</script>
