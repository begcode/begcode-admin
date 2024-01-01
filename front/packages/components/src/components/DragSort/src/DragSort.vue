<template>
  <div class="j-vxe-drag-box" style="display: flex; align-items: center; justify-content: space-between">
    <span class="drag-btn" style="cursor: move"> <Icon icon="ant-design:holder-outlined" /></span>
    <Popover placement="topLeft" trigger="click" :open="open">
      <template #content>
        <Row justify="space-around" align="middle">
          <Col :span="14"><InputNumber v-model:value="inputValue" style="display: inline-block"></InputNumber></Col>
          <Col :span="10">
            <Button type="primary" size="small" style="display: inline-block; margin-left: 10px" @click="closePopover(true)">确定</Button>
            <Button size="small" style="display: inline-block; margin-left: 10px" @click="closePopover(false)">取消</Button>
          </Col>
        </Row>
      </template>
      <template #title>
        <span>编辑排序值</span>
      </template>
      <Input :value="inputValue" :bordered="false" :disabled="true" style="text-align: center">
        <template #addonAfter v-if="!disabled">
          <Icon icon="ant-design:edit-outlined" @click="openPopover" />
        </template>
      </Input>
    </Popover>
    <span>
      <Dropdown :trigger="['click']">
        <span><Icon icon="mi:drag" /></span>
        <template #overlay>
          <Menu>
            <MenuItem key="0" :disabled="disabledMoveUp" @click="handleRowMoveUp">向上移</MenuItem>
            <MenuItem key="1" :disabled="disabledMoveDown" @click="handleRowMoveDown">向下移</MenuItem>
            <MenuDivider v-if="!disabled" />
            <MenuItem key="3" @click="handleRowInsertDown" v-if="!disabled">插入一行</MenuItem>
          </Menu>
        </template>
      </Dropdown>
    </span>
  </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, onUnmounted, ref } from 'vue';
import { Dropdown, Menu, MenuItem, MenuDivider, InputNumber, Popover, Button, Row, Col, Input } from 'ant-design-vue';
import Icon from '@/components/Icon/Icon.vue';
import Sortable from 'sortablejs';

defineOptions({
  name: 'DragSort',
  inheritAttrs: false,
});

const props = defineProps({
  type: {
    type: String,
    default: 'default',
  },
  params: {
    type: Object,
    default: () => ({}),
  },
  value: {
    type: Number,
    default: 0,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  remoteApi: {
    type: Function,
    default: null,
  },
});

const emit = defineEmits(['update:value']);

const inputValue = ref(props.value);

const open = ref(false);

let sortable2: Sortable;
let initTime: any;

const rowIndex = computed(() => props.params._rowIndex);
const fullDataLength = computed(() => props.params.$table.internalData.tableFullData.length);
const disabledMoveUp = computed(() => rowIndex.value === 0);
const disabledMoveDown = computed(() => rowIndex.value === fullDataLength.value - 1);

const openPopover = () => {
  open.value = true;
};
const closePopover = (update: boolean) => {
  if (update && inputValue.value !== props.value) {
    emit('update:value', props.value);
    if (props.remoteApi) {
      // todo remoteApi处理排序值
    } else {
      // Todo 表格重新排序
    }
  }
  open.value = false;
};

const trigger = (method, params) => {
  console.log('trigger', method, params);
};

/** 重新计算排序字段的数值 */
async function recalcSortNumber() {
  let xTable = props.params.$table;
  let sortKey = props.params.column.field;
  let sortBegin = 1;
  xTable.internalData.tableFullData.forEach((data: any) => (data[sortKey] = sortBegin++));
  await xTable.cacheRowMap();
  return await xTable.updateData();
}

/**
 * 排序表格
 * @param oldIndex
 * @param newIndex
 * @param force 强制排序
 */
async function doSort(oldIndex: number, newIndex: number, force = false) {
  let xTable = props.params.$table;
  let sort = array => {
    // 存储old数据，并删除该项
    let row = array.splice(oldIndex, 1)[0];
    // 向newIndex处添加old数据
    array.splice(newIndex, 0, row);
  };
  sort(xTable.internalData.tableFullData);
  if (xTable.keepSource) {
    sort(xTable.internalData.tableSourceData);
  }
  if (props.remoteApi) {
    // todo remoteApi处理排序值
  } else {
    return await recalcSortNumber();
  }
}

/** 行重新排序 */
function rowResort(oldIndex: number, newIndex: number) {
  return doSort(oldIndex, newIndex, true);
}

/** 向上移 */
function handleRowMoveUp() {
  if (!disabledMoveUp.value) {
    rowResort(rowIndex.value, rowIndex.value - 1);
  }
}

/** 向下移 */
function handleRowMoveDown() {
  if (!disabledMoveDown.value) {
    rowResort(rowIndex.value, rowIndex.value + 1);
  }
}

/** 插入一行 */
function handleRowInsertDown() {
  trigger('rowInsertDown', rowIndex.value);
}

function createSortable() {
  let xTable = props.params.$table;
  let dom = props.params.$grid.getRefMaps().refTable.value.$el.querySelector('.body--wrapper>.vxe-table--body tbody');
  let startChildren = [];
  sortable2 = Sortable.create(dom as HTMLElement, {
    handle: '.drag-btn',
    direction: 'vertical',
    animation: 300,
    onStart(e) {
      let from = e.from;
      // @ts-ignore
      startChildren = [...from.children];
    },
    onEnd(e) {
      let oldIndex = e.oldIndex as number;
      let newIndex = e.newIndex as number;
      if (oldIndex === newIndex) {
        return;
      }
      let rowNode = xTable.getRowNode(e.item);
      if (!rowNode) {
        return;
      }
      let from = e.from;
      let element = startChildren[oldIndex];
      let target = null;
      if (oldIndex > newIndex) {
        // 向上移动
        if (oldIndex + 1 < startChildren.length) {
          target = startChildren[oldIndex + 1];
        }
      } else {
        // 向下移动
        target = startChildren[oldIndex + 1];
      }
      from.removeChild(element);
      from.insertBefore(element, target);
      nextTick(() => {
        const diffIndex = rowNode!.index - oldIndex;
        if (diffIndex > 0) {
          oldIndex = oldIndex + diffIndex;
          newIndex = newIndex + diffIndex;
        }
        if (props.remoteApi) {
          // todo remoteApi处理排序值
        } else {
          recalcSortNumber();
        }
      });
    },
  });
}
onMounted(() => {
  // 加载完成之后再绑定拖动事件
  initTime = setTimeout(createSortable, 300);
});

onUnmounted(() => {
  clearTimeout(initTime);
  if (sortable2) {
    sortable2.destroy();
  }
});
</script>
