<template>
  <div class="w-100%">
    <Descriptions ref="systemConfigDetailRef" v-bind="descriptionsProps"></Descriptions>
    <a-tabs>
      <a-tab-pane :tab="relationTables.itemsGrid.title" :key="relationTables.itemsGrid.name">
        <Grid ref="itemsGridRef" v-bind="relationTables.itemsGrid.props()" v-on="relationTables.itemsGrid.events">
          <template #recordAction></template>
        </Grid>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { ISystemConfig } from '@/models/settings/system-config.model';
import { Grid, VxeGridProps } from 'vxe-table';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemSystemConfigDetail',
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

const systemConfigDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const systemConfig = reactive<ISystemConfig>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.settings.systemConfigService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(systemConfig, data);
      }
    });
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const relationTables = reactive({
  activeKey: 'baseInfo',
  itemsGrid: {
    name: 'vxe-grid',
    title: '配置项列表',
    props: () => {
      return {
        data: systemConfig['items'],
        columns: config.itemsColumns(),
        border: true,
        showOverflow: true,
        fieldMapToTime: [],
        compact: true,
        size: 'default',
        toolbarConfig: {
          buttons: [],
          tools: [],
          import: false,
          export: false,
          print: false,
          custom: false,
        },
      } as VxeGridProps;
    },
  },
});
const descriptionsProps = reactive({
  schema: config.fields(props.hideColumns.concat(['items'])),
  isEdit: () => false,
  // formConfig,
  labelWidth: '120px',
  data: systemConfig,
  column: props.columns || 1,
});
</script>
