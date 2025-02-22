<template>
  <a-form-item-rest>
    <div class="area-select">
      <!--省份-->
      <a-select v-model:value="province" @change="proChange" allowClear :disabled="disabled">
        <template v-for="item in provinceOptions" :key="`${item.value}`">
          <a-select-option :value="item.value">{{ item.label }}</a-select-option>
        </template>
      </a-select>
      <!--城市-->
      <a-select v-if="level >= 2" v-model:value="city" @change="cityChange" :disabled="disabled">
        <template v-for="item in cityOptions" :key="`${item.value}`">
          <a-select-option :value="item.value">{{ item.label }}</a-select-option>
        </template>
      </a-select>
      <!--地区-->
      <a-select v-if="level >= 3" v-model:value="area" @change="areaChange" :disabled="disabled">
        <template v-for="item in areaOptions" :key="`${item.value}`">
          <a-select-option :value="item.value">{{ item.label }}</a-select-option>
        </template>
      </a-select>
    </div>
  </a-form-item-rest>
</template>
<script lang="ts">
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { provinceOptions, getDataByCode, getRealCode } from '../utils/areaDataUtil';

export default defineComponent({
  name: 'AreaSelect',
  props: {
    value: {
      type: [Array, String] as PropType<string | string[]>,
    },
    province: [String],
    city: [String],
    area: [String],
    level: {
      type: Number,
      default: 3,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    codeField: {
      type: String,
    },
    size: {
      type: String,
    },
    placeholder: {
      type: String,
    },
    formValues: {
      type: [String, Number, Object, Array, Boolean, Function],
    },
    allowClear: {
      type: Boolean,
      default: false,
    },
    getPopupContainer: {
      type: Function,
      default: node => node?.parentNode,
    },
  },
  emits: ['change', 'update:value', 'update:area', 'update:city', 'update:province'],
  setup(props, { emit }) {
    const emitData = ref<any[]>([]);
    //下拉框的选择值
    const pca = reactive({
      province: '',
      city: '',
      area: '',
    });
    //表单值
    const [state] = useRuleFormItem(props, 'value', 'change', emitData);
    //城市下拉框的选项
    const cityOptions = computed(() => {
      return pca.province ? getDataByCode(pca.province) : [];
    });
    //地区下拉框的选项
    const areaOptions = computed(() => {
      return pca.city ? getDataByCode(pca.city) : [];
    });
    /**
     * 监听props值
     */
    watchEffect(() => {
      props && initValue();
    });

    /**
     * 监听组件值变化
     */
    watch(pca, () => {
      if (!props.value) {
        emit('update:province', pca.province);
        emit('update:city', pca.city);
        emit('update:area', pca.area);
      }
    });
    /**
     * 数据初始化
     */
    function initValue() {
      if (props.value) {
        //传参是数组的情况下的处理
        if (Array.isArray(props.value)) {
          pca.province = props.value[0];
          pca.city = props.value[1] ? props.value[1] : '';
          pca.area = props.value[2] ? props.value[2] : '';
        } else {
          //传参是数值
          let valueArr = getRealCode(props.value, props.level);
          if (valueArr) {
            pca.province = valueArr[0];
            pca.city = props.level >= 2 && valueArr[1] ? valueArr[1] : '';
            pca.area = props.level >= 3 && valueArr[2] ? valueArr[2] : '';
          }
        }
      } else {
        //绑定三个数据的情况
        pca.province = props.province ? props.province : '';
        pca.city = props.city ? props.city : '';
        pca.area = props.area ? props.area : '';
      }
    }

    /**
     * 省份change事件
     */
    function proChange(val) {
      pca.city = val && getDataByCode(val)[0]?.value;
      pca.area = pca.city && getDataByCode(pca.city)[0]?.value;
      state.value = props.level <= 1 ? val : props.level <= 2 ? pca.city : pca.area;
      emit('update:value', unref(state));
    }

    /**
     * 城市change事件
     */
    function cityChange(val) {
      pca.area = val && getDataByCode(val)[0]?.value;
      state.value = props.level <= 2 ? val : pca.area;
      emit('update:value', unref(state));
    }

    /**
     * 区域change事件
     */
    function areaChange(val) {
      state.value = val;
      emit('update:value', unref(state));
    }

    return {
      ...toRefs(pca),
      provinceOptions,
      cityOptions,
      areaOptions,
      proChange,
      cityChange,
      areaChange,
    };
  },
});
</script>
<style scoped>
.area-select {
  width: 100%;
}
.area-select .ant-select {
  width: calc(33.3% - 7px);
}
.area-select .ant-select:not(:first-child) {
  margin-left: 10px;
}
</style>
