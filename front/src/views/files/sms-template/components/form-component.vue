<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []">
    <BasicForm ref="formRef" v-bind="formProps"></BasicForm>
  </div>
</template>
<script lang="ts" setup>
import { getCurrentInstance, reactive, computed, h, ref, watch } from 'vue';
import { message } from 'ant-design-vue';
import { BasicForm } from '@begcode/components';
import config from '../config/edit-config';
import { SmsTemplate, ISmsTemplate } from '@/models/files/sms-template.model';
import ServerProvider from '@/api-service/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemSmsTemplateEdit',
  inheritAttrs: false,
});

const props = defineProps({
  entityId: {
    type: [String, Number] as PropType<string | number>,
    default: '',
  },
  containerType: {
    type: String,
    default: 'page',
  },
  formButtons: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  baseData: {
    type: Object,
    default: () => ({}),
  },
});
const ctx = getCurrentInstance()?.proxy;
const formRef = ref<any>(null);
const apiService = ctx?.$apiService as typeof ServerProvider;
const smsTemplate = reactive<ISmsTemplate>(new SmsTemplate());
watch(
  () => props.entityId,
  async val => {
    if (val) {
      const data = await apiService.files.smsTemplateService.find(Number(val)).catch(() => null);
      if (data) {
        Object.assign(smsTemplate, data);
      }
    } else {
      Object.assign(smsTemplate, props.baseData);
    }
  },
  { immediate: true },
);
const formItemsConfig = config.fields();

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.files.smsTemplateService.update : apiService.files.smsTemplateService.create;
const submit = async () => {
  const result = await validate().catch(() => ({ success: false, data: {} }));
  if (result.success) {
    Object.assign(smsTemplate, result.data);
    const saveData = await saveOrUpdateApi(smsTemplate).catch(() => false);
    if (saveData) {
      Object.assign(smsTemplate, saveData);
      message.success(submitButtonTitlePrefix + '成功！');
      return saveData;
    } else {
      message.error(submitButtonTitlePrefix + '失败！');
      return false;
    }
  } else {
    message.error('数据验证失败！');
    return false;
  }
};
const validate = async () => {
  const result = {
    success: true,
    data: {},
    errors: [] as any[],
  };
  const formValidResult = await formRef.value.validate();
  if (!formValidResult) {
    result.success = false;
    result.errors.push('表单校验未通过！');
  } else {
    result.data = { ...result.data, ...formValidResult };
  }
  return result;
};

const formProps = reactive({
  labelWidth: '120px',
  compact: true,
  alwaysShowLines: 1,
  fieldMapToTime: [],
  size: 'default',
  showAdvancedButton: false,
  showResetButton: props.formButtons.includes('reset'),
  showSubmitButton: props.formButtons.includes('submit'),
  showActionButtonGroup: props.formButtons.length > 0,
  model: smsTemplate,
  schemas: formItemsConfig,
  disabled: !isEdit.value,
  resetButtonOptions: {
    type: 'default',
    size: 'default',
    text: '重置',
    preIcon: null,
  },
  actionColOptions: {
    span: 24,
    style: {
      textAlign: 'right',
      borderTop: '1px solid #e8e8e8',
      paddingTop: '10px',
    },
  },
  submitButtonOptions: {
    type: 'primary',
    size: 'default',
    text: submitButtonTitlePrefix,
    preIcon: null,
  },
  resetFunc: () => {
    ctx?.$emit('cancel', { update: false, containerType: props.containerType });
  },
  submitFunc: submit,
});
watch(smsTemplate, val => {
  formRef.value?.setFieldsValue(val);
});

defineExpose({
  validate,
  submit,
});
</script>
<style scoped>
.pb-44px {
  padding-bottom: 44px;
}
</style>
