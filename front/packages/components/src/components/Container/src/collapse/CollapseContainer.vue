<script lang="tsx">
import { ref, unref, defineComponent, type PropType, type ExtractPropTypes, getCurrentInstance, ComponentInternalInstance } from 'vue';
import { isNil } from 'lodash-es';
import { Skeleton, theme } from 'ant-design-vue';
import { useTimeoutFn } from '@/hooks/vben';
import { CollapseTransition } from '@/components/Transition';
import CollapseHeader from './CollapseHeader.vue';
import { triggerWindowResize } from '@/utils/event';
import { useDesign } from '@/hooks/web/useDesign';

const collapseContainerProps = {
  title: { type: String, default: '' },
  loading: { type: Boolean },
  /**
   *  Can it be expanded
   */
  canExpan: { type: Boolean, default: true },
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

    const show = ref(true);

    const handleExpand = (val: boolean) => {
      show.value = isNil(val) ? !show.value : val;
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

    return () => (
      <div class={unref(prefixCls)}>
        <CollapseHeader
          {...props}
          prefixCls={unref(prefixCls)}
          onExpand={handleExpand}
          show={show.value}
          v-slots={{
            title: slots.title,
            action: slots.action,
          }}
        />

        <div class="p-2">
          <CollapseTransition enable={props.canExpan}>
            {props.loading ? (
              <Skeleton active={props.loading} />
            ) : (
              <div class={`${prefixCls}__body`} v-show={show.value}>
                {slots.default?.()}
              </div>
            )}
          </CollapseTransition>
        </div>

        {slots.footer && <div class={`${prefixCls}__footer`}>{slots.footer()}</div>}
      </div>
    );
  },
});
</script>

<style>
.vben-collapse-container {
  transition: all 0.3s ease-in-out;
  border-radius: 2px;
  background-color: v-bind('token["component-background"]');
}
.vben-collapse-container__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 32px;
  border-bottom: 1px solid v-bind('token["colorBorder"]');
}
.vben-collapse-container__footer {
  border-top: 1px solid v-bind('token["colorBorder"]');
}
.vben-collapse-container__action {
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: flex-end;
  text-align: right;
}
</style>
