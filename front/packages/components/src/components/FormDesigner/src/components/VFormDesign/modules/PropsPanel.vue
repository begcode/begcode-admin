<!--
 * @Description: 右侧属性配置面板
-->
<template>
  <div>
    <Tabs v-model:activeKey="formConfig.activeKey" :tabBarStyle="{ margin: 0 }">
      <TabPane :key="1" tab="表单">
        <FormProps />
      </TabPane>
      <TabPane :key="2" tab="控件">
        <FormItemProps />
      </TabPane>
      <TabPane :key="3" tab="栅格">
        <ComponentColumnProps />
      </TabPane>
      <TabPane :key="4" tab="组件">
        <slot v-if="slotProps" :name="slotProps.component + 'Props'"></slot>
        <ComponentProps v-else />
      </TabPane>
    </Tabs>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent } from 'vue';
import FormProps from '../components/FormProps.vue';
import FormItemProps from '../components/FormItemProps.vue';
import ComponentProps from '../components/ComponentProps.vue';
import ComponentColumnProps from '../components/FormItemColumnProps.vue';
import { useFormDesignState } from '../../../hooks/useFormDesignState';
import { customComponents } from '../../../core/formItemConfig';
import { TabPane, Tabs } from 'ant-design-vue';

type ChangeTabKey = 1 | 2;
export interface IPropsPanel {
  changeTab: (key: ChangeTabKey) => void;
}
export default defineComponent({
  name: 'PropsPanel',
  components: {
    FormProps,
    FormItemProps,
    ComponentProps,
    ComponentColumnProps,
    Tabs,
    TabPane,
  },
  setup() {
    const { formConfig } = useFormDesignState();
    const slotProps = computed(() => {
      return customComponents.find(item => item.component === formConfig.value.currentItem?.component);
    });
    return { formConfig, customComponents, slotProps };
  },
});
</script>

<style lang="css" scoped>
:deep(.ant-tabs) {
  box-sizing: border-box;
}
:deep(.ant-tabs) form {
  width: 100%;
  height: 85vh;
  margin-right: 10px;
  overflow: hidden auto;
}
:deep(.ant-tabs) .hint-box {
  margin-top: 200px;
}
:deep(.ant-tabs) .ant-form-item,
:deep(.ant-tabs) .ant-slider-with-marks {
  margin-right: 20px;
  margin-bottom: 0;
  margin-left: 10px;
}
:deep(.ant-tabs) .ant-form-item {
  margin-bottom: 0;
}
:deep(.ant-tabs) .ant-form-item .ant-form-item-label {
  line-height: 2;
  vertical-align: text-top;
}
:deep(.ant-tabs) .ant-input-number {
  width: 100%;
}
</style>
