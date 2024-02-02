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
          >报表存储列表</Button
        >
      </template>
      <template #extra>
        <Space>
          <Divider type="vertical" />
          <Button
            type="default"
            @click="xGrid.openImport()"
            preIcon="ant-design:cloud-upload-outlined"
            shape="circle"
            size="small"
          ></Button>
          <Button type="default" @click="xGrid.openExport()" preIcon="ant-design:download-outlined" shape="circle" size="small"></Button>
          <!--          <Button type="default" @click="xGrid.openPrint()" preIcon="ant-design:printer-outlined" shape="circle" size="small"></Button>-->
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
          <CardList ref="cardList" v-bind="cardListOptions" @getMethod="cardListFetchMethod" @delete="deleteById">
            <template #header_left>
              <Row class="toolbar_buttons_xgrid" :gutter="16">
                <Col :lg="2" :md="2" :sm="4" v-if="filterTreeConfig.treeFilterData.length > 0">
                  <span class="table-page-search-submitButtons">
                    <Button
                      type="primary"
                      :icon="filterTreeConfig.filterTreeSpan > 0 ? 'pic-center' : 'pic-right'"
                      @click="switchFilterTree"
                    ></Button>
                  </span>
                </Col>
                <Col v-if="!searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled">
                  <Input
                    placeholder="请输入关键字"
                    v-model:value="searchValue"
                    allow-clear
                    @search="formSearch"
                    enterButton
                    ref="searchInputRef"
                  >
                    <template #prefix>
                      <Icon icon="ant-design:search-outlined" />
                    </template>
                    <template #addonAfter v-if="searchFormConfig.allowSwitch">
                      <Button type="link" @click="formSearch" style="height: 30px"
                        >查询<Icon icon="ant-design:filter-outlined" @click="handleToggleSearch"></Icon
                      ></Button>
                    </template>
                  </Input>
                </Col>
              </Row>
            </template>
            <template #loadMore>
              <div v-if="loading" style="text-align: center; margin-top: 12px; height: 32px; line-height: 32px">
                <Spin />
              </div>
              <div v-else style="text-align: center; margin-top: 12px; height: 32px; line-height: 32px">
                <!--              <Button @click="handleLoadMore">加载更多</Button>-->
              </div>
            </template>
          </CardList>
        </Col>
      </Row>
      <BasicModal v-bind="modalConfig" @register="registerModal" @cancel="closeModal" @ok="okModal">
        <component
          :is="modalConfig.componentName"
          @cancel="closeModalOrDrawer"
          @refresh="formSearch"
          v-bind="modalConfig"
          ref="modalComponentRef"
        />
      </BasicModal>
      <BasicDrawer v-bind="drawerConfig" @register="registerDrawer" @cancel="closeDrawer" @ok="okDrawer">
        <component
          :is="drawerConfig.componentName"
          @cancel="closeModalOrDrawer"
          @refresh="formSearch"
          v-bind="drawerConfig"
          ref="drawerComponentRef"
        />
      </BasicDrawer>
    </Card>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, getCurrentInstance, shallowRef } from 'vue';
import { Modal, Card, Space, Divider, Row, Col, Tree, Input, Spin, message } from 'ant-design-vue';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import { useModalInner, BasicModal, useDrawerInner, BasicDrawer, Icon, Button, SearchForm, CardList } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import UReportFileEdit from './u-report-file-edit.vue';
import UReportFileDetail from './u-report-file-detail.vue';
import config from './config/list-config';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
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
const relationshipApis: any = {};
const apis = {
  find: apiService.report.uReportFileService.retrieve,
  deleteById: apiService.report.uReportFileService.delete,
  deleteByIds: apiService.report.uReportFileService.deleteByIds,
  update: apiService.report.uReportFileService.update,
};
const pageConfig = {
  title: '报表存储列表',
  baseRouteName: 'reportUReportFile',
};
const columns = config.columns();
const searchFormFields = config.searchForm();
const cardList = ref<any>(null);
const searchInputRef = ref(null);
const searchFormRef = ref<any>(null);
const searchFormConfig = reactive({
  fieldList: searchFormFields,
  toggleSearchStatus: false,
  matchType: 'and',
  disabled: false,
  allowSwitch: true,
});
const batchOperations = [];
const rowOperations = [
  {
    disabled: false,
    name: 'save',
  },
  {
    name: 'delete',
  },
  {
    title: '详情',
    name: 'detail',
    containerType: 'drawer',
  },
];
const tableRowOperations = reactive<any[]>([]);
const tableRowMoreOperations = reactive<any[]>([]);
const saveOperation = rowOperations.find(operation => operation.name === 'save');
if (rowOperations.length > 4 || (saveOperation && rowOperations.length > 3)) {
  if (saveOperation) {
    tableRowOperations.push(...rowOperations?.slice(0, 2));
    tableRowMoreOperations.push(...rowOperations.slice(3));
  } else {
    tableRowOperations.push(...rowOperations?.slice(0, 3));
    tableRowMoreOperations.push(...rowOperations.slice(4));
  }
} else {
  tableRowOperations.push(...rowOperations);
}
const selectedRows = reactive<any>([]);
const loading = ref(false);
const searchValue = ref('');
const mapOfFilter = ref({});
const mapOfSort = ref({});
columns?.forEach(column => {
  if (column.sortable && column.field) {
    mapOfSort.value[column.field] = false;
  }
});
const sort = () => {
  const result: any[] = [];
  Object.keys(mapOfSort.value).forEach(key => {
    if (mapOfSort.value[key] && mapOfSort.value[key] !== false) {
      if (mapOfSort.value[key] === 'asc') {
        result.push(key + ',asc');
      } else if (mapOfSort.value[key] === 'desc') {
        result.push(key + ',desc');
      }
    }
  });
  return result;
};
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
  destroyOnClose: true,
});
const drawerConfig = reactive<any>({
  componentName: '',
  containerType: 'drawer',
  entityId: '',
  baseData: props.baseData,
  destroyOnClose: true,
});
const cardListOptions = reactive({
  params: {},
  api: apis.find,
  imageField: 'url',
  resultField: 'records',
  totalField: 'total',
  metaTitle: '',
  metaDesc: 'createAt',
  showAvatar: false,
  toolButtons: [
    {
      title: '新增',
      icon: 'ant-design:upload-outlined',
      click: () => {
        modalConfig.componentName = shallowRef(UReportFileEdit);
        modalConfig.entityId = '';
        modalConfig.containerType = 'modal';
        setModalProps({
          open: true,
          title: '新增报表存储',
          width: 800,
        });
      },
      hidden: false,
      disabled: false,
    },
    { code: 'new', name: '', circle: true, icon: 'vxe-icon-add' },
  ],
  rowOperations: [
    {
      title: '编辑',
      click: (row: any) => {
        rowClickHandler('edit', { containerType: 'modal', title: '编辑' }, row);
      },
    },
  ],
  fetchMethod: params =>
    new Promise(resolve => {
      resolve(params);
    }),
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
  let params = {};
  if (searchValue.value) {
    params['jhiCommonSearchKeywords'] = searchValue.value;
  } else {
    params = Object.assign({}, cardListOptions.params, getSearchQueryData(searchFormConfig));
  }
  cardListOptions.fetchMethod(params).then(res => {
    console.log('fetch.res', res);
  });
};

const cardListFetchMethod = method => {
  cardListOptions.fetchMethod = method;
};

const handleToggleSearch = () => {
  searchFormConfig.toggleSearchStatus = !searchFormConfig.toggleSearchStatus;
};

const deleteById = id => {
  apis
    .deleteById(id)
    .then(() => {
      message.success('删除成功。');
      formSearch();
    })
    .catch(err => {
      console.log('err', err);
    });
};

const closeModalOrDrawer = ({ containerType, update }) => {
  if (containerType === 'modal') {
    closeModal();
  } else if (containerType === 'drawer') {
    closeDrawer();
  }
  if (update) {
    formSearch();
  }
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

const xGridSortChange = ({ property, order }) => {
  mapOfSort.value = {};
  mapOfSort.value[property] = order;
  formSearch();
};

const xGridFilterChange = ({ column, property, values, datas }) => {
  const type = column.params ? column.params.type : '';
  let tempValues;
  if (type === 'STRING') {
    tempValues = datas[0];
  } else if (type === 'INTEGER' || type === 'LONG' || type === 'DOUBLE' || type === 'FLOAT' || type === 'ZONED_DATE_TIME') {
    tempValues = datas[0];
  } else if (type === 'BOOLEAN') {
    tempValues = values;
  }
  mapOfFilter.value[property] = { value: tempValues, type: type };
  formSearch();
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
          switch (operation.containerType) {
            case 'modal':
              operation.title && (modalConfig.title = operation.title);
              modalConfig.componentName = shallowRef(UReportFileEdit);
              modalConfig.entityId = row.id;
              setModalProps({ open: true });
              break;
            case 'drawer':
              operation.title && (drawerConfig.title = operation.title);
              drawerConfig.componentName = shallowRef(UReportFileEdit);
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
      } else {
        if (pageConfig.baseRouteName) {
          go({ name: `${pageConfig.baseRouteName}Edit`, params: { entityId: row.id } });
        } else {
          console.log('未定义方法');
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
              operation.title && (modalConfig.title = operation.title);
              modalConfig.componentName = shallowRef(UReportFileDetail);
              modalConfig.entityId = row.id;
              setModalProps({ open: true });
              break;
            case 'drawer':
              operation.title && (drawerConfig.title = operation.title);
              drawerConfig.componentName = shallowRef(UReportFileDetail);
              drawerConfig.entityId = row.id;
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
            apis
              .deleteById(row.id)
              .then(() => {
                formSearch();
              })
              .catch(err => {
                console.log('err', err);
              });
          }
        },
      });
      break;
    case 'design':
      switch (operation.containerType) {
        case 'modal':
          this.modalConfig.componentName = 'a-iframe';
          this.modalConfig.iframeUrl = '/ureport/designer?_u=db:' + row.name;
          this.setModalProps({ open: true });
          break;
        case 'drawer':
          this.drawerConfig.componentName = 'a-iframe';
          this.modalConfig.iframeUrl = '/ureport/designer?_u=db:' + row.name;
          this.setDrawerProps({ open: true });
          break;
        case 'route':
        default:
          if (this.pageConfig.baseRouteName) {
            this.go({ name: `${this.pageConfig.baseRouteName}Detail`, params: { entityId: row.id } });
          } else {
            console.log('未定义方法');
          }
      }
      break;
    case 'preview':
      switch (operation.containerType) {
        case 'modal':
          this.modalConfig.componentName = 'a-iframe';
          this.modalConfig.iframeUrl = '/ureport/preview?_u=db:' + row.name;
          this.setModalProps({ open: true });
          break;
        case 'drawer':
          this.drawerConfig.componentName = 'a-iframe';
          this.modalConfig.iframeUrl = '/ureport/preview?_u=db:' + row.name;
          this.setDrawerProps({ open: true });
          break;
        case 'route':
        default:
          if (this.pageConfig.baseRouteName) {
            this.go({ name: `${this.pageConfig.baseRouteName}Detail`, params: { entityId: row.id } });
          } else {
            console.log('未定义方法');
          }
      }
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
</script>
<style>
.toolbar_buttons_xgrid {
  margin-left: 5px !important;
}
.table-page-search-submitButtons {
  display: inline-block !important;
}
.vxe-tools--wrapper {
  padding-right: 12px;
}
</style>
