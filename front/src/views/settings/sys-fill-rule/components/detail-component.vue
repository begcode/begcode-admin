<template>
  <div class="w-100%">
    <Descriptions ref="sysFillRuleDetailRef" v-bind="descriptionsProps"></Descriptions>
    <a-tabs>
      <a-tab-pane :tab="relationTables.ruleItemsGrid.title" :key="relationTables.ruleItemsGrid.name">
        <Grid ref="ruleItemsGridRef" v-bind="relationTables.ruleItemsGrid.props()" v-on="relationTables.ruleItemsGrid.events">
          <template #recordAction></template>
        </Grid>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script lang="ts" setup>
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { ISysFillRule } from '@/models/settings/sys-fill-rule.model';
import { Grid, VxeGridProps } from 'vxe-table';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemSysFillRuleDetail',
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

const sysFillRuleDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const sysFillRule = reactive<ISysFillRule>({});
const getEntityData = async () => {
  if (props.entityId) {
    apiService.settings.sysFillRuleService.find(Number(props.entityId)).then(data => {
      if (data) {
        Object.assign(sysFillRule, data);
      }
    });
  }
};
watch(() => props.entityId, getEntityData, { immediate: true });
const relationTables = reactive({
  activeKey: 'baseInfo',
  ruleItemsGrid: {
    name: 'vxe-grid',
    title: '配置项列表',
    props: () => {
      return {
        data: sysFillRule['ruleItems'],
        columns: config.ruleItemsColumns(),
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
  schema: config.fields(props.hideColumns.concat(['ruleItems'])),
  isEdit: () => false,
  // formConfig,
  labelWidth: '120px',
  data: sysFillRule,
  column: props.columns || 1,
});
</script>
