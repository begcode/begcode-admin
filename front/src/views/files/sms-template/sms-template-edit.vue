<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <SmsTemplateForm ref="smsTemplateFormRef" v-bind="formProps"></SmsTemplateForm>
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
import { Space } from 'ant-design-vue';
import SmsTemplateForm from './components/form-component.vue';
import { Button, Icon, PageWrapper } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

defineOptions({
  name: 'SystemSmsTemplateEdit',
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

const smsTemplateFormRef = ref<any>(null);
const pageProps = reactive({
  title: '编辑',
});
let smsTemplateId = ref(route.params?.entityId as string);

const saveOrUpdate = async () => {
  const result = await smsTemplateFormRef.value.submit();
  if (result) {
    if (!smsTemplateId.value) {
      const { fullPath } = route;
      tabStore.closeTabByKey(fullPath, router).then(() => {
        go('/system/sms-template/' + result.id + '/edit', true);
      });
    }
  }
};

const formProps = reactive({
  entityId: smsTemplateId.value,
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
      return !!smsTemplateId.value;
    },
    title: smsTemplateId.value ? '更新' : '保存',
    icon: '',
    type: 'primary',
    name: 'save',
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
