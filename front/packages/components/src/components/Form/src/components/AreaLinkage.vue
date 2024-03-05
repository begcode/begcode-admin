<template>
  <Cascader v-bind="attrs" :value="state" :options="getOptions" @change="handleChange" />
</template>
<script lang="ts">
import { defineComponent, ref, watchEffect, computed, type WritableComputedRef, type DeepReadonly } from 'vue';
import { Cascader } from 'ant-design-vue';
import { provinceAndCityData, regionData, provinceAndCityDataPlus, regionDataPlus } from '../utils/areaDataUtil';
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { propTypes } from '@/utils/propTypes';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { ValueType } from 'ant-design-vue/es/vc-cascader/Cascader';
import { FormItemContext } from 'ant-design-vue/es/form/FormItemContext';
export default defineComponent({
  name: 'AreaLinkage',
  components: {
    Cascader,
  },
  inheritAttrs: false,
  props: {
    value: propTypes.oneOfType([propTypes.object, propTypes.array, propTypes.string]),
    //是否显示区县
    showArea: propTypes.bool.def(true),
    //是否是全部
    showAll: propTypes.bool.def(false),
  },
  emits: ['options-change', 'change'],
  setup(props) {
    const emitData = ref<any[]>([]);
    const attrs = useAttrs();
    const [state] = useRuleFormItem(props, 'value', 'change', emitData) as [
      WritableComputedRef<ValueType>,
      (val: string) => void,
      DeepReadonly<any>,
      FormItemContext,
    ];
    const getOptions = computed(() => {
      if (props.showArea && props.showAll) {
        return regionDataPlus;
      }
      if (props.showArea && !props.showAll) {
        return regionData;
      }
      if (!props.showArea && !props.showAll) {
        return provinceAndCityData;
      }
      if (!props.showArea && props.showAll) {
        return provinceAndCityDataPlus;
      }
    });
    /**
     * 监听value变化
     */
    watchEffect(() => {
      props.value && initValue();
    });

    /**
     * 将字符串值转化为数组
     */
    function initValue() {
      let value = props.value ? props.value : [];
      if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
        state.value = value.split(',');
      }
    }

    function handleChange(array) {
      // emitData.value = args;
      state.value = array;
      console.info(emitData);
    }
    return {
      state,
      attrs,
      regionData,
      getOptions,
      handleChange,
    };
  },
});
</script>
