<!--
 * @Description: 中间表单布局面板
 *
-->
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

<style lang="css">
.draggable-box {
  height: 100%;
  overflow: auto;
}
.draggable-box .list-main {
  position: relative;
  padding: 5px;
  overflow: hidden;
}
.draggable-box .list-main .moving {
  position: relative;
  box-sizing: border-box;
  min-height: 35px;
  padding: 0 !important;
  overflow: hidden;
}
.draggable-box .list-main .moving::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 5px;
  background-color: #13c2c2;
}
.draggable-box .list-main .drag-move-box {
  position: relative;
  box-sizing: border-box;
  min-height: 60px;
  padding: 8px;
  overflow: hidden;
  transition: all 0.3s;
}
.draggable-box .list-main .drag-move-box:hover {
  background-color: rgba(19, 194, 194, 0.2);
}
.draggable-box .list-main .drag-move-box::before {
  content: '';
  position: absolute;
  top: 0;
  right: -100%;
  width: 100%;
  height: 5px;
  transition: all 0.3s;
  background-color: #13c2c2;
}
.draggable-box .list-main .drag-move-box.active {
  outline-offset: 0;
  background-color: rgba(19, 194, 194, 0.2);
}
.draggable-box .list-main .drag-move-box.active::before {
  right: 0;
}
.draggable-box .list-main .drag-move-box .form-item-box {
  position: relative;
  box-sizing: border-box;
  word-wrap: break-word;
}
.draggable-box .list-main .drag-move-box .form-item-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.draggable-box .list-main .drag-move-box .form-item-box .ant-form-item {
  margin: 0;
  padding-bottom: 6px;
}
.draggable-box .list-main .drag-move-box .show-key-box {
  position: absolute;
  right: 5px;
  bottom: 2px;
  color: #13c2c2;
  font-size: 14px;
}
.draggable-box .list-main .drag-move-box .copy,
.draggable-box .list-main .drag-move-box .delete {
  position: absolute;
  top: 0;
  width: 30px;
  height: 30px;
  transition: all 0.3s;
  color: #fff;
  line-height: 30px;
  text-align: center;
}
.draggable-box .list-main .drag-move-box .copy.unactivated,
.draggable-box .list-main .drag-move-box .delete.unactivated {
  opacity: 0 !important;
  pointer-events: none;
}
.draggable-box .list-main .drag-move-box .copy.active,
.draggable-box .list-main .drag-move-box .delete.active {
  opacity: 1 !important;
}
.draggable-box .list-main .drag-move-box .copy {
  right: 30px;
  border-radius: 0 0 0 8px;
  background-color: #13c2c2;
}
.draggable-box .list-main .drag-move-box .delete {
  right: 0;
  background-color: #13c2c2;
}
.draggable-box .list-main .grid-box {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  padding: 5px;
  overflow: hidden;
  transition: all 0.3s;
  background-color: rgba(152, 103, 247, 0.12);
}
.draggable-box .list-main .grid-box .form-item-box {
  position: relative;
  box-sizing: border-box;
}
.draggable-box .list-main .grid-box .form-item-box .ant-form-item {
  margin: 0;
  padding-bottom: 15px;
}
.draggable-box .list-main .grid-box .grid-row {
  background-color: rgba(152, 103, 247, 0.12);
}
.draggable-box .list-main .grid-box .grid-row .grid-col .draggable-box {
  min-width: 50px;
  min-height: 80px;
  border: 1px #ccc dashed;
}
.draggable-box .list-main .grid-box .grid-row .grid-col .draggable-box .list-main {
  position: relative;
  min-height: 83px;
  border: 1px #ccc dashed;
}
.draggable-box .list-main .grid-box::before {
  content: '';
  position: absolute;
  top: 0;
  right: -100%;
  width: 100%;
  height: 5px;
  transition: all 0.3s;
  background: transparent;
}
.draggable-box .list-main .grid-box.active {
  outline-offset: 0;
  background-color: rgba(152, 103, 247, 0.24);
}
.draggable-box .list-main .grid-box.active::before {
  right: 0;
  background-color: #9867f7;
}
.draggable-box .list-main .grid-box > .copy-delete-box > .copy,
.draggable-box .list-main .grid-box > .copy-delete-box > .delete {
  position: absolute;
  top: 0;
  width: 30px;
  height: 30px;
  transition: all 0.3s;
  color: #fff;
  line-height: 30px;
  text-align: center;
}
.draggable-box .list-main .grid-box > .copy-delete-box > .copy.unactivated,
.draggable-box .list-main .grid-box > .copy-delete-box > .delete.unactivated {
  opacity: 0 !important;
  pointer-events: none;
}
.draggable-box .list-main .grid-box > .copy-delete-box > .copy.active,
.draggable-box .list-main .grid-box > .copy-delete-box > .delete.active {
  opacity: 1 !important;
}
.draggable-box .list-main .grid-box > .copy-delete-box > .copy {
  right: 30px;
  border-radius: 0 0 0 8px;
  background-color: #9867f7;
}
.draggable-box .list-main .grid-box > .copy-delete-box > .delete {
  right: 0;
  background-color: #9867f7;
}
.v-form-container .ant-form-inline .list-main {
  display: flex;
  flex-wrap: wrap;
  place-content: flex-start flex-start;
}
.v-form-container .ant-form-inline .list-main .layout-width {
  width: 100%;
}
.v-form-container .ant-form-inline .ant-form-item-control-wrapper {
  min-width: 175px !important;
}
.form-panel {
  position: relative;
  height: 100%;
}
.form-panel .empty-text {
  position: absolute;
  z-index: 100;
  inset: -10% 0 0;
  height: 150px;
  margin: auto;
  color: #aaa;
}
.form-panel .draggable-box {
  height: calc(100vh - 120px);
  overflow: auto;
}
.form-panel .draggable-box .drag-move {
  min-height: 62px;
  cursor: move;
}
.form-panel .draggable-box .list-main .list-enter-active {
  transition: all 0.5s;
}
.form-panel .draggable-box .list-main .list-leave-active {
  transition: all 0.3s;
}
.form-panel .draggable-box .list-main .list-enter,
.form-panel .draggable-box .list-main .list-leave-to {
  transform: translateX(-100px);
  opacity: 0;
}
.form-panel .draggable-box .list-main .list-enter {
  height: 30px;
}
</style>
