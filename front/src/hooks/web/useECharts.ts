import type { EChartsOption } from 'echarts';
import { tryOnUnmounted, useDebounceFn, useTimeoutFn } from '@vueuse/core';
import { useEventListener } from '@/hooks/event/useEventListener';
import { useBreakpoint } from '@/hooks/event/useBreakpoint';
import echarts from '@/utils/lib/echarts';
// import { useTheme } from '@/hooks/web/useDesign';
// import { getCollapsed } from '@/hooks/web/useStore';

export function useECharts(elRef: Ref<HTMLDivElement>, theme: 'light' | 'dark' | 'default' = 'default') {
  // const themeData = useTheme();

  const getDarkMode = computed(() => {
    return theme === 'default' ? theme : theme;
  });
  let chartInstance: echarts.ECharts | null = null;
  let resizeFn = resize;
  const cacheOptions = ref({}) as Ref<EChartsOption>;
  let removeResizeFn = () => {};

  resizeFn = useDebounceFn(resize, 200);

  const getOptions = computed(() => {
    if (getDarkMode.value !== 'dark') {
      return cacheOptions.value as EChartsOption;
    }
    return {
      backgroundColor: 'transparent',
      ...cacheOptions.value,
    } as EChartsOption;
  });

  function initCharts(t = theme) {
    const el = unref(elRef);
    if (!el || !unref(el)) {
      return;
    }

    chartInstance = echarts.init(el, t);
    const { removeEvent } = useEventListener({
      el: window,
      name: 'resize',
      listener: resizeFn,
      options: { passive: true },
    });
    removeResizeFn = removeEvent;
    const { widthRef, screenEnum } = useBreakpoint();
    if (unref(widthRef) <= screenEnum.MD || el.offsetHeight === 0) {
      useTimeoutFn(() => {
        resizeFn();
      }, 30);
    }
  }

  function setOptions(options: EChartsOption, clear = true) {
    cacheOptions.value = options;
    return new Promise(resolve => {
      if (unref(elRef)?.offsetHeight === 0) {
        useTimeoutFn(() => {
          setOptions(unref(getOptions));
          resolve(null);
        }, 30);
      }
      nextTick(() => {
        useTimeoutFn(() => {
          if (!chartInstance) {
            initCharts(getDarkMode.value as 'default');

            if (!chartInstance) return;
          }
          clear && chartInstance?.clear();

          chartInstance?.setOption(unref(getOptions));
          resolve(null);
        }, 30);
      });
    });
  }

  function resize() {
    chartInstance?.resize({
      animation: {
        duration: 300,
        easing: 'quadraticIn',
      },
    });
  }

  watch(
    () => getDarkMode.value,
    theme => {
      if (chartInstance) {
        chartInstance.dispose();
        initCharts(theme as 'default');
        setOptions(cacheOptions.value);
      }
    },
  );

  // watch(getCollapsed, (_) => {
  //   useTimeoutFn(() => {
  //     resizeFn();
  //   }, 300);
  // });

  tryOnUnmounted(() => {
    if (!chartInstance) return;
    removeResizeFn();
    chartInstance.dispose();
    chartInstance = null;
  });

  function getInstance(): echarts.ECharts | null {
    if (!chartInstance) {
      initCharts(getDarkMode.value as 'default');
    }
    return chartInstance;
  }

  return {
    setOptions,
    resize,
    echarts,
    getInstance,
  };
}
