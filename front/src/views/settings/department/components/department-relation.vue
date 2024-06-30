<template>
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
      <template #title v-if="cardSlots?.includes('title')">
        <Button type="text" preIcon="ant-design:unordered-list-outlined" shape="default" size="large" @click="formSearch">部门列表</Button>
      </template>
      <template #extra v-if="cardSlots?.includes('extra')">
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
        <Col :span="24">
          <Grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents">
            <template #toolbar_buttons>
              <Row :gutter="16">
                <Col v-if="!searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled">
                  <Space>
                    <Input
                      v-model:value="searchValue"
                      placeholder="请输入关键字"
                      allow-clear
                      @change="inputSearch"
                      @pressEnter="formSearch"
                      style="width: 280px"
                      ref="searchInputRef"
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
                          <Icon icon="ant-design:down-outlined" />
                        </Button>
                      </Dropdown>
                    </template>
                  </Space>
                </Col>
              </Row>
            </template>
            <template #recordAction="{ row }">
              <ButtonGroup :row="row" :buttons="rowOperations" @click="rowClick" />
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
import { reactive, ref, getCurrentInstance, h, onMounted, shallowRef, toRaw } from 'vue';
import { Alert, message, Modal, Space, Card, Divider, Row, Col, Input, Dropdown, Menu, MenuItem } from 'ant-design-vue';
import { Grid } from 'vxe-table';
import type { VxeGridPropTypes, VxeGridInstance, VxeGridListeners, VxeGridProps } from 'vxe-table/types/grid';
import { mergeWith, isArray, isObject, isString, merge, debounce, pickBy, isEmpty } from 'lodash-es';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { Button, ButtonGroup, BasicModal, BasicDrawer, Icon, SearchForm, useModalInner, useDrawerInner } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import DepartmentEdit from '../department-edit.vue';
import DepartmentDetail from '../department-detail.vue';
import DepartmentList from '../department-list.vue';

import dayjs from 'dayjs';

const relationships = {
  'Authority.department': 'authorities',
};

const config = {
  searchForm: (): any[] => {
    return [
      {
        title: 'ID',
        field: 'id',
        componentType: 'Text',
        value: '',
        type: 'Long',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '名称',
        field: 'name',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '代码',
        field: 'code',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '地址',
        field: 'address',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '联系电话',
        field: 'phoneNum',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: 'logo地址',
        field: 'logo',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '联系人',
        field: 'contact',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '创建用户 Id',
        field: 'createUserId',
        componentType: 'Text',
        value: '',
        type: 'Long',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '创建时间',
        field: 'createTime',
        componentType: 'DateTime',
        operator: '',
        span: 8,
        type: 'Instant',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '下级部门',
        field: 'children',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.children, style: 'width: 100%', valueField: 'id', labelField: 'name' },
      },
      {
        title: '角色列表',
        field: 'authorities',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.authorities, style: 'width: 100%', valueField: 'id', labelField: 'name' },
      },
      {
        title: '上级',
        field: 'parent',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.parent, style: 'width: 100%', valueField: 'id', labelField: 'name' },
      },
      {
        title: '用户列表',
        field: 'users',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.users, style: 'width: 100%', valueField: 'id', labelField: 'firstName' },
      },
    ];
  },
  columns: (): VxeGridPropTypes.Columns => {
    return [
      {
        fixed: 'left',
        type: 'checkbox',
        width: 60,
      },
      {
        title: 'ID',
        field: 'id',
        minWidth: 80,
        visible: false,
        treeNode: false,
        params: { type: 'LONG' },
      },
      {
        title: '名称',
        field: 'name',
        minWidth: 160,
        visible: true,
        treeNode: true,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '代码',
        field: 'code',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '地址',
        field: 'address',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '联系电话',
        field: 'phoneNum',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: 'logo地址',
        field: 'logo',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        cellRender: { name: 'AImage' },
      },
      {
        title: '联系人',
        field: 'contact',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '创建用户 Id',
        field: 'createUserId',
        minWidth: 80,
        visible: true,
        treeNode: false,
        params: { type: 'LONG' },
      },
      {
        title: '创建时间',
        field: 'createTime',
        minWidth: 100,
        visible: true,
        treeNode: false,
        params: { type: 'Instant' },
        formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
      },
      {
        title: '操作',
        field: 'recordOperation',
        fixed: 'right',
        headerAlign: 'center',
        align: 'right',
        showOverflow: false,
        width: 120,
        slots: { default: 'recordAction' },
      },
    ];
  },
  baseGridOptions: (): VxeGridProps => {
    return {
      rowConfig: {
        keyField: 'id',
        isHover: true,
      },
      border: true,
      showHeaderOverflow: true,
      showOverflow: true,
      keepSource: true,
      id: 'full_edit_1',
      height: 600,
      printConfig: {
        columns: [
          // { field: 'name' },
        ],
      },
      filterConfig: {
        remote: true,
      },
      columnConfig: {
        resizable: true,
      },
      sortConfig: {
        trigger: 'cell',
        remote: true,
        orders: ['asc', 'desc', null],
        multiple: true,
        defaultSort: {
          field: 'id',
          order: 'desc',
        },
        showIcon: true,
      },
      pagerConfig: {
        layouts: ['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total'],
        pageSize: 15,
        pageSizes: [5, 10, 15, 20, 30, 50],
        total: 0,
        pagerCount: 5,
        currentPage: 1,
        autoHidden: true,
      },
      importConfig: {},
      exportConfig: {},
      checkboxConfig: {
        // labelField: 'id',
        reserve: true,
        highlight: true,
      },
      editRules: {},
      editConfig: {
        enabled: false,
        trigger: 'click',
        mode: 'cell',
        showStatus: false,
      },
      treeConfig: {
        childrenField: 'children',
        indent: 20,
        showLine: false,
        expandAll: false,
        accordion: false,
        trigger: 'default',
      },
    };
  },
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
const props = defineProps({
  query: {
    type: Object,
    default: () => ({}),
  },
  editIn: {
    type: String,
    default: '',
  },
  field: {
    type: String,
    default: '',
  },
  source: {
    type: String,
    default: '',
  },
  baseData: {
    type: Object,
    default: () => ({}),
  },
  cardExtra: {
    type: Array,
    default: ['import', 'export', 'print'],
  },
  cardSlots: {
    type: Array,
    default: ['title', 'extra'],
  },
  gridOptions: {
    type: Object,
    default: () => ({}),
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
  updateType: {
    type: String,
    default: 'remoteApi', // 'remoteApi' | 'emitSelected'
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
const relationshipApis: any = {
  children: apiService.settings.departmentService.tree,
  authorities: apiService.system.authorityService.tree,
  parent: apiService.settings.departmentService.tree,
  users: apiService.system.userService.retrieve,
};
const apis = {
  departmentService: apiService.settings.departmentService,
  find: apiService.settings.departmentService.tree,
  updateRelations: apiService.settings.departmentService.updateRelations,
};
const pageConfig = {
  title: '部门列表',
  baseRouteName: 'systemDepartment',
};
const columns = config.columns();
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
let rowOperations = [
  {
    title: '取消关联',
    name: 'cancelRelate',
    type: 'link',
  },
  {
    title: '详情',
    name: 'detail',
    containerType: 'drawer',
    type: 'link',
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
  width: '70%',
  destroyOnClose: true,
});
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
        if (props.updateType !== 'remoteApi') {
          const queryData = pickBy(props.query, e => !!e && !isEmpty(e));
          if (!queryData || Object.keys(queryData).length === 0) {
            return new Promise<any>(resolve => {
              resolve({ records: [], size: page?.pageSize || 15, page: page.currentPage, total: 0 });
            });
          }
        }
        const queryParams: any = { ...props.query };
        queryParams.page = page?.currentPage > 0 ? page.currentPage - 1 : 0;
        queryParams.size = page?.pageSize;
        const allSort = sorts || [];
        sort && allSort.push(sort);
        queryParams.sort = transVxeSorts(allSort);
        if (searchValue.value) {
          queryParams['jhiCommonSearchKeywords'] = searchValue.value;
        } else {
          delete queryParams['jhiCommonSearchKeywords'];
          Object.assign(queryParams, getSearchQueryData(searchFormConfig));
        }
        return await apis.find(queryParams);
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
        dropdowns: [
          { code: 'batchDelete', name: '删除', circle: false, icon: 'ant-design:delete-filled', status: 'primary' },
          { code: 'batchCancelRelate', name: '取消关联', circle: false, icon: 'ant-design:split-cells-outlined', status: 'primary' },
        ],
      },
    ],
    // 表格右上角自定义按钮
    tools: [{ code: 'add', name: '新增', circle: false, icon: 'vxe-icon-add' }],
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

const toolbarClick = ({ code }) => {
  const $grid = xGrid.value;
  switch (code) {
    case 'batchCancelRelate': {
      const records = $grid.getCheckboxRecords(true);
      if (records?.length > 0) {
        if (props.updateType === 'remoteApi') {
          const ids = records.map(record => record.id);
          Modal.confirm({
            title: `操作提示`,
            content: `是否取消ID为【${ids.join(',')}】的${records.length}项数据的关联？`,
            onOk() {
              const relatedIds = ids;
              const otherEntityIds: any[] = [];
              if (props.query) {
                Object.values(props.query).forEach((value: any) => {
                  if (value !== null && value !== undefined) {
                    otherEntityIds.push(`${value}`);
                  }
                });
              }
              const relationshipName = relationships[props.source + '.' + props.field];
              apis.updateRelations(otherEntityIds, relationshipName, relatedIds, 'delete').then(result => {
                if (result) {
                  message.success({ content: `取消关联成功`, duration: 1 });
                  formSearch();
                } else {
                  message.error({ content: `取消关联失败`, duration: 1 });
                }
              });
            },
          });
        } else {
          if (xGrid.value && records?.length > 0) {
            xGrid.value.remove(records).then(() => {
              message.success({ content: `取消关联成功`, duration: 1 });
            });
          }
        }
      }
      break;
    }
    case 'add': {
      const result: any = {};
      const tools: string[] = [];
      const buttons: string[] = [];
      const rowOperations = ['detail'];
      result.gridOptions = merge({}, { toolbarConfig: { import: false, print: false, export: false, custom: false, tools, buttons } });
      result.cardExtra = [];
      result.searchFormOptions = merge({});
      result.gridCustomConfig = merge({}, { rowOperations });
      result.componentName = shallowRef(DepartmentList);
      result.entityId = '';
      if (props.editIn === 'drawer') {
        Object.assign(drawerConfig, result);
        setDrawerProps({ open: true });
      } else {
        Object.assign(modalConfig, result);
        setModalProps({ open: true });
      }
      break;
    }
    default:
      console.log('事件未定义', code);
  }
};

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
  toolbarButtonClick: toolbarClick,
  // 表格右上角自定义按钮事件
  toolbarToolClick: toolbarClick,
});
const okModal = async () => {
  if (modalComponentRef.value) {
    const selectRows = modalComponentRef.value.getSelectRows();
    if (props.updateType === 'remoteApi') {
      // 对selectRows进行处理。
      const relatedIds = selectRows.map(row => row.id);
      const otherEntityIds: any[] = [];
      if (props.query) {
        Object.values(props.query).forEach((value: any) => {
          if (value && value.length > 0) {
            otherEntityIds.push(`${value}`);
          }
        });
      }
      const relationshipName = relationships[props.source + '.' + props.field];
      const result = await apis.updateRelations(otherEntityIds, relationshipName, relatedIds, 'add');
      if (result) {
        message.success({
          content: `关联成功`,
          duration: 1,
        });
        formSearch();
        closeModal();
      } else {
        message.error({
          content: `关联失败`,
          duration: 1,
        });
      }
    } else {
      if (xGrid.value && selectRows?.length > 0) {
        xGrid.value.insert(selectRows).then(() => {
          message.success({
            content: `关联成功`,
            duration: 1,
          });
        });
      }
      closeModal();
    }
  }
};
const okDrawer = async () => {
  if (drawerComponentRef.value) {
    const selectRows = drawerComponentRef.value.getSelectRows();
    if (props.updateType === 'remoteApi') {
      // 对selectRows进行处理。
      const relatedIds = selectRows.map(row => row.id);
      const otherEntityIds: any[] = [];
      if (props.query) {
        Object.values(props.query).forEach((value: any) => {
          if (value && value.length > 0) {
            otherEntityIds.push(`${value}`);
          }
        });
      }
      const relationshipName = relationships[props.source + '.' + props.field];
      const result = await apis.updateRelations(otherEntityIds, relationshipName, relatedIds, 'add');
      if (result) {
        message.success({
          content: `关联成功`,
          duration: 1,
        });
        closeDrawer();
        formSearch();
      } else {
        message.error({
          content: `关联失败`,
          duration: 1,
        });
      }
    } else {
      if (xGrid.value && selectRows?.length > 0) {
        xGrid.value.insert(selectRows).then(() => {
          message.success({
            content: `关联成功`,
            duration: 1,
          });
        });
      }
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

const rowClick = ({ name, data }) => {
  const row = data;
  const operation = tableRowOperations.find(operation => operation.name === name);
  switch (name) {
    case 'detail':
      if (operation) {
        if (operation.click) {
          operation.click(row);
        } else {
          switch (operation.containerType) {
            case 'drawer':
              drawerConfig.componentName = shallowRef(DepartmentDetail);
              drawerConfig.entityId = row.id;
              drawerConfig.title = '详情';
              setDrawerProps({ open: true });
              break;
            case 'route':
              if (pageConfig.baseRouteName) {
                go({ name: `${pageConfig.baseRouteName}Detail`, params: { entityId: row.id } });
              } else {
                console.log('未定义方法');
              }
              break;
            case 'modal':
            default:
              modalConfig.componentName = shallowRef(DepartmentDetail);
              modalConfig.entityId = row.id;
              modalConfig.title = '详情';
              setModalProps({ open: true });
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
    case 'cancelRelate':
      Modal.confirm({
        title: `操作提示`,
        content: `是否取消ID为${row.id}的关联？`,
        onOk() {
          if (operation.click) {
            operation.click(row);
          } else {
            if (props.updateType === 'remoteApi') {
              const relatedIds = [row.id];
              const otherEntityIds: any[] = [];
              if (props.query) {
                Object.values(props.query).forEach((value: any) => {
                  if (value !== null && value !== undefined) {
                    otherEntityIds.push(`${value}`);
                  }
                });
              }
              const relationshipName = relationships[props.source + '.' + props.field];
              apis.updateRelations(otherEntityIds, relationshipName, relatedIds, 'delete').then(result => {
                if (result) {
                  message.success({
                    content: `取消关联成功`,
                    duration: 1,
                  });
                  formSearch();
                } else {
                  message.error({
                    content: `取消关联失败！`,
                    duration: 1,
                  });
                }
              });
            } else {
              if (xGrid.value) {
                xGrid.value.remove([row]).then(() => {
                  message.success({
                    content: `取消成功`,
                    duration: 1,
                  });
                });
              }
            }
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

const getSelectRows = () => {
  return toRaw(selectedRows);
};
const getData = () => {
  if (xGrid.value) {
    const data = xGrid.value.getTableData();
    return data.fullData || [];
  } else {
    return [];
  }
};
defineExpose({
  getSelectRows,
  getData,
});
</script>
