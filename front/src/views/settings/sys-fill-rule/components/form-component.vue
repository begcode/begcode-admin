<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []">
    <BasicForm ref="formRef" v-bind="formProps"></BasicForm>
    <Tabs>
      <TabPane :tab="relationTables.ruleItemsGrid.title" :key="relationTables.ruleItemsGrid.name">
        <Grid ref="ruleItemsGridRef" v-bind="relationTables.ruleItemsGrid.props"></Grid>
      </TabPane>
    </Tabs>
  </div>
</template>
<script lang="ts" setup>
import { getCurrentInstance, reactive, computed, h, ref, watch } from 'vue';
import { message, Tabs, TabPane } from 'ant-design-vue';
import { BasicForm } from '@begcode/components';
import config from '../config/edit-config';
import { SysFillRule, ISysFillRule } from '@/models/settings/sys-fill-rule.model';
import ServerProvider from '@/api-service/index';
import { Grid, VxeGridProps } from 'vxe-table';

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
  baseData: {
    type: Object,
    default: () => ({}),
  },
});
const ctx = getCurrentInstance()?.proxy;
const formRef = ref<any>(null);
const ruleItemsGridRef = ref<any>(null);
const apiService = ctx?.$apiService as typeof ServerProvider;
const sysFillRule = reactive<ISysFillRule>(new SysFillRule());
if (props.entityId) {
  apiService.settings.sysFillRuleService.find(Number(props.entityId)).then(data => {
    if (data) {
      Object.assign(sysFillRule, data);
    }
  });
} else {
  Object.assign(sysFillRule, props.baseData);
}
const formItemsConfig = config.fields();

const isEdit = computed(() => {
  return true;
});

const submitButtonTitlePrefix = props.entityId ? '更新' : '保存';
const saveOrUpdateApi = props.entityId ? apiService.settings.sysFillRuleService.update : apiService.settings.sysFillRuleService.create;
const submit = async () => {
  const result = await validate().catch(() => ({ success: false, data: {} }));
  if (result.success) {
    Object.assign(sysFillRule, result.data);
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
  const formValidResult = await formRef.value.validate();
  if (!formValidResult) {
    result.success = false;
    result.errors.push('表单校验未通过！');
  } else {
    result.data = { ...result.data, ...formValidResult };
  }

  const ruleItemsvalidateErrors = await ruleItemsGridRef.value.validate(true);
  if (!ruleItemsvalidateErrors) {
    const { fullData } = ruleItemsGridRef.value.getTableData();
    fullData.forEach(row => {
      if (typeof row.id === 'string' && row.id.startsWith('row_')) {
        row.id = null;
      }
    });
    result.data['ruleItems'] = fullData;
  } else {
    result.errors.push(ruleItemsvalidateErrors);
    result.success = false;
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
  model: sysFillRule,
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
const relationTables = reactive({
  ruleItemsGrid: {
    name: 'ruleItemsGrid',
    title: '配置项列表列表',
    props: {
      modelName: 'ruleItems',
      data: sysFillRule['ruleItems'],
      columns: config.ruleItemsColumns(),
      border: true,
      showOverflow: true,
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
      compact: true,
      size: 'default',
      disabled: !isEdit.value,
      toolbarConfig: {
        buttons: [
          { code: 'insert_actived', name: '新增', icon: 'fa fa-plus' },
          { code: 'remove', name: '删除', icon: 'fa fa-trash-o' },
        ],
        // 表格右上角自定义按钮
        tools: [
          // { code: 'myPrint', name: '自定义打印' }
        ],
        import: false,
        export: false,
        print: false,
        custom: false,
      },
    } as VxeGridProps,
    slots: {},
  },
});
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
