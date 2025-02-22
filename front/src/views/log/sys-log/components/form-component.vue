<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []" data-cy="SysLogCreateUpdateHeading">
    <basic-form ref="formRef" v-bind="formProps">
      <template #resetBefore>
        <teleport to='[data-teleport="log/sys-log-edit-append-button"]' defer>
          <a-space>
            <basic-button v-for="operation in operations" :type="operation.type" @click="operation.click" v-bind="operation.attrs">
              <Icon :icon="operation.icon" v-if="operation.icon" />
              {{ operation.title }}
            </basic-button>
          </a-space>
        </teleport>
      </template>
    </basic-form>
  </div>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue';
import config from '../config/edit-config';
import { SysLog, ISysLog } from '@/models/log/sys-log.model';
import ServerProvider from '@/api-service/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'LogSysLogEdit',
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

const emit = defineEmits(['cancel', 'update-save-button']);

const operations = ref<any[]>([]);

const ctx = getCurrentInstance()?.proxy;
const formRef = ref<any>(null);
const apiService = ctx?.$apiService as typeof ServerProvider;
const relationshipApis: any = {};
const sysLogId = ref<any>(null);
const sysLog = reactive<ISysLog>(new SysLog());
const getEntityData = async (entityId: string | number) => {
  if (entityId) {
    sysLogId.value = entityId;
    const data = await apiService.log.sysLogService.find(Number(entityId)).catch(() => null);
    if (data) {
      Object.assign(sysLog, data);
    }
  } else {
    Object.assign(sysLog, props.baseData);
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const formItemsConfig = config.fields(relationshipApis);

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.log.sysLogService.update : apiService.log.sysLogService.create;
const submit = async (config = { submitToServer: true }) => {
  const result = await validate().catch(err => {
    console.log('validate.error:', err);
    return { success: false, data: {} };
  });
  if (result.success) {
    Object.assign(sysLog, result.data);
    if (!config.submitToServer) {
      return sysLog;
    }
    const saveData = await saveOrUpdateApi(sysLog).catch(() => false);
    if (saveData) {
      Object.assign(sysLog, saveData);
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
  const formValidResult = await formRef.value?.validate();
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
  model: sysLog,
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
    emit('cancel', { update: false, containerType: props.containerType });
  },
  submitFunc: submit,
});
watch(sysLog, val => {
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
