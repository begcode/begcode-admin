<template>
  <!-- begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->
  <div style="height: 100%; padding-bottom: 10px">
    <SplitPanes class="default-theme">
      <SplitPane size="40">
        <a-card :bordered="false" class="bc-list-result-card">
          <a-row :gutter="16" style="margin-bottom: 10px">
            <a-col :span="12" v-if="!searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled">
              <a-space>
                <a-input
                  v-model:value="searchFormConfig.jhiCommonSearchKeywords"
                  placeholder="请输入关键字"
                  allow-clear
                  @change="inputSearch"
                  @pressEnter="formSearch"
                  class="search-input"
                  style="width: 200px"
                  data-cy="listSearchInput"
                >
                  <template #addonAfter>
                    <BasicButton type="link" @click="formSearch" style="height: 30px; padding: 4px 8px" data-cy="listSearchButton"
                      ><Icon icon="ant-design:search-outlined" />查询</BasicButton
                    >
                  </template>
                </a-input>
                <template v-for="button of toolbarButtons">
                  <a-button v-if="!button.dropdowns">{{ button.name }}</a-button>
                  <a-dropdown v-else-if="selectedRows.length" :key="button.name" :content="button.name">
                    <template #overlay>
                      <a-menu v-for="subButton of button.dropdowns">
                        <a-menu-item :key="subButton.name + 's'">
                          <Icon :icon="subButton.icon"></Icon>
                          {{ subButton.name }}
                        </a-menu-item>
                      </a-menu>
                    </template>
                    <BasicButton>
                      {{ button.name }}
                      <Icon icon="ant-design:down-outlined" />
                    </BasicButton>
                  </a-dropdown>
                </template>
              </a-space>
            </a-col>
            <a-col :span="12" style="display: flex; justify-content: flex-end">
              <a-space align="end">
                <template v-for="button of toolbarTools">
                  <BasicButton v-if="!button.dropdowns" @click="toolbarClick({ code: button.code })">{{ button.name }}</BasicButton>
                  <a-dropdown v-else-if="selectedRows.length" :key="button.name" :content="button.name">
                    <template #overlay>
                      <a-menu v-for="subButton of button.dropdowns">
                        <a-menu-item :key="subButton.name + 's'">
                          <Icon :icon="subButton.icon"></Icon>
                          {{ subButton.name }}
                        </a-menu-item>
                      </a-menu>
                    </template>
                    <BasicButton>
                      {{ button.name }}
                      <Icon icon="ant-design:down-outlined" />
                    </BasicButton>
                  </a-dropdown>
                </template>
              </a-space>
            </a-col>
          </a-row>
          <BasicTree
            ref="treeRef"
            :expandedKeys="treeConfig.expandedKeys"
            :autoExpandParent="treeConfig.autoExpandParent"
            :selectedKeys="treeConfig.selectedKeys"
            :treeData="treeConfig.treeData"
            :checkable="true"
            :field-names="treeConfig.fieldNames"
            @select="treeSelect"
            @check="treeCheck"
          >
            <template #title="item">
              <a-dropdown :trigger="['contextmenu']">
                <a-popconfirm
                  :open="treeConfig.visibleTreeKey === item.id"
                  title="确定要删除吗？"
                  ok-text="确定"
                  cancel-text="取消"
                  placement="rightTop"
                  @confirm="deleteById(item.id)"
                  @openChange="onVisibleChange"
                >
                  <span>{{ item.title }}</span>
                </a-popconfirm>

                <template #overlay>
                  <a-menu @click="">
                    <a-menu-item key="1" @click="rowClick({ name: 'addChildren', data: item, params: {} })">
                      <Icon icon="ant-design:file-add-outlined" style="margin-right: 4px" />添加子级</a-menu-item
                    >
                    <a-menu-item key="2" @click="treeConfig.visibleTreeKey = item.id">
                      <span style="color: red"><Icon icon="ant-design:delete-outlined" style="margin-right: 4px" />删除</span>
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
          </BasicTree>

          <BasicModal
            v-bind="popupConfig.containerProps"
            @register="registerModal"
            @cancel="closeModal"
            @ok="okModal"
            v-on="popupConfig.containerEvents"
          >
            <component
              v-if="popupConfig.componentProps.is"
              v-bind="popupConfig.componentProps"
              :is="popupConfig.componentProps.is"
              @cancel="closeModal"
              @refresh="formSearch"
              v-on="popupConfig.componentEvents"
              ref="modalComponentRef"
            />
          </BasicModal>
          <BasicDrawer
            v-bind="popupConfig.containerProps"
            @register="registerDrawer"
            @close="closeDrawer"
            @ok="okDrawer"
            v-on="popupConfig.containerEvents"
          >
            <component
              v-if="popupConfig.componentProps.is"
              v-bind="popupConfig.componentProps"
              :is="popupConfig.componentProps.is"
              @cancel="closeDrawer"
              @refresh="formSearch"
              v-on="popupConfig.componentEvents"
              ref="drawerComponentRef"
            />
          </BasicDrawer>
        </a-card>
      </SplitPane>
      <SplitPane>
        <a-card :bordered="false" class="bc-list-result-card" :bodyStyle="{ 'padding-top': '8px' }">
          <a-tabs defaultActiveKey="baseInfo" type="card" v-if="currentRow">
            <a-tab-pane key="baseInfo" tab="基本信息">
              <ResourceCategoryEdit :entity-id="currentRow?.id || ''" :form-buttons="['submit', 'reset']" />
            </a-tab-pane>
          </a-tabs>
          <a-empty description="尚未选择资源分类" v-else />
        </a-card>
      </SplitPane>
    </SplitPanes>
  </div>
</template>

<script lang="ts" setup>
import { Modal, message } from 'ant-design-vue';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { useDrawer } from '@/components/Drawer';
import { useModal } from '@/components/Modal';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import { useSetShortcutButtons } from '@/components/VxeTable/src/helper';
import ResourceCategoryEdit from './components/form-component.vue';
import ResourceCategoryDetail from './components/detail-component.vue';
import config from './config/list-config';
import UploadImageList from '@/views/files/upload-image/upload-image-list.vue';
import { upperFirst as _upperFirst } from 'lodash-es';
import { AvatarGroupInfo } from '@/components/AvatarGroupInfo';
import UploadFileList from '@/views/files/upload-file/upload-file-list.vue';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const props = defineProps(config.ListProps);

const [registerModal, { closeModal, setModalProps }] = useModal();
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawer();
const shallowRefs = {
  ResourceCategoryEdit: shallowRef(ResourceCategoryEdit),
  ResourceCategoryDetail: shallowRef(ResourceCategoryDetail),
  UploadImageList: shallowRef(UploadImageList),
  AvatarGroupInfo: shallowRef(AvatarGroupInfo),
  UploadFileList: shallowRef(UploadFileList),
};
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const apis = {
  resourceCategoryService: apiService.files.resourceCategoryService,
  find: apiService.files.resourceCategoryService.tree,
  findByParentId: apiService.files.resourceCategoryService.treeByParentId,
  deleteById: apiService.files.resourceCategoryService.delete,
  deleteByIds: apiService.files.resourceCategoryService.deleteByIds,
  import: apiService.files.resourceCategoryService.importExcel,
  export: apiService.files.resourceCategoryService.exportExcel,
  update: apiService.files.resourceCategoryService.update,
  imagesStats: apiService.files.uploadImageService.stats,
  filesStats: apiService.files.uploadFileService.stats,
  children: apiService.files.resourceCategoryService.tree,
  parent: apiService.files.resourceCategoryService.tree,
  images: apiService.files.uploadImageService.retrieve,
  files: apiService.files.uploadFileService.retrieve,
};
const pageConfig = {
  title: '资源分类列表',
  baseRouteName: 'ossResourceCategory',
};
const treeRef = ref<any>();
const currentRow = ref<any>(null);
const searchFormConfig = reactive<Record<string, any>>({
  fieldList: config.searchForm(apis),
  toggleSearchStatus: false,
  useOr: false,
  disabled: false,
  allowSwitch: false,
  compact: true,
  jhiCommonSearchKeywords: '',
  ...props.searchFormOptions,
});
const rowOperations = ref<any[]>([
  {
    title: '下级',
    name: 'addChildren',
    containerType: 'modal',
    type: 'link',
  },
  {
    title: '删除',
    name: 'delete',
    type: 'link',
  },
]);

const extraButtons = ref([
  {
    show: props.cardExtra?.includes('import'),
    title: '导入',
    name: 'import',
    icon: 'ant-design:cloud-upload-outlined',
    click: () => {},
  },
  {
    show: props.cardExtra?.includes('export'),
    name: 'export',
    title: '导出',
    icon: 'ant-design:download-outlined',
    click: () => {},
  },
  {
    show: props.cardExtra?.includes('print'),
    title: '打印',
    name: 'print',
    icon: 'ant-design:printer-outlined',
    click: () => {},
  },
]);
useSetShortcutButtons('OssResourceCategoryList', extraButtons);

const selectedRows = reactive<any>([]);
const searchFormRef = ref<any>(null);
const treeConfig = reactive({
  treeData: [] as any[],
  expandedKeys: [],
  checkedKeys: [],
  selectedKeys: [],
  autoExpandParent: true,
  visibleTreeKey: null,
  fieldNames: {
    title: 'title',
    key: 'id',
    children: 'children',
  },
});
const popupConfig = reactive<any>({
  needSubmit: false,
  containerProps: {
    width: '80%',
    destroyOnClose: true,
    okText: '确定',
    cancelText: '取消',
  },
  containerEvents: {},
  componentProps: {
    containerType: 'modal',
    baseData: props.baseData,
    is: '',
    entityId: '',
  },
  componentEvents: {},
});
const queryParams = ref<any>({ ...props.query });
// 表格左上角自定义按钮
const toolbarButtons = [
  {
    name: '批量操作',
    circle: false,
    icon: 'vxe-icon-add',
    status: 'primary',
    dropdowns: [{ code: 'batchDelete', name: '删除', circle: false, icon: 'ant-design:delete-filled', status: 'primary' }],
  },
];
// 表格右上角自定义按钮
const toolbarTools: any[] = [
  { code: 'new', name: '新增', circle: false, icon: 'vxe-icon-add' },
  { code: 'open-tree', name: '展开', circle: false, icon: 'vxe-icon-square-plus' },
  { code: 'close-tree', name: '折叠', circle: false, icon: 'vxe-icon-square-minus' },
];
const toolbarClick = ({ code }) => {
  switch (code) {
    case 'batchDelete': {
      const records = treeConfig.checkedKeys.map(id => ({ id }));
      if (records?.length > 0) {
        const ids = records.map(record => record.id);
        Modal.confirm({
          title: `操作提示`,
          content: `是否删除ID为【${ids.join(',')}】的${records.length}项数据？`,
          onOk() {
            apis.deleteByIds(ids).then(() => {
              formSearch();
            });
          },
        });
      }
      break;
    }
    case 'open-tree':
      treeRef.value.expandAll(true);
      break;
    case 'close-tree':
      treeRef.value.expandAll(false);
      break;
    case 'new':
      popupConfig.containerProps.title = '新建';
      popupConfig.containerProps.okText = '保存';
      popupConfig.containerProps.cancelText = '取消';
      popupConfig.needSubmit = true;
      popupConfig.containerProps.showOkBtn = true;
      popupConfig.containerProps.showCancelBtn = true;
      popupConfig.componentProps.is = shallowRefs.ResourceCategoryEdit;
      popupConfig.componentProps.entityId = '';
      if (props.editIn === 'modal') {
        popupConfig.componentProps.containerType = 'modal';
        setModalProps({ open: true });
      } else if (props.editIn === 'drawer') {
        popupConfig.componentProps.containerType = 'drawer';
        setDrawerProps({ open: true });
      } else {
        if (pageConfig.baseRouteName) {
          go({ name: `${pageConfig.baseRouteName}New` });
        } else {
          console.log('未定义方法');
        }
      }
      break;
  }
};

const treeCheck = checkedKeys => {
  treeConfig.checkedKeys = checkedKeys;
};

const onVisibleChange = visible => {
  if (!visible) {
    treeConfig.visibleTreeKey = null;
  }
};

const treeSelect = (_selectedKeys, info) => {
  currentRow.value = info.node.dataRef;
};
const okModal = async () => {
  if (popupConfig.needSubmit && modalComponentRef.value) {
    const result = await modalComponentRef.value.submit();
    if (result) {
      formSearch();
      closeModal();
    }
  }
};
const okDrawer = async () => {
  if (popupConfig.needSubmit && drawerComponentRef.value) {
    const result = await drawerComponentRef.value.submit();
    if (result) {
      formSearch();
      closeDrawer();
    }
  }
};
const formSearch = () => {
  queryParams.value.page = 0;
  queryParams.value.size = -1;
  Object.assign(queryParams.value, props.query);
  if (searchFormConfig.jhiCommonSearchKeywords) {
    queryParams.value['jhiCommonSearchKeywords'] = searchFormConfig.jhiCommonSearchKeywords;
  } else {
    delete queryParams.value['jhiCommonSearchKeywords'];
    Object.assign(queryParams.value, getSearchQueryData(searchFormConfig));
  }
  apis.find(queryParams.value).then(res => {
    treeConfig.treeData = res.records;
  });
};
const deleteById = id => {
  apis.deleteById(id).then(() => {
    if (currentRow.value?.id === id) {
      currentRow.value = null;
    }
    formSearch();
  });
};
const inputSearch = _debounce(formSearch, 700);
const handleToggleSearch = () => {
  if (searchFormConfig.allowSwitch) {
    searchFormConfig.toggleSearchStatus = !searchFormConfig.toggleSearchStatus;
  }
};
const showSearchFormSetting = () => {
  if (searchFormRef.value) {
    searchFormRef.value.showSettingModal();
  }
};

const rowClick = ({ name, data, params }) => {
  const row = data;
  const operation = rowOperations.value.find(operation => operation.name === name);
  if (operation?.click) {
    operation.click(row);
  } else {
    switch (name) {
      case 'addChildren':
        popupConfig.containerProps.title = '添加下级';
        popupConfig.containerProps.okText = '更新';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.needSubmit = true;
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.ResourceCategoryEdit;
        popupConfig.componentProps.entityId = '';
        popupConfig.componentProps.baseData = { parentId: row.id, parent: row };
        switch (operation?.containerType || props.editIn) {
          case 'modal':
            popupConfig.componentProps.containerType = 'modal';
            setModalProps({ open: true });
            break;
          case 'drawer':
            popupConfig.componentProps.containerType = 'drawer';
            setDrawerProps({ open: true });
            break;
          case 'page':
          default:
            if (pageConfig.baseRouteName) {
              go({ name: `${pageConfig.baseRouteName}New`, query: { parentId: row.id, parent: row } });
            } else {
              console.log('未定义方法');
            }
        }
        break;
      case 'delete':
        Modal.confirm({
          title: `操作提示`,
          content: `是否确认删除ID为${row.id}的记录？`,
          onOk() {
            apis.deleteById(row.id).then(() => {
              formSearch();
            });
          },
        });
        break;
      case 'imagesColumnView': {
        popupConfig.containerProps.title = '查看上传图片';
        popupConfig.containerProps.okText = '';
        popupConfig.containerProps.cancelText = '关闭';
        popupConfig.needSubmit = false;
        popupConfig.containerProps.showOkBtn = false;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.UploadImageList;
        const rowParams = {
          ...params,
          containerType: 'modal',
          cardExtra: [],
          cardSlots: [],
          selectType: 'none',
          gridOptions: {
            toolbarConfig: {
              import: false,
              print: false,
              export: false,
              custom: false,
              tools: [],
              buttons: [],
            },
            height: row.images?.length > 0 ? 50 * ((row.images || []).length + 3) : undefined,
          },
          source: 'ResourceCategory',
          gridCustomConfig: {
            rowOperations: ['detail'],
            hideColumns: ['category'],
          },
          field: 'images',
          query: {
            categoryId: row.id,
          },
        };
        Object.assign(popupConfig.componentProps, rowParams);
        if (rowParams.containerType === 'drawer') {
          popupConfig.containerProps.width = '45%';
          popupConfig.containerProps.showFooter = true;
          setDrawerProps({ open: true });
        } else {
          popupConfig.containerProps.width = '80%';
          setModalProps({ open: true });
        }
        break;
      }
      case 'filesColumnView': {
        popupConfig.containerProps.title = '查看上传文件';
        popupConfig.containerProps.okText = '';
        popupConfig.containerProps.cancelText = '关闭';
        popupConfig.needSubmit = false;
        popupConfig.containerProps.showOkBtn = false;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.UploadFileList;
        const rowParams = {
          ...params,
          containerType: 'modal',
          cardExtra: [],
          cardSlots: [],
          selectType: 'none',
          gridOptions: {
            toolbarConfig: {
              import: false,
              print: false,
              export: false,
              custom: false,
              tools: [],
              buttons: [],
            },
            height: row.files?.length > 0 ? 50 * ((row.files || []).length + 3) : undefined,
          },
          source: 'ResourceCategory',
          gridCustomConfig: {
            rowOperations: ['detail'],
            hideColumns: ['category'],
          },
          field: 'files',
          query: {
            categoryId: row.id,
          },
        };
        Object.assign(popupConfig.componentProps, rowParams);
        if (rowParams.containerType === 'drawer') {
          popupConfig.containerProps.width = '45%';
          popupConfig.containerProps.showFooter = true;
          setDrawerProps({ open: true });
        } else {
          popupConfig.containerProps.width = '80%';
          setModalProps({ open: true });
        }
        break;
      }
      default:
        console.log('error', `${name}未定义`);
    }
  }
};

const getSelectRows = () => {
  return toRaw(selectedRows);
};
formSearch();
defineExpose({ getSelectRows });
</script>
<style lang="less" scoped>
.search-input :deep(.ant-input-group-addon) {
  padding: 0;
  .ant-btn > .anticon + span {
    margin-inline-start: 2px;
  }
}
</style>
