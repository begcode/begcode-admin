<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []">
    <template v-for="(formConfigModel, index) in formSaveDataRecords">
      <div style="margin: 10px">
        <a-badge>
          <template #count>
            <Icon icon="ant-design:close-circle-twotone" color="red" :size="20" @click="removeForm(index)"></Icon>
          </template>
          <div style="padding: 10px; border-radius: 4px; border: #e5e7eb 1px solid">
            <FormCreate
              v-if="showCreate"
              :form-config="formConfigData as any"
              v-model:fApi="formConfigModel.fApi"
              v-model:formModel="formConfigModel.formModel"
              :ref="el => (formConfigModel.fApi.formRef = el)"
            >
            </FormCreate>
          </div>
        </a-badge>
      </div>
    </template>
    <a-row>
      <a-col :span="1"></a-col>
      <a-col :span="23">
        <a-button type="primary" @click="addForm">
          <Icon icon="ant-design:plus-circle-outlined" />
          添加数据
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue';
import { FormCreate } from '@/components/FormDesigner';
import { FormConfig, IFormConfig } from '@/models/settings/form-config.model';
import { IFormSaveData, FormSaveData } from '@/models/settings/form-save-data.model';
import ServerProvider from '@/api-service/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemFormConfigCreate',
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
  viewType: {
    type: String,
    default: 'preview', // preview // edit 编辑数据
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

const emit = defineEmits(['cancel']);

const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const formConfig = reactive<IFormConfig>(new FormConfig());
const formSaveDataRecords = reactive<any[]>([]);
const showCreate = ref(false);

watch(
  () => props.entityId,
  async val => {
    if (val) {
      const data = await apiService.settings.formConfigService.find(Number(val)).catch(() => null);
      if (data) {
        Object.assign(formConfig, data);
        if (props.viewType === 'preview') {
          const data = new FormSaveData();
          data.formConfigId = formConfig.id;
          data.formConfig = { id: formConfig.id };
          data.formModel = {};
          const saveConfigData = {
            formSaveData: data,
            formModel: data.formModel,
            fApi: {},
          };
          formSaveDataRecords.push(saveConfigData);
        } else {
          const saveDataList = await apiService.settings.formSaveDataService.retrieve({ formConfigId: val }).catch(() => null);
          console.log('saveDataList', saveDataList);
          if (saveDataList?.records && saveDataList?.records.length > 0) {
            if (formConfig.multiItems) {
              const dataList = saveDataList.records.map(item => {
                item.formModel = JSON.parse(item.formData || '{}');
                return {
                  formSaveData: item,
                  formModel: item.formModel,
                  fApi: {},
                };
              });
              formSaveDataRecords.push(...dataList);
            } else {
              const data = saveDataList.records[0];
              data.formModel = JSON.parse(data.formData || '{}');
              const saveConfigData = {
                formSaveData: data,
                formModel: data.formModel,
                fApi: {},
              };
              formSaveDataRecords.push(saveConfigData);
            }
          } else {
            const data = new FormSaveData();
            data.formConfigId = formConfig.id;
            data.formConfig = { id: formConfig.id };
            data.formModel = {};
            const saveConfigData = {
              formSaveData: data,
              formModel: data.formModel,
              fApi: {},
            };
            formSaveDataRecords.push(saveConfigData);
          }
        }
        showCreate.value = true;
      }
    } else {
      Object.assign(formConfig, props.baseData);
    }
  },
  { immediate: true },
);

const removeForm = index => {
  if (formSaveDataRecords.length > 1) {
    const data = formSaveDataRecords[index];
    if (data?.formSaveData?.id) {
      apiService.settings.formSaveDataService.delete(data.formSaveData.id).then(res => {
        console.log('delete.res', res);
      });
    }
    formSaveDataRecords.splice(index, 1);
  }
};

const addForm = () => {
  const data = new FormSaveData();
  data.formConfigId = formConfig.id;
  data.formConfig = { id: formConfig.id };
  data.formModel = {};
  const saveConfigData = {
    formSaveData: data,
    formModel: data.formModel,
    fApi: {},
  };
  formSaveDataRecords.push(saveConfigData);
};

const formConfigData = computed(() => {
  return JSON.parse(formConfig.formJson || '{}');
});
const saveFormData = async () => {
  let validateSuccess = true;
  for (const item of formSaveDataRecords) {
    const result = await item.fApi.formRef.eFormModel.validate?.().catch(() => null);
    if (!result || result?.errorFields) {
      console.log('result', result);
      validateSuccess = false;
      break;
    }
  }
  if (!validateSuccess) {
    message.error('请检查表单数据是否填写正确。');
    return;
  }
  for (const item of formSaveDataRecords) {
    const data = await item.fApi.submit?.();
    const formSaveData = item.formSaveData;
    formSaveData.formData = JSON.stringify(data);
    if (formSaveData.id) {
      await apiService.settings.formSaveDataService.update(formSaveData, [formSaveData.id], ['formData']);
    } else {
      await apiService.settings.formSaveDataService.create(formSaveData);
    }
  }
  message.success('保存成功。');
};
defineExpose({
  submit: saveFormData,
});
</script>
<style scoped>
.pb-44px {
  padding-bottom: 44px;
}
</style>
