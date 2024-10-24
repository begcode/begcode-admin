<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <SysLogForm ref="sysLogFormRef" v-bind="formProps" @updateSaveButton="updateSaveButton"></SysLogForm>
    </template>
    <template #rightFooter>
      <a-space>
        <BasicButton v-for="operation in operations" :type="operation.type" @click="operation.click">
          <Icon :icon="operation.icon" v-if="operation.icon" />
          {{ operation.title }}
        </BasicButton>
      </a-space>
    </template>
  </PageWrapper>
</template>
<script lang="ts" setup>
import SysLogForm from './components/form-component.vue';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

defineOptions({
  name: 'LogSysLogEdit',
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

const sysLogFormRef = ref<any>(null);
const pageProps = reactive({
  title: '编辑',
});
let sysLogId = ref(route.params?.entityId as string);

const saveOrUpdate = async () => {
  const result = await sysLogFormRef.value.submit();
  if (result) {
    if (!sysLogId.value) {
      const { fullPath } = route;
      tabStore.closeTabByKey(fullPath, router).then(() => {
        go('/log/sys-log/' + result.id + '/edit', true);
      });
    }
  }
};

const formProps = reactive({
  entityId: sysLogId.value,
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
    title: sysLogId.value ? '更新' : '保存',
    icon: '',
    type: 'primary',
    name: 'save',
    click: saveOrUpdate,
    hide: () => !showSaveButton.value,
  },
]);
const showSaveButton = ref<boolean>(true);
function updateSaveButton(show: boolean) {
  showSaveButton.value = show;
}
const operations = ref<any[]>([]);
watch(
  () => showSaveButton.value,
  () => {
    operations.value = operationsConfig.value.filter((operation: any) => {
      if (operation.hide === undefined) {
        return true;
      }
      if (typeof operation.hide === 'function') {
        return !operation.hide();
      }
      return !operation.hide;
    });
  },
  { immediate: true, deep: true },
);
</script>
