<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <CommonFieldDataForm ref="commonFieldDataFormRef" v-bind="formProps"> </CommonFieldDataForm>
    </template>
    <template #rightFooter>
      <Space>
        <Button v-for="operation in operations" :type="operation.type" @click="operation.click">
          <Icon icon="operation.icon" v-if="operation.icon" />
          {{ operation.title }}
        </Button>
      </Space>
    </template>
  </PageWrapper>
</template>
<script lang="ts" setup>
import { computed, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message, Space } from 'ant-design-vue';
import CommonFieldDataForm from './components/form-component.vue';
import { Button, Icon, PageWrapper } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

defineOptions({
  name: 'SystemCommonFieldDataEdit',
  inheritAttrs: false,
});

const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
});

const route = useRoute();
const router = useRouter();
const go = useGo();
const tabStore = useMultipleTabStore();

const commonFieldDataFormRef = ref<any>(null);
const pageProps = reactive({
  title: '编辑',
});
let commonFieldDataId = ref(route.params?.entityId as string);

const saveOrUpdate = async () => {
  const result = await commonFieldDataFormRef.value.submit();
  if (result) {
    if (!commonFieldDataId.value) {
      const { fullPath } = route;
      tabStore.closeTabByKey(fullPath, router).then(() => {
        go('/settings/common-field-data/' + result.id + '/edit', true);
      });
    }
  }
};

const formProps = reactive({
  entityId: commonFieldDataId.value,
  baseData: props.baseData,
});
const operationsConfig = ref<any>([
  {
    title: '关闭',
    type: 'default',
    name: 'close',
    skipValidate: true,
    click: async () => {
      const { fullPath } = route;
      await tabStore.closeTabByKey(fullPath, router);
    },
  },
  {
    hide: () => {
      return !!commonFieldDataId.value;
    },
    title: '保存',
    icon: '',
    type: 'primary',
    name: 'save',
    click: saveOrUpdate,
  },
  {
    hide: () => {
      return !commonFieldDataId.value;
    },
    title: '更新',
    icon: '',
    name: 'update',
    type: 'primary',
    click: saveOrUpdate,
  },
]);
const operations = computed(() => {
  return operationsConfig.value
    .map((operation: any) => {
      return {
        ...operation,
        hide: operation.hide ? operation.hide() : false,
      };
    })
    .filter((operation: any) => !operation.hide);
});
</script>
