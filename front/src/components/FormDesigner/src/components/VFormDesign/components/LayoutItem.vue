<template>
  <a-col v-bind="colPropsComputed">
    <template v-if="['Grid'].includes(schema.component)">
      <div class="grid-box" :class="{ active: schema.key === currentItem.key }" @click.stop="handleSetSelectItem(schema)">
        <a-row class="grid-row" v-bind="schema.componentProps">
          <a-col class="grid-col" v-for="(colItem, index) in schema.columns" :key="index" :span="colItem.span">
            <draggable
              class="list-main draggable-box"
              v-bind="{
                group: 'form-draggable',
                ghostClass: 'moving',
                animation: 180,
                handle: '.drag-move',
              }"
              item-key="key"
              v-model="colItem.children"
              @start="$emit('dragStart', $event, colItem.children)"
              @add="$emit('handleColAdd', $event, colItem.children)"
            >
              <LayoutItem
                class="drag-move"
                v-for="element in colItem.children"
                :schema="element"
                :current-item="currentItem"
                @handle-copy="$emit('handle-copy')"
                @handle-delete="$emit('handle-delete')"
              />
            </draggable>
          </a-col>
        </a-row>
        <FormNodeOperate :schema="schema" :currentItem="currentItem" />
      </div>
    </template>
    <FormNode
      v-else
      :key="schema.key"
      :schema="schema"
      :current-item="currentItem"
      @handle-copy="$emit('handle-copy')"
      @handle-delete="$emit('handle-delete')"
    />
  </a-col>
</template>
<script lang="ts">
import { VueDraggableNext } from 'vue-draggable-next';
import FormNode from './FormNode.vue';
import FormNodeOperate from './FormNodeOperate.vue';
import { useFormDesignState } from '../../../hooks/useFormDesignState';
import { IVFormComponent } from '../../../typings/v-form-component';

export default defineComponent({
  name: 'LayoutItem',
  components: {
    FormNode,
    FormNodeOperate,
    draggable: VueDraggableNext,
  },
  props: {
    schema: {
      type: Object as PropType<IVFormComponent>,
      required: true,
    },
    currentItem: {
      type: Object,
      required: true,
    },
  },
  emits: ['dragStart', 'handleColAdd', 'handle-copy', 'handle-delete'],
  setup(props) {
    const {
      formDesignMethods: { handleSetSelectItem },
      formConfig,
    } = useFormDesignState();
    const state = reactive({});
    const colPropsComputed = computed(() => {
      const { colProps = {} } = props.schema;
      return colProps;
    });

    const list1 = computed(() => props.schema.columns);

    // 计算布局元素，水平模式下为ACol，非水平模式下为div
    const layoutTag = computed(() => {
      return formConfig.value.layout === 'horizontal' ? 'Col' : 'div';
    });

    return {
      ...toRefs(state),
      colPropsComputed,
      handleSetSelectItem,
      layoutTag,
      list1,
    };
  },
});
</script>
<style lang="less">
@import url('../styles/variable.less');

.layout-width {
  width: 100%;
}

.hidden-item {
  background-color: rgb(240 191 195);
}
</style>
