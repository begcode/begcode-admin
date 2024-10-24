<!--
 * @Description: 右侧属性面板控件 表单属性面板
-->
<template>
  <div class="properties-content">
    <a-form class="properties-body" label-align="left" layout="vertical">
      <!--      <e-upload v-model="fileList"></e-upload>-->

      <a-form-item label="表单布局">
        <a-radio-group button-style="solid" v-model:value="formConfig.layout">
          <a-radio-button value="horizontal">水平</a-radio-button>
          <a-radio-button value="vertical" :disabled="formConfig.labelLayout === 'Grid'"> 垂直 </a-radio-button>
          <a-radio-button value="inline" :disabled="formConfig.labelLayout === 'Grid'"> 行内 </a-radio-button>
        </a-radio-group>
      </a-form-item>

      <!-- <a-row> -->
      <a-form-item label="标签布局">
        <a-radio-group buttonStyle="solid" v-model:value="formConfig.labelLayout" @change="lableLayoutChange">
          <a-radio-button value="flex">固定</a-radio-button>
          <a-radio-button value="Grid" :disabled="formConfig.layout !== 'horizontal'"> 栅格 </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <!-- </a-row> -->
      <a-form-item label="标签宽度（px）" v-show="formConfig.labelLayout === 'flex'">
        <a-input-number :style="{ width: '100%' }" v-model:value="formConfig.labelWidth" :min="0" :step="1" />
      </a-form-item>
      <div v-if="formConfig.labelLayout === 'Grid'">
        <a-form-item label="labelCol">
          <a-slider v-model:value="sliderSpan" :max="24" />
        </a-form-item>
        <a-form-item label="wrapperCol">
          <a-slider v-model:value="sliderSpan" :max="24" />
        </a-form-item>

        <a-form-item label="标签对齐">
          <a-radio-group button-style="solid" v-model:value="formConfig.labelAlign">
            <a-radio-button value="left">靠左</a-radio-button>
            <a-radio-button value="right">靠右</a-radio-button>
          </a-radio-group>
        </a-form-item>

        <a-form-item label="控件大小">
          <a-radio-group button-style="solid" v-model:value="formConfig.size">
            <a-radio-button value="default">默认</a-radio-button>
            <a-radio-button value="small">小</a-radio-button>
            <a-radio-button value="large">大</a-radio-button>
          </a-radio-group>
        </a-form-item>
      </div>
      <a-form-item label="表单属性">
        <a-col
          ><a-checkbox v-model:checked="formConfig.colon" v-if="formConfig.layout == 'horizontal'">label后面显示冒号</a-checkbox></a-col
        >
        <a-col><a-checkbox v-model:checked="formConfig.disabled">禁用</a-checkbox></a-col>
        <a-col><a-checkbox v-model:checked="formConfig.hideRequiredMark">隐藏必选标记</a-checkbox></a-col>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { useFormDesignState } from '../../../hooks/useFormDesignState';
import type { RadioChangeEvent } from 'ant-design-vue';

defineOptions({
  name: 'FormProps',
});
const { formConfig } = useFormDesignState();

formConfig.value = formConfig.value || {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 },
};

const lableLayoutChange = (e: RadioChangeEvent) => {
  if (e.target.value === 'Grid') {
    formConfig.value.layout = 'horizontal';
  }
};

const sliderSpan = computed(() => {
  if (formConfig.value.labelLayout) {
    return Number(formConfig.value.labelCol!.span);
  }
  return 0;
});
</script>
