<script lang="tsx">
import resumeSvg from '@/assets/svg/preview/resume.svg';
import rotateSvg from '@/assets/svg/preview/p-rotate.svg';
import scaleSvg from '@/assets/svg/preview/scale.svg';
import unScaleSvg from '@/assets/svg/preview/unscale.svg';
import unRotateSvg from '@/assets/svg/preview/unrotate.svg';

enum StatueEnum {
  LOADING,
  DONE,
  FAIL,
}
interface ImgState {
  currentUrl: string;
  imgScale: number;
  imgRotate: number;
  imgTop: number;
  imgLeft: number;
  currentIndex: number;
  status: StatueEnum;
  moveX: number;
  moveY: number;
  show: boolean;
}

const props = {
  show: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  imageList: {
    type: Array as PropType<string[]>,
    default: null,
  },
  index: {
    type: Number as PropType<number>,
    default: 0,
  },
  scaleStep: {
    type: Number as PropType<number>,
  },
  defaultWidth: {
    type: Number as PropType<number>,
  },
  maskClosable: {
    type: Boolean as PropType<boolean>,
  },
  rememberState: {
    type: Boolean as PropType<boolean>,
  },
};

const prefixCls = 'img-preview';
export default defineComponent({
  name: 'ImagePreview',
  props,
  emits: ['img-load', 'img-error'],
  setup(props, { expose, emit }) {
    interface stateInfo {
      scale: number;
      rotate: number;
      top: number;
      left: number;
    }

    const stateMap = new Map<string, stateInfo>();
    const imgState = reactive<ImgState>({
      currentUrl: '',
      imgScale: 1,
      imgRotate: 0,
      imgTop: 0,
      imgLeft: 0,
      status: StatueEnum.LOADING,
      currentIndex: 0,
      moveX: 0,
      moveY: 0,
      show: props.show,
    });

    const wrapElRef = ref<HTMLDivElement | null>(null);
    const imgElRef = ref<HTMLImageElement | null>(null);

    // 初始化
    function init() {
      initMouseWheel();
      const { index, imageList } = props;

      if (!imageList || !imageList.length) {
        throw new Error('imageList is undefined');
      }
      imgState.currentIndex = index;
      handleIChangeImage(imageList[index]);
    }

    // 重置
    function initState() {
      imgState.imgScale = 1;
      imgState.imgRotate = 0;
      imgState.imgTop = 0;
      imgState.imgLeft = 0;
    }

    // 初始化鼠标滚轮事件
    function initMouseWheel() {
      const wrapEl = unref(wrapElRef);
      if (!wrapEl) {
        return;
      }
      (wrapEl as any).onmousewheel = scrollFunc;
      // 火狐浏览器没有onmousewheel事件，用DOMMouseScroll代替
      document.body.addEventListener('DOMMouseScroll', scrollFunc);
      // 禁止火狐浏览器下拖拽图片的默认事件
      document.ondragstart = function () {
        return false;
      };
    }

    const getScaleStep = computed(() => {
      const scaleStep = props?.scaleStep ?? 0;
      if (scaleStep ?? (0 > 0 && scaleStep < 100)) {
        return scaleStep / 100;
      } else {
        return imgState.imgScale / 10;
      }
    });

    // 监听鼠标滚轮
    function scrollFunc(e: any) {
      e = e || window.event;
      e.delta = e.wheelDelta || -e.detail;

      e.preventDefault();
      if (e.delta > 0) {
        // 滑轮向上滚动
        scaleFunc(getScaleStep.value);
      }
      if (e.delta < 0) {
        // 滑轮向下滚动
        scaleFunc(-getScaleStep.value);
      }
    }

    // 缩放函数
    function scaleFunc(num: number) {
      // 最小缩放
      const MIN_SCALE = 0.02;
      // 放大缩小的颗粒度
      const GRA = 0.1;
      if (imgState.imgScale <= 0.2 && num < 0) return;
      imgState.imgScale += num * GRA;
      // scale 不能 < 0，否则图片会倒置放大
      if (imgState.imgScale < 0) {
        imgState.imgScale = MIN_SCALE;
      }
    }

    // 旋转图片
    function rotateFunc(deg: number) {
      imgState.imgRotate += deg;
    }

    // 鼠标事件
    function handleMouseUp() {
      const imgEl = unref(imgElRef);
      if (!imgEl) return;
      imgEl.onmousemove = null;
    }

    // 更换图片
    function handleIChangeImage(url: string) {
      imgState.status = StatueEnum.LOADING;
      const img = new Image();
      img.src = url;
      img.onload = (e: Event) => {
        if (imgState.currentUrl !== url) {
          const ele: any[] = e.composedPath();
          if (props.rememberState) {
            // 保存当前图片的缩放信息
            stateMap.set(imgState.currentUrl, {
              scale: imgState.imgScale,
              top: imgState.imgTop,
              left: imgState.imgLeft,
              rotate: imgState.imgRotate,
            });
            // 如果之前已存储缩放信息，就应用
            const stateInfo = stateMap.get(url);
            if (stateInfo) {
              imgState.imgScale = stateInfo.scale;
              imgState.imgTop = stateInfo.top;
              imgState.imgRotate = stateInfo.rotate;
              imgState.imgLeft = stateInfo.left;
            } else {
              initState();
              if (props.defaultWidth) {
                imgState.imgScale = props.defaultWidth / ele[0].naturalWidth;
              }
            }
          } else {
            if (props.defaultWidth) {
              imgState.imgScale = props.defaultWidth / ele[0].naturalWidth;
            }
          }

          ele &&
            emit('img-load', {
              index: imgState.currentIndex,
              dom: ele[0] as HTMLImageElement,
              url,
            });
        }
        imgState.currentUrl = url;
        imgState.status = StatueEnum.DONE;
      };
      img.onerror = (e: Event | string) => {
        const ele: EventTarget[] = (e as Event).composedPath();
        ele &&
          emit('img-error', {
            index: imgState.currentIndex,
            dom: ele[0] as HTMLImageElement,
            url,
          });
        imgState.status = StatueEnum.FAIL;
      };
    }

    // 关闭
    function handleClose(e: MouseEvent) {
      e && e.stopPropagation();
      close();
    }

    function close() {
      imgState.show = false;
      // 移除火狐浏览器下的鼠标滚动事件
      document.body.removeEventListener('DOMMouseScroll', scrollFunc);
      // 恢复火狐及Safari浏览器下的图片拖拽
      document.ondragstart = null;
    }

    // 图片复原
    function resume() {
      initState();
    }

    expose({
      resume,
      close,
      prev: handleChange.bind(null, 'left'),
      next: handleChange.bind(null, 'right'),
      setScale: (scale: number) => {
        if (scale > 0 && scale <= 10) imgState.imgScale = scale;
      },
      setRotate: (rotate: number) => {
        imgState.imgRotate = rotate;
      },
    });

    // 上一页下一页
    function handleChange(direction: 'left' | 'right') {
      const { currentIndex } = imgState;
      const { imageList } = props;
      if (direction === 'left') {
        imgState.currentIndex--;
        if (currentIndex <= 0) {
          imgState.currentIndex = imageList.length - 1;
        }
      }
      if (direction === 'right') {
        imgState.currentIndex++;
        if (currentIndex >= imageList.length - 1) {
          imgState.currentIndex = 0;
        }
      }
      handleIChangeImage(imageList[imgState.currentIndex]);
    }

    function handleAddMoveListener(e: MouseEvent) {
      e = e || window.event;
      imgState.moveX = e.clientX;
      imgState.moveY = e.clientY;
      const imgEl = unref(imgElRef);
      if (imgEl) {
        imgEl.onmousemove = moveFunc;
      }
    }

    function moveFunc(e: MouseEvent) {
      e = e || window.event;
      e.preventDefault();
      const movementX = e.clientX - imgState.moveX;
      const movementY = e.clientY - imgState.moveY;
      imgState.imgLeft += movementX;
      imgState.imgTop += movementY;
      imgState.moveX = e.clientX;
      imgState.moveY = e.clientY;
    }

    // 获取图片样式
    const getImageStyle = computed(() => {
      const { imgScale, imgRotate, imgTop, imgLeft } = imgState;
      return {
        transform: `scale(${imgScale}) rotate(${imgRotate}deg)`,
        marginTop: `${imgTop}px`,
        marginLeft: `${imgLeft}px`,
        maxWidth: props.defaultWidth ? 'unset' : '100%',
      };
    });

    const getIsMultipleImage = computed(() => {
      const { imageList } = props;
      return imageList.length > 1;
    });

    watchEffect(() => {
      if (props.show) {
        init();
      }
      if (props.imageList) {
        initState();
      }
    });

    const handleMaskClick = (e: MouseEvent) => {
      if (props.maskClosable && e.target && (e.target as HTMLDivElement).classList.contains(`${prefixCls}-content`)) {
        handleClose(e);
      }
    };

    const renderClose = () => {
      return (
        <div class={`${prefixCls}__close`} onClick={handleClose}>
          <Icon icon="ant-design:close-outlined" class={`${prefixCls}__close-icon`} />
        </div>
      );
    };

    const renderIndex = () => {
      if (!unref(getIsMultipleImage)) {
        return null;
      }
      const { currentIndex } = imgState;
      const { imageList } = props;
      return (
        <div class={`${prefixCls}__index`}>
          {currentIndex + 1} / {imageList.length}
        </div>
      );
    };

    const renderController = () => {
      return (
        <div class={`${prefixCls}__controller`}>
          <div class={`${prefixCls}__controller-item`} onClick={() => scaleFunc(-getScaleStep.value)}>
            <img src={unScaleSvg} />
          </div>
          <div class={`${prefixCls}__controller-item`} onClick={() => scaleFunc(getScaleStep.value)}>
            <img src={scaleSvg} />
          </div>
          <div class={`${prefixCls}__controller-item`} onClick={resume}>
            <img src={resumeSvg} />
          </div>
          <div class={`${prefixCls}__controller-item`} onClick={() => rotateFunc(-90)}>
            <img src={unRotateSvg} />
          </div>
          <div class={`${prefixCls}__controller-item`} onClick={() => rotateFunc(90)}>
            <img src={rotateSvg} />
          </div>
        </div>
      );
    };

    const renderArrow = (direction: 'left' | 'right') => {
      if (!unref(getIsMultipleImage)) {
        return null;
      }
      return (
        <div class={[`${prefixCls}__arrow`, direction]} onClick={() => handleChange(direction)}>
          <Icon icon={direction === 'left' ? 'ant-design:left-outline' : 'ant-design:right-outline'} />
        </div>
      );
    };

    return () => {
      return (
        imgState.show && (
          <div class={prefixCls} ref={wrapElRef} onMouseup={handleMouseUp} onClick={handleMaskClick}>
            <div class={`${prefixCls}-content`}>
              {/*<Spin*/}
              {/*  indicator={<LoadingOutlined style="font-size: 24px" spin />}*/}
              {/*  spinning={true}*/}
              {/*  class={[*/}
              {/*    `${prefixCls}-image`,*/}
              {/*    {*/}
              {/*      hidden: imgState.status !== StatueEnum.LOADING,*/}
              {/*    },*/}
              {/*  ]}*/}
              {/*/>*/}
              <img
                style={unref(getImageStyle)}
                class={[`${prefixCls}-image`, imgState.status === StatueEnum.DONE ? '' : 'hidden']}
                ref={imgElRef}
                src={imgState.currentUrl}
                onMousedown={handleAddMoveListener}
              />
              {renderClose()}
              {renderIndex()}
              {renderController()}
              {renderArrow('left')}
              {renderArrow('right')}
            </div>
          </div>
        )
      );
    };
  },
});
</script>
<style>
.img-preview {
  position: fixed;
  inset: 0;
  z-index: 1000;
  background: rgba(0, 0, 0, 0.5);
  user-select: none;
}

.img-preview-content {
  align-items: center;
  justify-content: center;
  display: flex;
  width: 100%;
  height: 100%;
  color: #fff;
}

.img-preview-image {
  cursor: pointer;
  transition: transform 0.3s;
}

.img-preview__close {
  position: absolute;
  top: -40px;
  right: -40px;
  width: 80px;
  height: 80px;
  overflow: hidden;
  color: #fff;
  cursor: pointer;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  transition: all 0.2s;
}

.img-preview__close-icon {
  position: absolute;
  top: 46px;
  left: 16px;
  font-size: 16px;
}

.img-preview__close:hover {
  background-color: rgba(0, 0, 0, 0.8);
}

.img-preview__index {
  position: absolute;
  bottom: 5%;
  left: 50%;
  padding: 0 22px;
  font-size: 16px;
  background: rgba(109, 109, 109, 0.6);
  border-radius: 15px;
  transform: translateX(-50%);
}

.img-preview__controller {
  position: absolute;
  bottom: 10%;
  left: 50%;
  display: flex;
  width: 260px;
  height: 44px;
  padding: 0 22px;
  margin-left: -139px;
  background: rgba(109, 109, 109, 0.6);
  border-radius: 22px;
  justify-content: center;
}

.img-preview__controller-item {
  display: flex;
  height: 100%;
  padding: 0 9px;
  font-size: 24px;
  cursor: pointer;
  transition: all 0.2s;
}

.img-preview__controller-item:hover {
  transform: scale(1.2);
}

.img-preview__controller-item img {
  width: 1em;
}

.img-preview__arrow {
  position: absolute;
  top: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  font-size: 28px;
  cursor: pointer;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  transition: all 0.2s;
}

.img-preview__arrow:hover {
  background-color: rgba(0, 0, 0, 0.8);
}

.img-preview__arrow.left {
  left: 50px;
}

.img-preview__arrow.right {
  right: 50px;
}
</style>
