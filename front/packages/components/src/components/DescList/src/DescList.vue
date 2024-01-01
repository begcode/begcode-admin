<template>
  <div class="p-2">
    <div class="p-2 bg-white">
      <List item-layout="horizontal" :data-source="data">
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
                <template #title>增加</template>
                <Button @click="addClick"><PlusOutlined /></Button>
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
            <template #actions>
              <slot name="item_header_right"></slot>
              <Button pre-icon="ant-design:edit-outlined" @click="editItem(item)"></Button>
              <Button pre-icon="ant-design:delete-twotone" @click="deleteItem(item)"></Button>
            </template>
            <ARow
              ><slot :item="item">{{ item.name || item.id }}</slot></ARow
            >
          </ListItem>
        </template>
      </List>
    </div>
    <BasicModal v-bind="modalConfig" @register="registerModal" @ok="okModal">
      <component
        :is="modalConfig.componentName"
        @cancel="closeModalOrDrawer"
        @submit="closeModalOrDrawer"
        v-bind="modalConfig"
        ref="modalComponentRef"
      />
    </BasicModal>
    <BasicDrawer v-bind="drawerConfig" @register="registerDrawer">
      <component
        :is="drawerConfig.componentName"
        @cancel="closeModalOrDrawer"
        @submit="closeModalOrDrawer"
        v-bind="drawerConfig"
        ref="drawerComponentRef"
      />
    </BasicDrawer>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref, markRaw, defineAsyncComponent } from 'vue';
import { RedoOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { List, Tooltip, Avatar } from 'ant-design-vue';
import { propTypes } from '@/utils/propTypes';
import { Button } from '@/components/Button';
import { useSlider } from './data';
import { BasicModal, useModalInner } from '@/components/Modal';
import { BasicDrawer, useDrawerInner } from '@/components/Drawer';

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
  configData: propTypes.array.def([]),
  editComponentPath: propTypes.string.def(''),
  size: propTypes.number.def(0),
  savedOpen: propTypes.bool.def(false),
  baseData: propTypes.object.def({}),
  data: propTypes.array.def([]),
});
//暴露内部方法
const emit = defineEmits(['getMethod', 'delete', 'submit', 'register']);

const [registerModal, { closeModal, setModalProps }] = useModalInner(data => {
  console.log(data);
});
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(data => {
  console.log(data);
});

const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);

const modalConfig = reactive({
  componentName: '',
  entityId: '',
  containerType: 'modal',
  savedOpen: props.savedOpen,
  baseData: props.baseData,
  destroyOnClose: true,
});
const drawerConfig = reactive({
  componentName: '',
  containerType: 'drawer',
  entityId: '',
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

function deleteItem(item) {
  const { editComponentPath } = props;
}

const okModal = () => {
  if (modalComponentRef.value) {
    modalComponentRef.value.saveOrUpdate();
  }
};
const okDrawer = () => {
  if (drawerComponentRef.value) {
    drawerComponentRef.value.saveOrUpdate();
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

const grid = 1 || props.size;

const descRefs = [];
const setDescRef = el => {
  if (el) {
    descRefs.push(el);
  }
};

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
  props.configData.forEach(item => {
    if (item) {
      item.formConfig = { ...baseFormConfig, ...item.formConfig };
      result.push(item);
    }
  });
  return result;
};
const data = ref(props.data);
// 切换每行个数
// cover图片自适应高度
//修改pageSize并重新请求数据

const height = computed(() => {
  return `h-${120 - grid * 6}`;
});

//表单提交
async function handleSubmit() {
  // 验证所有的表单组件
  const data = [];
  descRefs.forEach(item => {
    // item 即为对应的组件ref
    // 可通过 item 获取对应组件上的属性或方法
  });
  return data;
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
</script>
<style></style>
