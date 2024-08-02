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
import { SmsMessage, ISmsMessage } from '@/models/system/sms-message.model';
import ServerProvider from '@/api-service/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemSmsMessageEdit',
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
  baseData: {
    type: Object,
    default: () => ({}),
  },
});
const ctx = getCurrentInstance()?.proxy;
const formRef = ref<any>(null);
const apiService = ctx?.$apiService as typeof ServerProvider;
const smsMessage = reactive<ISmsMessage>(new SmsMessage());
if (props.entityId) {
  apiService.system.smsMessageService.find(Number(props.entityId)).then(data => {
    if (data) {
      Object.assign(smsMessage, data);
    }
  });
} else {
  Object.assign(smsMessage, props.baseData);
}
const formItemsConfig = config.fields();

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.system.smsMessageService.update : apiService.system.smsMessageService.create;
const submit = async () => {
  const result = await validate().catch(() => ({ success: false, data: {} }));
  if (result.success) {
    Object.assign(smsMessage, result.data);
    const saveData = await saveOrUpdateApi(smsMessage).catch(() => false);
    if (saveData) {
      Object.assign(smsMessage, saveData);
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
  showResetButton: false,
  showSubmitButton: false,
  showActionButtonGroup: false,
  model: smsMessage,
  schemas: formItemsConfig,
  disabled: !isEdit.value,
  resetButtonOptions: {
    type: 'default',
    size: 'default',
    text: '关闭',
    preIcon: null,
  },
  actionColOptions: {
    span: 18,
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
watch(smsMessage, val => {
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
