declare interface Fn<T = any, R = T> {
  (...arg: T[]): R;
}

declare type LabelValueOptions = {
  label: string;
  value: any;
  [key: string]: string | number | boolean;
}[];

declare type EmitType = ReturnType<typeof defineEmits>;

declare type ElRef<T extends HTMLElement = HTMLDivElement> = Nullable<T>;
