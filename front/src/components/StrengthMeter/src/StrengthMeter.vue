<template>
  <div :class="prefixCls" class="relative">
    <a-input-password v-if="showInput" v-bind="$attrs" allowClear :value="innerValueRef" @change="handleChange" :disabled="disabled">
      <template #[item]="data" v-for="item in Object.keys($slots)">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </a-input-password>
    <div :class="`${prefixCls}-bar`">
      <div :class="`${prefixCls}-bar--fill`" :data-score="getPasswordStrength"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { theme } from 'ant-design-vue';
import { zxcvbn } from '@zxcvbn-ts/core';
import { useDesign } from '@/hooks/web/useDesign';

defineOptions({ name: 'StrengthMeter' });

const props = defineProps({
  value: {
    type: String,
  },
  showInput: {
    type: Boolean,
    default: true,
  },
  disabled: {
    type: Boolean,
  },
});

const emit = defineEmits(['score-change', 'change']);

const { useToken } = theme;
const { token } = useToken();

const innerValueRef = ref('');
const { prefixCls } = useDesign('strength-meter');

const getPasswordStrength = computed(() => {
  const { disabled } = props;
  if (disabled) return -1;
  const innerValue = unref(innerValueRef);
  const score = innerValue ? zxcvbn(unref(innerValueRef)).score : -1;
  emit('score-change', score);
  return score;
});

function handleChange(e) {
  emit('change', e.target.value);
  innerValueRef.value = e.target.value;
}

watchEffect(() => {
  innerValueRef.value = props.value || '';
});

watch(
  () => unref(innerValueRef),
  val => {
    emit('change', val);
  },
);
</script>
<style scoped>
.vben-strength-meter-bar {
  position: relative;
  height: 6px;
  margin: 10px auto 6px;
  background-color: v-bind('token.colorTextDisabled');
  border-radius: 6px;
}
.vben-strength-meter-bar::before,
.vben-strength-meter-bar::after {
  position: absolute;
  z-index: 10;
  display: block;
  width: 20%;
  height: inherit;
  background-color: transparent;
  border-color: v-bind('token.colorWhite');
  border-style: solid;
  border-width: 0 5px 0 5px;
  content: '';
}
.vben-strength-meter-bar::before {
  left: 20%;
}
.vben-strength-meter-bar::after {
  right: 20%;
}
.vben-strength-meter-bar--fill {
  position: absolute;
  width: 0;
  height: inherit;
  background-color: transparent;
  border-radius: inherit;
  transition:
    width 0.5s ease-in-out,
    background 0.25s;
}
.vben-strength-meter-bar--fill[data-score='0'] {
  width: 20%;
  background-color: #e64d4f;
}
.vben-strength-meter-bar--fill[data-score='1'] {
  width: 40%;
  background-color: v-bind('token["colorError"]');
}
.vben-strength-meter-bar--fill[data-score='2'] {
  width: 60%;
  background-color: v-bind('token["colorWarning"]');
}
.vben-strength-meter-bar--fill[data-score='3'] {
  width: 80%;
  background-color: v-bind('token["colorSuccess"]');
}
.vben-strength-meter-bar--fill[data-score='4'] {
  width: 100%;
  background-color: v-bind('token["colorSuccess"]');
}
</style>
