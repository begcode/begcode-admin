<template>
  <div :class="containerType === 'page' ? ['pb-44px'] : []">
    <FormDesigner v-if="showDesigner" :form-config="formConfigData" @cancel="designerCancel" @save="designerSave" />
  </div>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue';
import { FormDesigner } from '@/components/FormDesigner';
import { FormConfig, IFormConfig } from '@/models/settings/form-config.model';
import ServerProvider from '@/api-service/index';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemFormConfigDesign',
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

const emit = defineEmits(['cancel']);

const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const formConfig = reactive<IFormConfig>(new FormConfig());
const showDesigner = ref(false);
watch(
  () => props.entityId,
  async val => {
    if (val) {
      const data = await apiService.settings.formConfigService.find(Number(val)).catch(() => null);
      if (data) {
        showDesigner.value = true;
        Object.assign(formConfig, data);
      }
    } else {
      Object.assign(formConfig, props.baseData);
    }
  },
  { immediate: true },
);

const formConfigData = computed(() => {
  return JSON.parse(formConfig.formJson || '{}');
});
const designerCancel = () => {
  emit('cancel');
};
const designerSave = formConfigJson => {
  console.log('save', formConfigJson);
  apiService.settings.formConfigService
    .update({ ...formConfig, formJson: JSON.stringify(formConfigJson || {}) }, [formConfig.id!], ['formJson'])
    .then(res => {
      message.success('保存成功。');
    })
    .catch(res => {
      message.warn('保存失败！');
    });
};
</script>
<style scoped>
.pb-44px {
  padding-bottom: 44px;
}
</style>
