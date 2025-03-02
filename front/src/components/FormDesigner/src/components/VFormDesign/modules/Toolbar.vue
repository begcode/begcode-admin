<template>
  <div class="operating-area">
    <!-- 头部操作按钮区域 start -->
    <!-- 操作左侧区域 start -->
    <div class="left-btn-box">
      <a-tooltip v-for="item in toolbarsConfigs" :title="item.title" :key="item.icon">
        <a @click="$emit(item.event)" class="toolbar-text">
          <Icon :icon="item.icon" />
        </a>
      </a-tooltip>
      <a-divider type="vertical" />
      <a-tooltip title="撤销">
        <a :class="{ disabled: !canUndo }" :disabled="!canUndo" @click="undo">
          <Icon icon="ant-design:undo-outlined" />
        </a>
      </a-tooltip>
      <a-tooltip title="重做">
        <a :class="{ disabled: !canRedo }" :disabled="!canRedo" @click="redo">
          <Icon icon="ant-design:redo-outlined" />
        </a>
      </a-tooltip>
      <a-divider type="vertical" />
      <a-tooltip title="保存">
        <a @click="$emit('save')">
          <Icon icon="ant-design:save-outlined" />
        </a>
      </a-tooltip>
      <a-tooltip title="关闭">
        <a @click="$emit('close')">
          <Icon icon="ant-design:close-circle-outlined" />
        </a>
      </a-tooltip>
    </div>
  </div>
  <!-- 操作区域 start -->
</template>
<script lang="ts">
import { UseRefHistoryReturn } from '@vueuse/core';
import { IFormConfig } from '../../../typings/v-form-component';

interface IToolbarsConfig {
  type: string;
  title: string;
  icon: string;
  event: string;
}

export default defineComponent({
  name: 'OperatingArea',
  components: {
    Icon,
  },
  setup() {
    const state = reactive<{
      toolbarsConfigs: IToolbarsConfig[];
    }>({
      toolbarsConfigs: [
        {
          title: '预览-支持布局',
          type: 'preview',
          event: 'handlePreview',
          icon: 'ant-design:chrome-filled',
        },
        {
          title: '预览-不支持布局',
          type: 'preview',
          event: 'handlePreview2',
          icon: 'ant-design:chrome-filled',
        },
        {
          title: '导入JSON',
          type: 'importJson',
          event: 'handleOpenImportJsonModal',
          icon: 'ant-design:import-outlined',
        },
        {
          title: '生成JSON',
          type: 'exportJson',
          event: 'handleOpenJsonModal',
          icon: 'ant-design:export-outlined',
        },
        {
          title: '生成代码',
          type: 'exportCode',
          event: 'handleOpenCodeModal',
          icon: 'ant-design:code-filled',
        },
        {
          title: '清空',
          type: 'reset',
          event: 'handleClearFormItems',
          icon: 'ant-design:clear-outlined',
        },
      ],
    });
    const historyRef = inject('historyReturn') as UseRefHistoryReturn<IFormConfig, IFormConfig>;

    const { undo, redo, canUndo, canRedo } = historyRef;
    return { ...toRefs(state), undo, redo, canUndo, canRedo };
  },
});
</script>
<style lang="less" scoped>
@import url('../styles/variable.less');

.operating-area {
  display: flex;
  place-content: center space-between;
  height: @operating-area-height;
  padding: 0 12px;
  padding-left: 30px;
  border-bottom: 2px solid @border-color;
  font-size: 16px;
  line-height: @operating-area-height;
  text-align: left;

  a {
    margin: 0 5px;
    color: #666;

    &.disabled,
    &.disabled:hover {
      color: #ccc;
    }

    &:hover {
      color: @primary-color;
    }

    > span {
      padding-left: 2px;
      font-size: 14px;
    }
  }
}
</style>
