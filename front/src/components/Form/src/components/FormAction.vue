<template>
  <a-col v-bind="actionColOpt" v-if="showActionButtonGroup">
    <div style="width: 100%" :style="{ textAlign: actionColOpt.style.textAlign }">
      <a-form-item :wrapperCol="{ span: 24 }">
        <slot name="resetBefore"></slot>
        <basic-button type="default" class="mr-2" v-bind="getResetBtnOptions" @click="resetAction" v-if="showResetButton">
          {{ getResetBtnOptions.text }}
        </basic-button>
        <slot name="submitBefore"></slot>

        <basic-button type="primary" class="mr-2" v-bind="getSubmitBtnOptions" @click="submitAction" v-if="showSubmitButton">
          {{ getSubmitBtnOptions.text }}
        </basic-button>

        <slot name="advanceBefore"></slot>
        <basic-button
          type="link"
          size="small"
          @click="toggleAdvanced"
          v-if="showAdvancedButton && !hideAdvanceBtn"
          data-cy="formAdvanceToggle"
        >
          {{ isAdvanced ? t('component.form.putAway') : t('component.form.unfold') }}
          <BasicArrow class="ml-1" :expand="!isAdvanced" up />
        </basic-button>
        <slot name="advanceAfter"></slot>
      </a-form-item>
    </div>
  </a-col>
</template>
<script lang="ts" setup>
import type { ColEx } from '../types';
import { ButtonProps } from '@/components/Button';
import { useFormContext } from '../hooks/useFormContext';
import { useI18n } from '@/hooks/web/useI18nOut';

defineOptions({ name: 'BasicFormAction' });

const props = defineProps({
  showActionButtonGroup: {
    type: Boolean,
    default: true,
  },
  showResetButton: {
    type: Boolean,
    default: true,
  },
  showSubmitButton: {
    type: Boolean,
    default: true,
  },
  showAdvancedButton: {
    type: Boolean,
    default: true,
  },
  resetButtonOptions: {
    type: Object as PropType<ButtonProps>,
    default: () => ({}),
  },
  submitButtonOptions: {
    type: Object as PropType<ButtonProps>,
    default: () => ({}),
  },
  actionColOptions: {
    type: Object as PropType<Partial<ColEx>>,
    default: () => ({}),
  },
  actionSpan: {
    type: Number,
    default: 8,
  },
  isAdvanced: {
    type: Boolean,
  },
  hideAdvanceBtn: {
    type: Boolean,
  },
  layout: {
    type: String as PropType<'horizontal' | 'vertical' | 'inline'>,
    default: 'horizontal',
  },
});

const emit = defineEmits(['toggle-advanced']);

const { t } = useI18n();
const { resetAction, submitAction } = useFormContext();

const actionColOpt = computed(() => {
  const { showAdvancedButton, actionSpan: span, actionColOptions } = props;
  const actionSpan = 24 - span;
  const advancedSpanObj = showAdvancedButton ? { span: actionSpan < 8 ? 24 : actionSpan } : {};
  const defaultSpan = props.layout === 'inline' ? {} : { span: showAdvancedButton ? 10 : 8 };
  const actionColOpt: Partial<ColEx> = {
    style: { textAlign: 'right' },
    ...defaultSpan,
    ...advancedSpanObj,
    ...actionColOptions,
  };
  return actionColOpt;
});
const getResetBtnOptions = computed((): ButtonProps => {
  return Object.assign(
    {
      text: t('common.resetText'),
      preIcon: 'ic:baseline-restart-alt',
    },
    props.resetButtonOptions,
  );
});

const getSubmitBtnOptions = computed((): ButtonProps => {
  return Object.assign(
    {
      text: t('common.queryText'),
      preIcon: 'ant-design:search-outlined',
    },
    props.submitButtonOptions,
  );
});

function toggleAdvanced() {
  emit('toggle-advanced');
}
</script>
<style>
.ml-1 {
  margin-left: 0.25rem;
}
</style>
