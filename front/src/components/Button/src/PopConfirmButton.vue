<script lang="ts">
import BasicButton from './BasicButton.vue';
import { Popconfirm } from 'ant-design-vue';
import { extendSlots } from '@/utils/helper/tsxHelper';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { useI18n } from '@/hooks/web/useI18nOut';

const props = {
  /**
   * Whether to enable the drop-down menu
   * @default: true
   */
  enable: {
    type: Boolean,
    default: true,
  },
};

export default defineComponent({
  name: 'PopButton',
  inheritAttrs: false,
  props,
  setup(props, { slots }) {
    const { t } = useI18n();
    const attrs = useAttrs();

    // get inherit binding value
    const getBindValues = computed(() => {
      return Object.assign(
        {
          okText: t('common.okText'),
          cancelText: t('common.cancelText'),
        },
        { ...props, ...unref(attrs) },
      );
    });

    return () => {
      const bindValues = _omit(unref(getBindValues), 'icon', 'color');
      const btnBind = _omit(unref(getBindValues), 'title') as any;
      if (btnBind.disabled) btnBind.color = '';
      const Button = h(BasicButton, btnBind, extendSlots(slots));

      // If it is not enabled, it is a normal button
      if (!props.enable) {
        return Button;
      }
      return h(Popconfirm, bindValues, { default: () => Button });
    };
  },
});
</script>
