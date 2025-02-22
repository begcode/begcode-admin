<script lang="tsx">
import { theme } from 'ant-design-vue';
import { useTimeoutFn } from '@vueuse/core';
import { CollapseTransition } from '@/components/Transition';
import CollapseHeader from './CollapseHeader.vue';
import { triggerWindowResize } from '@/utils/event';
import { useDesign } from '@/hooks/web/useDesign';

const collapseContainerProps = {
  title: { type: String, default: '' },
  defaultExpan: { type: Boolean, default: true },
  loading: { type: Boolean },
  /**
   *  Can it be expanded
   */
  canExpand: { type: Boolean, default: true },
  /**
   * Warm reminder on the right side of the title
   */
  helpMessage: {
    type: [Array, String] as PropType<string[] | string>,
    default: '',
  },
  /**
   * Whether to trigger window.resize when expanding and contracting,
   * Can adapt to tables and forms, when the form shrinks, the form triggers resize to adapt to the height
   */
  triggerWindowResize: { type: Boolean },
  /**
   * Delayed loading time
   */
  lazyTime: { type: Number, default: 0 },
};

export type CollapseContainerProps = ExtractPropTypes<typeof collapseContainerProps>;

export default defineComponent({
  name: 'CollapseContainer',

  props: collapseContainerProps,

  setup(props, { expose, slots }) {
    const { prefixCls } = useDesign('collapse-container');

    const show = ref(props.defaultExpan);

    const handleExpand = (val: boolean) => {
      show.value = _isNil(val) ? !show.value : val;
      if (props.triggerWindowResize) {
        // 200 milliseconds here is because the expansion has animation,
        useTimeoutFn(triggerWindowResize, 200);
      }
    };

    expose({ handleExpand });

    const { useToken } = theme;
    const { token } = useToken();

    const { proxy } = getCurrentInstance() as ComponentInternalInstance;
    if (proxy) {
      proxy['token'] = token.value;
    }

    const footerStyle = { 'border-top': token.value.colorBorder };
    const headStyle = { 'border-bottom': token.value.colorBorder };
    const containerStyle = { 'background-color': 'fff' };

    return () => (
      <div class={unref(prefixCls)} style={containerStyle}>
        <CollapseHeader
          {...props}
          prefixCls={unref(prefixCls)}
          onExpand={handleExpand}
          show={show.value}
          style={headStyle}
          v-slots={{
            title: slots.title,
            action: slots.action,
          }}
        />

        <div class="p-2">
          <CollapseTransition enable={props.canExpand}>
            {props.loading ? (
              <a-skeleton active={props.loading} />
            ) : (
              <div class={`${prefixCls}__body`} v-show={show.value}>
                {slots.default?.()}
              </div>
            )}
          </CollapseTransition>
        </div>

        {slots.footer && <div style={footerStyle}>{slots.footer()}</div>}
      </div>
    );
  },
});
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-collapse-container';
.@{prefix-cls} {
  background-color: @component-background;
  border-radius: 2px;
  transition: all 0.3s ease-in-out;

  &__header {
    display: flex;
    height: 32px;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid @border-color-light;
  }

  &__footer {
    border-top: 1px solid @border-color-light;
  }

  &__action {
    display: flex;
    text-align: right;
    flex: 1;
    align-items: center;
    justify-content: flex-end;
  }
}
</style>
