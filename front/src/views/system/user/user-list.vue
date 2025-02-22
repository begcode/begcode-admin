<template>
  <!-- begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->
  <div data-cy="UserHeading">
    <SplitPanes class="default-theme">
      <SplitPane size="20">
        <a-card
          title="过滤列表"
          class="bc-list-result-card"
          :body-style="{ padding: '12px 12px 8px 12px' }"
          :head-style="{ 'min-height': '40px', padding: '12px' }"
        >
          <BasicTree
            :expandedKeys="filterTreeConfig.expandedKeys"
            :autoExpandParent="filterTreeConfig.autoExpandParent"
            :selectedKeys="filterTreeConfig.selectedKeys"
            :treeData="filterTreeConfig.treeFilterData"
            @select="filterTreeSelect"
          />
        </a-card>
      </SplitPane>
      <SplitPane>
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
              <ButtonGroup
                :row="row"
                :buttons="rowOperations"
                @click="rowClick"
                :ref="el => rowOperationRef('row_operation_' + row.id, el)"
              />
            </template>

            <template #authorities_default="{ row, column, $grid }">
              <AvatarGroupInfo
                :value="row.authorities"
                :disabled="!$grid?.isEditByRow(row)"
                :query="{ 'id.in': row.authorities }"
                avatar-slot-name="default"
                avatar-slot-field="name"
                avatar-tip-field="name"
                @click="
                  buttonType => rowClick({ name: column.field + 'Column' + _upperFirst(buttonType), data: row, params: column.params })
                "
              />
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
      </SplitPane>
    </SplitPanes>
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
import UserForm from './components/form-component.vue';
import UserDetail from './components/detail-component.vue';
import config from './config/list-config';
import { transformToFilterTree } from '@/components/VxeTable/src/helper';
import { ApiTree } from '@/components/Form';
import { upperFirst as _upperFirst } from 'lodash-es';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const props = defineProps(config.ListProps);

const [registerModal, { closeModal, setModalProps }] = useModal();
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawer();
const shallowRefs = {
  UserEdit: shallowRef(UserForm),
  UserDetail: shallowRef(UserDetail),
  ApiTree: shallowRef(ApiTree),
};
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const apis = {
  userService: apiService.system.userService,
  auditingUser: apiService.system.userService.retrieve,
  find: apiService.system.userService.retrieve,
  deleteById: apiService.system.userService.delete,
  deleteByIds: apiService.system.userService.deleteByIds,
  update: apiService.system.userService.update,
  import: apiService.system.userService.importExcel,
  export: apiService.system.userService.exportExcel,
  departmentList: apiService.settings.departmentService.tree,
  authoritiesList: apiService.system.authorityService.tree,
  department: apiService.settings.departmentService.tree,
  position: apiService.settings.positionService.retrieve,
  authorities: apiService.system.authorityService.tree,
};
const pageConfig = {
  title: '用户列表',
  baseRouteName: 'systemUser',
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
    attrs: {
      'data-cy': 'entitySaveButton',
    },
  },
  {
    title: '编辑',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    name: 'edit',
    type: 'link',
    attrs: {
      'data-cy': 'entityEditButton',
    },
  },
  {
    title: '删除',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    name: 'delete',
    type: 'link',
    attrs: {
      'data-cy': 'entityDeleteButton',
    },
  },
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
useSetShortcutButtons('SystemUserList', extraButtons);

const selectedRows = reactive<any>([]);
const searchFormRef = ref<any>(null);
const mapOfFilter = ref({});
const filterTreeConfig = reactive({
  treeFilterData: [] as any[],
  expandedKeys: [],
  checkedKeys: [],
  selectedKeys: [],
  autoExpandParent: true,
});
apis.departmentList().then(data => {
  filterTreeConfig.treeFilterData.push(
    transformToFilterTree(
      data.records,
      { key: 'id', title: 'name', children: 'children' },
      { filterName: 'departmentId.equals', title: '部门' },
    ),
  );
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
    if (Object.keys(mapOfFilter.value).length) {
      queryParams.value.and = mapOfFilter.value;
    } else {
      delete queryParams.value.and;
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
  { code: 'custom-column', name: '列配置', circle: false, icon: 'vxe-icon-custom-column' },
];
const gridOptions = reactive<VxeGridProps>({
  ...config.baseGridOptions(ajax, toolbarButtons, toolbarTools),
  columns,
});
useMergeGridProps(gridOptions, props.gridOptions);
function toolbarClick({ code }) {
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
          okButtonProps: {
            'data-cy': 'entityConfirmDeleteButton',
            'data-cy-heading': 'userDeleteDialogHeading',
          } as any,
        });
      }
      break;
    }
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
      popupConfig.componentProps.is = shallowRefs.UserEdit;
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

function filterTreeSelect(selectedKeys, info) {
  const filterData = info.node.dataRef;
  mapOfFilter.value = {};
  if (selectedKeys && selectedKeys.length > 0) {
    if (filterData.type === 'filterItem') {
      mapOfFilter.value[info.node.dataRef.filterName] = info.node.dataRef.filterValue;
      filterTreeConfig.selectedKeys = selectedKeys;
    }
  }
  formSearch();
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
      case 'edit':
        popupConfig.needSubmit = true;
        popupConfig.containerProps.title = '编辑用户';
        popupConfig.containerProps.okText = '更新';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.UserEdit;
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
        popupConfig.componentProps.is = shallowRefs.UserDetail;
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
      case 'delete':
        Modal.confirm({
          title: `操作提示`,
          content: `是否确认删除ID为${row.id}的记录？`,
          onOk() {
            apis.deleteById(row.id).then(() => {
              formSearch();
            });
          },
          okButtonProps: {
            'data-cy': 'entityConfirmDeleteButton',
            'data-cy-heading': 'userDeleteDialogHeading',
          } as any,
        });
        break;
      case 'authoritiesColumnView': {
        popupConfig.containerProps.title = '关联角色';
        popupConfig.containerProps.okText = '';
        popupConfig.containerProps.cancelText = '关闭';
        popupConfig.needSubmit = false;
        popupConfig.containerProps.showOkBtn = false;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.ApiTree;
        const rowParams = {
          ...params,
          containerType: 'modal',
          checkStrictly: true,
          defaultExpandAll: true,
          checkable: true,
          fieldNames: { title: 'name', key: 'id', children: 'children' },
          api: apis.authoritiesList,
          resultField: 'records',
          disabled: true,
          multiple: true,
          checkedKeys: (row.authorities || []).map(item => item.id),
        };
        Object.assign(popupConfig.componentProps, rowParams);
        if (rowParams.containerType === 'drawer') {
          popupConfig.containerProps.showFooter = true;
          popupConfig.containerProps.width = '45%';
          setDrawerProps({ open: true });
        } else {
          popupConfig.containerProps.width = '80%';
          setModalProps({ open: true });
        }
        break;
      }
      case 'authoritiesColumnEdit': {
        popupConfig.containerProps.title = '关联角色';
        popupConfig.containerProps.okText = '保存';
        popupConfig.containerProps.cancelText = '关闭';
        popupConfig.needSubmit = false;
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.ApiTree;
        const rowParams = {
          ...params,
          containerType: 'drawer',
          checkStrictly: true,
          defaultExpandAll: true,
          checkable: true,
          fieldNames: { title: 'name', key: 'id', children: 'children' },
          api: apis.authoritiesList,
          resultField: 'records',
          disabled: false,
          multiple: true,
          checkedKeys: (row.authorities || []).map(item => item.id),
        };
        popupConfig.componentEvents = {
          check: ({ checked }: any) => {
            popupConfig.componentProps.checkedKeys = checked;
            row.authorities = checked.map(id => ({ id: id }));
          },
        };
        popupConfig.containerEvents = {
          ok: async () => {
            const authorities = popupConfig.componentProps.checkedKeys.map(id => ({ id: id }));
            const data = await apis.update({ ...row, authorities }, [row.id], ['authorities']).catch(err => {
              console.log(err);
              message.error('保存失败！');
            });
            if (data) {
              xGrid.value.reloadRow(row, data, 'authorities');
              message.success('保存成功！');
              closeDrawer();
            }
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
}

const getSelectRows = () => {
  return toRaw(selectedRows);
};

defineExpose({ getSelectRows });
</script>
