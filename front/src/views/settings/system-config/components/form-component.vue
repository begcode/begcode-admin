<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []" data-cy="SystemConfigCreateUpdateHeading">
    <a-tabs v-model:active-key="relationTables.activeKey" @change="changeActiveKey">
      <a-tab-pane tab="基本信息" key="baseInfo">
        <basic-form ref="formRef" v-bind="formProps">
          <template #resetBefore>
            <teleport to='[data-teleport="settings/system-config-edit-append-button"]' defer>
              <a-space>
                <basic-button v-for="operation in operations" :type="operation.type" @click="operation.click" v-bind="operation.attrs">
                  <Icon :icon="operation.icon" v-if="operation.icon" />
                  {{ operation.title }}
                </basic-button>
              </a-space>
            </teleport>
          </template>
        </basic-form>
      </a-tab-pane>
      <a-tab-pane
        :tab="relationTables.itemsRelation.title"
        :key="relationTables.itemsRelation.name"
        :disabled="relationTables.itemsRelation.disabled()"
      >
        <CommonFieldDataRelation
          ref="itemsRelationRef"
          v-bind="relationTables.itemsRelation.props()"
          v-on="relationTables.itemsRelation.events"
        >
        </CommonFieldDataRelation>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue';
import config from '../config/edit-config';
import { SystemConfig, ISystemConfig } from '@/models/settings/system-config.model';
import ServerProvider from '@/api-service/index';
import CommonFieldDataRelation from '@/views/settings/common-field-data/components/common-field-data-relation.vue';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemSystemConfigEdit',
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
const itemsRelationRef = ref<any>(null);
const apiService = ctx?.$apiService as typeof ServerProvider;
const relationshipApis: any = {
  items: apiService.settings.commonFieldDataService.retrieve,
};
const systemConfigId = ref<any>(null);
const systemConfig = reactive<ISystemConfig>(new SystemConfig());
const getEntityData = async (entityId: string | number) => {
  if (entityId) {
    systemConfigId.value = entityId;
    const data = await apiService.settings.systemConfigService.find(Number(entityId)).catch(() => null);
    if (data) {
      Object.assign(systemConfig, data);
    }
  } else {
    Object.assign(systemConfig, props.baseData);
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const formItemsConfig = config.fields(relationshipApis);

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.settings.systemConfigService.update : apiService.settings.systemConfigService.create;
const submit = async (config = { submitToServer: true }) => {
  const result = await validate().catch(err => {
    console.log('validate.error:', err);
    return { success: false, data: {} };
  });
  if (result.success) {
    Object.assign(systemConfig, result.data);
    if (!config.submitToServer) {
      return systemConfig;
    }
    const saveData = await saveOrUpdateApi(systemConfig).catch(() => false);
    if (saveData) {
      Object.assign(systemConfig, saveData);
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
  if (itemsRelationRef.value) {
    const itemsRelationResult = await itemsRelationRef.value?.validate();
    if (!itemsRelationResult) {
      result.success = false;
      result.errors.push('配置项列表校验未通过！');
    } else {
      result.data = { ...result.data, ...{ items: itemsRelationResult } };
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
  model: systemConfig,
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
  itemsRelation: {
    name: 'itemsRelation',
    title: '配置项列表',
    props: () => ({
      cardSlots: [''],
      modelName: 'items',
      relationData: toRaw(systemConfig['items']),
      deleteRelationType: 'delete',
      columns: config.itemsColumns(),
      query: { ownerEntityName: 'SystemConfig', ownerEntityId: systemConfigId.value },
      baseData: { ownerEntityName: 'SystemConfig', ownerEntityId: systemConfigId.value },
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
          apiService.settings.systemConfigService
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
        systemConfig.items = data;
      },
    },
    disabled: () => {
      return !systemConfig.id;
    },
  },
});
function changeActiveKey(activeKey: string) {
  relationTables.activeKey = activeKey;
  emit('update-save-button', activeKey === 'baseInfo');
}
watch(systemConfig, val => {
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
