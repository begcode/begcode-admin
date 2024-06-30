<template>
  <div :class="prefixCls">
    <template v-for="color in colorList || []" :key="color">
      <span
        @click="!isDisabledColor && handleClick(color)"
        :class="[
          `${prefixCls}__item`,
          {
            [`${prefixCls}__item--active`]: def === color,
            [`${prefixCls}__item--black`]: color == '#ffffff',
            disabledColor: isDisabledColor,
          },
        ]"
        :style="{ background: color }"
      >
        <CheckOutlined />
      </span>
    </template>
  </div>
</template>
<script lang="ts" setup>
import type { PropType } from 'vue';
import { watch, ref } from 'vue';
import { CheckOutlined } from '@ant-design/icons-vue';

import { useDesign } from '@begcode/components';

import { baseHandler } from '../handler';
import { HandlerEnum } from '../enum';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { ThemeEnum } from '@/enums/appEnum';

defineOptions({ name: 'ThemeColorPicker' });

const props = defineProps({
  colorList: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  event: {
    type: Number as PropType<HandlerEnum>,
  },
  def: {
    type: String,
  },
});

const { getDarkMode } = useRootSetting();
const isDisabledColor = ref(false);

const { prefixCls } = useDesign('setting-theme-picker');

watch(
  () => getDarkMode.value,
  newValue => {
    isDisabledColor.value = props.event === 1 ? false : newValue === ThemeEnum.DARK;
  },
  { immediate: true },
);

function handleClick(color: string) {
  props.event && baseHandler(props.event, color);
}
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-setting-theme-picker';

.@{prefix-cls} {
  display: flex;
  flex-wrap: wrap;
  margin: 16px 0;
  justify-content: space-around;
  line-height: 1.3;

  &__item {
    width: 20px;
    height: 20px;
    cursor: pointer;
    border: 1px solid #ddd;
    border-radius: 2px;
    &.disabledColor {
      cursor: not-allowed;
      opacity: 0.5;
    }

    svg {
      display: none;
    }

    &--active {
      svg {
        display: inline-block;
        margin: 0 0 3px 3px;
        font-size: 12px;
        fill: @white !important;
      }
    }
    &--black {
      svg {
        fill: #000 !important;
      }
    }
  }
}
</style>
