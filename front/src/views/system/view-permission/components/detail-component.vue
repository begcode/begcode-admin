<template>
  <div class="w-100%" data-cy="viewPermissionDetailsHeading">
    <Descriptions ref="viewPermissionDetailRef" v-bind="descriptionsProps"></Descriptions>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { IViewPermission } from '@/models/system/view-permission.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemViewPermissionDetail',
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

const viewPermissionDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const viewPermission = reactive<IViewPermission>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.system.viewPermissionService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(viewPermission, data);
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
  data: viewPermission,
  column: props.columns || 1,
});
</script>
