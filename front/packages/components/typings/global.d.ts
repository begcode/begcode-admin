declare module '@vue/runtime-core' {
  export interface GlobalComponents {
    Bar: (typeof import('@begcode/components'))['Bar'];
    BasicArrow: (typeof import('@begcode/components'))['BasicArrow'];
    BasicTitle: (typeof import('@begcode/components'))['BasicTitle'];
    BasicHelp: (typeof import('@begcode/components'))['BasicHelp'];
  }
}
