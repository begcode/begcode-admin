<script lang="tsx">
import AppSearchModal from './AppSearchModal.vue';
import { useI18n } from '@/hooks/web/useI18n';

export default defineComponent({
  name: 'AppSearch',
  setup() {
    const showModal = ref(false);
    const { t } = useI18n();

    function changeModal(show: boolean) {
      showModal.value = show;
    }

    return () => {
      return (
        <div class="p-1" onClick={changeModal.bind(null, true)}>
          <a-tooltip>
            {{
              title: () => t('common.searchText'),
              default: () => <Icon icon="ant-design:search-outlined" />,
            }}
          </a-tooltip>
          <AppSearchModal onClose={changeModal.bind(null, false)} visible={unref(showModal)} />
        </div>
      );
    };
  },
});
</script>
