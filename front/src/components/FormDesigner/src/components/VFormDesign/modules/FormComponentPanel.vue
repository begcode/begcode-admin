<template>
  <div class="form-panel v-form-container">
    <a-empty class="empty-text" v-show="formConfig.schemas.length === 0" description="从左侧选择控件添加" />
    <a-form v-bind="formConfig">
      <div class="draggable-box">
        <draggable
          :class="['list-main', 'ant-row']"
          group="form-draggable"
          tag="div"
          ghostClass="moving"
          :animation="180"
          handle=".drag-move"
          v-model="formConfig.schemas"
          item-key="key"
          @add="addItem"
          @start="handleDragStart"
        >
          <transition-group type="transition" name="flip-list">
            <LayoutItem
              v-for="element in formConfig.schemas"
              class="drag-move"
              :schema="element"
              :data="formConfig"
              :current-item="formConfig.currentItem || {}"
              :key="element.key"
            />
          </transition-group>
        </draggable>
      </div>
    </a-form>
  </div>
</template>
<script lang="ts">
import { VueDraggableNext } from 'vue-draggable-next';
import LayoutItem from '../components/LayoutItem.vue';
import { useFormDesignState } from '../../../hooks/useFormDesignState';

export default defineComponent({
  name: 'FormComponentPanel',
  components: {
    LayoutItem,
    draggable: VueDraggableNext,
  },
  emits: ['handleSetSelectItem'],
  setup(_, { emit }) {
    const { formConfig } = useFormDesignState();

    /**
     * 拖拽完成事件
     * @param newIndex
     */
    const addItem = ({ newIndex }: any) => {
      formConfig.value.schemas = formConfig.value.schemas || [];

      const schemas = formConfig.value.schemas;
      schemas[newIndex] = _cloneDeep(schemas[newIndex]);
      emit('handleSetSelectItem', schemas[newIndex]);
    };

    /**
     * 拖拽开始事件
     * @param e {Object} 事件对象
     */
    const handleDragStart = (e: any) => {
      emit('handleSetSelectItem', formConfig.value.schemas[e.oldIndex]);
    };

    // 获取祖先组件传递的currentItem

    // 计算布局元素，水平模式下为ACol，非水平模式下为div
    const layoutTag = computed(() => {
      return formConfig.value.layout === 'horizontal' ? 'Col' : 'div';
    });

    return {
      addItem,
      handleDragStart,
      formConfig,
      layoutTag,
    };
  },
});
</script>

<style lang="less" scoped>
@import url('../styles/variable.less');
@import url('../styles/drag.less');

.v-form-container {
  // 内联布局样式
  .ant-form-inline {
    .list-main {
      display: flex;
      flex-wrap: wrap;
      place-content: flex-start flex-start;

      .layout-width {
        width: 100%;
      }
    }

    .ant-form-item-control-wrapper {
      min-width: 175px !important;
    }
  }
}

.form-panel {
  position: relative;
  height: 100%;

  .empty-text {
    position: absolute;
    z-index: 100;
    inset: -10% 0 0;
    height: 150px;
    margin: auto;
    color: #aaa;
  }

  .draggable-box {
    height: calc(100vh - 120px);
    // width: 100%;
    overflow: auto;

    .drag-move {
      min-height: 62px;
      cursor: move;
    }

    .list-main {
      // 列表动画
      .list-enter-active {
        transition: all 0.5s;
      }

      .list-leave-active {
        transition: all 0.3s;
      }

      .list-enter,
      .list-leave-to {
        transform: translateX(-100px);
        opacity: 0;
      }

      .list-enter {
        height: 30px;
      }
    }
  }
}
</style>
