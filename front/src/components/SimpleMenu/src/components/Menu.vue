<template>
  <ul :class="getClass">
    <slot></slot>
  </ul>
</template>

<script lang="ts" setup>
import type { SubMenuProvider } from './types';
import { useDesign } from '@/hooks/web/useDesign';
import { createSimpleRootMenuContext, type MenuEmitterEvents } from './useSimpleMenuContext';
import { mitt } from '@/utils/mitt';

defineOptions({ name: 'Menu' });

const props = defineProps({
  theme: {
    type: String as PropType<'light' | 'dark'>,
    default: 'light',
  },
  activeName: {
    type: [String, Number],
    default: '',
  },
  openNames: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  accordion: {
    type: Boolean,
    default: true,
  },
  width: {
    type: String as PropType<string>,
    default: '100%',
  },
  collapsedWidth: {
    type: String as PropType<string>,
    default: '48px',
  },
  indentSize: {
    type: Number as PropType<number>,
    default: 16,
  },
  collapse: {
    type: Boolean,
    default: true,
  },
  activeSubMenuNames: {
    type: Array as PropType<(string | number)[]>,
    default: () => [],
  },
});

const emit = defineEmits(['select', 'open-change']);

const rootMenuEmitter = mitt<MenuEmitterEvents>();
const instance = getCurrentInstance();

const currentActiveName = ref<string | number>('');
const openedNames = ref<(string | number)[]>([]);

const { prefixCls } = useDesign('menu');
const isRemoveAllPopup = ref(false);

createSimpleRootMenuContext({
  rootMenuEmitter: rootMenuEmitter,
  activeName: currentActiveName,
});

const getClass = computed(() => {
  const { theme } = props;
  return [
    prefixCls,
    `${prefixCls}-${theme}`,
    `${prefixCls}-vertical`,
    {
      [`${prefixCls}-collapse`]: props.collapse,
    },
  ];
});

watchEffect(() => {
  openedNames.value = props.openNames;
});

watchEffect(() => {
  if (props.activeName) {
    currentActiveName.value = props.activeName;
  }
});

watch(
  () => props.openNames,
  () => {
    nextTick(() => {
      updateOpened();
    });
  },
);

function updateOpened() {
  rootMenuEmitter.emit('on-update-opened', openedNames.value);
}

function addSubMenu(name: string | number) {
  if (openedNames.value.includes(name)) return;
  openedNames.value.push(name);
  updateOpened();
}
function removeSubMenu(name: string | number) {
  openedNames.value = openedNames.value.filter(item => item !== name);
  updateOpened();
}
function removeAll() {
  openedNames.value = [];
  updateOpened();
}
function sliceIndex(index: number) {
  if (index === -1) return;
  openedNames.value = openedNames.value.slice(0, index + 1);
  updateOpened();
}
provide<SubMenuProvider>(`subMenu:${instance?.uid}`, {
  addSubMenu,
  removeSubMenu,
  getOpenNames: () => openedNames.value,
  removeAll,
  isRemoveAllPopup,
  sliceIndex,
  level: 0,
  props: props as any,
});

onMounted(() => {
  openedNames.value = !props.collapse ? [...props.openNames] : [];
  updateOpened();
  rootMenuEmitter.on('on-menu-item-select', (name: string | number) => {
    currentActiveName.value = name;
    nextTick(() => {
      props.collapse && removeAll();
    });
    emit('select', name);
  });
  rootMenuEmitter.on('open-name-change', ({ name, opened }) => {
    if (opened && !openedNames.value.includes(name)) {
      openedNames.value.push(name);
    } else if (!opened) {
      const index = openedNames.value.findIndex(item => item === name);
      index !== -1 && openedNames.value.splice(index, 1);
    }
  });
});
</script>
<style lang="less">
@import url('./menu.less');
</style>
