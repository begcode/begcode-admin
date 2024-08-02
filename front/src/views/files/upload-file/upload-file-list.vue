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
      <List :grid="listGrid" item-layout="vertical" :data-source="listData" :pagination="false">
        <template #header>
          <div class="flex justify-between space-x-2">
            <div>
              <Row class="toolbar_buttons_xgrid" :gutter="16">
                <Col v-if="!searchFormConfig.toggleSearchStatus && !searchFormConfig.disabled">
                  <Space>
                    <Input
                      placeholder="请输入关键字"
                      v-model:value="searchFormConfig.jhiCommonSearchKeywords"
                      allow-clear
                      @search="formSearch"
                      enterButton
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
                    <Dropdown v-if="selectedRows.length && batchOperations.length">
                      <template #overlay>
                        <Menu @click="batchOperationClick">
                          <MenuItem :key="batchOperation.name" v-for="batchOperation of batchOperations">
                            <Icon :icon="batchOperation.icon" v-if="batchOperation.icon"></Icon>
                            {{ batchOperation.title }}
                          </MenuItem>
                        </Menu>
                      </template>
                      <Button>
                        批量处理
                        <Icon icon="ant-design:down-outlined" />
                      </Button>
                    </Dropdown>
                  </Space>
                </Col>
              </Row>
            </div>
            <div>
              <Space>
                <template v-for="button in cardListOptions.toolButtons">
                  <Tooltip v-if="!button.hidden">
                    <template #title>{{ button.title }}</template>
                    <Button :disabled="button.disabled" @click="button.click">
                      <Icon :icon="button.icon" v-if="button.icon"></Icon>
                      {{ button.title }}
                    </Button>
                  </Tooltip>
                </template>
                <Tooltip>
                  <template #title>
                    <div class="w-50">每行显示数量</div>
                    <Slider id="slider" v-bind="sliderProp" :value="listColumns" @change="sliderChange" />
                  </template>
                  <Button><Icon icon="ant-design:table-outlined" />列数</Button>
                </Tooltip>
              </Space>
            </div>
          </div>
        </template>
        <template #renderItem="{ item }">
          <div style="margin-top: 24px">
            <List.Item>
              <Badge>
                <template #count>
                  <Checkbox :checked="getItemSelected(item)" @change="selectChange($event, item)" />
                </template>
                <Card :body-style="{ padding: '12px 4px' }">
                  <template #title></template>
                  <template #cover>
                    <Badge.Ribbon :text="metaConfig.titleValue(item)" placement="start" class="cover-ribbon">
                      <div class="cover-height">
                        <Image
                          v-if="coverConfig.showImage"
                          :src="item[coverConfig.coverFieldName] || '/resource/img/filetype/other.png'"
                          v-bind="coverConfig.props"
                          @click="coverConfig.click"
                          fallback="/resource/img/filetype/other.png"
                        />
                      </div>
                    </Badge.Ribbon>
                  </template>
                  <template class="ant-card-actions" #actions>
                    <ButtonGroup :row="item" :buttons="rowOperations" @click="rowClick" />
                  </template>
                  <Card.Meta>
                    <template #avatar>
                      <Avatar :src="item.avatar" v-if="metaConfig.showAvatar" />
                    </template>
                    <template #description v-if="metaConfig.showDesc">{{ metaConfig.descValue(item) }}</template>
                  </Card.Meta>
                </Card>
              </Badge>
            </List.Item>
          </div>
        </template>
      </List>
      <Affix :offset-bottom="0">
        <Row justify="space-between" style="background: #fff; padding: 10px 0">
          <Col :span="12" style="display: flex; justify-content: flex-start">
            <Alert type="warning" :banner="true" style="height: 30px" :message="`已选${selectedRows.length}条记录。`"></Alert>
          </Col>
          <Col :span="12" style="display: flex; justify-content: flex-end">
            <Pagination
              v-model:current="paginationProps.current"
              :page-size="paginationProps.pageSize"
              :total="paginationProps.total"
              :show-size-changer="false"
              :show-quick-jumper="false"
              @change="pageChange"
            />
          </Col>
        </Row>
      </Affix>
      <BasicModal v-bind="modalConfig" @register="registerModal" @cancel="closeModal" @ok="okModal">
        <component
          :is="modalConfig.componentName"
          @cancel="closeModalOrDrawer"
          @refresh="formSearch"
          v-bind="modalConfig"
          ref="modalComponentRef"
        />
      </BasicModal>
      <BasicDrawer v-bind="drawerConfig" @register="registerDrawer" @close="closeDrawer" @ok="okDrawer">
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
import { reactive, ref, getCurrentInstance, shallowRef, onMounted, computed, toRaw } from 'vue';
import {
  Modal,
  Card,
  Space,
  Row,
  Col,
  Input,
  message,
  List,
  Tooltip,
  Upload,
  Slider,
  Avatar,
  Image,
  Badge,
  Checkbox,
  Affix,
  Pagination,
  Alert,
  Dropdown,
  MenuItem,
  Menu,
} from 'ant-design-vue';
import { breakpointsAntDesign, useBreakpoints } from '@vueuse/core';
import { getSearchQueryData } from '@/utils/jhipster/entity-utils';
import {
  useModalInner,
  BasicModal,
  useDrawerInner,
  BasicDrawer,
  Icon,
  Button,
  SearchForm,
  ButtonGroup,
  ImageUpload,
} from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import ServerProvider from '@/api-service/index';
import UploadFileEdit from './components/form-component.vue';
import UploadFileDetail from './components/detail-component.vue';
import { IUploadFile } from '@/models/files/upload-file.model';
import config from './config/list-config';
import { useSetShortcutButtons, useSlider } from '@/components/VxeTable/src/helper';
import { getFilePreview } from '@/utils/file-preview';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
  editIn: {
    ype: String,
    default: 'modal',
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
  cardExtra: {
    type: Array,
    default: ['import', 'export'],
  },
});

const [registerModal, { closeModal, setModalProps }] = useModalInner(data => {
  console.log(data);
});
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(data => {
  console.log(data);
});
const breakpoints = useBreakpoints(breakpointsAntDesign);
const listGrid = reactive({ gutter: 5, xs: 1, sm: 2, md: 3, lg: 4, xl: 5, xxl: 6, xxxl: 8 });
const listColumns = computed(() => {
  const breakpoint = breakpoints.current().value.pop();
  return listGrid[breakpoint!];
});
const sliderProp = computed(() => useSlider(3));

function sliderChange(n) {
  paginationProps.pageSize = n * 2;
  const breakpoint = breakpoints.current().value.pop();
  listGrid[breakpoint!] = n;
  formSearch();
}
const metaConfig = reactive({
  titleValue: item => {
    return item.url;
  },
  showAvatar: false,
  showDesc: true,
  descValue: item => {
    return item.createAt;
  },
});
const coverConfig = reactive({
  showImage: true,
  coverFieldName: 'imageUrl',
  props: {
    preview: true,
  },
  click: item => {
    console.log('item:::', item);
  },
});
const listData = ref<IUploadFile[]>([]);
const height = computed(() => {
  return 140 - listColumns.value * 6 + 'px';
});
const modalComponentRef = ref<any>(null);
const drawerComponentRef = ref<any>(null);
const ctx = getCurrentInstance()?.proxy;
const go = useGo();
const apiService = ctx?.$apiService as typeof ServerProvider;
const relationshipApis: any = {
  category: apiService.files.resourceCategoryService.tree,
};
const apis = {
  find: apiService.files.uploadFileService.retrieve,
  deleteById: apiService.files.uploadFileService.delete,
  deleteByIds: apiService.files.uploadFileService.deleteByIds,
  update: apiService.files.uploadFileService.update,
};
const pageConfig = {
  title: '上传文件列表',
  baseRouteName: 'ossUploadFile',
};
const searchFormRef = ref<any>(null);
const searchFormConfig = reactive<any>({
  fieldList: config.searchForm(),
  toggleSearchStatus: false,
  useOr: false,
  disabled: false,
  allowSwitch: true,
  compact: true,
  jhiCommonSearchKeywords: '',
});
const rowOperations: any[] = [
  {
    title: '删除',
    name: 'delete',
    type: 'link',
  },
  {
    title: '详情',
    name: 'detail',
    type: 'link',
    containerType: 'drawer',
  },
];
const selectedRows = ref<any[]>([]);
const getItemSelected = computed(() => {
  return (item: any) => {
    return selectedRows.value.some(row => row.id === item.id);
  };
});
const selectChange = (e, item) => {
  if (e.target.checked) {
    if (!selectedRows.value.some(row => row.id === item.id)) {
      selectedRows.value.push(item);
    }
  } else {
    selectedRows.value = selectedRows.value.filter(row => row.id !== item.id);
  }
};
const alertMessage = computed(() => {
  return `已选${selectedRows.value.length}条记录。`;
});

const batchOperations = ref<any[]>([
  {
    title: '批量删除',
    name: 'batchDelete',
    type: 'link',
    icon: 'ant-design:delete-outlined',
  },
]);

const batchOperationClick = ({ key }) => {
  console.log('key', key);
  switch (key) {
    case 'batchDelete':
      Modal.confirm({
        title: `操作提示`,
        content: `是否确认删除ID为${selectedRows.value.map(row => row.id).join(',')}的记录？`,
        onOk() {
          apis.deleteByIds(selectedRows.value.map(row => row.id)).then(() => {
            message.success('删除成功。');
            formSearch();
          });
        },
      });
      break;
    default:
      console.log('error', `${key}未定义`);
  }
};

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

const cardListOptions = reactive<any>({
  params: {},
  metaDesc: 'createAt',
  toolButtons: [
    {
      title: '新增',
      icon: 'ant-design:upload-outlined',
      click: () => {
        popupConfig.needSubmit = true;
        popupConfig.containerProps.title = '新增上传文件';
        popupConfig.containerProps.okText = '保存';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.UploadFileEdit;
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
      },
      hidden: false,
      disabled: false,
    },
  ],
});
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
]);
useSetShortcutButtons('OssUploadFileList', extraButtons);

const okModal = async () => {
  if (modalConfig.needSubmit && modalComponentRef.value) {
    const result = await modalComponentRef.value.submit();
    if (result) {
      formSearch();
      closeModal();
    }
  }
};
const okDrawer = async () => {
  if (drawerConfig.needSubmit && drawerComponentRef.value) {
    const result = await drawerComponentRef.value.submit();
    if (result) {
      formSearch();
      closeDrawer();
    }
  }
};
const formSearch = () => {
  selectedRows.value = [];
  let params: any = {};
  if (searchFormConfig.jhiCommonSearchKeywords) {
    params['jhiCommonSearchKeywords'] = searchFormConfig.jhiCommonSearchKeywords;
  } else {
    params = Object.assign({}, cardListOptions.params, getSearchQueryData(searchFormConfig));
  }
  params.page = paginationProps.current;
  params.size = paginationProps.pageSize;
  apis.find(params).then(data => {
    data.records.forEach((file: any) => {
      Object.assign(file, getFilePreview(file));
    });
    listData.value = data.records;
    paginationProps.total = data.total;
  });
};

const handleToggleSearch = () => {
  searchFormConfig.toggleSearchStatus = !searchFormConfig.toggleSearchStatus;
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

const showSearchFormSetting = () => {
  if (searchFormRef.value) {
    searchFormRef.value.showSettingModal();
  }
};

//分页相关
const paginationProps = reactive({
  showSizeChanger: false,
  showQuickJumper: true,
  pageSize: 36,
  current: 0,
  total: 0,
  showTotal: (total: number) => `共 ${total} 条`,
});

function pageChange(page: number, pageSize: number) {
  paginationProps.current = page;
  paginationProps.pageSize = pageSize;
  formSearch();
}

const rowClick = ({ name, data }) => {
  const row = data;
  const operation = rowOperations.find(operation => operation.name === name);
  if (operation?.click) {
    operation.click(row);
  } else {
    switch (name) {
      case 'edit':
        popupConfig.needSubmit = true;
        popupConfig.containerProps.title = '编辑上传文件';
        popupConfig.containerProps.okText = '更新';
        popupConfig.containerProps.cancelText = '取消';
        popupConfig.containerProps.showOkBtn = true;
        popupConfig.containerProps.showCancelBtn = true;
        popupConfig.componentProps.is = shallowRefs.UploadFileEdit;
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
        popupConfig.componentProps.is = shallowRefs.UploadFileDetail;
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
          case 'route':
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
            if (operation.click) {
              operation.click(row);
            } else {
              apis
                .deleteById(row.id)
                .then(() => {
                  message.success('删除成功。');
                  formSearch();
                })
                .catch(err => {
                  console.log('err', err);
                });
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

onMounted(() => {
  formSearch();
});

defineExpose({ getSelectRows });
</script>
<style scoped>
.toolbar_buttons_xgrid {
  margin-left: 5px !important;
}
.table-page-search-submitButtons {
  display: inline-block !important;
}
.vxe-tools--wrapper {
  padding-right: 12px;
}
.cover-height {
  height: v-bind('height');
  overflow: hidden;
}
:deep(.ant-ribbon-wrapper) .cover-ribbon {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 95%;
}
</style>
