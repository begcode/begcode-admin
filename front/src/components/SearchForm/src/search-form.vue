<template>
  <a-form data-cy="listSearchForm">
    <a-row :gutter="[8, 8]">
      <a-col :md="item.span || 6" v-for="(item, index) in config.fieldList.filter(field => !field.hidden)" :key="index">
        <search-form-item :field="item" />
      </a-col>
      <a-col v-for="index of formActionSlotsShow ? blankCols : 0" :key="'blank' + index" style="flex: 1"></a-col>
      <form-action v-bind="formActionConfig" @toggle-advanced="handleToggleAdvanced" v-if="config.compact && formActionSlotsShow">
        <template #[item]="data" v-for="item in Object.keys(formActionSlots)">
          <slot :name="item" v-bind="data || {}">
            <div class="mr-2 inline-block" :key="item" v-if="formActionSlots[item].includes('mathType')">
              <a-radio-group v-model:value="config.matchType" button-style="solid">
                <a-radio-button value="and">AND</a-radio-button>
                <a-radio-button value="or">OR</a-radio-button>
              </a-radio-group>
            </div>
            <div class="mr-2 ml-2 inline-block" :key="item" v-if="formActionSlots[item].includes('exportButton')">
              <a-button type="primary" button-style="solid" @click="exportClick" preIcon="ant-design:file-excel-filled">导出</a-button>
            </div>
          </slot>
        </template>
      </form-action>
    </a-row>
    <basic-modal
      title="高级搜索设置"
      :footer="null"
      :open="settingModalVisible"
      @ok="settingModalVisible = false"
      @cancel="settingModalVisible = false"
    >
      <basic-form v-bind="formConfig"></basic-form>
    </basic-modal>
    <form-action v-bind="formActionConfig" @toggle-advanced="handleToggleAdvanced" v-if="!config.compact && formActionSlotsShow">
      <template #[item]="data" v-for="item in Object.keys(formActionSlots)">
        <slot :name="item" v-bind="data || {}" v-if="formActionSlots[item].length">
          <div class="mr-2 inline-block" :key="item" v-if="formActionSlots[item].includes('exportButton')">
            <a-button button-style="solid" @click="exportClick">导出</a-button>
          </div>
        </slot>
      </template>
    </form-action>
  </a-form>
</template>

<script lang="ts" setup>
import SearchFormItem from './search-form-item.vue';
import { createFormContext } from '@/components/Form/src/hooks/useFormContext';

defineOptions({
  name: 'BSearchForm',
});

const props = defineProps({
  config: {
    type: Object,
    required: true,
    default: () => {
      return {};
    },
  },
  /*
   * 这个回调函数接收一个数组参数 即查询条件
   *
   */
  callback: {
    type: String,
    required: false,
    default: 'handleSuperQuery',
  },

  // 当前是否在加载中
  loading: {
    type: Boolean,
    default: false,
  },

  // 保存查询条件的唯一 code，通过该 code 区分
  // 默认为 null，代表以当前路由全路径为区分Code
  saveCode: {
    type: String,
    default: null,
  },
});

const emit = defineEmits(['formSearch', 'close', 'export']);

const formConfig: any = reactive({
  schemas: [
    {
      label: '条件组合关系',
      field: 'useOr',
      show: true,
      component: 'RadioButtonGroup',
      componentProps: {
        style: 'width: 100%',
        options: [
          { value: true, label: '或者' },
          { value: false, label: '并且' },
        ],
      },
    },
  ],
  model: props.config,
  showSubmitButton: true,
  showResetButton: true,
  submitButtonOptions: {
    preIcon: '',
    text: '确认',
  },
  resetButtonOptions: {
    text: '关闭',
    preIcon: '',
  },
  resetFunc() {
    settingModalVisible.value = false;
  },
  submitFunc(params) {
    console.log('submit.params', params);
    settingModalVisible.value = false;
  },
  actionColOptions: {
    span: 24,
  },
});

const formActionConfig = reactive({
  isAdvanced: true,
  hideAdvanceBtn: props.config.hideAdvanceBtn || false,
  showResetButton: !(props.config.hideResetButton || false),
  showSubmitButton: !(props.config.hideResetButton || false),
  submitButtonOptions: Object.assign({ 'data-cy': 'searchFormSubmit' }, props.config.submitButtonOptions || {}),
  resetButtonOptions: Object.assign({ 'data-cy': 'searchFormReset' }, props.config.resetButtonOptions || {}),
  isLoad: false,
  actionSpan: props.config.actionSpan || (props.config.compact ? 8 : 24),
});
const settingModalVisible = ref(false);

const blankCols = computed(() => {
  let count = 0;
  props.config.fieldList
    .filter(field => !field.hidden)
    .forEach(field => {
      count = count + (field.span || 6);
      if (count > 24) {
        count = field.span || 6;
      }
      if (count === 24) {
        count = 0;
      }
    });
  if (count > props.config.showExportButton ? 14 : 16) {
    count = 0;
  }
  return 24 - count - (props.config.showExportButton ? 10 : 8);
});

const formActionSlots = computed(() => {
  const configResult: any = {
    resetBefore: [],
    advanceAfter: [],
    advanceBefore: [],
    submitBefore: [],
  };
  if (props.config.showExportButton) {
    configResult.advanceAfter.push('exportButton');
  }
  return configResult;
});

const formActionSlotsShow = computed(() => {
  const configResult: any = {
    resetBefore: [],
    advanceAfter: [],
    advanceBefore: [],
    submitBefore: [],
  };
  if (props.config.showExportButton) {
    configResult.advanceAfter.push('exportButton');
  }
  if (!props.config.hideMatchType) {
    configResult.resetBefore.push('matchType');
  }
  const buttonCount =
    (formActionConfig.hideAdvanceBtn ? 0 : 1) +
    (formActionConfig.showResetButton ? 1 : 0) +
    (formActionConfig.showSubmitButton ? 1 : 0) +
    configResult.advanceBefore.length +
    configResult.advanceAfter.length +
    configResult.submitBefore.length +
    configResult.resetBefore.length;
  return buttonCount > 0;
});

const resetAction = (): Promise<void> => {
  return new Promise(() => {
    props.config.fieldList.forEach(field => {
      field.value = field.defaultValue || null;
    });
    emit('formSearch');
  });
};

const submitAction = (): Promise<void> => {
  return new Promise(() => {
    emit('formSearch');
  });
};

createFormContext({
  resetAction: resetAction,
  submitAction: submitAction,
});

const handleToggleAdvanced = () => {
  emit('close');
};

const exportClick = () => {
  emit('export');
};

const showSettingModal = () => {
  settingModalVisible.value = true;
};

defineExpose({
  showSettingModal,
});
</script>
<style scoped>
:deep(.ant-form-item) {
  margin-bottom: 4px;
}
</style>
