<template>
  <!-- begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->
  <div>
    <Card v-if="searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled" title="高级搜索" class="bc-list-search-form-card">
      <template #extra>
        <Space>
          <Button type="default" @click="showSearchFormSetting" preIcon="ant-design:setting-outlined" shape="circle" size="small"></Button>
        </Space>
      </template>
      <SearchForm :config="searchFormConfig" @formSearch="formSearch" @close="handleToggleSearch" />
    </Card>
    <Card :bordered="false" class="bc-list-result-card" :bodyStyle="{ 'padding-top': '1px' }">
      <template #title>
        <Button type="text" preIcon="ant-design:unordered-list-outlined" shape="default" size="large" @click="formSearch"
          >业务类型列表</Button
        >
      </template>
      <template #extra>
        <Space>
          <Divider type="vertical" />
          <Button
            v-if="cardExtra?.includes('import')"
            type="default"
            @click="xGrid.openImport()"
            preIcon="ant-design:cloud-upload-outlined"
            shape="circle"
            size="small"
          ></Button>
          <Button
            v-if="cardExtra?.includes('export')"
            type="default"
            @click="xGrid.openExport()"
            preIcon="ant-design:download-outlined"
            shape="circle"
            size="small"
          ></Button>
          <Button
            v-if="cardExtra?.includes('print')"
            type="default"
            @click="xGrid.openPrint()"
            preIcon="ant-design:printer-outlined"
            shape="circle"
            size="small"
          ></Button>
          <!--          <Button type="default" preIcon="ant-design:setting-outlined" shape="circle" size="small"></Button>-->
        </Space>
      </template>
      <Row :gutter="16">
        <Col :span="filterTreeConfig.filterTreeSpan" v-if="filterTreeConfig.filterTreeSpan > 0">
          <Tree
            style="border: #bbcedd 1px solid; height: 100%"
            v-model="filterTreeConfig.checkedKeys"
            :expandedKeys="filterTreeConfig.expandedKeys"
            :autoExpandParent="filterTreeConfig.autoExpandParent"
            :selectedKeys="filterTreeConfig.selectedKeys"
            :treeData="filterTreeConfig.treeFilterData"
            @select="onSelect"
            @expand="onExpand"
          />
        </Col>
        <Col :span="24 - filterTreeConfig.filterTreeSpan">
          <Grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents">
            <template #toolbar_buttons>
              <Row :gutter="16">
                <Col :lg="2" :md="2" :sm="4" v-if="filterTreeConfig.treeFilterData.length > 0">
                  <span class="table-page-search-submitButtons">
                    <Button
                      type="primary"
                      :icon="filterTreeConfig.filterTreeSpan > 0 ? 'pic-center' : 'pic-right'"
                      @click="switchFilterTree"
                    />
                  </span>
                </Col>
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
                    >
                      <template #prefix>
                        <Icon icon="ant-design:search-outlined" />
                      </template>
                      <template #addonAfter>
                        <Button type="link" @click="formSearch" style="height: 30px"
                          >查询<Icon icon="ant-design:filter-outlined" @click="handleToggleSearch"></Icon
                        ></Button>
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
                          <DownOutlined />
                        </Button>
                      </Dropdown>
                    </template>
                  </Space>
                </Col>
              </Row>
            </template>
            <template #recordAction="{ row, column }">
              <template v-if="tableRowOperations.length">
                <Space :size="4">
                  <template
                    v-for="operation in tableRowOperations.filter(
                      rowOperation => !rowOperation.disabled && !(rowOperation.hide && rowOperation.hide(row)),
                    )"
                  >
                    <template v-if="operation.name === 'save'">
                      <Button
                        v-if="xGrid.isEditByRow(row) && xGrid.props.editConfig.mode === 'row'"
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        :title="getButtonTitle(operation.title || '保存', row)"
                        @click="rowClick('save', row)"
                        status="primary"
                      >
                        <Icon icon="ant-design:save-outlined" #icon v-if="operation.type !== 'link'" />
                        <span v-else>{{ getButtonTitle(operation.title || '保存', row) }}</span>
                      </Button>
                      <Button
                        v-else
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        :title="getButtonTitle(operation.title || '编辑', row)"
                        shape="circle"
                        @click="rowClick('edit', row)"
                      >
                        <Icon icon="ant-design:edit-outlined" #icon v-if="operation.type !== 'link'" />
                        <span v-else>{{ getButtonTitle(operation.title || '编辑', row) }}</span>
                      </Button>
                    </template>
                    <template v-else-if="operation.name === 'delete' && !operation.disabled">
                      <Button
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        :title="getButtonTitle(operation.title || '删除', row)"
                        @click="rowClick('delete', row)"
                        shape="circle"
                      >
                        <Icon :icon="operation.icon || 'ant-design:delete-outlined'" #icon v-if="operation.type !== 'link'" />
                        <span v-else>{{ getButtonTitle(operation.title || '删除', row) }}</span>
                      </Button>
                    </template>
                    <template v-else>
                      <Button
                        v-if="!operation.disabled"
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        :title="getButtonTitle(operation.title || '操作', row)"
                        @click="rowClick(operation.name, row)"
                        shape="circle"
                      >
                        <Icon :icon="operation.icon || 'ant-design:info-circle-outlined'" v-if="operation.type !== 'link'" #icon />
                        <span v-else>{{ getButtonTitle(operation.title || '操作', row) }}</span>
                      </Button>
                    </template>
                  </template>
                  <Dropdown v-if="tableRowMoreOperations && tableRowMoreOperations.length">
                    <template #overlay>
                      <Menu @click="rowMoreClick($event, row)">
                        <MenuItem
                          :key="operation.name"
                          v-for="operation in tableRowMoreOperations.filter(
                            operationItem => !operationItem.disabled && !(operationItem.hide && operationItem.hide(row)),
                          )"
                        >
                          <Icon :icon="operation.icon" v-if="operation.icon" />
                          <span v-if="operation.type === 'link'">{{ getButtonTitle(operation.title || '操作', row) }}</span>
                        </MenuItem>
                      </Menu>
                    </template>
                    <a class="ant-dropdown-link" @click.prevent>
                      &nbsp;
                      <DownOutlined />
                    </a>
                  </Dropdown>
                </Space>
              </template>
            </template>
          </Grid>
        </Col>
      </Row>
      <BasicModal v-bind="modalConfig" @register="registerModal" @cancel="closeModal" @ok="okModal">
        <component
          :is="modalConfig.componentName"
          @cancel="closeModal"
          @refresh="formSearch"
          v-bind="modalConfig"
          ref="modalComponentRef"
        />
      </BasicModal>
      <BasicDrawer v-bind="drawerConfig" @register="registerDrawer" @cancel="closeDrawer" @ok="okDrawer">
        <component
          :is="drawerConfig.componentName"
          @cancel="closeDrawer"
          @refresh="formSearch"
          v-bind="drawerConfig"
          ref="drawerComponentRef"
        />
      </BasicDrawer>
    </Card>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, getCurrentInstance, h, onMounted, toRaw, shallowRef } from 'vue';
import { Alert, message, Modal, Space, Card, Divider, Row, Col, Tree, Input, Dropdown, Menu, MenuItem } from 'ant-design-vue';
import { DownOutlined } from '@ant-design/icons-vue';
import { VxeGridInstance, VxeGridListeners, VxeGridProps, Grid } from 'vxe-table';
import { mergeWith, isArray, isObject, isString, debounce } from 'lodash-es';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { Button, Icon, useModalInner, BasicModal, useDrawerInner, BasicDrawer, SearchForm } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import BusinessTypeEdit from './business-type-edit.vue';
import BusinessTypeDetail from './business-type-detail.vue';

import config from './config/list-config';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const props = defineProps({
  query: {
    type: Object,
    default: () => ({}),
  },
  editIn: {
    type: String,
    default: 'page',
  },
  baseData: {
    type: Object,
    default: () => ({}),
  },
  cardExtra: {
    type: Array,
    default: ['import', 'export', 'print'],
  },
  gridOptions: {
    type: Object,
    default: () => ({}),
  },
  selectType: {
    type: String,
    default: 'checkbox', // checkbox/radio/none/seq
  },
  searchFormOptions: {
    type: Object,
    default: () => ({
      disabled: false,
    }),
  },
  gridCustomConfig: {
    type: Object,
    default: () => ({
      hideOperations: false,
      hideSlots: [],
      hideColumns: [],
    }),
  },
});

const [registerModal, { closeModal, setModalProps }] = useModalInner(data => {
  console.log(data);
});
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(data => {
  console.log(data);
});
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const { getPageSetting } = useRootSetting();
const apis = {
  businessTypeService: apiService.settings.businessTypeService,
  find: apiService.settings.businessTypeService.retrieve,
  deleteById: apiService.settings.businessTypeService.delete,
  deleteByIds: apiService.settings.businessTypeService.deleteByIds,
  update: apiService.settings.businessTypeService.update,
};
const pageConfig = {
  title: '业务类型列表',
  baseRouteName: 'systemBusinessType',
};
const columns = config.columns();
if (props.selectType !== 'checkbox') {
  const checkBoxColumn = columns.find(column => column.type === 'checkbox');
  if (checkBoxColumn) {
    if (['radio', 'seq'].includes(props.selectType)) {
      checkBoxColumn.type = props.selectType;
    } else if (props.selectType === 'none') {
      checkBoxColumn.visible = false;
    }
  }
}
const searchFormFields = config.searchForm();
if (props.gridCustomConfig?.hideColumns?.length > 0) {
  const filterColumns = columns.filter(column => !props.gridCustomConfig.hideColumns.includes(column.field));
  columns.length = 0;
  columns.push(...filterColumns);
}
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
    },
    props.searchFormOptions,
  ),
);
const batchOperations = [];
let rowOperations = [
  {
    disabled: false,
    name: 'save',
    type: getPageSetting.value.buttonType || 'link',
  },
  {
    name: 'delete',
    type: getPageSetting.value.buttonType || 'link',
  },
  {
    title: '详情',
    name: 'detail',
    containerType: 'drawer',
    type: getPageSetting.value.buttonType || 'link',
  },
];
if (props.gridCustomConfig?.rowOperations && isArray(props.gridCustomConfig.rowOperations)) {
  if (props.gridCustomConfig.rowOperations.length === 0) {
    rowOperations = [];
  } else {
    rowOperations = rowOperations.filter(item =>
      props.gridCustomConfig.rowOperations.some(rowItem => (isObject(rowItem) ? item.name === rowItem['name'] : item.name === rowItem)),
    );
  }
}
const tableRowOperations = reactive<any[]>([]);
const tableRowMoreOperations = reactive<any[]>([]);
const saveOperation = rowOperations.find(operation => operation.name === 'save');
if (rowOperations.length > 4 || (saveOperation && rowOperations.length > 3)) {
  if (saveOperation) {
    tableRowOperations.push(...rowOperations?.slice(0, 3));
    tableRowMoreOperations.push(...rowOperations.slice(3));
  } else {
    tableRowOperations.push(...rowOperations?.slice(0, 4));
    tableRowMoreOperations.push(...rowOperations.slice(4));
  }
} else {
  tableRowOperations.push(...rowOperations);
}
const selectedRows = reactive<any>([]);
const searchFormRef = ref<any>(null);
const searchValue = ref('');
const mapOfFilter = ref({});
const treeFilterData = [];
const filterTreeConfig = reactive({
  filterTreeSpan: treeFilterData && treeFilterData.length > 0 ? 6 : 0,
  treeFilterData,
  expandedKeys: [],
  checkedKeys: [],
  selectedKeys: [],
  autoExpandParent: true,
});
const modalConfig = reactive<any>({
  componentName: '',
  entityId: '',
  containerType: 'modal',
  baseData: props.baseData,
  width: '80%',
  destroyOnClose: true,
});
const drawerConfig = reactive<any>({
  componentName: '',
  containerType: 'drawer',
  entityId: '',
  baseData: props.baseData,
  width: '30%',
  destroyOnClose: true,
});
const queryParams = ref<any>({ ...props.query });
const gridOptions = reactive<VxeGridProps>({
  ...config.baseGridOptions(),
  customConfig: {
    storage: true,
    checkMethod({ column }) {
      return !['nickname', 'role'].includes(column.field);
    },
  },
  proxyConfig: {
    enabled: true,
    autoLoad: true,
    seq: true,
    sort: true,
    filter: true,
    props: {
      result: 'records',
      total: 'total',
    },
    ajax: {
      query: async ({ filters, page, sort, sorts }) => {
        console.log('filters', filters);
        queryParams.value.page = page?.currentPage > 0 ? page.currentPage - 1 : 0;
        queryParams.value.size = page?.pageSize;
        const allSort = sorts || [];
        sort && allSort.push(sort);
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
    },
  },
  toolbarConfig: {
    custom: true,
    import: false,
    print: false,
    export: false,
    slots: {
      buttons: 'toolbar_buttons',
    },
    // 表格左上角自定义按钮
    buttons: [
      {
        name: '批量操作',
        circle: false,
        icon: 'vxe-icon-add',
        status: 'primary',
        dropdowns: [{ code: 'batchDelete', name: '删除', circle: false, icon: 'ant-design:delete-filled', status: 'primary' }],
      },
    ],
    // 表格右上角自定义按钮
    tools: [{ code: 'new', name: '新增', circle: false, icon: 'vxe-icon-add' }],
  },
  columns,
});
gridOptions!.pagerConfig!.slots = {
  left: () => {
    return h(Alert, { type: 'warning', banner: true, message: `已选择 ${selectedRows.length} 项`, style: 'height: 30px' });
  },
};
mergeWith(gridOptions, props.gridOptions, (objValue: any, srcValue: any, key: any) => {
  if (isArray(objValue) && ['buttons', 'tools'].includes(key)) {
    if (!srcValue) {
      return objValue;
    } else if (isArray(srcValue) && srcValue.length === 0) {
      return srcValue;
    } else if (isArray(srcValue) && srcValue.length > 0) {
      const newObjValue: any[] = [];
      srcValue.forEach((srcItem: any) => {
        if (isObject(srcItem)) {
          const objItem = objValue.find(item => item.code === srcItem['code']) || {};
          newObjValue.push(Object.assign(objItem, srcItem));
        } else if (isString(srcItem)) {
          const objItem = objValue.find(item => item.code === srcItem);
          objItem && newObjValue.push(objItem);
        }
      });
      return newObjValue;
    }
  }
});
const gridEvents = reactive<VxeGridListeners>({
  checkboxAll: () => {
    const $grid = xGrid.value;
    selectedRows.length = 0;
    selectedRows.push(...$grid.getCheckboxRecords());
  },
  checkboxChange: () => {
    const $grid = xGrid.value;
    selectedRows.length = 0;
    selectedRows.push(...$grid.getCheckboxRecords());
  },
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
  toolbarButtonClick({ code }) {
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
      case 'mySave': {
        break;
      }
      case 'myExport': {
        break;
      }
    }
  },
  // 表格右上角自定义按钮事件
  toolbarToolClick({ code }) {
    switch (code) {
      case 'new':
        if (props.editIn === 'modal') {
          modalConfig.componentName = shallowRef(BusinessTypeEdit);
          modalConfig.entityId = '';
          setModalProps({ open: true, title: '新建' });
        } else if (props.editIn === 'drawer') {
          drawerConfig.componentName = shallowRef(BusinessTypeEdit);
          drawerConfig.entityId = '';
          setDrawerProps({ open: true, title: '新建' });
        } else {
          if (pageConfig.baseRouteName) {
            go({ name: `${pageConfig.baseRouteName}New` });
          } else {
            console.log('未定义方法');
          }
        }
        break;
    }
  },
  editClosed({ row, column }) {
    const field = column.property;
    const cellValue = row[field];
    // 判断单元格值是否被修改
    if (xGrid.value.isUpdateByRow(row, field)) {
      const entity = { id: row.id };
      entity[field] = cellValue;
      apis
        .update(entity, [row.id], [field])
        .then(() => {
          message.success({
            content: `信息更新成功。 ${field}=${cellValue}`,
            duration: 1,
          });
          xGrid.value.reloadRow(row, null, field);
        })
        .catch(error => {
          console.log('error', error);
          message.error({
            content: `信息保存可能存在问题！ ${field}=${cellValue}`,
            onClose: () => {},
          });
        });
    }
  },
});
const okModal = () => {
  if (modalConfig.containerType === 'modal') {
    if (modalComponentRef.value) {
      modalComponentRef.value.saveOrUpdate();
      formSearch();
    }
  }
};
const okDrawer = () => {
  if (drawerConfig.containerType === 'drawer') {
    if (drawerComponentRef.value) {
      drawerComponentRef.value.saveOrUpdate();
      formSearch();
    }
  }
};
const formSearch = () => {
  xGrid.value.commitProxy('reload');
};
const inputSearch = debounce(formSearch, 700);
onMounted(() => {
  const $grid: HTMLElement = xGrid.value.$el as HTMLElement;
  const myElement = $grid.querySelector('.vxe-toolbar .vxe-custom--wrapper .vxe-button.type--button');
  if (myElement?.className) {
    myElement.className = myElement.className.replace('is--circle', '');
    myElement.setAttribute('style', 'border-radius: 4px !important;');
  }

  const parent = myElement?.parentElement;
  if (parent) {
    parent.className = parent.className + ' begcode';
  }
  const text = document.createElement('span');
  text.className = 'vxe-button--content';
  text.innerText = '列配置';
  myElement?.appendChild(text);
});

const handleToggleSearch = () => {
  searchFormConfig.toggleSearchStatus = !searchFormConfig.toggleSearchStatus;
};

const onCheck = checkedKeys => {
  filterTreeConfig.checkedKeys = checkedKeys;
};

const showSearchFormSetting = () => {
  if (searchFormRef.value) {
    searchFormRef.value.showSettingModal();
  }
};

const onSelect = (selectedKeys, info) => {
  const filterData = info.node.dataRef;
  if (filterData.type === 'filterGroup') {
    mapOfFilter.value[info.node.dataRef.key].value = [];
  } else if (filterData.type === 'filterItem') {
    mapOfFilter.value[info.node.dataRef.filterName].value = [info.node.dataRef.filterValue];
  }
  formSearch();
  filterTreeConfig.selectedKeys = selectedKeys;
};

const switchFilterTree = () => {
  filterTreeConfig.filterTreeSpan = filterTreeConfig.filterTreeSpan > 0 ? 0 : 6;
};

const rowMoreClick = (e, row) => {
  const { key } = e;
  const operation = tableRowMoreOperations.find(operation => operation.name === key);
  rowClickHandler(key, operation, row);
};

const rowClick = (name, row) => {
  const operation = tableRowOperations.find(operation => operation.name === name);
  rowClickHandler(name, operation, row);
};

const rowClickHandler = (name, operation, row) => {
  switch (name) {
    case 'save':
      break;
    case 'edit':
      if (operation) {
        if (operation.click) {
          operation.click(row);
        } else {
          const containerType = props.editIn || operation.containerType;
          switch (containerType) {
            case 'modal':
              modalConfig.componentName = shallowRef(BusinessTypeEdit);
              modalConfig.entityId = row.id;
              setModalProps({ open: true });
              break;
            case 'drawer':
              drawerConfig.componentName = shallowRef(BusinessTypeEdit);
              drawerConfig.entityId = row.id;
              drawerConfig.title = '编辑';
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
        }
      } else {
        switch (props.editIn) {
          case 'modal':
            modalConfig.componentName = shallowRef(BusinessTypeEdit);
            modalConfig.entityId = row.id;
            setModalProps({ open: true });
            break;
          case 'drawer':
            drawerConfig.componentName = shallowRef(BusinessTypeEdit);
            drawerConfig.entityId = row.id;
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
      }
      break;
    case 'detail':
      if (operation) {
        if (operation.click) {
          operation.click(row);
        } else {
          switch (operation.containerType) {
            case 'modal':
              modalConfig.componentName = shallowRef(BusinessTypeDetail);
              modalConfig.entityId = row.id;
              modalConfig.title = '详情';
              setModalProps({ open: true });
              break;
            case 'drawer':
              drawerConfig.componentName = shallowRef(BusinessTypeDetail);
              drawerConfig.entityId = row.id;
              drawerConfig.title = '详情';
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
          if (operation.click) {
            operation.click(row);
          } else {
            apis.deleteById(row.id).then(() => {
              formSearch();
            });
          }
        },
      });
      break;
    default:
      if (operation) {
        if (operation.click) {
          operation.click(row);
        } else {
          console.log('error', `click方法未定义`);
        }
      } else {
        console.log('error', `${name}未定义`);
      }
  }
};
const getButtonTitle = (title: any, row: any) => {
  if (typeof title === 'function') {
    return title(row);
  }
  return title;
};
const getSelectRows = () => {
  return toRaw(selectedRows);
};
defineExpose({ getSelectRows });
</script>
