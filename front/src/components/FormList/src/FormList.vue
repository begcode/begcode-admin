<template>
  <div class="p-2">
    <div class="p-2 bg-white">
      <a-list :data-source="data" item-layout="horizontal">
        <template #header v-if="showHeader">
          <div class="flex" style="justify-content: space-between">
            <div class="space-x-2">
              <slot name="header_left" />
            </div>
            <div class="space-x-2">
              <slot name="header_right"></slot>
              <template v-for="button in toolButtons">
                <a-tooltip @click="button.click" v-if="!button.hidden">
                  <template #title>{{ button.title }}</template>
                  <BasicButton :disabled="button.disabled">
                    <Icon :icon="button.icon" v-if="button.icon"></Icon>
                  </BasicButton>
                </a-tooltip>
              </template>
              <!--              <a-tooltip>-->
              <!--                <template #title>插入</template>-->
              <!--                <BasicButton @click="insert"><Icon icon="ant-design:vertical-align-bottom-outlined" /></BasicButton>-->
              <!--              </a-tooltip>-->
              <!--              <a-tooltip>-->
              <!--                <template #title>追加</template>-->
              <!--                <BasicButton @click="add"><Icon icon="ant-design:vertical-align-top-outlined" /></BasicButton>-->
              <!--              </a-tooltip>-->
              <!--              <a-tooltip>-->
              <!--                <template #title>刷新</template>-->
              <!--                <BasicButton><Icon icon="ant-design:redo-outlined" /></BasicButton>-->
              <!--              </a-tooltip>-->
            </div>
          </div>
        </template>
        <template #renderItem="{ item, index }">
          <a-list-item>
            <a-row justify="center" align="middle">
              <a-col :span="multiple ? 21 : 24">
                <BasicForm v-bind="item" :ref="el => setFormRef(el, index)"></BasicForm>
              </a-col>
              <a-col :span="3" justify="center" align="middle" v-if="multiple">
                <a-space direction="vertical" :size="18">
                  <a-tooltip>
                    <template #title>插入</template>
                    <BasicButton type="link" @click="insertItem(item, index)"
                      ><Icon icon="ant-design:vertical-align-bottom-outlined"
                    /></BasicButton>
                  </a-tooltip>
                  <a-tooltip>
                    <template #title>追加</template>
                    <BasicButton type="link" @click="appendItem(item, index)"><Icon icon="ant-design:plus-outlined" /></BasicButton>
                  </a-tooltip>
                  <a-tooltip>
                    <template #title>上移</template>
                    <BasicButton type="link" @click="moveUp(item, index)"><Icon icon="ant-design:arrow-up-outlined" /></BasicButton>
                  </a-tooltip>
                  <a-tooltip>
                    <template #title>下移</template>
                    <BasicButton type="link" @click="moveDown(item, index)"><Icon icon="ant-design:arrow-down-outlined" /></BasicButton>
                  </a-tooltip>
                  <a-tooltip>
                    <template #title>删除</template>
                    <BasicButton type="link" @click="deleteItem(item, index)"><Icon icon="ant-design:delete-outlined" /></BasicButton>
                  </a-tooltip>
                </a-space>
              </a-col>
            </a-row>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { Modal } from 'ant-design-vue';
import { Icon } from '@/components/Icon';

// 组件接收参数
const props = defineProps({
  // 请求API的参数
  params: {
    type: Object as PropType<any>,
    default: () => ({}),
  },
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
  rowOperations: {
    type: Array as PropType<any[]>,
    default: [],
  },
  showAvatar: {
    type: Boolean,
    default: true,
  },
  showDesc: {
    type: Boolean,
    default: true,
  },
  formConfig: {
    type: Object as PropType<any>,
    default: () => ({}),
  },
  size: {
    type: Number,
    default: 1,
  },
  fieldName: {
    type: String,
    default: '',
  },
  fieldData: {
    type: Array || Object,
    default: [],
  },
  sortFieldName: {
    type: String,
    default: '',
  },
  multiple: {
    type: Boolean,
    default: true,
  },
});
// 暴露内部方法
const emit = defineEmits(['getMethod', 'delete', 'submit']);

const formRefs = ref({});
const setFormRef = (el, index) => {
  if (el) {
    formRefs.value['el' + index] = el;
  }
};

const showHeader = computed(() => {
  return props.toolButtons.length > 0;
});

onBeforeUpdate(() => {
  formRefs.value = {};
});

const baseFormConfig = {
  schemas: [],
  labelWidth: 80,
  baseColProps: { span: 24 },
  actionColOptions: { span: 24 },
  compact: true,
  showActionButtonGroup: false,
};

const data = ref<any[]>([]);

watchEffect(() => {
  const result: any[] = [];
  const allData = _isArray(props.fieldData) ? props.fieldData : props.fieldData ? [props.fieldData] : [];
  allData.forEach(item => {
    if (item) {
      const formConfig = { model: item, ...baseFormConfig, ...props.formConfig };
      result.push(formConfig);
    }
  });
  if (result.length === 0) {
    result.push({ ...baseFormConfig, ...props.formConfig, model: { content: '' } });
  }
  data.value = [];
  formRefs.value = {};
  nextTick(() => {
    data.value = result;
  });
});

const add = () => {
  const result = data.value;
  result.push({ ...baseFormConfig, ...props.formConfig, model: { content: '' } });
  data.value = [];
  formRefs.value = {};
  nextTick(() => {
    data.value = result;
  });
};

const moveUp = (item, index) => {
  const result = data.value;
  if (index > 0) {
    const temp = result[index];
    result[index] = result[index - 1];
    result[index - 1] = temp;
    data.value = [];
    formRefs.value = {};
    nextTick(() => {
      data.value = result;
    });
  }
};

const moveDown = (item, index) => {
  const result = data.value;
  if (index < result.length - 1) {
    const temp = result[index];
    result[index] = result[index + 1];
    result[index + 1] = temp;
    data.value = [];
    formRefs.value = {};
    nextTick(() => {
      data.value = result;
    });
  }
};

const deleteItem = (item, index) => {
  Modal.confirm({
    title: '是否删除该项？',
    icon: h(Icon, { icon: 'ant-design:question-circle-outlined', size: 36 }),
    content: '仅删除当前项，不会更新到系统中，如果需要更新，请点击保存或更新按钮。',
    onOk() {
      return new Promise((resolve, reject) => {
        const result = data.value;
        result.splice(index, 1);
        formRefs.value = {};
        nextTick(() => {
          data.value = result;
        });
        resolve();
      }).catch(() => console.log('error'));
    },
    onCancel() {},
  });
};

const insertItem = (item, index) => {
  const result = data.value;
  result.splice(index, 0, { ...baseFormConfig, ...props.formConfig, model: { content: '' } });
  data.value = [];
  formRefs.value = {};
  nextTick(() => {
    data.value = result;
  });
};

const appendItem = (item, index) => {
  const result = data.value;
  if (index < result.length - 1) {
    const temp = result[index];
    result[index] = result[index + 1];
    result[index + 1] = temp;
    data.value = [];
    formRefs.value = {};
    nextTick(() => {
      data.value = result;
    });
  }
};

const insert = () => {
  const result = data.value;
  result.unshift({ ...baseFormConfig, ...props.formConfig, model: {} });
  data.value = [];
  formRefs.value = {};
  nextTick(() => {
    data.value = result;
  });
};

async function validate() {
  // 验证所有的表单组件
  const data: any[] = [];
  let success = true;
  for (const itemRef of Object.values(formRefs.value)) {
    const itemData = await itemRef.validate();
    if (itemData) {
      data.push(itemData);
    } else {
      success = false;
    }
  }
  return new Promise(resolve => {
    return resolve(success ? { [props.fieldName]: props.multiple ? data : data[0] } : success);
  });
}

defineExpose({
  validate,
});
</script>
