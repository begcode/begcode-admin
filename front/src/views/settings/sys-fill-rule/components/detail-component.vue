<template>
  <div>
    <Descriptions ref="sysFillRuleDetailRef" v-bind="descriptionsProps"></Descriptions>
    <Tabs>
      <TabPane :tab="relationTables.ruleItemsGrid.title" :key="relationTables.ruleItemsGrid.name">
        <Grid ref="formRef" v-bind="relationTables.ruleItemsGrid.props"></Grid>
      </TabPane>
    </Tabs>
  </div>
</template>
<script lang="ts" setup>
import { getCurrentInstance, ref, reactive, h } from 'vue';
import { CollapsePanel, Collapse } from 'ant-design-vue';
import { Descriptions } from '@begcode/components';
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
});

const sysFillRuleDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const sysFillRule = reactive<ISysFillRule>({});
if (props.entityId) {
  apiService.settings.sysFillRuleService.find(Number(props.entityId)).then(data => {
    if (data) {
      Object.assign(sysFillRule, data);
    }
  });
}
const formItemsConfig = reactive(config.fields);
//获得关联表属性。

const descriptionsProps = reactive({
  schema: formItemsConfig,
  isEdit: () => false,
  // formConfig,
  labelWidth: '120px',
  data: sysFillRule,
  column: 1,
});
const relationTables = reactive({
  ruleItemsGrid: {
    name: 'vxe-grid',
    title: '配置项列表列表',
    props: {
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
    } as VxeGridProps,
  },
});
</script>
