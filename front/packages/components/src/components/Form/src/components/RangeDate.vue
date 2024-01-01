<template>
  <a-row justify="space-around">
    <a-col :span="11">
      <a-date-picker
        v-model:value="startDate"
        @change="handleChangeStart"
        :show-time="datetime"
        :placeholder="placeholder[0]"
        :valueFormat="valueFormatConfig[0]"
        :format="format"
        style="display: inline-block; width: 100%"
      />
    </a-col>
    <a-col :span="2" style="text-align: center; margin: auto">--</a-col>
    <a-col :span="11">
      <a-date-picker
        v-model:value="endDate"
        @change="handleChangeEnd"
        :show-time="datetime"
        :placeholder="placeholder[1]"
        :format="format"
        :valueFormat="valueFormatConfig[1]"
        style="display: inline-block; width: 100%"
      />
    </a-col>
  </a-row>
</template>

<script>
import { defineComponent, ref, watch, unref, computed } from 'vue';
import { propTypes } from '@/utils/propTypes';
import { Form } from 'ant-design-vue';
import dayjs from 'dayjs';

const placeholder = ['开始日期', '结束日期'];
/**
 * 用于范围查询
 */
export default defineComponent({
  name: 'JRangeDate',
  props: {
    value: propTypes.oneOfType([propTypes.string, propTypes.array]).def([]),
    datetime: propTypes.bool.def(false),
    placeholder: propTypes.string.def(''),
    format: propTypes.string.def('YYYY-MM-DD'),
    valueFormat: propTypes.string.def('YYYY-MM-DD HH:mm:ss'),
  },
  emits: ['change', 'update:value'],
  setup(props, { emit }) {
    const startDate = ref(null);
    const endDate = ref(null);
    const formItemContext = Form.useInjectFormItemContext();

    watch(
      () => props.value,
      val => {
        if (val instanceof String && val) {
          const dateRange = unref(val).split(',');
          startDate.value = dateRange[0];
          endDate.value = dateRange[1];
        } else if (val instanceof Array) {
          if (val.length > 1) {
            startDate.value = val[0];
            endDate.value = val[1];
          } else if (val.length === 1) {
            startDate.value = val[0];
          }
        }
      },
      { immediate: true },
    );

    const valueFormatConfig = computed(() => {
      if (!props.valueFormat) {
        if (props.datetime === true) {
          return ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD HH:mm:ss'];
        } else {
          return ['YYYY-MM-DD', 'YYYY-MM-DD'];
        }
      } else {
        if (props.valueFormat instanceof String && props.valueFormat) {
          return [props.valueFormat, props.valueFormat];
        }
        if (props.valueFormat instanceof Array) {
          if (props.valueFormat.length === 0) {
            if (props.datetime === true) {
              return ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD HH:mm:ss'];
            } else {
              return ['YYYY-MM-DD', 'YYYY-MM-DD'];
            }
          } else if (props.valueFormat.length === 1) {
            return [props.valueFormat[0], props.valueFormat[0]];
          } else {
            return props.valueFormat;
          }
        }
        if (props.datetime === true) {
          return ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD HH:mm:ss'];
        } else {
          return ['YYYY-MM-DD', 'YYYY-MM-DD'];
        }
      }
    });

    function handleChangeStart(value) {
      emit('change', [value, endDate.value || (value && dayjs(value).format(valueFormatConfig.value[1]))]);
      emit('update:value', [value, endDate.value || (value && dayjs(value).format(valueFormatConfig.value[1]))]);
      formItemContext.onFieldChange();
    }
    function handleChangeEnd(value) {
      emit('change', [startDate.value || (value && dayjs(value).format(valueFormatConfig.value[0])), value]);
      emit('update:value', [startDate.value || (value && dayjs(value).format(valueFormatConfig.value[0])), value]);
      formItemContext.onFieldChange();
    }
    return {
      startDate,
      endDate,
      placeholder,
      handleChangeStart,
      handleChangeEnd,
      valueFormatConfig,
    };
  },
});
</script>
