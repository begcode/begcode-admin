<template>
  <div class="p-2">
    <div class="p-2 bg-white">
      <List :grid="grid" :data-source="data">
        <template #header>
          <div class="flex" style="justify-content: space-between">
            <div class="space-x-2">
              <slot name="header_left" />
            </div>
            <div class="space-x-2">
              <slot name="header_right"></slot>
              <template v-for="button in toolButtons">
                <Tooltip @click="button.click" v-if="!button.hidden">
                  <template #title>{{ button.title }}</template>
                  <Button :disabled="button.disabled">
                    <Icon :icon="button.icon" v-if="button.icon"></Icon>
                  </Button>
                </Tooltip>
              </template>
              <Tooltip>
                <template #title>插入</template>
                <Button @click="insert"><VerticalAlignBottomOutlined /></Button>
              </Tooltip>
              <Tooltip>
                <template #title>追加</template>
                <Button @click="add"><VerticalAlignTopOutlined /></Button>
              </Tooltip>
              <Tooltip>
                <template #title>刷新</template>
                <Button><RedoOutlined /></Button>
              </Tooltip>
            </div>
          </div>
        </template>
        <template #renderItem="{ item }">
          <ListItem>
            <BasicForm v-bind="item" :ref="setFormRef"></BasicForm>
          </ListItem>
        </template>
      </List>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, ref, reactive, onBeforeUpdate, PropType } from 'vue';
import { RedoOutlined, VerticalAlignBottomOutlined, VerticalAlignTopOutlined } from '@ant-design/icons-vue';
import { List, Tooltip } from 'ant-design-vue';
import { propTypes } from '@/utils/propTypes';
import { Button } from '@/components/Button';
import { BasicForm } from '@/components/Form';

const ListItem = List.Item;
// 组件接收参数
const props = defineProps({
  // 请求API的参数
  params: propTypes.object.def({}),
  // imageField
  imageField: {
    type: String,
    default: 'url',
  },
  resultField: {
    type: String,
    default: 'data',
  },
  totalField: {
    type: String,
    default: 'total',
  },
  toolButtons: {
    type: Array as PropType<any[]>,
    default: [],
  },
  rowOperations: propTypes.array.def([]),
  showAvatar: {
    type: Boolean,
    default: true,
  },
  showDesc: {
    type: Boolean,
    default: true,
  },
  formConfig: propTypes.object.def({}),
  size: {
    type: Number,
    default: 1,
  },
  fieldName: propTypes.string.def('unknown'),
  fieldData: propTypes.array.def([]),
});
//暴露内部方法
const emit = defineEmits(['getMethod', 'delete', 'submit']);

const grid = reactive({
  gutter: 16,
  column: 1 || props.size,
});

const formRefs = ref<any>([]);
const setFormRef = el => {
  if (el) {
    formRefs.value.push(el);
  }
};

onBeforeUpdate(() => {
  formRefs.value = [];
});

const baseFormConfig = {
  schemas: [],
  labelWidth: 80,
  baseColProps: { span: 24 },
  actionColOptions: { span: 24 },
  compact: true,
  showActionButtonGroup: false,
};

const getConfigData = () => {
  const result: any[] = [];
  props.fieldData.forEach(item => {
    if (item) {
      const formConfig = { model: item, ...baseFormConfig, ...props.formConfig };
      result.push(formConfig);
    }
  });
  if (result.length === 0) {
    result.push({ ...baseFormConfig, ...props.formConfig, model: {} });
  }
  return result;
};

const data = ref(getConfigData());

const add = () => {
  data.value.push({ ...baseFormConfig, ...props.formConfig, model: {} });
};

const insert = () => {
  data.value.unshift({ ...baseFormConfig, ...props.formConfig, model: {} });
};

async function validate() {
  // 验证所有的表单组件
  const data: any[] = [];
  let success = true;
  for (let i = 0; i < formRefs.value.length; i++) {
    const itemRef = formRefs.value[i];
    const itemData = await itemRef.validate();
    if (itemData) {
      data.push(itemData);
    } else {
      success = false;
    }
  }
  return new Promise(resolve => {
    return resolve(success ? { [props.fieldName]: data } : success);
  });
}

// 自动请求并暴露内部方法
onMounted(() => {
  emit('getMethod', fetch);
});

defineExpose({
  validate,
});
</script>
