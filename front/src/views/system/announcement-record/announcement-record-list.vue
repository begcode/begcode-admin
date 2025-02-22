<template>
  <!-- begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->
  <div data-cy="AnnouncementRecordHeading">
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
      <vxe-grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents" data-cy="entityTable">
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
                      <a-menu @click="gridEvents.toolbarButtonClick?.(subButton as any)" v-for="subButton of button.dropdowns">
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
      </vxe-grid>
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
import { VxeGridInstance, VxeGridListeners, VxeGridProps } from 'vxe-table';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { useDrawer } from '@/components/Drawer';
import { useModal } from '@/components/Modal';
import { ButtonGroup } from '@/components/Button';
import { clearSearchFieldValue } from '@/components/SearchForm';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import { useMergeGridProps, useColumnsConfig, useSetOperationColumn, useSetShortcutButtons } from '@/components/VxeTable/src/helper';
import AnnouncementRecordForm from './components/form-component.vue';
import AnnouncementRecordDetail from './components/detail-component.vue';
import config from './config/list-config';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const props = defineProps(config.ListProps);

const [registerModal, { closeModal, setModalProps }] = useModal();
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawer();
const shallowRefs = {
  AnnouncementRecordEdit: shallowRef(AnnouncementRecordForm),
  AnnouncementRecordDetail: shallowRef(AnnouncementRecordDetail),
};
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const apis = {
  announcementRecordService: apiService.system.announcementRecordService,
  auditingUser: apiService.system.userService.retrieve,
  find: apiService.system.announcementRecordService.retrieve,
  deleteById: apiService.system.announcementRecordService.delete,
  deleteByIds: apiService.system.announcementRecordService.deleteByIds,
  update: apiService.system.announcementRecordService.update,
  import: apiService.system.announcementRecordService.importExcel,
  export: apiService.system.announcementRecordService.exportExcel,
};
const pageConfig = {
  title: '通告阅读记录列表',
  baseRouteName: 'systemAnnouncementRecord',
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
    title: '详情',
    name: 'detail',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    containerType: 'drawer',
    type: 'link',
    attrs: {
      'data-cy': 'entityDetailsButton',
    },
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
    attrs: {
      'data-cy': 'entityImportButton',
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
    attrs: {
      'data-cy': 'entityExportButton',
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
    attrs: {
      'data-cy': 'entityPrintButton',
    },
  },
]);
useSetShortcutButtons('SystemAnnouncementRecordList', extraButtons);

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
const toolbarButtons = [{ name: '批量操作', circle: false, icon: 'vxe-icon-add', status: 'primary', dropdowns: [] }];
// 表格右上角自定义按钮
const toolbarTools = [{ code: 'custom-column', name: '列配置', circle: false, icon: 'vxe-icon-custom-column' }];
const gridOptions = reactive<VxeGridProps>({
  ...config.baseGridOptions(ajax, toolbarButtons, toolbarTools),
  columns,
});
useMergeGridProps(gridOptions, props.gridOptions);
function toolbarClick({ code }) {
  const $grid = xGrid.value;
  switch (code) {
    case 'custom-column':
      xGrid.value.openCustom();
      break;
  }
}

function checkboxChange() {
  const $grid = xGrid.value;
  selectedRows.length = 0;
  selectedRows.push(...$grid.getCheckboxRecords());
}
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
async function okModal() {
  if (popupConfig.needSubmit && modalComponentRef.value) {
    const result = await modalComponentRef.value.submit();
    if (result) {
      formSearch();
      closeModal();
    }
  }
}
async function okDrawer() {
  if (popupConfig.needSubmit && drawerComponentRef.value) {
    const result = await drawerComponentRef.value.submit();
    if (result) {
      formSearch();
      closeDrawer();
    }
  }
}
function formSearch() {
  xGrid.value.commitProxy('reload');
}
function closeSearchFieldTag(field) {
  clearSearchFieldValue(field);
  formSearch();
}
const inputSearch = _debounce(formSearch, 700);
function handleToggleSearch() {
  searchFormConfig.toggleSearchStatus = !searchFormConfig.toggleSearchStatus;
}
function showSearchFormSetting() {
  if (searchFormRef.value) {
    searchFormRef.value.showSettingModal();
  }
}

function rowClick({ name, data, params }) {
  const row = data;
  const operation = rowOperations.value.find(operation => operation.name === name);
  if (operation?.click) {
    operation.click(row);
  } else {
    switch (name) {
      case 'save':
        break;
      case 'detail':
        popupConfig.containerProps.title = '详情';
        popupConfig.containerProps.cancelText = '关闭';
        popupConfig.needSubmit = false;
        popupConfig.containerProps.showOkBtn = false;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.AnnouncementRecordDetail;
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
      default:
        console.log('error', `${name}未定义`);
    }
  }
}

const getSelectRows = () => {
  return toRaw(selectedRows);
};

defineExpose({ getSelectRows });
</script>
