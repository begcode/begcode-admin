// copy from element-plus
type OptionalKeys<T extends Record<string, unknown>> = {
  [K in keyof T]: T extends Record<K, T[K]> ? never : K;
}[keyof T];

type RequiredKeys<T extends Record<string, unknown>> = Exclude<keyof T, OptionalKeys<T>>;

type MonoArgEmitter<T, Keys extends keyof T> = <K extends Keys>(evt: K, arg?: T[K]) => void;

type BiArgEmitter<T, Keys extends keyof T> = <K extends Keys>(evt: K, arg: T[K]) => void;

export type EventEmitter<T extends Record<string, unknown>> = MonoArgEmitter<T, OptionalKeys<T>> & BiArgEmitter<T, RequiredKeys<T>>;

export type AnyFunction<T> = (...args: any[]) => T;

export type PartialReturnType<T extends (...args: unknown[]) => unknown> = Partial<ReturnType<T>>;

export type SFCWithInstall<T> = T & Plugin;

export type Nullable<T> = T | null;

export type RefElement = Nullable<HTMLElement>;

export type CustomizedHTMLElement<T> = HTMLElement & T;

export type Indexable<T> = {
  [key: string]: T;
};

export type Hash<T> = Indexable<T>;

export type TimeoutHandle = ReturnType<typeof global.setTimeout>;

export type ComponentSize = 'large' | 'medium' | 'small' | 'mini';

export type StyleValue = string | CSSProperties | Array<StyleValue>;

export type Mutable<T> = { -readonly [P in keyof T]: T[P] };
export type Merge<O extends object, T extends object> = {
  [K in keyof O | keyof T]: K extends keyof T ? T[K] : K extends keyof O ? O[K] : never;
};

/**
 * T = [
 *  { name: string; age: number; },
 *  { sex: 'male' | 'female'; age: string }
 * ]
 * =>
 * MergeAll<T> = {
 *  name: string;
 *  sex: 'male' | 'female';
 *  age: string
 * }
 */
export type MergeAll<T extends object[], R extends object = {}> = T extends [infer F extends object, ...infer Rest extends object[]]
  ? MergeAll<Rest, Merge<R, F>>
  : R;

export type TargetContext = '_self' | '_blank';

export interface Fn<T = any, R = T> {
  (...arg: T[]): R;
}

export type RefType<T> = T | null;

declare interface ComponentElRef<T extends HTMLElement = HTMLDivElement> {
  $el: T;
}

export type ComponentRef<T extends HTMLElement = HTMLDivElement> = ComponentElRef<T> | null;

export type EmitType = ReturnType<typeof defineEmits>;

export type VueNode = VNodeChild | VNode;

export interface PromiseFn<T = any, R = T> {
  (...arg: T[]): Promise<R>;
}

export type DynamicProps<T> = {
  [P in keyof T]: Ref<T[P]> | T[P] | ComputedRef<T[P]>;
};

export namespace JSX {
  // tslint:disable no-empty-interface
  type Element = VNode;
  // tslint:disable no-empty-interface
  type ElementClass = ComponentRenderProxy;
  interface ElementAttributesProperty {
    $props: any;
  }
  interface IntrinsicElements {
    [elem: string]: any;
  }
  interface IntrinsicAttributes {
    [elem: string]: any;
  }
}
