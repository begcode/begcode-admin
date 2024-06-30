<template>
  <div>
    <Descriptions ref="positionDetailRef" v-bind="descriptionsProps"></Descriptions>
  </div>
</template>
<script lang="ts" setup>
import { getCurrentInstance, ref, reactive, h } from 'vue';
import { Descriptions } from '@begcode/components';
import ServerProvider from '@/api-service/index';
import config from '../config/detail-config';
import { IPosition } from '@/models/settings/position.model';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
defineOptions({
  name: 'SystemPositionDetail',
  inheritAttrs: false,
});

const props = defineProps({
  entityId: {
    type: [String, Number] as PropType<string | number>,
    default: '',
    required: true,
  },
});

const positionDetailRef = ref(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const position = reactive<IPosition>({});
if (props.entityId) {
  apiService.settings.positionService.find(Number(props.entityId)).then(data => {
    if (data) {
      Object.assign(position, data);
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
  data: position,
  column: 1,
});
</script>
