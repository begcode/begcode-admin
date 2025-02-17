import type { DescriptionsProps } from 'ant-design-vue/es/descriptions';
import type { CollapseContainerOptions } from '@/components/Container';
import type { Recordable } from '#/utils';
import type { JSX } from '#/types.d';

export interface DescItem {
  labelMinWidth?: number;
  contentMinWidth?: number;
  labelStyle?: CSSProperties;
  field: string;
  label: string | VNode | JSX.Element;
  // Merge column
  span?: number;
  show?: boolean | ((...arg: any) => boolean);
  format?: (value: any, data?) => string;
  // render
  render?: (val: any, data: Recordable) => VNode | undefined | JSX.Element | Element | string | number;
}

export interface DescriptionProps extends DescriptionsProps {
  // Whether to include the collapse component
  useCollapse?: boolean;
  /**
   * item configuration
   * @type DescItem
   */
  schema: DescItem[];
  /**
   * 数据
   * @type object
   */
  data: Recordable;
  /**
   * Built-in CollapseContainer component configuration
   * @type CollapseContainerOptions
   */
  collapseOptions?: CollapseContainerOptions;

  showLabel?: boolean;
}

export interface DescInstance {
  setDescProps(descProps: Partial<DescriptionProps>): void;
}

export type Register = (descInstance: DescInstance) => void;

export type UseDescReturnType = [Register, DescInstance];
