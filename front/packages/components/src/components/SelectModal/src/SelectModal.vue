<template>
  <div>
    <Row class="select-row" type="flex" :gutter="8">
      <Col class="left" :class="{ full: true }">
        <!-- 显示加载效果 -->
        <Input v-if="loading" readOnly placeholder="加载中…">
          <template #prefix>
            <LoadingOutlined />
          </template>
        </Input>
        <Select
          v-if="false"
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
        <Button v-else-if="showComponentName === 'Button'" :pre-icon="buttonIcon" @click="!disabled && openModal()">{{
          modalTitle
        }}</Button>
        <div class="flex items-center justify-between">
          <AvatarGroup v-bind="showComponentProps" v-if="avatarGroupData.length">
            <Tooltip :title="item[avatarSlotField]" placement="top" v-for="item in avatarGroupData">
              <Avatar :src="avatarSlotName === 'src' ? item[avatarSlotField] : undefined">
                <template #icon v-if="avatarSlotName === 'icon' && item[avatarSlotField]"><Icon :icon="item[avatarSlotField]" /></template>
                <span v-if="avatarSlotName === 'default'">{{ (avatarSlotField && item[avatarSlotField]) || item }}</span>
              </Avatar>
            </Tooltip>
          </AvatarGroup>
          <Button v-if="disabled" pre-icon="ant-design:file-search-outlined" type="link" @click.stop="disabled && openModal()"></Button>
          <Button v-else pre-icon="ant-design:edit-outlined" type="link" @click.stop="!disabled && openModal()"></Button>
        </div>
      </Col>
    </Row>
    <BasicModal
      :title="modalTitle"
      :mask-closable="false"
      @register="register"
      @ok="handleOk"
      @visible-change="visibleChange"
      width="900px"
      destroy-on-close
      wrap-class-name="vxe-table--ignore-clear"
    >
      <FormItemRest>
        <component v-bind="componentProps" v-on="componentEvents" :is="dynamicComponent" ref="tableModalRef" />
      </FormItemRest>
    </BasicModal>
    <BasicDrawer
      :title="modalTitle"
      :mask-closable="false"
      :showFooter="!disabled"
      @register="registerDrawer"
      @ok="handleOk"
      @visible-change="visibleChange"
      width="450px"
      destroy-on-close
      root-class-name="vxe-table--ignore-clear"
    >
      <FormItemRest>
        <component v-bind="componentProps" v-on="componentEvents" :is="dynamicComponent" ref="tableDrawerRef" />
      </FormItemRest>
    </BasicDrawer>
  </div>
</template>
<script lang="ts">
import { defineComponent, ref, reactive, computed, inject } from 'vue';
import type { PropType } from 'vue';
import { Row, Col, Input, Select, Avatar, Tooltip, AvatarGroup, FormItemRest } from 'ant-design-vue';
import { LoadingOutlined, MehOutlined } from '@ant-design/icons-vue';
import type { SelectValue } from 'ant-design-vue/es/select';
import type { VxeGridInstance } from 'vxe-table/types/grid';
import { isArray, isObject, merge } from 'lodash-es';
import { propTypes } from '@/utils/propTypes';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { BasicModal, useModalInner } from '@/components/Modal';
import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
import { ApiSelect, ApiTreeSelect, ApiCascader, ApiTree } from '@/components/Form';
import { Icon } from '@/components/Icon';
import { Button } from '@/components/Button';

export default defineComponent({
  name: 'SelectModal',
  components: {
    BasicModal,
    LoadingOutlined,
    MehOutlined,
    BasicDrawer,
    Row,
    Col,
    Input,
    Select,
    Button,
    Icon,
    Avatar,
    Tooltip,
    AvatarGroup,
    FormItemRest,
  },
  inheritAttrs: false,
  props: {
    value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
    disabled: propTypes.bool.def(false),
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
    loading: propTypes.bool.def(false),
    // 最多显示多少个 tag
    maxTagCount: propTypes.number,
    // buttonIcon
    buttonIcon: propTypes.string.def(''),
    modalTitle: {
      type: String,
      default: '请选择',
    },
    showComponentName: propTypes.string.def('Select'),
    componentName: propTypes.string.def(''),
    api: propTypes.func,
    resultField: propTypes.string.def('records'),
    fieldNames: propTypes.object.def({
      label: 'label',
      value: 'value',
      children: 'children',
    }),
    labelInValue: propTypes.bool.def(false),
    checkStrictly: propTypes.bool.def(false),
    selectOptions: propTypes.arrayOf(propTypes.any).def([]),
    container: propTypes.string.def('modal'),
    avatarSlotName: propTypes.string.def('default'), // default icon src
    avatarSlotField: propTypes.string.def(''),
    avatarTipField: propTypes.string.def(''),
    queryNames: propTypes.arrayOf(propTypes.string).def([]),
    xGrid: { type: Object as PropType<VxeGridInstance> },
    row: { type: Object, default: null },
    column: { type: Object, default: {} },
    rowIdField: { type: String, default: '' },
    source: { type: String, default: '' },
    gridCustomConfig: propTypes.object.def({}),
    searchFormOptions: propTypes.object.def({}),
    gridOptions: propTypes.object.def({}),
    updateType: propTypes.string.def('remoteApi'), // default icon src
    valueType: propTypes.string.def(''), // array | object | splitString
  },
  emits: ['handleOpen', 'change', 'register', 'update:value'],
  async setup(props, { emit }) {
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
      if (isArray(props.value) && props.value.length) {
        options.push(
          ...props.value.map(valueItem => ({
            ...valueItem,
            value: valueItem[valueField],
            label: valueItem[labelField],
          })),
        );
      }
      if (isObject(props.value) && Object.keys(props.value).length > 1) {
        options.push({ ...props.value, value: props.value[valueField], label: props.value[labelField] });
      }
    } else if (props.api) {
      const params: any = {};
      if (props.multiple && isArray(props.value) && props.value.length > 0) {
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
        const tools: string[] = [];
        const buttons: string[] = [];
        const rowOperations = ['detail'];
        if (props.componentName?.endsWith('Relation')) {
          tools.push('add');
          buttons.push('cancelRelate');
          rowOperations.push('cancelRelate');
        }
        result.gridOptions = merge(
          {},
          { toolbarConfig: { import: false, print: false, export: false, custom: false, tools, buttons } },
          props.gridOptions,
        );
        result.cardExtra = [];
        result.searchFormOptions = merge({}, props.searchFormOptions);
        result.gridCustomConfig = merge({}, { rowOperations }, props.gridCustomConfig);
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
      if (isArray(data)) {
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
            selectRecords = tableRef.value.getData() || [];
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

    return {
      attrs,
      options,
      handleChange,
      openModal,
      register,
      setModalProps,
      closeModal,
      registerDrawer,
      setDrawerProps,
      closeDrawer,
      handleOk,
      visibleChange,
      dynamicComponent,
      selectData,
      modalVisible,
      tableRef,
      tableDrawerRef,
      tableModalRef,
      componentProps,
      componentEvents,
      showComponentProps,
      avatarGroupData,
      isObject,
    };
  },
});
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
