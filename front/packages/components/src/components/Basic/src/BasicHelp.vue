<script lang="tsx">
import { CSSProperties, PropType, reactive, VNodeChild } from 'vue';
import { defineComponent, computed, unref } from 'vue';
import { Tooltip, theme } from 'ant-design-vue';
import { InfoCircleOutlined } from '@ant-design/icons-vue';
import { getPopupContainer } from '@/utils';
import { isString, isArray } from '@/utils/is';
import { getSlot } from '@/utils/helper/tsxHelper';
import { useDesign } from '@/hooks/web/useDesign';

const props = {
  /**
   * Help text max-width
   * @default: 600px
   */
  maxWidth: { type: String, default: '600px' },
  /**
   * Whether to display the serial number
   * @default: false
   */
  showIndex: { type: Boolean },
  /**
   * Help text font color
   * @default: #ffffff
   */
  color: { type: String, default: '#ffffff' },
  /**
   * Help text font size
   * @default: 14px
   */
  fontSize: { type: String, default: '14px' },
  /**
   * Help text list
   */
  placement: { type: String, default: 'right' },
  /**
   * Help text list
   */
  text: {
    type: [Array, String, Object] as PropType<string[] | string | VNodeChild | JSX.Element>,
  },
};

export default defineComponent({
  name: 'BasicHelp',
  components: { Tooltip },
  props,
  setup(props, { slots }) {
    const { prefixCls } = useDesign('basic-help');

    const getTooltipStyle = computed((): CSSProperties => ({ color: props.color, fontSize: props.fontSize }));

    const getOverlayStyle = computed((): CSSProperties => ({ maxWidth: props.maxWidth }));

    function renderTitle() {
      const textList = props.text;

      if (isString(textList)) {
        return <p>{textList}</p>;
      }

      if (isArray(textList)) {
        return textList.map((text, index) => {
          return (
            <p key={text}>
              <>
                {props.showIndex ? `${index + 1}. ` : ''}
                {text}
              </>
            </p>
          );
        });
      }
      return <div>{textList}</div>;
    }

    const { useToken } = theme;
    const { token } = useToken();
    const helpColor = reactive({ color: token.value.colorPrimary });

    const onMousemove = () => {
      helpColor.color = '#909399';
    };
    const onMouseout = () => {
      helpColor.color = token.value.colorPrimary;
    };

    return () => {
      return (
        <Tooltip
          overlayClassName={`${prefixCls}__wrap`}
          title={<div style={unref(getTooltipStyle)}>{renderTitle()}</div>}
          autoAdjustOverflow={true}
          overlayStyle={unref(getOverlayStyle)}
          placement={props.placement as 'right'}
          getPopupContainer={() => getPopupContainer()}
        >
          <span class={prefixCls} style={helpColor} onMousemove={onMousemove} onMouseout={onMouseout}>
            {getSlot(slots) || <InfoCircleOutlined />}
          </span>
        </Tooltip>
      );
    };
  },
});
</script>
<style>
.vben-basic-help {
  display: inline-block;
  margin-left: 6px;
  font-size: 14px;
  cursor: pointer;
}
.vben-basic-help__wrap p {
  margin-bottom: 0;
}
</style>
