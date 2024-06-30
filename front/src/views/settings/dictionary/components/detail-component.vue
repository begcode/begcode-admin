<template>
  <div>
    <Descriptions ref="dictionaryDetailRef" v-bind="descriptionsProps"></Descriptions>
    <Tabs>
      <TabPane :tab="relationTables.itemsGrid.title" :key="relationTables.itemsGrid.name">
        <Grid ref="formRef" v-bind="relationTables.itemsGrid.props"></Grid>
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
import { IDictionary } from '@/models/settings/dictionary.model';
import { Grid, VxeGridProps } from 'vxe-table';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemDictionaryDetail',
  inheritAttrs: false,
});

const props = defineProps({
  entityId: {
    type: [String, Number] as PropType<string | number>,
    default: '',
    required: true,
  },
});

const dictionaryDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const dictionary = reactive<IDictionary>({});
if (props.entityId) {
  apiService.settings.dictionaryService.find(Number(props.entityId)).then(data => {
    if (data) {
      Object.assign(dictionary, data);
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
  data: dictionary,
  column: 1,
});
const relationTables = reactive({
  itemsGrid: {
    name: 'vxe-grid',
    title: '字典项列表列表',
    props: {
      data: dictionary['items'],
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
    } as VxeGridProps,
  },
});
</script>
