<template>
  <div class="w-100%" data-cy="regionCodeDetailsHeading">
    <Descriptions ref="regionCodeDetailRef" v-bind="descriptionsProps"></Descriptions>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { IRegionCode } from '@/models/settings/region-code.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemRegionCodeDetail',
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

const regionCodeDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const regionCode = reactive<IRegionCode>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.settings.regionCodeService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(regionCode, data);
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
  data: regionCode,
  column: props.columns || 1,
});
</script>
