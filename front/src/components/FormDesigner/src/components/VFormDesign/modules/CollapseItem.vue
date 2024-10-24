<template>
  <div :class="prefixCls">
    <draggable
      tag="ul"
      :list="list"
      v-bind="{
        group: { name: 'form-draggable', pull: 'clone', put: false },
        sort: false,
        clone: cloneItem,
        animation: 180,
        ghostClass: 'moving',
      }"
      item-key="type"
      @start="handleStart($event, list)"
      @add="handleAdd"
    >
      <li
        class="bs-box text-ellipsis"
        @dragstart="$emit('add-attrs', list)"
        @click="$emit('handle-list-push', element)"
        v-for="element in list"
        :key="element.field"
      >
        <Icon :icon="element.icon" />
        {{ element.label }}
      </li>
    </draggable>
  </div>
</template>
<script lang="ts">
import { IVFormComponent } from '../../../typings/v-form-component';
import { VueDraggableNext } from 'vue-draggable-next';
import { useDesign } from '@/hooks/web/useDesign';

export default defineComponent({
  name: 'CollapseItem',
  components: { draggable: VueDraggableNext, Icon },
  props: {
    list: {
      type: [Array] as PropType<IVFormComponent[]>,
      default: () => [],
    },
    handleListPush: {
      type: Function,
      default: null,
    },
  },
  setup(props, { emit }) {
    const { prefixCls } = useDesign('form-design-collapse-item');

    const state = reactive({});
    const handleStart = (e: any, list1: IVFormComponent[]) => {
      emit('start', list1[e.oldIndex].component);
    };
    const handleAdd = (e: any) => {
      console.log(e);
    };
    // https://github.com/SortableJS/vue.draggable.next
    // https://github.com/SortableJS/vue.draggable.next/blob/master/example/components/custom-clone.vue
    const cloneItem = one => {
      return props.handleListPush(one);
    };
    return { prefixCls, state, handleStart, handleAdd, cloneItem };
  },
});
</script>
<style lang="css" scoped>
.vben-form-design-collapse-item ul {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 0;
  padding: 5px;
  list-style: none;
}
.vben-form-design-collapse-item ul li {
  width: calc(50% - 6px);
  height: 36px;
  margin: 2.7px;
  padding: 8px 12px;
  transition: all 0.3s;
  border: 1px solid #ccc;
  border-radius: 3px;
  line-height: 20px;
  cursor: move;
}
.vben-form-design-collapse-item ul li:hover {
  position: relative;
  border: 1px solid #13c2c2;
  box-shadow: 0 2px 6px #13c2c2;
  color: #13c2c2;
}
.vben-form-design-collapse-item svg {
  display: inline !important;
}
</style>
