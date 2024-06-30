<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <FormConfigForm ref="formConfigFormRef" v-bind="formProps"> </FormConfigForm>
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
import FormConfigForm from './components/form-component.vue';
import { Button, Icon, PageWrapper } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

defineOptions({
  name: 'SystemFormConfigEdit',
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

const formConfigFormRef = ref<any>(null);
const pageProps = reactive({
  title: '编辑',
});
let formConfigId = ref(route.params?.entityId as string);

const saveOrUpdate = async () => {
  const result = await formConfigFormRef.value.submit();
  if (result) {
    if (!formConfigId.value) {
      const { fullPath } = route;
      tabStore.closeTabByKey(fullPath, router).then(() => {
        go('/system/form-config/' + result.id + '/edit', true);
      });
    }
  }
};

const formProps = reactive({
  entityId: formConfigId.value,
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
      return !!formConfigId.value;
    },
    title: '保存',
    icon: '',
    type: 'primary',
    name: 'save',
    click: saveOrUpdate,
  },
  {
    hide: () => {
      return !formConfigId.value;
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
