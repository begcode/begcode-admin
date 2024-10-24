import { theme } from 'ant-design-vue';
import { useAppProviderContext } from '@/hooks/useAppContext';
export function useDesign(scope: string) {
  const values = useAppProviderContext();
  // const $style = cssModule ? useCssModule() : {};

  // const style: Record<string, string> = {};
  // if (cssModule) {
  //   Object.keys($style).forEach((key) => {
  //     // const moduleCls = $style[key];
  //     const k = key.replace(new RegExp(`^${values.prefixCls}-?`, 'ig'), '');
  //     style[_lowerFirst(k)] = $style[key];
  //   });
  // }
  return {
    // prefixCls: computed(() => `${values.prefixCls}-${scope}`),
    prefixCls: `${values.prefixCls}-${scope}`,
    prefixVar: values.prefixCls,
    // style,
  };
}

export function useTheme() {
  const { useToken } = theme;
  return useToken().theme;
}
