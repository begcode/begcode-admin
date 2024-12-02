<template>
  <!-- begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->
  <div>
    <a-card
      v-if="searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled"
      title="高级搜索"
      class="bc-list-search-form-card"
      :body-style="{ 'padding-top': '12px', 'padding-bottom': '8px' }"
      :head-style="{ 'min-height': '40px' }"
    >
      <template #extra>
        <a-space>
          <BasicButton
            type="default"
            @click="showSearchFormSetting"
            pre-icon="ant-design:setting-outlined"
            shape="circle"
            size="small"
          ></BasicButton>
        </a-space>
      </template>
      <SearchForm :config="searchFormConfig" @formSearch="formSearch" @close="handleToggleSearch" ref="searchFormRef" />
    </a-card>
    <a-row
      v-if="fieldSearchValues && fieldSearchValues.length && !searchFormConfig.toggleSearchStatus"
      style="background-color: #ffffff; padding: 4px; margin: 8px; border-radius: 4px"
    >
      <a-col :span="24" style="padding-left: 20px">
        <span>搜索条件：</span>
        <a-space>
          <a-tag closable v-for="fieldVale of fieldSearchValues" @close="closeSearchFieldTag(fieldVale)">
            {{ fieldVale.title }}: {{ fieldVale.value }}
          </a-tag>
        </a-space>
      </a-col>
    </a-row>
    <a-card :bordered="false" class="bc-list-result-card" :bodyStyle="{ 'padding-top': '1px' }">
      <Grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents" data-cy="entityTable">
        <template #toolbar_buttons>
          <a-row :gutter="16">
            <a-col v-if="!searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled">
              <a-space>
                <a-input
                  v-model:value="searchFormConfig.jhiCommonSearchKeywords"
                  placeholder="请输入关键字"
                  allow-clear
                  @change="inputSearch"
                  @pressEnter="formSearch"
                  style="width: 280px"
                  data-cy="listSearchInput"
                >
                  <template #prefix>
                    <Icon icon="ant-design:search-outlined" />
                  </template>
                  <template #addonAfter>
                    <BasicButton type="link" @click="formSearch" style="height: 30px" data-cy="listSearchButton"
                      >查询<Icon icon="ant-design:filter-outlined" @click="handleToggleSearch" data-cy="listSearchMore" />
                    </BasicButton>
                  </template>
                </a-input>
                <template v-for="button of gridOptions?.toolbarConfig?.buttons">
                  <BasicButton v-if="!button.dropdowns">{{ button.name }}</BasicButton>
                  <a-dropdown v-else-if="selectedRows.length" :key="button.name" :content="button.name">
                    <template #overlay>
                      <a-menu @click="gridEvents.toolbarButtonClick(subButton)" v-for="subButton of button.dropdowns">
                        <a-menu-item :key="subButton.name + 's'">
                          <Icon :icon="subButton.icon" />
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
        </template>
        <template #recordAction="{ row }">
          <ButtonGroup :row="row" :buttons="rowOperations" @click="rowClick" :ref="el => rowOperationRef('row_operation_' + row.id, el)" />
        </template>
        <template #pagerLeft>
          <a-alert type="warning" banner :message="'已选择 ' + selectedRows.length + ' 项'" style="height: 30px" />
        </template>
      </Grid>
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
  </div>
</template>

<script lang="ts" setup>
import { Modal, message } from 'ant-design-vue';
import { VxeGridInstance, VxeGridListeners, VxeGridProps, Grid } from 'vxe-table';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { useDrawer } from '@/components/Drawer';
import { useModal } from '@/components/Modal';
import { ButtonGroup } from '@/components/Button';
import { clearSearchFieldValue } from '@/components/SearchForm';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import { useMergeGridProps, useColumnsConfig, useSetOperationColumn, useSetShortcutButtons } from '@/components/VxeTable/src/helper';
import ViewPermissionForm from './components/form-component.vue';
import ViewPermissionDetail from './components/detail-component.vue';
import config from './config/list-config';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const props = defineProps(config.ListProps);

const [registerModal, { closeModal, setModalProps }] = useModal();
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawer();
const shallowRefs = {
  ViewPermissionEdit: shallowRef(ViewPermissionForm),
  ViewPermissionDetail: shallowRef(ViewPermissionDetail),
};
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const apis = {
  viewPermissionService: apiService.system.viewPermissionService,
  find: apiService.system.viewPermissionService.tree,
  deleteById: apiService.system.viewPermissionService.delete,
  deleteByIds: apiService.system.viewPermissionService.deleteByIds,
  update: apiService.system.viewPermissionService.update,
  import: apiService.system.viewPermissionService.importExcel,
  export: apiService.system.viewPermissionService.exportExcel,
  children: apiService.system.viewPermissionService.tree,
  parent: apiService.system.viewPermissionService.tree,
  authorities: apiService.system.authorityService.tree,
};
const pageConfig = {
  title: '可视权限列表',
  baseRouteName: 'systemViewPermission',
};
const { columns } = useColumnsConfig(config.columns(), props.selectType, props.gridCustomConfig);
const xGrid = ref({} as VxeGridInstance);
const searchFormConfig = reactive<Record<string, any>>({
  fieldList: config.searchForm(apis),
  toggleSearchStatus: false,
  useOr: false,
  disabled: false,
  allowSwitch: true,
  compact: true,
  jhiCommonSearchKeywords: '',
  ...props.searchFormOptions,
});
const fieldSearchValues = computed(() => {
  return searchFormConfig.fieldList
    .filter(field => !field.hidden)
    .filter(field => {
      return (
        field.value !== null && field.value !== undefined && field.value !== '' && !(_isArray(field.value) && field.value.length === 0)
      );
    });
});
const rowOperations = ref<any[]>([
  {
    title: '保存',
    hide: row => !xGrid.value.isEditByRow(row) || !xGrid.value.props.editConfig?.mode === 'row',
    name: 'save',
    type: 'link',
  },
  {
    title: '编辑',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    name: 'edit',
    type: 'link',
  },
  {
    title: '下级',
    name: 'addChildren',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    containerType: 'modal',
    type: 'link',
  },
  {
    title: '删除',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    name: 'delete',
    type: 'link',
  },
  {
    title: '详情',
    name: 'detail',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    containerType: 'drawer',
    type: 'link',
  },
]);
const { rowOperationRef } = useSetOperationColumn(props.gridCustomConfig, rowOperations, xGrid);

const extraButtons = ref([
  {
    show: props.cardExtra?.includes('import'),
    title: '导入',
    name: 'import',
    icon: 'ant-design:cloud-upload-outlined',
    click: () => {
      xGrid.value.openImport();
    },
  },
  {
    show: props.cardExtra?.includes('export'),
    name: 'export',
    title: '导出',
    icon: 'ant-design:download-outlined',
    click: () => {
      xGrid.value.openExport();
    },
  },
  {
    show: props.cardExtra?.includes('print'),
    title: '打印',
    name: 'print',
    icon: 'ant-design:printer-outlined',
    click: () => {
      xGrid.value.openPrint();
    },
  },
]);
useSetShortcutButtons('SystemViewPermissionList', extraButtons);

const selectedRows = reactive<any>([]);
const searchFormRef = ref<any>(null);
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
const ajax = {
  query: async ({ filters, page, sort, sorts }) => {
    console.log('filters', filters);
    queryParams.value.page = page?.currentPage > 0 ? page.currentPage - 1 : 0;
    queryParams.value.size = page?.pageSize;
    const allSort = sorts || [];
    sort && allSort.push(sort);
    queryParams.value = { ...props.query };
    queryParams.value.sort = transVxeSorts(allSort);
    if (searchFormConfig.jhiCommonSearchKeywords) {
      queryParams.value['jhiCommonSearchKeywords'] = searchFormConfig.jhiCommonSearchKeywords;
    } else {
      delete queryParams.value['jhiCommonSearchKeywords'];
      Object.assign(queryParams.value, getSearchQueryData(searchFormConfig));
    }
    return await apis.find(queryParams.value);
  },
  queryAll: async () => await apis.find({ size: -1 }),
  import: async ({ file, options }) => {
    apis.import(file).then(() => {
      formSearch();
    });
  },
  delete: async records => await apis.deleteByIds(records.body.removeRecords.map(record => record.id)),
};
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
const toolbarTools = [
  { code: 'new', name: '新增', circle: false, icon: 'vxe-icon-add' },
  { code: 'open-tree', name: '展开', circle: false, icon: 'vxe-icon-square-plus' },
  { code: 'close-tree', name: '折叠', circle: false, icon: 'vxe-icon-square-minus' },
  { code: 'custom-column', name: '列配置', circle: false, icon: 'vxe-icon-custom-column' },
];
const gridOptions = reactive<VxeGridProps>({
  ...config.baseGridOptions(ajax, toolbarButtons, toolbarTools),
  columns,
});
useMergeGridProps(gridOptions, props.gridOptions);
const toolbarClick = ({ code }) => {
  const $grid = xGrid.value;
  switch (code) {
    case 'batchDelete': {
      const records = $grid.getCheckboxRecords(true);
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
      gridOptions.loadingConfig = { text: '正在展开...' };
      gridOptions.loading = true;
      setTimeout(() => {
        xGrid.value.setAllTreeExpand(true).then(() => {
          gridOptions.loading = undefined;
          gridOptions.loadingConfig = undefined;
        });
      }, 10);
      break;
    case 'close-tree':
      gridOptions.loadingConfig = { text: '正在折叠...' };
      gridOptions.loading = true;
      setTimeout(() => {
        xGrid.value.clearTreeExpand().then(() => {
          gridOptions.loading = undefined;
          gridOptions.loadingConfig = undefined;
        });
      }, 10);
      break;
    case 'custom-column':
      xGrid.value.openCustom();
      break;
    case 'new':
      popupConfig.needSubmit = true;
      popupConfig.containerProps.title = '新建';
      popupConfig.containerProps.okText = '保存';
      popupConfig.containerProps.cancelText = '取消';
      popupConfig.containerProps.showOkBtn = true;
      popupConfig.containerProps.showCancelBtn = true;
      popupConfig.componentProps.is = shallowRefs.ViewPermissionEdit;
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

const checkboxChange = () => {
  const $grid = xGrid.value;
  selectedRows.length = 0;
  selectedRows.push(...$grid.getCheckboxRecords());
};
const gridEvents = reactive<VxeGridListeners>({
  checkboxAll: checkboxChange,
  checkboxChange: checkboxChange,
  pageChange({ currentPage, pageSize }) {
    if (gridOptions.pagerConfig) {
      gridOptions.pagerConfig.currentPage = currentPage;
      gridOptions.pagerConfig.pageSize = pageSize;
    }
  },
  radioChange() {
    const $grid = xGrid.value;
    selectedRows.length = 0;
    selectedRows.push($grid.getRadioRecord());
  },
  // 表格左上角按钮事件
  toolbarButtonClick: toolbarClick,
  // 表格右上角自定义按钮事件
  toolbarToolClick: toolbarClick,
});
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
  xGrid.value.commitProxy('reload');
};
const closeSearchFieldTag = field => {
  clearSearchFieldValue(field);
  formSearch();
};
const inputSearch = _debounce(formSearch, 700);
const handleToggleSearch = () => {
  searchFormConfig.toggleSearchStatus = !searchFormConfig.toggleSearchStatus;
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
      case 'save':
        break;
      case 'edit':
        popupConfig.needSubmit = true;
        popupConfig.containerProps.title = '编辑可视权限';
        popupConfig.containerProps.okText = '更新';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.ViewPermissionEdit;
        popupConfig.componentProps.entityId = row.id;
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
              go({ name: `${pageConfig.baseRouteName}Edit`, params: { entityId: row.id } });
            } else {
              console.log('未定义方法');
            }
        }
        break;
      case 'detail':
        popupConfig.containerProps.title = '详情';
        popupConfig.containerProps.cancelText = '关闭';
        popupConfig.needSubmit = false;
        popupConfig.containerProps.showOkBtn = false;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.ViewPermissionDetail;
        popupConfig.componentProps.entityId = row.id;
        switch (operation?.containerType || 'page') {
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
              go({ name: `${pageConfig.baseRouteName}Detail`, params: { entityId: row.id } });
            } else {
              console.log('未定义方法');
            }
        }
        break;
      case 'addChildren':
        popupConfig.containerProps.title = '添加下级';
        popupConfig.containerProps.okText = '保存';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.needSubmit = true;
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.ViewPermissionEdit;
        popupConfig.componentProps.entityId = '';
        popupConfig.componentProps.baseData = { parentId: row.id, parent: row };
        switch (operation?.containerType || props.editIn || 'page') {
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
      default:
        console.log('error', `${name}未定义`);
    }
  }
};

const getSelectRows = () => {
  return toRaw(selectedRows);
};

defineExpose({ getSelectRows });
</script>
