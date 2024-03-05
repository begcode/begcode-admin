<script lang="tsx">
import { defineComponent, computed, unref, reactive, watch, ref } from 'vue';
import { theme } from 'ant-design-vue';
import type { MoveData, DragVerifyActionType } from './typing';
import type { Nullable } from '#/types';
import { useTimeoutFn } from '@/hooks/vben';
import BasicDragVerify from './DragVerify.vue';
import { hackCss } from '@/utils/domUtils';
import { fadeColor } from '@/utils/color';
import { rotateProps } from './props';
import { useI18n } from '@/hooks/web/useI18nOut';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

export default defineComponent({
  name: 'ImgRotateDragVerify',
  inheritAttrs: false,
  props: rotateProps,
  emits: ['success', 'change', 'update:value'],
  setup(props, { emit, attrs, expose }) {
    const basicRef = ref<Nullable<DragVerifyActionType>>(null);
    const state = reactive({
      showTip: false,
      isPassing: false,
      imgStyle: {},
      randomRotate: 0,
      currentRotate: 0,
      toOrigin: false,
      startTime: 0,
      endTime: 0,
      draged: false,
      tipStyle: {},
    });
    const { t } = useI18n();
    const { useToken } = theme;
    const { token } = useToken();

    watch(
      () => state.isPassing,
      isPassing => {
        if (isPassing) {
          const { startTime, endTime } = state;
          const time = (endTime - startTime) / 1000;
          emit('success', { isPassing, time: time.toFixed(1) });
          emit('change', isPassing);
          emit('update:value', isPassing);
          state.tipStyle['background-color'] = fadeColor(token.value.colorSuccess, 0.6);
        } else {
          state.tipStyle['background-color'] = fadeColor(token.value.colorError, 0.6);
        }
      },
    );

    const getImgWrapStyleRef = computed(() => {
      const { imgWrapStyle, imgWidth } = props;
      return {
        width: `${imgWidth}px`,
        height: `${imgWidth}px`,
        ...imgWrapStyle,
      };
    });

    const getFactorRef = computed(() => {
      const { minDegree, maxDegree } = props;
      if (minDegree === maxDegree) {
        return Math.floor(1 + Math.random() * 1) / 10 + 1;
      }
      return 1;
    });
    function handleStart() {
      state.startTime = new Date().getTime();
    }

    function handleDragBarMove(data: MoveData) {
      state.draged = true;
      const { imgWidth, height, maxDegree } = props;
      const { moveX } = data;
      const currentRotate = Math.ceil((moveX / (imgWidth! - parseInt(height as string))) * maxDegree! * unref(getFactorRef));
      state.currentRotate = currentRotate;
      state.imgStyle = hackCss('transform', `rotateZ(${state.randomRotate - currentRotate}deg)`);
    }

    function handleImgOnLoad() {
      const { minDegree, maxDegree } = props;
      const ranRotate = Math.floor(minDegree! + Math.random() * (maxDegree! - minDegree!)); // 生成随机角度
      state.randomRotate = ranRotate;
      state.imgStyle = hackCss('transform', `rotateZ(${ranRotate}deg)`);
    }

    function handleDragEnd() {
      const { randomRotate, currentRotate } = state;
      const { diffDegree } = props;

      if (Math.abs(randomRotate - currentRotate) >= (diffDegree || 20)) {
        state.imgStyle = hackCss('transform', `rotateZ(${randomRotate}deg)`);
        state.toOrigin = true;
        useTimeoutFn(() => {
          state.toOrigin = false;
          state.showTip = true;
          //  时间与动画时间保持一致
        }, 300);
      } else {
        checkPass();
      }
      state.showTip = true;
    }
    function checkPass() {
      state.isPassing = true;
      state.endTime = new Date().getTime();
    }

    function resume() {
      state.showTip = false;
      const basicEl = unref(basicRef);
      if (!basicEl) {
        return;
      }
      state.isPassing = false;

      basicEl.resume();
      handleImgOnLoad();
    }

    expose({ resume });

    // handleImgOnLoad();
    return () => {
      const { src } = props;
      const { toOrigin, isPassing, startTime, endTime } = state;
      const imgCls: string[] = [];
      if (toOrigin) {
        imgCls.push('to-origin');
      }
      const time = (endTime - startTime) / 1000;

      return (
        <div class="ir-dv">
          <div class={`ir-dv-img__wrap`} style={unref(getImgWrapStyleRef)}>
            <img
              src={src}
              onLoad={handleImgOnLoad}
              width={parseInt(props.width as string)}
              class={imgCls}
              style={state.imgStyle}
              onClick={() => {
                resume();
              }}
              alt="verify"
            />
            {state.showTip && (
              <span style={state.tipStyle}>
                {state.isPassing ? t('component.verify.time', { time: time.toFixed(1) }) : t('component.verify.error')}
              </span>
            )}
            {!state.showTip && !state.draged && (
              <span class={[`ir-dv-img__tip`, 'normal']} style={{ color: unref(token).colorWhite }}>
                {t('component.verify.redoTip')}
              </span>
            )}
          </div>
          <BasicDragVerify
            class={`ir-dv-drag__bar`}
            onMove={handleDragBarMove}
            onEnd={handleDragEnd}
            onStart={handleStart}
            ref={basicRef}
            {...{ ...attrs, ...props }}
            value={isPassing}
            isSlot={true}
          />
        </div>
      );
    };
  },
});
</script>
<style>
.ir-dv {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.ir-dv-img__wrap {
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
.ir-dv-img__wrap img {
  width: 100%;
  border-radius: 50%;
}
.ir-dv-img__wrap img.to-origin {
  transition: transform 0.3s;
}
.ir-dv-img__tip {
  position: absolute;
  bottom: 10px;
  left: 0;
  z-index: 1;
  display: block;
  width: 100%;
  height: 30px;
  font-size: 12px;
  line-height: 30px;
  text-align: center;
}
.ir-dv-img__tip.normal {
  background-color: rgba(0, 0, 0, 0.3);
}
.ir-dv-drag__bar {
  margin-top: 20px;
}
</style>
