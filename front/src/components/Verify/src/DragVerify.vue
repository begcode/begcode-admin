<script lang="tsx">
import { theme } from 'ant-design-vue';
import { useTimeoutFn } from '@vueuse/core';
import { useEventListener } from '@/hooks/event/useEventListener';
import { basicProps } from './props';
import { getSlot } from '@/utils/helper/tsxHelper';

export default defineComponent({
  name: 'BaseDragVerify',
  props: basicProps,
  emits: ['success', 'update:value', 'change', 'start', 'move', 'end'],
  setup(props, { emit, slots, expose }) {
    const state = reactive({
      isMoving: false,
      isPassing: false,
      moveDistance: 0,
      toLeft: false,
      startTime: 0,
      endTime: 0,
    });

    const wrapElRef = ref<HTMLDivElement | null>(null);
    const barElRef = ref<HTMLDivElement | null>(null);
    const contentElRef = ref<HTMLDivElement | null>(null);
    const actionElRef = ref(null) as Ref<HTMLDivElement | null>;

    useEventListener({
      el: document,
      name: 'mouseup',
      listener: () => {
        if (state.isMoving) {
          resume();
        }
      },
    });

    const { useToken } = theme;
    const { token } = useToken();

    const getActionStyleRef = computed(() => {
      const { height, actionStyle } = props;
      const h = `${parseInt(height as string)}px`;
      return {
        left: 0,
        width: h,
        height: h,
        ...actionStyle,
        'background-color': unref(token).colorWhite,
      };
    });

    const getWrapStyleRef = computed(() => {
      const { height, width, circle, wrapStyle } = props;
      const h = parseInt(height as string);
      const w = `${parseInt(width as string)}px`;
      return {
        width: w,
        height: `${h}px`,
        lineHeight: `${h}px`,
        borderRadius: circle ? h / 2 + 'px' : 0,
        ...wrapStyle,
      };
    });

    const getBarStyleRef = computed(() => {
      const { height, circle, barStyle } = props;
      const h = parseInt(height as string);
      return {
        height: `${h}px`,
        borderRadius: circle ? h / 2 + 'px 0 0 ' + h / 2 + 'px' : 0,
        ...barStyle,
        'background-color': unref(token).colorSuccess,
      };
    });

    const getContentStyleRef = computed(() => {
      const { height, width, contentStyle } = props;
      const h = `${parseInt(height as string)}px`;
      const w = `${parseInt(width as string)}px`;

      return {
        height: h,
        width: w,
        ...contentStyle,
      };
    });

    watch(
      () => state.isPassing,
      isPassing => {
        if (isPassing) {
          const { startTime, endTime } = state;
          const time = (endTime - startTime) / 1000;
          emit('success', { isPassing, time: time.toFixed(1) });
          emit('update:value', isPassing);
          emit('change', isPassing);
        }
      },
    );

    watchEffect(() => {
      state.isPassing = !!props.value;
    });

    function getEventPageX(e: MouseEvent | TouchEvent) {
      return (e as MouseEvent).pageX || (e as TouchEvent).touches[0].pageX;
    }

    function handleDragStart(e: MouseEvent | TouchEvent) {
      if (state.isPassing) {
        return;
      }
      const actionEl = unref(actionElRef);
      if (!actionEl) return;
      emit('start', e);
      state.moveDistance = getEventPageX(e) - parseInt(actionEl.style.left.replace('px', ''), 10);
      state.startTime = new Date().getTime();
      state.isMoving = true;
    }

    function getOffset(el: HTMLDivElement) {
      const actionWidth = parseInt(el.style.width);
      const { width } = props;
      const widthNum = parseInt(width as string);
      const offset = widthNum - actionWidth - 6;
      return { offset, widthNum, actionWidth };
    }

    function handleDragMoving(e: MouseEvent | TouchEvent) {
      const { isMoving, moveDistance } = state;
      if (isMoving) {
        const actionEl = unref(actionElRef);
        const barEl = unref(barElRef);
        if (!actionEl || !barEl) return;
        const { offset, widthNum, actionWidth } = getOffset(actionEl);
        const moveX = getEventPageX(e) - moveDistance;

        emit('move', {
          event: e,
          moveDistance,
          moveX,
        });
        if (moveX > 0 && moveX <= offset) {
          actionEl.style.left = `${moveX}px`;
          barEl.style.width = `${moveX + actionWidth / 2}px`;
        } else if (moveX > offset) {
          actionEl.style.left = `${widthNum - actionWidth}px`;
          barEl.style.width = `${widthNum - actionWidth / 2}px`;
          if (!props.isSlot) {
            checkPass();
          }
        }
      }
    }

    function handleDragOver(e: MouseEvent | TouchEvent) {
      const { isMoving, isPassing, moveDistance } = state;
      if (isMoving && !isPassing) {
        emit('end', e);
        const actionEl = unref(actionElRef);
        const barEl = unref(barElRef);
        if (!actionEl || !barEl) return;
        const moveX = getEventPageX(e) - moveDistance;
        const { offset, widthNum, actionWidth } = getOffset(actionEl);
        if (moveX < offset) {
          if (!props.isSlot) {
            resume();
          } else {
            setTimeout(() => {
              if (!props.value) {
                resume();
              } else {
                const contentEl = unref(contentElRef);
                if (contentEl) {
                  contentEl.style.width = `${parseInt(barEl.style.width)}px`;
                }
              }
            }, 0);
          }
        } else {
          actionEl.style.left = `${widthNum - actionWidth}px`;
          barEl.style.width = `${widthNum - actionWidth / 2}px`;
          checkPass();
        }
        state.isMoving = false;
      }
    }

    function checkPass() {
      if (props.isSlot) {
        resume();
        return;
      }
      state.endTime = new Date().getTime();
      state.isPassing = true;
      state.isMoving = false;
    }

    function resume() {
      state.isMoving = false;
      state.isPassing = false;
      state.moveDistance = 0;
      state.toLeft = false;
      state.startTime = 0;
      state.endTime = 0;
      const actionEl = unref(actionElRef);
      const barEl = unref(barElRef);
      const contentEl = unref(contentElRef);
      if (!actionEl || !barEl || !contentEl) return;
      state.toLeft = true;
      useTimeoutFn(() => {
        state.toLeft = false;
        actionEl.style.left = '0';
        barEl.style.width = '0';
        //  The time is consistent with the animation time
      }, 300);
      contentEl.style.width = unref(getContentStyleRef).width;
    }

    expose({
      resume,
    });

    return () => {
      const renderBar = () => {
        const cls = [`drag-verify-bar`];
        if (state.toLeft) {
          cls.push('to-left');
        }
        return <div class={cls} ref={barElRef} style={unref(getBarStyleRef)} />;
      };

      const renderContent = () => {
        const cls = [`drag-verify-content`];
        const { isPassing } = state;
        const { text, successText } = props;
        if (isPassing) {
          getContentStyleRef.value['-webkit-text-fill-color'] = unref(token).colorWhite;
        } else {
          delete getContentStyleRef.value['-webkit-text-fill-color'];
        }

        return (
          <div class={cls} ref={contentElRef} style={unref(getContentStyleRef)}>
            {getSlot(slots, 'text', isPassing) || (isPassing ? successText : text)}
          </div>
        );
      };

      const renderAction = () => {
        const cls = [`drag-verify-action`];
        const { toLeft, isPassing } = state;
        if (toLeft) {
          cls.push('to-left');
        }
        return (
          <div class={cls} onMousedown={handleDragStart} onTouchstart={handleDragStart} style={unref(getActionStyleRef)} ref={actionElRef}>
            {getSlot(slots, 'actionIcon', isPassing) || (
              <Icon
                icon={isPassing ? 'ant-design:check-outlined' : 'ant-design:double-right-outlined'}
                class={`drag-verify-action__icon`}
              />
            )}
          </div>
        );
      };

      return (
        <div
          class="drag-verify"
          ref={wrapElRef}
          style={unref(getWrapStyleRef)}
          onMousemove={handleDragMoving}
          onTouchmove={handleDragMoving}
          onMouseleave={handleDragOver}
          onMouseup={handleDragOver}
          onTouchend={handleDragOver}
        >
          {renderBar()}
          {renderContent()}
          {renderAction()}
        </div>
      );
    };
  },
});
</script>
<style>
.drag-verify {
  position: relative;
  overflow: hidden;
  text-align: center;
  background-color: #000000;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.drag-verify-bar {
  position: absolute;
  width: 0;
  height: 36px;
  border-radius: 4px;
}
.drag-verify-bar.to-left {
  width: 0 !important;
  transition: width 0.3s;
}
.drag-verify-content {
  position: absolute;
  top: 0;
  font-size: 12px;
  -webkit-text-size-adjust: none;
  background-color: -webkit-gradient(
    linear,
    left top,
    right top,
    color-stop(0, #333),
    color-stop(0.4, #333),
    color-stop(0.5, #fff),
    color-stop(0.6, #333),
    color-stop(1, #333)
  );
  animation: slidetounlock 3s infinite;
  background-clip: text;
  user-select: none;
}
.drag-verify-content > * {
  -webkit-text-fill-color: #333;
}
.drag-verify-action {
  position: absolute;
  top: 0;
  left: 0;
  display: flex;
  cursor: move;
  border-radius: 4px;
  justify-content: center;
  align-items: center;
}
.drag-verify-action__icon {
  cursor: inherit;
}
.drag-verify-action.to-left {
  left: 0 !important;
  transition: left 0.3s;
}
@keyframes slidetounlock {
  0% {
    background-position: -120px 0;
  }
  100% {
    background-position: 120px 0;
  }
}
</style>
