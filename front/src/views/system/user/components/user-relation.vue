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
      <template #title>
        <Button type="text" preIcon="ant-design:unordered-list-outlined" shape="default" size="large" @click="formSearch">用户列表</Button>
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
                        :title="operation.title || '保存'"
                        @click="rowClick('save', row)"
                        status="primary"
                      >
                        <Icon icon="ant-design:save-outlined" #icon v-if="operation.type !== 'link'" />
                        <span v-else>{{ operation.title || '保存' }}</span>
                      </Button>
                      <Button
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        v-else
                        :title="operation.title || '编辑'"
                        shape="circle"
                        @click="rowClick('edit', row)"
                      >
                        <Icon icon="ant-design:edit-outlined" #icon v-if="operation.type !== 'link'" />
                        <span v-else>{{ operation.title || '编辑' }}</span>
                      </Button>
                    </template>
                    <template v-else-if="operation.name === 'delete' && !operation.disabled">
                      <Button
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        :title="operation.title || '删除'"
                        @click="rowClick('delete', row)"
                        shape="circle"
                      >
                        <Icon :icon="operation.icon || 'ant-design:delete-outlined'" #icon v-if="operation.type !== 'link'" />
                        <span v-else>{{ operation.title || '删除' }}</span>
                      </Button>
                    </template>
                    <template v-else>
                      <Button
                        v-if="!operation.disabled"
                        :type="operation.type || 'link'"
                        :key="operation.name"
                        :title="operation.title || '操作'"
                        @click="rowClick(operation.name, row)"
                        shape="circle"
                      >
                        <Icon :icon="operation.icon || 'ant-design:info-circle-outlined'" v-if="operation.type !== 'link'" #icon />
                        <span v-else>{{ operation.title || '操作' }}</span>
                      </Button>
                    </template>
                  </template>
                  <Dropdown v-if="tableRowMoreOperations && tableRowMoreOperations.length">
                    <template #overlay>
                      <Menu @click="rowMoreClick($event, row)">
                        <MenuItem
                          :key="operation.name"
                          v-for="operation in tableRowMoreOperations.filter(operation => !operation.disabled)"
                        >
                          <Icon :icon="operation.icon" v-if="operation.icon" />
                          <span v-if="operation.type === 'link'">{{ operation.title }}</span>
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
import { reactive, ref, getCurrentInstance, h, onMounted, shallowRef, toRaw } from 'vue';
import { Alert, message, Modal, Space, Card, Divider, Row, Col, Tree, Input, Dropdown, Menu, MenuItem } from 'ant-design-vue';
import { DownOutlined } from '@ant-design/icons-vue';
import { Grid } from 'vxe-table';
import type { VxeGridPropTypes, VxeGridInstance, VxeGridListeners, VxeGridProps } from 'vxe-table/types/grid';
import { mergeWith, isArray, isObject, isString, merge, debounce, pickBy, isEmpty } from 'lodash-es';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { Button, Icon, useModalInner, BasicModal, useDrawerInner, BasicDrawer, SearchForm } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import UserEdit from '../user-edit.vue';
import UserDetail from '../user-detail.vue';
import UserList from '../user-list.vue';

import dayjs from 'dayjs';

const relationships = {
  'Authority.users': 'authorities',
};

const config = {
  searchForm: (): any[] => {
    return [
      {
        title: '用户ID',
        field: 'id',
        componentType: 'Text',
        value: '',
        type: 'Long',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '账户名',
        field: 'login',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '名字',
        field: 'firstName',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '姓氏',
        field: 'lastName',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '电子邮箱',
        field: 'email',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '手机号码',
        field: 'mobile',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '出生日期',
        field: 'birthday',
        componentType: 'DateTime',
        operator: '',
        span: 8,
        type: 'ZonedDateTime',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '激活状态',
        field: 'activated',
        componentType: 'RadioGroup',
        value: '',
        operator: '',
        span: 8,
        type: 'Boolean',
        componentProps: {
          optionType: 'button',
          buttonStyle: 'solid',
          options: [
            { label: '是', value: true },
            { label: '否', value: false },
          ],
        },
      },
      {
        title: '语言Key',
        field: 'langKey',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '头像地址',
        field: 'imageUrl',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '创建者Id',
        field: 'createdBy',
        componentType: 'Text',
        value: '',
        type: 'Long',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '创建时间',
        field: 'createdDate',
        componentType: 'DateTime',
        operator: '',
        span: 8,
        type: 'Instant',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '修改者Id',
        field: 'lastModifiedBy',
        componentType: 'Text',
        value: '',
        type: 'Long',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '修改时间',
        field: 'lastModifiedDate',
        componentType: 'DateTime',
        operator: '',
        span: 8,
        type: 'Instant',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '部门',
        field: 'department',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.department, style: 'width: 100%', valueField: 'id', labelField: 'name' },
      },
      {
        title: '岗位',
        field: 'position',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.position, style: 'width: 100%', valueField: 'id', labelField: 'name' },
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
        title: '用户ID',
        field: 'id',
        minWidth: 80,
        visible: true,
        treeNode: false,
        params: { type: 'LONG' },
        editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
      },
      {
        title: '账户名',
        field: 'login',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '名字',
        field: 'firstName',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '姓氏',
        field: 'lastName',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '电子邮箱',
        field: 'email',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '手机号码',
        field: 'mobile',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '出生日期',
        field: 'birthday',
        minWidth: 140,
        visible: true,
        treeNode: false,
        params: { type: 'ZONED_DATE_TIME' },
        formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
      },
      {
        title: '激活状态',
        field: 'activated',
        minWidth: 70,
        visible: true,
        treeNode: false,
        params: { type: 'BOOLEAN' },
        cellRender: { name: 'ASwitch', props: { disabled: false } },
      },
      {
        title: '语言Key',
        field: 'langKey',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '头像地址',
        field: 'imageUrl',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        cellRender: { name: 'AAvatar' },
      },
      {
        title: '创建者Id',
        field: 'createdBy',
        minWidth: 80,
        visible: true,
        treeNode: false,
        params: { type: 'LONG' },
        editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
      },
      {
        title: '创建时间',
        field: 'createdDate',
        minWidth: 100,
        visible: true,
        treeNode: false,
        params: { type: 'Instant' },
        formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
      },
      {
        title: '修改者Id',
        field: 'lastModifiedBy',
        minWidth: 80,
        visible: true,
        treeNode: false,
        params: { type: 'LONG' },
        editRender: { name: 'AInputNumber', enabled: false, props: { controls: false } },
      },
      {
        title: '修改时间',
        field: 'lastModifiedDate',
        minWidth: 100,
        visible: true,
        treeNode: false,
        params: { type: 'Instant' },
        formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
      },
      {
        title: '操作',
        field: 'operation',
        fixed: 'right',
        headerAlign: 'center',
        align: 'right',
        showOverflow: false,
        width: 170,
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
const { getPageSetting } = useRootSetting();
const relationshipApis: any = {
  department: apiService.settings.departmentService.tree,
  position: apiService.settings.positionService.retrieve,
  authorities: apiService.system.authorityService.tree,
};
const apis = {
  userService: apiService.system.userService,
  find: apiService.system.userService.retrieve,
  deleteById: apiService.system.userService.delete,
  deleteByIds: apiService.system.userService.deleteByIds,
  update: apiService.system.userService.update,
  updateRelations: apiService.system.userService.updateRelations,
};
const pageConfig = {
  title: '用户列表',
  baseRouteName: 'systemUser',
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
    name: 'cancelRelate',
    title: '取消关联',
    type: 'link',
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
const loading = ref(false);
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
                    message.success({
                      content: `取消关联成功`,
                      duration: 1,
                    });
                    formSearch();
                  } else {
                    message.error({
                      content: `取消关联失败`,
                      duration: 1,
                    });
                  }
                });
              },
            });
          } else {
            if (xGrid.value && records?.length > 0) {
              xGrid.value.remove(records).then(() => {
                message.success({
                  content: `取消关联成功`,
                  duration: 1,
                });
              });
            }
          }
        }
        break;
      }
      default:
        console.log('事件未定义', code);
    }
  },
  // 表格右上角自定义按钮事件
  toolbarToolClick({ code }) {
    switch (code) {
      case 'new': {
        const newConfig: any = {};
        newConfig.componentName = shallowRef(UserEdit);
        newConfig.entityId = '';
        if (props.editIn === 'drawer') {
          Object.assign(drawerConfig, newConfig);
          setDrawerProps({ open: true });
        } else {
          Object.assign(modalConfig, newConfig);
          setModalProps({ open: true });
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
        result.componentName = shallowRef(UserList);
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
onMounted(() => {
  // 临时方案
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
            case 'drawer':
              drawerConfig.componentName = shallowRef(UserEdit);
              drawerConfig.entityId = row.id;
              drawerConfig.title = '编辑';
              setDrawerProps({ open: true });
              break;
            case 'route':
              if (pageConfig.baseRouteName) {
                go({ name: `${pageConfig.baseRouteName}Edit`, params: { entityId: row.id } });
              } else {
                console.log('未定义方法');
              }
              break;
            case 'modal':
            default:
              modalConfig.componentName = shallowRef(UserEdit);
              modalConfig.entityId = row.id;
              setModalProps({ open: true });
          }
        }
      } else {
        switch (props.editIn) {
          case 'drawer':
            drawerConfig.componentName = shallowRef(UserEdit);
            drawerConfig.entityId = row.id;
            setDrawerProps({ open: true });
            break;
          case 'route':
            if (pageConfig.baseRouteName) {
              go({ name: `${pageConfig.baseRouteName}Edit`, params: { entityId: row.id } });
            } else {
              console.log('未定义方法');
            }
            break;
          case 'modal':
          default:
            modalConfig.componentName = shallowRef(UserEdit);
            modalConfig.entityId = row.id;
            setModalProps({ open: true });
        }
      }
      break;
    case 'detail':
      if (operation) {
        if (operation.click) {
          operation.click(row);
        } else {
          switch (operation.containerType) {
            case 'drawer':
              drawerConfig.componentName = shallowRef(UserDetail);
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
              modalConfig.componentName = shallowRef(UserDetail);
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
