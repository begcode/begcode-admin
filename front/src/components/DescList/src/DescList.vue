<template>
  <div class="p-2">
    <div class="p-2 bg-white">
      <a-list item-layout="horizontal" :data-source="data">
        <template #header>
          <div class="flex" style="justify-content: space-between">
            <div class="space-x-2">
              <slot name="header_left" />
            </div>
            <div class="space-x-2">
              <slot name="header_right"></slot>
              <template v-for="button in toolButtons">
                <a-tooltip @click="button.click" v-if="!button.hidden">
                  <template #title>{{ button.title }}</template>
                  <basic-button :disabled="button.disabled">
                    <Icon :icon="button.icon" v-if="button.icon" />
                  </basic-button>
                </a-tooltip>
              </template>
              <a-tooltip>
                <template #title>增加</template>
                <basic-button @click="addClick"><Icon icon="ant-design:plus-outlined" /></basic-button>
              </a-tooltip>
              <a-tooltip>
                <template #title>刷新</template>
                <basic-button><Icon icon="ant-design:redo-outlined" /></basic-button>
              </a-tooltip>
            </div>
          </div>
        </template>
        <template #renderItem="{ item, index }">
          <a-list-item style="align-items: start">
            <template #actions>
              <slot name="item_header_right"></slot>
              <a-space direction="vertical" :size="4">
                <a-tooltip placement="left">
                  <template #title>编辑</template>
                  <basic-button type="link" @click="editItem(item)">
                    <Icon icon="ant-design:edit-outlined" />
                  </basic-button>
                </a-tooltip>
                <a-tooltip placement="left">
                  <template #title>删除</template>
                  <basic-button type="link" @click="deleteItem(item, index)">
                    <Icon icon="ant-design:delete-outlined" />
                  </basic-button>
                </a-tooltip>
                <a-divider style="margin: 4px 0" />
                <a-tooltip placement="left">
                  <template #title>上移</template>
                  <basic-button type="link" @click="moveUp(item, index)">
                    <Icon icon="ant-design:arrow-up-outlined" />
                  </basic-button>
                </a-tooltip>
                <a-tooltip placement="left">
                  <template #title>下移</template>
                  <basic-button type="link" @click="moveDown(item, index)">
                    <Icon icon="ant-design:arrow-down-outlined" />
                  </basic-button>
                </a-tooltip>
              </a-space>
            </template>
            <a-row style="width: 100%">
              <slot :item="item">{{ item.name || item.id }}</slot>
            </a-row>
          </a-list-item>
        </template>
      </a-list>
    </div>
    <basic-modal v-bind="modalConfig" @register="registerModal" @ok="okModal">
      <component
        :is="modalConfig.componentName"
        @cancel="closeModalOrDrawer"
        @submit="closeModalOrDrawer"
        v-bind="modalConfig"
        ref="modalComponentRef"
      />
    </basic-modal>
    <basic-drawer v-bind="drawerConfig" @register="registerDrawer" @ok="okDrawer">
      <component
        :is="drawerConfig.componentName"
        @cancel="closeModalOrDrawer"
        @submit="closeModalOrDrawer"
        v-bind="drawerConfig"
        ref="drawerComponentRef"
      />
    </basic-drawer>
  </div>
</template>
<script lang="ts" setup>
import { Modal } from 'ant-design-vue';
import { useModal } from '@/components/Modal';
import { useDrawer } from '@/components/Drawer';
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
  configData: {
    type: Array as PropType<any[]>,
    default: [],
  },
  editComponentPath: {
    type: String,
    default: '',
  },
  size: {
    type: Number,
    default: 0,
  },
  savedOpen: {
    type: Boolean,
    default: false,
  },
  baseData: {
    type: Object as PropType<any>,
    default: () => ({}),
  },
  data: {
    type: Array as PropType<any[]>,
    default: [],
  },
  fieldName: {
    type: String,
    default: '',
  },
});
// 暴露内部方法
const emit = defineEmits(['getMethod', 'delete', 'submit', 'register', 'refresh']);

const [registerModal, { closeModal, setModalProps }] = useModal();
const [registerDrawer, { closeDrawer }] = useDrawer();

const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);

const modalConfig = reactive({
  componentName: '',
  entityId: '',
  containerType: 'modal',
  width: '80%',
  height: 500,
  savedOpen: props.savedOpen,
  baseData: props.baseData,
  destroyOnClose: true,
});
const drawerConfig = reactive({
  componentName: '',
  containerType: 'drawer',
  entityId: '',
  width: '75%',
  savedOpen: props.savedOpen,
  baseData: props.baseData,
  destroyOnClose: true,
});

function addClick() {
  const { editComponentPath } = props;
  if (editComponentPath) {
    const componentPath = (editComponentPath as string).replace('@/', '../../../');
    modalConfig.componentName = markRaw(defineAsyncComponent(() => import(/* @vite-ignore */ componentPath)));
    setModalProps({ open: true });
  }
}

function editItem(item) {
  const { editComponentPath } = props;
  if (editComponentPath) {
    const componentPath = (editComponentPath as string).replace('@/', '../../../');
    modalConfig.componentName = markRaw(defineAsyncComponent(() => import(/* @vite-ignore */ componentPath)));
    modalConfig.entityId = item.id;
    setModalProps({ open: true });
  }
}

// function deleteItem(item) {
//   console.log('item', item);
//   const { editComponentPath } = props;
//   console.log('editComponentPath', editComponentPath);
// }

const okModal = async () => {
  if (modalComponentRef.value) {
    await modalComponentRef.value.submit();
    emit('refresh');
  }
};

const okDrawer = async () => {
  if (drawerComponentRef.value) {
    await drawerComponentRef.value.submit();
    emit('refresh');
  }
};

function closeModalOrDrawer({ containerType, update }): void {
  if (containerType === 'modal') {
    closeModal();
  } else if (containerType === 'drawer') {
    closeDrawer();
  }
  if (update) {
    // todo
  }
}

const data = ref<any[]>([]);
watchEffect(() => {
  data.value = props.data;
});

const moveUp = (item, index) => {
  const temp = data.value[index];
  data.value[index] = data.value[index - 1];
  data.value[index - 1] = temp;
};

const moveDown = (item, index) => {
  const temp = data.value[index];
  data.value[index] = data.value[index + 1];
  data.value[index + 1] = temp;
};

const deleteItem = (item, index) => {
  Modal.confirm({
    title: '是否删除该项？',
    icon: h(Icon, { icon: 'ant-design:question-circle-outlined', size: 36 }),
    content: '仅删除当前项，不会更新到系统中，如果需要更新，请点击保存或更新按钮。',
    onOk() {
      return new Promise((resolve, reject) => {
        data.value.splice(index, 1);
        emit('delete', item);
        resolve();
      }).catch(() => console.log('error'));
    },
    onCancel() {},
  });
};

async function validate() {
  let success = true;
  /*// 验证所有的表单组件
  const data: any[] = [];
  let success = true;
  for (const itemRef of Object.values(formRefs.value)) {
    const itemData = await itemRef.validate();
    if (itemData) {
      data.push(itemData);
    } else {
      success = false;
    }
  }*/
  return new Promise(resolve => {
    return resolve(success ? { [props.fieldName]: data.value } : success);
  });
}

defineExpose({
  validate,
});
</script>
<style></style>
