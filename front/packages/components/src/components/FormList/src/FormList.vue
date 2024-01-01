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
import { computed, onMounted, ref, reactive, onBeforeUpdate } from 'vue';
import { RedoOutlined, VerticalAlignBottomOutlined, VerticalAlignTopOutlined } from '@ant-design/icons-vue';
import { List, Tooltip, Slider, Avatar } from 'ant-design-vue';
import { propTypes } from '@/utils/propTypes';
import { Button } from '@/components/Button';
import { useSlider } from './data';
import { BasicForm } from '@/components/Form';

const ListItem = List.Item;
// 获取slider属性
const sliderProp = computed(() => useSlider(4));
// 组件接收参数
const props = defineProps({
  // 请求API的参数
  params: propTypes.object.def({}),
  // imageField
  imageField: propTypes.string.def('url'),
  resultField: propTypes.string.def('data'),
  totalField: propTypes.string.def('total'),
  toolButtons: propTypes.array.def([]),
  rowOperations: propTypes.array.def([]),
  showAvatar: propTypes.bool.def(true),
  showDesc: propTypes.bool.def(true),
  formConfig: propTypes.object.def({}),
  size: propTypes.number.def(1),
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
// 切换每行个数
// cover图片自适应高度
//修改pageSize并重新请求数据

const height = computed(() => {
  return `h-${120 - grid.column * 6}`;
});

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

function sliderChange(n) {
  pageSize.value = n * 4;
}

// 自动请求并暴露内部方法
onMounted(() => {
  emit('getMethod', fetch);
});

//分页相关
const page = ref(0);
const pageSize = ref(36);
const total = ref(0);
const paginationProp = ref({
  showSizeChanger: false,
  showQuickJumper: true,
  pageSize,
  current: page,
  total,
  showTotal: total => `总 ${total} 条`,
  onChange: pageChange,
  onShowSizeChange: pageSizeChange,
});

function pageChange(p, pz) {
  page.value = p;
  pageSize.value = pz;
}

function pageSizeChange(_current, size) {
  pageSize.value = size;
}

async function handleDelete(id) {
  emit('delete', id);
}

defineExpose({
  validate,
});
</script>
