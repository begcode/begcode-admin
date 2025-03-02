<template>
  <div>
    <a-row class="select-row" type="flex" :gutter="8">
      <a-col class="left" :class="{ full: true }">
        <basic-button v-if="showComponentName === 'Button'" :pre-icon="buttonIcon" @click="!disabled && openModal()">{{
          modalTitle
        }}</basic-button>
        <div class="flex items-center justify-between">
          <a-avatar-group v-bind="showComponentProps" v-if="avatarGroupData.length">
            <a-tooltip :title="item[avatarSlotField]" placement="top" v-for="item in avatarGroupData">
              <a-avatar :src="avatarSlotName === 'src' ? item[avatarSlotField] : undefined">
                <template #icon v-if="avatarSlotName === 'icon' && item[avatarSlotField]"><Icon :icon="item[avatarSlotField]" /></template>
                <span v-if="avatarSlotName === 'default'">{{ (avatarSlotField && item[avatarSlotField]) || item }}</span>
              </a-avatar>
            </a-tooltip>
          </a-avatar-group>
          <a-select
            v-else-if="!disabled"
            ref="select"
            v-model:value="selectData.value"
            :placeholder="placeholder"
            :mode="multiple ? 'tags' : undefined"
            :open="false"
            :disabled="true"
            :maxTagCount="maxTagCount"
            :labelInValue="labelInValue"
            style="width: 100%"
            @click="!disabled && openModal()"
          />
          <div>
            <basic-button
              v-if="disabled"
              pre-icon="ant-design:file-search-outlined"
              type="link"
              size="small"
              @click.stop="disabled && openModal()"
            ></basic-button>
            <basic-button
              v-else
              pre-icon="ant-design:edit-outlined"
              type="link"
              size="small"
              @click.stop="!disabled && openModal()"
            ></basic-button>
            <basic-button
              v-if="updateType === 'emitSelected' && avatarGroupData.length && !disabled"
              pre-icon="ant-design:close-outlined"
              type="link"
              size="small"
              @click.stop="!disabled && clear()"
            ></basic-button>
          </div>
        </div>
      </a-col>
    </a-row>
    <basic-modal
      :title="modalTitle"
      :mask-closable="false"
      :ok-button-props="okButtonProps"
      :cancel-button-props="cancelButtonProps"
      @register="register"
      @ok="handleOk"
      @visible-change="visibleChange"
      width="900px"
      destroy-on-close
      wrap-class-name="vxe-table--ignore-clear"
    >
      <a-form-item-rest>
        <component v-bind="componentProps" v-on="componentEvents" :is="dynamicComponent" ref="tableModalRef" />
      </a-form-item-rest>
    </basic-modal>
    <basic-drawer
      :title="modalTitle"
      :mask-closable="false"
      :showFooter="!disabled"
      :ok-button-props="okButtonProps"
      :cancel-button-props="cancelButtonProps"
      @register="registerDrawer"
      @ok="handleOk"
      @visible-change="visibleChange"
      width="450px"
      destroy-on-close
      root-class-name="vxe-table--ignore-clear"
    >
      <a-form-item-rest>
        <component v-bind="componentProps" v-on="componentEvents" :is="dynamicComponent" ref="tableDrawerRef" />
      </a-form-item-rest>
    </basic-drawer>
  </div>
</template>
<script lang="ts" setup>
import type { SelectValue } from 'ant-design-vue/es/select';
import type { VxeGridInstance } from 'vxe-table/types/grid';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { useModalInner } from '@/components/Modal';
import { useDrawerInner } from '@/components/Drawer';
import { ApiSelect, ApiTreeSelect, ApiCascader, ApiTree } from '@/components/Form';

defineOptions({
  name: 'SelectModal',
  inheritAttrs: false,
});

const props = defineProps({
  value: {
    type: [Array, Object, String, Number] as PropType<SelectValue>,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
  // 是否支持多选，默认 true
  multiple: {
    type: Boolean,
    default: true,
  },
  // 是否正在加载
  loading: {
    type: Boolean,
    default: false,
  },
  // 最多显示多少个 tag
  maxTagCount: {
    type: Number,
    default: 3,
  },
  // buttonIcon
  buttonIcon: {
    type: String,
    default: '',
  },
  modalTitle: {
    type: String,
    default: '请选择',
  },
  showComponentName: {
    type: String,
    default: 'Select',
  },
  componentName: {
    type: String,
    default: '',
  },
  api: {
    type: Function,
    default: null,
  },
  resultField: {
    type: String,
    default: 'records',
  },
  fieldNames: {
    type: Object,
    default: () => ({
      label: 'label',
      value: 'value',
      children: 'children',
    }),
  },
  labelInValue: {
    type: Boolean,
    default: false,
  },
  checkStrictly: {
    type: Boolean,
    default: false,
  },
  selectOptions: {
    type: Array,
    default: () => [],
  },
  container: {
    type: String,
    default: 'modal',
  },
  // default icon src
  avatarSlotName: {
    type: String,
    default: 'default',
  },
  avatarSlotField: {
    type: String,
    default: '',
  },
  avatarTipField: {
    type: String,
    default: '',
  },
  queryNames: {
    type: Array,
    default: () => [],
  },
  xGrid: {
    type: Object as PropType<VxeGridInstance>,
  },
  row: {
    type: Object,
    default: null,
  },
  column: {
    type: Object,
    default: () => ({}),
  },
  rowIdField: {
    type: String,
    default: '',
  },
  source: {
    type: String,
    default: '',
  },
  gridCustomConfig: {
    type: Object,
    default: () => ({}),
  },
  searchFormOptions: {
    type: Object,
    default: () => ({}),
  },
  gridOptions: {
    type: Object,
    default: () => ({}),
  },
  updateType: {
    type: String,
    default: 'remoteApi',
  },
  // array | object | splitString
  valueType: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['handleOpen', 'change', 'register', 'update:value']);

const okButtonProps = {
  'data-cy': 'selectModalOkButton',
};

const cancelButtonProps = {
  'data-cy': 'selectModalCancelButton',
};

const getViewComponent: any = inject('GET_VIEW_COMPONENT');
const { value: valueField, label: labelField } = props.fieldNames;
const selectData = reactive({ value: props.value, change: false });

if (props.componentName === 'ApiTree') {
  if (selectData.value) {
    selectData.value = (selectData.value as Array<any>).map(valueItem => valueItem[valueField]);
  } else {
    selectData.value = [];
  }
}
const getComponent = (componentName: string) => {
  if (componentName === 'ApiSelect') {
    return ApiSelect;
  }
  if (componentName === 'ApiTreeSelect') {
    return ApiTreeSelect;
  }
  if (componentName === 'ApiCascader') {
    return ApiCascader;
  }
  if (componentName === 'ApiTree') {
    return ApiTree;
  }
  return getViewComponent(componentName);
};
const tableDrawerRef = ref<any>(null);
const tableModalRef = ref<any>(null);
const tableRef = computed(() => {
  return props.container === 'modal' ? tableModalRef.value : tableDrawerRef.value;
});
const [register, { closeModal, setModalProps }] = useModalInner(() => {
  setTimeout(() => {
    if (tableRef.value) {
      const $grid = tableRef.value as VxeGridInstance;
      $grid.setCheckboxRow(selectData['value'] || [], true);
    }
  }, 800);
});
const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(() => {
  setTimeout(() => {
    if (tableRef.value) {
      const $grid = tableRef.value as VxeGridInstance;
      $grid.setCheckboxRow(selectData['value'] || [], true);
    }
  }, 800);
});
const options = reactive<any[]>([]);
//接收下拉框选项
if (props.selectOptions && props.selectOptions.length) {
  props.selectOptions.forEach(option => {
    options.push({
      ...option,
      [valueField]: option[valueField],
      [labelField]: option[labelField],
    });
  });
} else if (props.labelInValue) {
  if (_isArray(props.value) && props.value.length) {
    options.push(
      ...props.value.map(valueItem => ({
        ...valueItem,
        value: valueItem[valueField],
        label: valueItem[labelField],
      })),
    );
  }
  if (_isObject(props.value) && Object.keys(props.value).length > 1) {
    options.push({ ...props.value, value: props.value[valueField], label: props.value[labelField] });
  }
} else if (props.api) {
  const params: any = {};
  if (props.multiple && _isArray(props.value) && props.value.length > 0) {
    params[`${valueField}.in`] = props.value.map(valueItem => valueItem[valueField]);
  }
  if (!props.multiple && props.value) {
    // eslint-disable-next-line vue/no-setup-props-destructure
    params[`${valueField}.equals`] = props.value;
  }
  const data = await props.api(params);
  if (data.records && data.records.length) {
    options.push(...data.records.map(record => ({ ...record, value: record[valueField], label: record[labelField] })));
  }
}

//接收选择的值
const attrs = useAttrs();
const dynamicComponent = computed(() => {
  return getComponent(props.componentName);
});
const modalVisible = ref(false);

const componentProps = computed(() => {
  const result: Record<string, unknown> = attrs || {};
  if (['ApiSelect', 'ApiTreeSelect', 'ApiTree'].includes(props.componentName)) {
    result.disabled = props.disabled;
    result.resultField = props.resultField;
    result.labelInValue = props.labelInValue;
    result.style = {
      width: '100%',
    };
    if (['ApiTreeSelect', 'ApiTree'].includes(props.componentName)) {
      result.multiple = props.multiple;
      result.checkStrictly = props.checkStrictly;
    }
    result.fieldNames = props.fieldNames;
    if (props.componentName === 'ApiTree') {
      result.defaultExpandAll = true;
      result.checkable = true;
      result.fieldNames = {
        title: props.fieldNames.label,
        key: props.fieldNames.value,
        children: props.fieldNames.children,
      };
      (result.style as any).height = '400px';
    }
    if (['ApiTree', 'ApiTreeSelect'].includes(props.componentName)) {
      if (props.api) {
        result.api = props.api;
      } else {
        result.treeData = options;
      }
      result.checkedKeys = selectData.value;
    } else {
      if (props.api) {
        result.api = props.api;
      } else {
        result.options = options;
      }
    }
  } else if (props.componentName?.endsWith('List') || props.componentName?.endsWith('Relation')) {
    result.updateType = props.updateType;
    if (props.multiple) {
      result.selectType = 'checkbox';
    } else {
      result.selectType = 'radio';
    }
    const tools: string[] = [];
    const buttons: string[] = [];
    const rowOperations = ['detail'];
    if (props.componentName?.endsWith('Relation')) {
      tools.push('add');
      buttons.push('cancelRelate');
      rowOperations.push('cancelRelate');
    }
    result.gridOptions = _merge(
      {},
      { toolbarConfig: { import: false, print: false, export: false, custom: false, tools, buttons } },
      props.gridOptions,
    );
    result.cardExtra = [];
    result.cardSlots = [];
    result.searchFormOptions = _merge({}, props.searchFormOptions);
    result.gridCustomConfig = _merge({}, { rowOperations }, props.gridCustomConfig);
    if (props.queryNames && props.rowIdField) {
      const queryParams: any = {};
      const valueData = props.valueType === 'splitString' ? JSON.parse(`[${props.value || ''}]`) : props.value;
      const valueObject = props.rowIdField.startsWith('row.') ? props.row : valueData || (props.multiple ? [] : {});
      const valueField = props.rowIdField.includes('.') ? props.rowIdField.split('.')[1] : props.rowIdField;
      props.queryNames
        .filter(item => item)
        .forEach(key => {
          if (Array.isArray(valueObject)) {
            if (props.valueType === 'splitString') {
              queryParams[key!] = valueObject;
            } else {
              queryParams[key!] = valueObject.map(valueItem => valueItem[valueField]);
            }
          } else {
            queryParams[key!] = valueObject[valueField];
          }
        });
      if (props?.column?.field) {
        result.field = props.column.field;
      }
      if (props?.source) {
        result.source = props.source;
      }
      result.query = queryParams;
    }
  } else if (props.componentName?.endsWith('Detail')) {
    result.containerType = props.container;
    const valueObject = props.rowIdField.startsWith('row.') ? props.row : props.value || {};
    const valueField = props.rowIdField.split('.')[1];
    result.entityId = valueObject[valueField];
  }
  return result;
});

const componentEvents = computed(() => {
  const result: any = {};
  if (props.componentName === 'ApiTree') {
    result.check = ({ checked }) => {
      selectData.value = checked;
      selectData.change = true;
    };
  } else if (props.componentName?.endsWith('List')) {
  } else if (props.componentName?.endsWith('Desc')) {
  }
  return result;
});

const showComponentProps = computed(() => {
  const result: any = { size: 'small' };
  if (['Avatar'].includes(props.showComponentName)) {
    result.shape = 'square';
    if (props.avatarSlotName === 'icon') {
      result.shape = 'circle';
    }
  }
  result.maxCount = 3;
  return result;
});

const avatarGroupData = computed(() => {
  const data = props.value;
  if (_isArray(data)) {
    return data;
  } else {
    return data ? [data] : [];
  }
});

/**
 * 打开弹出框
 */
function openModal() {
  if (props.container === 'modal') {
    setModalProps({
      open: true,
    });
  } else {
    setDrawerProps({
      open: true,
    });
  }
}

/**
 * 下拉框值改变事件
 */
function handleChange(value) {
  selectData.value = value;
  selectData.change = true;
  emit('change', value);
  emit('update:value', value);
}

function handleOk() {
  if (props.updateType === 'emitSelected') {
    let selectRecords: any[] = [];
    if (props.componentName === 'ApiTree') {
      if (props.labelInValue) {
        const data: any[] = (selectData.value as Array<any>).map(key => {
          return {
            [valueField]: key,
          };
        });
        selectRecords.push(...data);
      }
    } else {
      if (tableRef.value) {
        // selectRecords = tableRef.value.getCheckboxRecords() || [];
        // selectRecords = tableRef.value.getData() || [];
        selectRecords = tableRef.value.getSelectRows() || [];
      }
    }
    if (props.valueType === 'splitString') {
      const valueField = props.rowIdField.includes('.') ? props.rowIdField.split('.')[1] : props.rowIdField;
      let joinData = selectRecords.map(record => record[valueField]).join(',');
      emit('change', joinData);
      emit('update:value', joinData);
    } else {
      emit('change', selectRecords);
      emit('update:value', selectRecords);
    }
  }
  if (props.container === 'modal') {
    closeModal();
  } else {
    closeDrawer();
  }
}
function visibleChange(_visible) {}

function clear() {
  if (props.updateType === 'emitSelected') {
    if (props.valueType === 'splitString') {
      emit('change', '');
      emit('update:value', '');
    } else {
      if (props.multiple) {
        emit('change', []);
        emit('update:value', []);
      } else {
        emit('change', null);
        emit('update:value', null);
      }
    }
  }
}
</script>
<style scoped>
.select-row .left {
  width: calc(10%);
}
.select-row .right {
  width: 82px;
}
.select-row .full {
  width: 100%;
}
.select-row :deep(.ant-select-search__field) {
  display: none !important;
}
</style>
