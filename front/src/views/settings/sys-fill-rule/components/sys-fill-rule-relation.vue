<template>
  <div data-cy="SysFillRuleRelationHeading">
    <a-card
      v-if="searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled"
      title="高级搜索"
      class="bc-list-search-form-card"
      :body-style="{ 'padding-top': '12px', 'padding-bottom': '8px' }"
      :head-style="{ 'min-height': '40px' }"
    >
      <template #extra>
        <a-space>
          <basic-button
            type="default"
            @click="showSearchFormSetting"
            pre-icon="ant-design:setting-outlined"
            shape="circle"
            size="small"
          ></basic-button>
        </a-space>
      </template>
      <search-form :config="searchFormConfig" @formSearch="formSearch" @close="handleToggleSearch" ref="searchFormRef" />
    </a-card>
    <a-card :bordered="false" class="bc-list-result-card" :bodyStyle="{ 'padding-top': '1px' }">
      <template #title v-if="cardSlots?.includes('title')">
        <basic-button type="text" pre-icon="ant-design:unordered-list-outlined" shape="default" size="large" @click="formSearch"
          >填充规则列表</basic-button
        >
      </template>
      <template #extra v-if="cardSlots?.includes('extra')">
        <a-space>
          <a-divider type="vertical" />
          <basic-button
            v-if="cardExtra?.includes('import')"
            type="default"
            @click="xGrid.openImport()"
            pre-icon="ant-design:cloud-upload-outlined"
            shape="circle"
            size="small"
          ></basic-button>
          <basic-button
            v-if="cardExtra?.includes('export')"
            type="default"
            @click="xGrid.openExport()"
            pre-icon="ant-design:download-outlined"
            shape="circle"
            size="small"
          ></basic-button>
          <basic-button
            v-if="cardExtra?.includes('print')"
            type="default"
            @click="xGrid.openPrint()"
            pre-icon="ant-design:printer-outlined"
            shape="circle"
            size="small"
          ></basic-button>
          <!--          <basic-button type="default" pre-icon="ant-design:setting-outlined" shape="circle" size="small"></basic-button>-->
        </a-space>
      </template>
      <vxe-grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents">
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
                    <basic-button type="link" @click="formSearch" style="height: 30px" data-cy="listSearchButton"
                      >查询<Icon icon="ant-design:filter-outlined" @click="handleToggleSearch" data-cy="listSearchMore"></Icon>
                    </basic-button>
                  </template>
                </a-input>
                <template v-for="button of gridOptions?.toolbarConfig?.buttons">
                  <basic-button v-if="!button.dropdowns">{{ button.name }}</basic-button>
                  <a-dropdown v-else-if="selectedRows.length" :key="button.name" :content="button.name">
                    <template #overlay>
                      <a-menu @click="gridEvents.toolbarButtonClick?.(subButton as any)" v-for="subButton of button.dropdowns">
                        <a-menu-item :key="subButton.name + 's'">
                          <Icon :icon="subButton.icon"></Icon>
                          {{ subButton.name }}
                        </a-menu-item>
                      </a-menu>
                    </template>
                    <basic-button>
                      {{ button.name }}
                      <Icon icon="ant-design:down-outlined" />
                    </basic-button>
                  </a-dropdown>
                </template>
              </a-space>
            </a-col>
          </a-row>
        </template>
        <template #recordAction="{ row }">
          <button-group :row="row" :buttons="rowOperations" @click="rowClick" />
        </template>
        <template #pagerLeft>
          <a-alert type="warning" banner :message="'已选择 ' + selectedRows.length + ' 项'" style="height: 30px" />
        </template>
      </vxe-grid>
    </a-card>
    <basic-modal v-bind="popupConfig.containerProps" @register="registerModal" @cancel="closeModal" v-on="popupConfig.containerEvents">
      <component
        v-if="popupConfig.componentProps.is"
        v-bind="popupConfig.componentProps"
        :is="popupConfig.componentProps.is"
        @cancel="closeModal"
        @refresh="formSearch"
        v-on="popupConfig.componentEvents"
        ref="modalComponentRef"
      />
    </basic-modal>
    <basic-drawer v-bind="popupConfig.containerProps" @register="registerDrawer" @close="closeDrawer" v-on="popupConfig.containerEvents">
      <component
        v-if="popupConfig.componentProps.is"
        v-bind="popupConfig.componentProps"
        :is="popupConfig.componentProps.is"
        @cancel="closeDrawer"
        @refresh="formSearch"
        v-on="popupConfig.componentEvents"
        ref="drawerComponentRef"
      />
    </basic-drawer>
  </div>
</template>

<script lang="ts" setup>
import { Modal, message } from 'ant-design-vue';
import type { VxeGridPropTypes, VxeGridInstance, VxeGridListeners, VxeGridProps } from 'vxe-table/types/grid';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { transVxeSorts } from '@/utils/jhipster/sorts';
import { useDrawer } from '@/components/Drawer';
import { useModal } from '@/components/Modal';
import ServerProvider from '@/api-service/index';
import SysFillRuleDetail from './detail-component.vue';
import SysFillRuleEdit from './form-component.vue';
import SysFillRuleList from '../sys-fill-rule-list.vue';
import { useMergeGridProps } from '@/components/VxeTable/src/helper';
import FillRuleItemList from '@/views/settings/fill-rule-item/fill-rule-item-list.vue';
import { upperFirst as _upperFirst } from 'lodash-es';
import { AvatarGroupInfo } from '@/components/AvatarGroupInfo';

import { useI18n } from '@/hooks/web/useI18n';
import dayjs from 'dayjs';

const relationships = {};

const config = {
  searchForm: (relationshipApis): any[] => {
    const { getEnumDict } = useI18n();
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
        title: '规则名称',
        field: 'name',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '规则Code',
        field: 'code',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        componentProps: {},
      },
      {
        title: '规则描述',
        field: 'desc',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        hidden: true,
        componentProps: {},
      },
      {
        title: '是否启用',
        field: 'enabled',
        componentType: 'RadioGroup',
        value: '',
        operator: '',
        span: 8,
        hidden: true,
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
        title: '重置频率',
        field: 'resetFrequency',
        componentType: 'Select',
        value: '',
        span: 8,
        hidden: true,
        operator: 'equals',
        type: 'Enum',
        componentProps: () => {
          return { options: getEnumDict('ResetFrequency'), style: 'width: 100%' };
        },
      },
      {
        title: '序列值',
        field: 'seqValue',
        componentType: 'Text',
        value: '',
        type: 'Long',
        operator: '',
        span: 8,
        hidden: true,
        componentProps: {},
      },
      {
        title: '生成值',
        field: 'fillValue',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        hidden: true,
        componentProps: {},
      },
      {
        title: '规则实现类',
        field: 'implClass',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        hidden: true,
        componentProps: {},
      },
      {
        title: '规则参数',
        field: 'params',
        componentType: 'Text',
        value: '',
        type: 'String',
        operator: '',
        span: 8,
        hidden: true,
        componentProps: {},
      },
      {
        title: '重置开始日期',
        field: 'resetStartTime',
        componentType: 'DateTimeRange',
        operator: '',
        span: 8,
        hidden: true,
        type: 'ZonedDateTime',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '重置结束日期',
        field: 'resetEndTime',
        componentType: 'DateTimeRange',
        operator: '',
        span: 8,
        hidden: true,
        type: 'ZonedDateTime',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '重置时间',
        field: 'resetTime',
        componentType: 'DateTimeRange',
        operator: '',
        span: 8,
        hidden: true,
        type: 'ZonedDateTime',
        componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
      },
      {
        title: '配置项列表',
        field: 'ruleItems',
        componentType: 'ApiSelect',
        value: '',
        operator: '',
        span: 8,
        componentProps: { api: relationshipApis.ruleItems, style: 'width: 100%', valueField: 'id', labelField: 'datePattern' },
      },
    ];
  },
  columns: (): VxeGridPropTypes.Columns => {
    const { getEnumDict } = useI18n();
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
        visible: true,
        treeNode: false,
        params: { type: 'LONG' },
      },
      {
        title: '规则名称',
        field: 'name',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '规则Code',
        field: 'code',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '规则描述',
        field: 'desc',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '是否启用',
        field: 'enabled',
        minWidth: 70,
        visible: true,
        treeNode: false,
        params: { type: 'BOOLEAN' },
        cellRender: { name: 'ASwitch', props: { disabled: true } },
      },
      {
        title: '重置频率',
        field: 'resetFrequency',
        minWidth: 100,
        visible: true,
        treeNode: false,
        params: { type: 'ENUM' },
        formatter: ({ cellValue }) => {
          return (getEnumDict('ResetFrequency').find(item => item.value === cellValue) || { label: cellValue }).label;
        },
        editRender: { name: 'ASelect', props: { options: getEnumDict('ResetFrequency') }, enabled: false },
      },
      {
        title: '序列值',
        field: 'seqValue',
        minWidth: 80,
        visible: true,
        treeNode: false,
        params: { type: 'LONG' },
      },
      {
        title: '生成值',
        field: 'fillValue',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '规则实现类',
        field: 'implClass',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '规则参数',
        field: 'params',
        minWidth: 160,
        visible: true,
        treeNode: false,
        params: { type: 'STRING' },
        editRender: { name: 'AInput', enabled: false },
      },
      {
        title: '重置开始日期',
        field: 'resetStartTime',
        minWidth: 140,
        visible: true,
        treeNode: false,
        params: { type: 'ZONED_DATE_TIME' },
        formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
      },
      {
        title: '重置结束日期',
        field: 'resetEndTime',
        minWidth: 140,
        visible: true,
        treeNode: false,
        params: { type: 'ZONED_DATE_TIME' },
        formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
      },
      {
        title: '重置时间',
        field: 'resetTime',
        minWidth: 140,
        visible: true,
        treeNode: false,
        params: { type: 'ZONED_DATE_TIME' },
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
    ] as VxeGridPropTypes.Columns;
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
      id: 'vxe_grid_sysFillRule_relation',
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
        layouts: [
          'Sizes',
          'PrevJump',
          'PrevPage',
          'Number',
          'NextPage',
          'NextJump',
          'FullJump',
          'Total',
        ] as VxeGridPropTypes.PagerConfig['layouts'],
        pageSize: 15,
        pageSizes: [5, 10, 15, 20, 30, 50],
        total: 0,
        pagerCount: 5,
        currentPage: 1,
        autoHidden: false,
        slots: {
          left: 'pagerLeft',
        },
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
  ownerId: {
    type: [String, Number],
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
  relationData: {
    type: Array,
    default: null,
  },
  deleteRelationType: {
    type: String,
    default: 'delete', // 'delete' | 'cancel' 一对多时，使用delete，多对多时，使用cancel
  },
  parentContainer: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['updateRelationData']);

const [registerModal, { closeModal, setModalProps }] = useModal();
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawer();
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const apiService = ctx?.$apiService as typeof ServerProvider;
const apis = {
  sysFillRuleService: apiService.settings.sysFillRuleService,
  find: apiService.settings.sysFillRuleService.retrieve,
  deleteById: apiService.settings.sysFillRuleService.delete,
  deleteByIds: apiService.settings.sysFillRuleService.deleteByIds,
  import: apiService.settings.sysFillRuleService.importExcel,
  export: apiService.settings.sysFillRuleService.exportExcel,
  ruleItems: apiService.settings.fillRuleItemService.retrieve,
};
const columns = config.columns();
if (props.gridCustomConfig?.hideColumns?.length > 0) {
  const filterColumns = columns.filter(column => !props.gridCustomConfig.hideColumns.includes(column.field));
  columns.length = 0;
  columns.push(...filterColumns);
}
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
let rowOperations = [
  {
    title: '取消关联',
    name: 'cancelRelate',
    type: 'link',
    hide: () => props.deleteRelationType !== 'cancel',
  },
  {
    title: '删除',
    name: 'delete',
    type: 'link',
    hide: () => props.deleteRelationType !== 'delete',
  },
  {
    title: '详情',
    name: 'detail',
    containerType: 'drawer',
    type: 'link',
  },
];
if (props.gridCustomConfig?.rowOperations && _isArray(props.gridCustomConfig.rowOperations)) {
  if (props.gridCustomConfig.rowOperations.length === 0) {
    rowOperations = [];
  } else {
    rowOperations = rowOperations.filter(item =>
      props.gridCustomConfig.rowOperations.some(rowItem => (_isObject(rowItem) ? item.name === rowItem['name'] : item.name === rowItem)),
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
const gridOptions = computed(() => {
  const result: VxeGridProps = {
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
      response: {
        result: 'records',
        total: 'total',
      },
      ajax: {
        query: async ({ filters, page, sort, sorts }) => {
          console.log('filters', filters);
          const queryData = _pickBy(props.query);
          const queryParams: any = { ...queryData };
          queryParams.page = page?.currentPage > 0 ? page.currentPage - 1 : 0;
          queryParams.size = page?.pageSize;
          if (props.relationData) {
            queryParams['id.in'] = props.relationData.map(item => item.id);
          }
          if (Object.values(queryData).length === 0 && (!props.relationData || props.relationData.length === 0)) {
            return new Promise<any>(resolve => {
              resolve({ records: [], size: page?.pageSize || 15, page: page.currentPage, total: 0 });
            });
          }
          const allSort = sorts || [];
          sort && allSort.push(sort);
          queryParams.sort = transVxeSorts(allSort);
          if (searchFormConfig.jhiCommonSearchKeywords) {
            queryParams['jhiCommonSearchKeywords'] = searchFormConfig.jhiCommonSearchKeywords;
          } else {
            delete queryParams['jhiCommonSearchKeywords'];
            Object.assign(queryParams, getSearchQueryData(searchFormConfig));
          }
          return await apis.find(queryParams);
        },
        queryAll: async () => await apis.find({ size: -1 }),
        import: async ({ file, options }) => {
          apis.import(file).then(() => {
            formSearch();
          });
        },
      },
    },
    toolbarConfig: {
      custom: false,
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
          visible: true,
          circle: false,
          icon: 'vxe-icon-add',
          status: 'primary',
          dropdowns: [
            {
              code: 'batchDelete',
              name: '删除',
              visible: props.deleteRelationType === 'delete',
              circle: false,
              icon: 'ant-design:delete-filled',
              status: 'primary',
            },
            {
              code: 'batchCancelRelate',
              name: '取消关联',
              visible: props.deleteRelationType === 'cancel',
              circle: false,
              icon: 'ant-design:split-cells-outlined',
              status: 'primary',
            },
          ],
        },
      ],
      // 表格右上角自定义按钮
      tools: [
        { code: 'add', name: '选择', visible: true, circle: false, icon: 'vxe-icon-arrow-double-left' },
        { code: 'new', name: '新建', visible: true, circle: false, icon: 'vxe-icon-add' },
        { code: 'custom-column', name: '列配置', visible: true, circle: false, icon: 'vxe-icon-custom-column' },
      ],
    },
    columns,
  };
  useMergeGridProps(result, props.gridOptions);
  return result;
});

const toolbarClick = ({ code }) => {
  const $grid = xGrid.value;
  switch (code) {
    case 'batchDelete': {
      const records = $grid.getCheckboxRecords(true);
      if (records?.length > 0) {
        if (props.updateType === 'remoteApi') {
          const ids = records.map(record => record.id);
          Modal.confirm({
            title: `操作提示`,
            content: `是否删除ID为【${ids.join(',')}】的${records.length}项数据？`,
            onOk() {
              apis
                .deleteByIds(ids)
                .then(() => {
                  message.success({ content: `删除成功`, duration: 1 });
                  formSearch();
                })
                .catch(err => {
                  console.log('err', err);
                  message.error({ content: `删除失败`, duration: 1 });
                });
            },
          });
        } else {
          if (xGrid.value && records?.length > 0) {
            xGrid.value.remove(records).then(() => {
              message.success({ content: `删除成功`, duration: 1 });
            });
          }
        }
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
              // todo 暂时不需要
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
      popupConfig.needSubmit = true;
      popupConfig.containerProps.title = '选择';
      popupConfig.containerProps.okText = '确定';
      popupConfig.containerProps.cancelText = '取消';
      popupConfig.containerProps.showOkBtn = true;
      popupConfig.containerProps.showCancelBtn = true;
      popupConfig.componentProps.is = shallowRef(SysFillRuleList);
      popupConfig.componentProps.baseData = props.baseData;
      popupConfig.componentProps.query = {};
      popupConfig.componentProps.entityId = '';
      if (getData().length > 0) {
        popupConfig.componentProps.query['id.notIn'] = getData().map(item => item.id);
      }
      const tools: string[] = [];
      const buttons: string[] = [];
      const rowOperations = ['detail'];
      popupConfig.componentProps.gridOptions = _merge(
        {},
        { toolbarConfig: { import: false, print: false, export: false, custom: false, tools, buttons } },
      );
      popupConfig.componentProps.cardExtra = [];
      popupConfig.componentProps.searchFormOptions = _merge({});
      popupConfig.componentProps.gridCustomConfig = _merge({}, { rowOperations });
      if (props.editIn === 'drawer') {
        popupConfig.componentProps.containerType = 'drawer';
        popupConfig.containerEvents.ok = okDrawer;
        popupConfig.containerProps.width = '40%';
        setDrawerProps({ open: true });
      } else {
        popupConfig.componentProps.containerType = 'modal';
        popupConfig.containerProps.width = '80%';
        popupConfig.containerEvents.ok = okModal;
        setModalProps({ open: true });
      }
      break;
    }
    case 'new':
      popupConfig.needSubmit = true;
      popupConfig.containerProps.title = '新建';
      popupConfig.containerProps.okText = '保存';
      popupConfig.containerProps.cancelText = '取消';
      popupConfig.containerProps.showOkBtn = true;
      popupConfig.containerProps.showCancelBtn = true;
      popupConfig.componentProps.is = shallowRef(SysFillRuleEdit);
      popupConfig.componentProps.entityId = '';
      popupConfig.componentProps.baseData = props.baseData;
      popupConfig.containerEvents.ok = async () => {
        const result = await drawerComponentRef.value.submit({ submitToServer: false });
        if (result) {
          // 对新增的记录进行关联关系处理
          if (props.editIn === 'drawer') {
            closeDrawer();
          } else {
            closeModal();
          }
          formSearch();
        } else {
          // 此时result = false;
          console.log('保存失败。');
        }
      };
      popupConfig.componentProps.containerType = props.editIn === 'drawer' ? 'drawer' : 'modal';
      popupConfig.containerProps.width = props.editIn === 'drawer' ? '40%' : '80%';
      if (props.editIn === 'drawer') {
        setDrawerProps({ open: true });
      } else {
        setModalProps({ open: true });
      }
      break;
    case 'custom-column':
      xGrid.value.openCustom();
      break;
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
      if (xGrid.value && selectRows?.length > 0) {
        xGrid.value.insert(selectRows).then(() => {
          emit('updateRelationData', getData());
          message.success({
            content: `关联成功`,
            duration: 1,
          });
        });
      }
    } else {
      if (xGrid.value && selectRows?.length > 0) {
        xGrid.value.insert(selectRows).then(() => {
          emit('updateRelationData', getData());
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
      if (xGrid.value && selectRows?.length > 0) {
        xGrid.value.insert(selectRows).then(() => {
          emit('updateRelationData', getData());
          message.success({
            content: `关联成功`,
            duration: 1,
          });
        });
      }
    } else {
      if (xGrid.value && selectRows?.length > 0) {
        xGrid.value.insert(selectRows).then(() => {
          emit('updateRelationData', getData());
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
const inputSearch = _debounce(formSearch, 700);

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
  if (operation?.click) {
    operation.click(row);
  } else {
    switch (name) {
      case 'detail':
        popupConfig.componentProps.is = shallowRef(SysFillRuleDetail);
        popupConfig.componentProps.entityId = row.id;
        popupConfig.containerProps.title = '详情';
        popupConfig.containerProps.showOkBtn = false;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.containerProps.cancelText = '关闭';
        if (operation?.containerType === 'drawer') {
          popupConfig.componentProps.containerType = 'drawer';
          popupConfig.containerProps.width = '40%';
          setDrawerProps({ open: true });
        } else {
          popupConfig.componentProps.containerType = 'modal';
          popupConfig.containerProps.width = '80%';
          setModalProps({ open: true });
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
                // todo 暂时不需要
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
      case 'delete':
        Modal.confirm({
          title: `操作提示`,
          content: `是否删除ID为${row.id}的数据？`,
          onOk() {
            if (operation.click) {
              operation.click(row);
            } else {
              if (props.updateType === 'remoteApi') {
                apis.deleteById(row.id).then(result => {
                  message.success({
                    content: `删除成功`,
                    duration: 1,
                  });
                  formSearch();
                });
              } else {
                if (xGrid.value) {
                  xGrid.value.remove([row]).then(() => {
                    message.success({
                      content: `删除成功`,
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

const validate = async () => {
  const result = await xGrid.value.validate(true);
  if (!result) {
    return getData();
  } else {
    return false;
  }
};

watchEffect(() => {
  if ((props.relationData && props.relationData.length > 0) || (props.query && Object.keys(props.query).length > 0)) {
    nextTick(() => {
      formSearch();
    });
  }
});

defineExpose({
  getSelectRows,
  getData,
  validate,
});
</script>
<style lang="less" scoped>
:deep(.ant-card-bordered.bc-list-result-card) {
  border: none;
}
</style>
