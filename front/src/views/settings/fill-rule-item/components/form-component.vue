<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []" data-cy="FillRuleItemCreateUpdateHeading">
    <basic-form ref="formRef" v-bind="formProps">
      <template #resetBefore>
        <teleport to='[data-teleport="settings/fill-rule-item-edit-append-button"]' defer>
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
import { FillRuleItem, IFillRuleItem } from '@/models/settings/fill-rule-item.model';
import ServerProvider from '@/api-service/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemFillRuleItemEdit',
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
const relationshipApis: any = {
  fillRule: apiService.settings.sysFillRuleService.retrieve,
};
const fillRuleItemId = ref<any>(null);
const fillRuleItem = reactive<IFillRuleItem>(new FillRuleItem());
const getEntityData = async (entityId: string | number) => {
  if (entityId) {
    fillRuleItemId.value = entityId;
    const data = await apiService.settings.fillRuleItemService.find(Number(entityId)).catch(() => null);
    if (data) {
      Object.assign(fillRuleItem, data);
    }
  } else {
    Object.assign(fillRuleItem, props.baseData);
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const formItemsConfig = config.fields(relationshipApis);

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.settings.fillRuleItemService.update : apiService.settings.fillRuleItemService.create;
const submit = async (config = { submitToServer: true }) => {
  const result = await validate().catch(err => {
    console.log('validate.error:', err);
    return { success: false, data: {} };
  });
  if (result.success) {
    Object.assign(fillRuleItem, result.data);
    if (!config.submitToServer) {
      return fillRuleItem;
    }
    const saveData = await saveOrUpdateApi(fillRuleItem).catch(() => false);
    if (saveData) {
      Object.assign(fillRuleItem, saveData);
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
  model: fillRuleItem,
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
watch(fillRuleItem, val => {
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
