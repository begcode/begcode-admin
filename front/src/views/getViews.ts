const modules: Record<string, AsyncComponentLoader> = import.meta.glob('./**/*.vue');

const viewsMap = new Map<String, Component>();

for (const path in modules) {
  const componentName = path
    .split('/')
    .pop()
    ?.replace(/\.\w+$/, '') as string;
  const component = defineAsyncComponent(modules[path]);
  const name = _upperFirst(_camelCase(componentName));
  viewsMap.set(name, component);
}

export function getViewComponent(componentName: string) {
  return viewsMap.get(componentName);
}
