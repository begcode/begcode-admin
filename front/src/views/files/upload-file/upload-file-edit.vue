<template>
  <PageWrapper v-bind="pageProps">
    <template #default>
      <UploadFileForm ref="uploadFileFormRef" v-bind="formProps" @updateSaveButton="updateSaveButton"></UploadFileForm>
    </template>
    <template #rightFooter>
      <a-space>
        <basic-button v-for="operation in operations" :type="operation.type" @click="operation.click" v-bind="operation.attrs">
          <Icon :icon="operation.icon" v-if="operation.icon" />
          {{ operation.title }}
        </basic-button>
      </a-space>
    </template>
  </PageWrapper>
</template>
<script lang="ts" setup>
import UploadFileForm from './components/form-component.vue';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

defineOptions({
  name: 'OssUploadFileEdit',
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

const uploadFileFormRef = ref<any>(null);
const pageProps = reactive({
  title: '编辑',
});
let uploadFileId = ref(route.params?.entityId as string);

async function saveOrUpdate() {
  const result = await uploadFileFormRef.value.submit();
  if (result) {
    if (!uploadFileId.value) {
      const { fullPath } = route;
      tabStore.closeTabByKey(fullPath, router).then(() => {
        go('/files/upload-file/' + result.id + '/edit', true);
      });
    }
  }
}

const formProps = reactive({
  entityId: uploadFileId.value,
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
    attrs: {
      'data-cy': 'entityCreateCancelButton',
    },
  },
  {
    title: uploadFileId.value ? '更新' : '保存',
    icon: '',
    type: 'primary',
    name: 'save',
    click: saveOrUpdate,
    hide: () => !showSaveButton.value,
    attrs: {
      'data-cy': 'entityCreateSaveButton',
    },
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
