<template>
  <div class="w-100%" data-cy="businessTypeDetailsHeading">
    <Descriptions ref="businessTypeDetailRef" v-bind="descriptionsProps"></Descriptions>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { IBusinessType } from '@/models/settings/business-type.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemBusinessTypeDetail',
  inheritAttrs: false,
});

const props = defineProps({
  entityId: {
    type: [String, Number] as PropType<string | number>,
    default: '',
    required: true,
  },
  columns: {
    type: Number,
    default: 1,
  },
  hideColumns: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
});

const businessTypeDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const businessType = reactive<IBusinessType>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.settings.businessTypeService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(businessType, data);
      }
    });
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const descriptionsProps = reactive({
  schema: config.fields(props.hideColumns),
  isEdit: () => false,
  // formConfig,
  labelWidth: '120px',
  data: businessType,
  column: props.columns || 1,
});
</script>
