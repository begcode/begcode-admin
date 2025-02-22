<template>
  <div class="w-100%" data-cy="taskJobConfigDetailsHeading">
    <Descriptions ref="taskJobConfigDetailRef" v-bind="descriptionsProps"></Descriptions>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { ITaskJobConfig } from '@/models/taskjob/task-job-config.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'TaskjobTaskJobConfigDetail',
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

const taskJobConfigDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const taskJobConfig = reactive<ITaskJobConfig>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.taskjob.taskJobConfigService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(taskJobConfig, data);
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
  data: taskJobConfig,
  column: props.columns || 1,
});
</script>
