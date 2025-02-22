<template>
  <div class="w-100%" data-cy="resourceCategoryDetailsHeading">
    <Descriptions ref="resourceCategoryDetailRef" v-bind="descriptionsProps"></Descriptions>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { IResourceCategory } from '@/models/files/resource-category.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'OssResourceCategoryDetail',
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

const resourceCategoryDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const resourceCategory = reactive<IResourceCategory>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.files.resourceCategoryService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(resourceCategory, data);
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
  data: resourceCategory,
  column: props.columns || 1,
});
</script>
