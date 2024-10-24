<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <FormConfigDescriptions ref="formConfigDescriptionsRef" v-bind="descriptionProps"> </FormConfigDescriptions>
    </template>
    <template #rightFooter>
      <a-space>
        <a-button v-for="operation in operations" :type="operation.type" @click="operation.click">
          <Icon :icon="operation.icon" v-if="operation.icon" />{{ operation.title }}</a-button
        >
      </a-space>
    </template>
  </PageWrapper>
</template>
<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router';
import { PageWrapper } from '@/components/Page';
import { useMultipleTabStore } from '@/store/modules/multipleTab';
import FormConfigDescriptions from './components/detail-component.vue';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

defineOptions({
  name: 'SystemFormConfigDetail',
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

const formConfigDescriptionsRef = ref(null);
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
