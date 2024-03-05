<script lang="tsx">
import type { ContextMenuItem, ItemContentProps, Axis } from './typing';
import type { FunctionalComponent, CSSProperties, PropType } from 'vue';
import { defineComponent, nextTick, onMounted, computed, ref, unref, onUnmounted } from 'vue';
import Icon from '@/components/Icon/Icon.vue';
import { Menu, Divider } from 'ant-design-vue';

const prefixCls = 'context-menu';

const props = {
  width: { type: Number, default: 156 },
  customEvent: { type: Object as PropType<Event>, default: null },
  styles: { type: Object as PropType<CSSProperties> },
  showIcon: { type: Boolean, default: true },
  axis: {
    // The position of the right mouse button click
    type: Object as PropType<Axis>,
    default() {
      return { x: 0, y: 0 };
    },
  },
  items: {
    // The most important list, if not, will not be displayed
    type: Array as PropType<ContextMenuItem[]>,
    default() {
      return [];
    },
  },
};

const ItemContent: FunctionalComponent<ItemContentProps> = props => {
  const { item } = props;
  return (
    <span style="display: inline-block; width: 100%; " class="px-4" onClick={props.handler.bind(null, item)}>
      {props.showIcon && item.icon && <Icon class="mr-2" icon={item.icon} />}
      <span>{item.label}</span>
    </span>
  );
};

export default defineComponent({
  name: 'ContextMenu',
  props,
  setup(props) {
    const wrapRef = ref(null);
    const showRef = ref(false);

    const getStyle = computed((): CSSProperties => {
      const { axis, items, styles, width } = props;
      const { x, y } = axis || { x: 0, y: 0 };
      const menuHeight = (items || []).length * 40;
      const menuWidth = width;
      const body = document.body;

      const left = body.clientWidth < x + menuWidth ? x - menuWidth : x;
      const top = body.clientHeight < y + menuHeight ? y - menuHeight : y;
      return {
        position: 'absolute',
        width: `${width}px`,
        left: `${left + 1}px`,
        top: `${top + 1}px`,
        zIndex: 9999,
        ...styles,
      };
    });

    onMounted(() => {
      nextTick(() => (showRef.value = true));
    });

    onUnmounted(() => {
      const el = unref(wrapRef);
      el && document.body.removeChild(el);
    });

    function handleAction(item: ContextMenuItem, e: MouseEvent) {
      const { handler, disabled } = item;
      if (disabled) {
        return;
      }
      showRef.value = false;
      e?.stopPropagation();
      e?.preventDefault();
      handler?.();
    }

    function renderMenuItem(items: ContextMenuItem[]) {
      const visibleItems = items.filter(item => !item.hidden);
      return visibleItems.map(item => {
        const { disabled, label, children, divider = false } = item;

        const contentProps = {
          item,
          handler: handleAction,
          showIcon: props.showIcon,
        };

        if (!children || children.length === 0) {
          return (
            <>
              <Menu.Item disabled={disabled} class={`${prefixCls}__item`} key={label}>
                <ItemContent {...contentProps} />
              </Menu.Item>
              {divider ? <Divider key={`d-${label}`} /> : null}
            </>
          );
        }
        if (!unref(showRef)) return null;

        return (
          <Menu.SubMenu key={label} disabled={disabled} popupClassName={`${prefixCls}__popup`}>
            {{
              title: () => <ItemContent {...contentProps} />,
              default: () => renderMenuItem(children),
            }}
          </Menu.SubMenu>
        );
      });
    }

    return () => {
      if (!unref(showRef)) {
        return null;
      }
      const { items } = props;
      return (
        <div class={prefixCls}>
          return (
          <Menu inlineIndent={12} mode="vertical" class={prefixCls} ref={wrapRef} style={unref(getStyle)}>
            {renderMenuItem(items)}
          </Menu>
        </div>
      );
    };
  },
});
</script>
<style>
.context-menu {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 200;
  display: block;
  width: 156px;
  margin: 0;
  list-style: none;
  background-color: #fff;
  border: 1px solid #000000;
  border-radius: 8px;
  box-shadow:
    0 2px 2px 0 #000000,
    0 3px 1px -2px #000000,
    0 1px 5px 0 #000000;
  background-clip: padding-box;
  user-select: none;
}

.context-menu__item {
  margin: 0 !important;
}

.context-menu li {
  display: inline-block;
  width: 100% !important;
  height: 42px !important;
  margin: 0 !important;
  line-height: 42px !important;
}

.context-menu li span {
  line-height: 42px !important;
}

.context-menu li > div {
  width: 100% !important;
  height: 100% !important;
  margin: 0 !important;
}

.context-menu li:not(.ant-menu-item-disabled):hover {
  color: rgba(0, 0, 0, 0.88);
  background-color: rgba(0, 0, 0, 0.04);
}

.context-menu .ant-divider {
  margin: 0;
}

.context-menu__popup .ant-divider {
  margin: 0;
}

.context-menu__popup li {
  display: inline-block;
  width: 100% !important;
  height: 42px !important;
  margin: 0 !important;
  line-height: 42px !important;
}

.context-menu__popup li span {
  line-height: 42px !important;
}

.context-menu__popup li > div {
  width: 100% !important;
  height: 100% !important;
  margin: 0 !important;
}

.context-menu__popup li:not(.ant-menu-item-disabled):hover {
  color: rgba(0, 0, 0, 0.88);
  background-color: rgba(0, 0, 0, 0.04);
}

.context-menu .ant-menu-submenu-title,
.context-menu .ant-menu-item {
  padding: 0 !important;
}
</style>
