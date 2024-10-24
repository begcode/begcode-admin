<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []">
    <a-tabs v-model:active-key="relationTables.activeKey" @change="changeActiveKey">
      <a-tab-pane tab="基本信息" key="baseInfo">
        <BasicForm ref="formRef" v-bind="formProps" />
      </a-tab-pane>
      <a-tab-pane
        :tab="relationTables.ruleItemsRelation.title"
        :key="relationTables.ruleItemsRelation.name"
        :disabled="relationTables.ruleItemsRelation.disabled()"
      >
        <FillRuleItemRelation
          ref="ruleItemsRelationRef"
          v-bind="relationTables.ruleItemsRelation.props()"
          v-on="relationTables.ruleItemsRelation.events"
        >
        </FillRuleItemRelation>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue';
import config from '../config/edit-config';
import { SysFillRule, ISysFillRule } from '@/models/settings/sys-fill-rule.model';
import ServerProvider from '@/api-service/index';
import FillRuleItemRelation from '@/views/settings/fill-rule-item/components/fill-rule-item-relation.vue';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemSysFillRuleEdit',
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

const ctx = getCurrentInstance()?.proxy;
const formRef = ref<any>(null);
const ruleItemsRelationRef = ref<any>(null);
const apiService = ctx?.$apiService as typeof ServerProvider;
const sysFillRuleId = ref<any>(null);
const sysFillRule = reactive<ISysFillRule>(new SysFillRule());
const getEntityData = async (entityId: string | number) => {
  if (entityId) {
    sysFillRuleId.value = entityId;
    const data = await apiService.settings.sysFillRuleService.find(Number(entityId)).catch(() => null);
    if (data) {
      Object.assign(sysFillRule, data);
    }
  } else {
    Object.assign(sysFillRule, props.baseData);
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const formItemsConfig = config.fields();

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.settings.sysFillRuleService.update : apiService.settings.sysFillRuleService.create;
const submit = async (config = { submitToServer: true }) => {
  const result = await validate().catch(err => {
    console.log('validate.error:', err);
    return { success: false, data: {} };
  });
  if (result.success) {
    Object.assign(sysFillRule, result.data);
    if (!config.submitToServer) {
      return sysFillRule;
    }
    const saveData = await saveOrUpdateApi(sysFillRule).catch(() => false);
    if (saveData) {
      Object.assign(sysFillRule, saveData);
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
  if (ruleItemsRelationRef.value) {
    const ruleItemsRelationResult = await ruleItemsRelationRef.value?.validate();
    if (!ruleItemsRelationResult) {
      result.success = false;
      result.errors.push('配置项列表校验未通过！');
    } else {
      result.data = { ...result.data, ...{ ruleItems: ruleItemsRelationResult } };
    }
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
  model: sysFillRule,
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
const relationTables = reactive({
  activeKey: 'baseInfo',
  ruleItemsRelation: {
    name: 'ruleItemsRelation',
    title: '配置项列表',
    props: () => ({
      cardSlots: [''],
      modelName: 'ruleItems',
      relationData: toRaw(sysFillRule['ruleItems']),
      deleteRelationType: 'delete',
      columns: config.ruleItemsColumns(),
      query: { fillRuleId: sysFillRuleId.value },
      baseData: { fillRule: { id: sysFillRuleId.value } },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        beforeEditMethod({ row, rowIndex, column, columnIndex }) {
          console.log('beforeEditMethod', row, rowIndex, column, columnIndex);
          return true;
        },
      },
      onEditClosed: ({ $table, row, column }) => {
        console.log('$table', $table);
        if ($table.isUpdateByRow(row)) {
          row.loading = true;
          apiService.settings.sysFillRuleService
            .update({ id: row.id, [column.field]: row[column.field] }, [row.id], [column.field])
            .then(data => {
              $table.reloadRow(row, data, column.field);
              message.success('保存成功！');
            })
            .finally(() => {
              row.loading = false;
            });
        }
      },
      fieldMapToTime: [],
      size: 'default',
      disabled: !isEdit.value,
      updateType: 'emitSelected',
      gridOptions: {
        toolbarConfig: {
          // buttons未配置表示使用组件原有的，如果配置了，以配置为主。通过code进行比较
          // buttons: [],

          import: false,
          export: false,
          print: false,
          custom: false,
          // 表格右上角自定义按钮, tools未配置表示使用组件原有的，如果配置了，以配置为主。通过code进行比较
          tools: ['new'],
        },
      },
    }),
    slots: {},
    events: {
      updateRelationData: data => {
        sysFillRule.ruleItems = data;
      },
    },
    disabled: () => {
      return !sysFillRule.id;
    },
  },
});
function changeActiveKey(activeKey: string) {
  relationTables.activeKey = activeKey;
  emit('update-save-button', activeKey === 'baseInfo');
}
watch(sysFillRule, val => {
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
