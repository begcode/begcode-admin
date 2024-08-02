<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <RegionCodeDescriptions ref="regionCodeDescriptionsRef" v-bind="descriptionProps"> </RegionCodeDescriptions>
    </template>
    <template #rightFooter>
      <Space>
        <Button v-for="operation in operations" :type="operation.type" @click="operation.click">
          <Icon icon="operation.icon" v-if="operation.icon" />{{ operation.title }}</Button
        >
      </Space>
    </template>
  </PageWrapper>
</template>
<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { Space, Button } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import { PageWrapper, Icon } from '@begcode/components';
import { useMultipleTabStore } from '@/store/modules/multipleTab';
import RegionCodeDescriptions from './components/detail-component.vue';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

defineOptions({
  name: 'SystemRegionCodeDetail',
  inheritAttrs: false,
});

const props = defineProps({
  entityId: {
    type: [String, Number] as PropType<string | number>,
    default: 0,
  },
});

const route = useRoute();
const router = useRouter();
const tabStore = useMultipleTabStore();

const regionCodeDescriptionsRef = ref(null);
const descriptionProps = reactive({
  entityId: (route.params?.entityId || props.entityId || '') as string,
});

const pageProps = reactive({
  title: '详情',
});

const operations = ref<any>([
  {
    title: '关闭',
    name: 'close',
    type: 'default',
    icon: '',
    click: async () => {
      const { fullPath } = route;
      await tabStore.closeTabByKey(fullPath, router);
    },
  },
]);
</script>
