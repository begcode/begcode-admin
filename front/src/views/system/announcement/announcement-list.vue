<template>
  <!-- begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->
  <div>
    <Card
      v-if="searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled"
      title="高级搜索"
      class="bc-list-search-form-card"
      :body-style="{ 'padding-top': '12px', 'padding-bottom': '8px' }"
      :head-style="{ 'min-height': '40px' }"
    >
      <template #extra>
        <Space>
          <Button type="default" @click="showSearchFormSetting" preIcon="ant-design:setting-outlined" shape="circle" size="small"></Button>
        </Space>
      </template>
      <SearchForm :config="searchFormConfig" @formSearch="formSearch" @close="handleToggleSearch" />
    </Card>
    <Card :bordered="false" class="bc-list-result-card" :bodyStyle="{ 'padding-top': '1px' }">
      <Grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents" data-cy="entityTable">
        <template #toolbar_buttons>
          <Row :gutter="16">
            <Col v-if="!searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled">
              <Space>
                <Input
                  v-model:value="searchValue"
                  ref="searchInputRef"
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
                    <Button type="link" @click="formSearch" style="height: 30px" data-cy="listSearchButton"
                      >查询<Icon icon="ant-design:filter-outlined" @click="handleToggleSearch" data-cy="listSearchMore"></Icon>
                    </Button>
                  </template>
                </Input>
                <template v-for="button of gridOptions?.toolbarConfig?.buttons">
                  <Button v-if="!button.dropdowns">{{ button.name }}</Button>
                  <Dropdown v-else-if="selectedRows.length" :key="button.name" :content="button.name">
                    <template #overlay>
                      <Menu @click="gridEvents.toolbarButtonClick(subButton)" v-for="subButton of button.dropdowns">
                        <MenuItem :key="subButton.name + 's'">
                          <Icon :icon="subButton.icon"></Icon>
                          {{ subButton.name }}
                        </MenuItem>
                      </Menu>
                    </template>
                    <Button>
                      {{ button.name }}
                      <Icon icon="ant-design:down-outlined" />
                    </Button>
                  </Dropdown>
                </template>
              </Space>
            </Col>
          </Row>
        </template>
        <template #recordAction="{ row }">
          <ButtonGroup :row="row" :buttons="rowOperations" @click="rowClick" :ref="el => rowOperationRef('row_operation_' + row.id, el)" />
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
        @cancel="closeDrawer"
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
    </Card>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, getCurrentInstance, h, onMounted, toRaw, shallowRef, onUnmounted, watch } from 'vue';
import { Alert, message, Modal, Space, Card, Divider, Row, Col, Input, Dropdown, Menu, MenuItem } from 'ant-design-vue';
import { VxeGridInstance, VxeGridListeners, VxeGridProps, Grid } from 'vxe-table';
import { debounce, upperFirst } from 'lodash-es';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { Button, ButtonGroup, Icon, BasicModal, BasicDrawer, SearchForm, useModalInner, useDrawerInner } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import { useMergeGridProps, useColumnsConfig, useSetOperationColumn, useSetShortcutButtons } from '@/components/VxeTable/src/helper';
import AnnouncementEdit from './components/form-component.vue';
import AnnouncementDetail from './components/detail-component.vue';
import config from './config/list-config';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const props = defineProps(config.ListProps);

const [registerModal, { closeModal, setModalProps }] = useModalInner(data => {
  console.log(data);
});
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(data => {
  console.log(data);
});
const shallowRefs = {
  AnnouncementEdit: shallowRef(AnnouncementEdit),
  AnnouncementDetail: shallowRef(AnnouncementDetail),
};
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const apis = {
  announcementService: apiService.system.announcementService,
  find: apiService.system.announcementService.retrieve,
  deleteById: apiService.system.announcementService.delete,
  deleteByIds: apiService.system.announcementService.deleteByIds,
  update: apiService.system.announcementService.update,
};
const pageConfig = {
  title: '系统通告列表',
  baseRouteName: 'systemAnnouncement',
};
const { columns } = useColumnsConfig(config.columns(), props.selectType, props.gridCustomConfig);
const searchFormFields = config.searchForm();
const xGrid = ref({} as VxeGridInstance);
const searchInputRef = ref(null);
const searchFormConfig = reactive(
  Object.assign(
    {
      fieldList: searchFormFields,
      toggleSearchStatus: false,
      useOr: false,
      disabled: false,
      allowSwitch: true,
      compact: true,
    },
    props.searchFormOptions,
  ),
);
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
    title: '删除',
    hide: row => xGrid.value.isEditByRow(row) && xGrid.value.props.editConfig?.mode === 'row',
    name: 'delete',
    type: 'link',
  },
  {
    title: '发布',
    name: 'release',
    containerType: 'drawer',
    type: 'link',
    click: row => {
      Modal.confirm({
        title: `操作提示`,
        content: `是否确认发布ID为${row.id}的消息？`,
        onOk() {
          apis.announcementService
            .release(row.id)
            .then(res => {
              message.success('发布成功');
            })
            .catch(err => {
              message.error('发布失败');
              console.log(err);
            });
        },
      });
    },
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
useSetShortcutButtons('SystemAnnouncementList', extraButtons);

const selectedRows = reactive<any>([]);
const searchFormRef = ref<any>(null);
const searchValue = ref('');
const popupConfig = reactive<any>({
  needSubmit: false,
  containerProps: {
    width: '80%',
    destroyOnClose: true,
    okText: '确定',
    cancelText: '取消',
    needSubmit: false,
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
    if (searchValue.value) {
      queryParams.value['jhiCommonSearchKeywords'] = searchValue.value;
    } else {
      delete queryParams.value['jhiCommonSearchKeywords'];
      Object.assign(queryParams.value, getSearchQueryData(searchFormConfig));
    }
    return await apis.find(queryParams.value);
  },
  queryAll: async () => await apis.find({ size: -1 }),
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
const pagerLeft = () => {
  return h(Alert, { type: 'warning', banner: true, message: `已选择 ${selectedRows.length} 项`, style: 'height: 30px' });
};
const gridOptions = reactive<VxeGridProps>({
  ...config.baseGridOptions(ajax, toolbarButtons, toolbarTools, pagerLeft),
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
    case 'custom-column':
      xGrid.value.openCustom();
      break;
    case 'new':
      popupConfig.containerProps.title = '新建';
      popupConfig.containerProps.okText = '保存';
      popupConfig.containerProps.cancelText = '取消';
      popupConfig.needSubmit = true;
      popupConfig.containerProps.showOkBtn = true;
      popupConfig.containerProps.showCancelBtn = true;
      popupConfig.componentProps.is = shallowRefs.AnnouncementEdit;
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
const inputSearch = debounce(formSearch, 700);
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
        popupConfig.containerProps.title = '编辑系统通告';
        popupConfig.containerProps.okText = '更新';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.needSubmit = true;
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.AnnouncementEdit;
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
          case 'route':
          default:
            if (pageConfig.baseRouteName) {
              go({ name: `${pageConfig.baseRouteName}Edit`, params: { entityId: row.id } });
            } else {
              console.log('未定义方法');
            }
        }
        break;
      case 'detail':
        if (operation?.containerType) {
          popupConfig.containerProps.title = '详情';
          popupConfig.containerProps.cancelText = '关闭';
          popupConfig.needSubmit = false;
          popupConfig.containerProps.showOkBtn = false;
          popupConfig.containerProps.showCancelBtn = true;
          popupConfig.componentProps.is = shallowRefs.AnnouncementDetail;
          popupConfig.componentProps.entityId = row.id;
          switch (operation.containerType) {
            case 'modal':
              popupConfig.componentProps.containerType = 'modal';
              setModalProps({ open: true });
              break;
            case 'drawer':
              popupConfig.componentProps.containerType = 'drawer';
              setDrawerProps({ open: true });
              break;
            case 'route':
            default:
              if (pageConfig.baseRouteName) {
                go({ name: `${pageConfig.baseRouteName}Detail`, params: { entityId: row.id } });
              } else {
                console.log('未定义方法');
              }
          }
        } else {
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
