<template>
  <Col v-bind="actionColOpt" v-if="showActionButtonGroup">
    <div style="width: 100%" :style="{ textAlign: actionColOpt.style.textAlign }">
      <Form.Item :wrapperCol="{ span: 24 }">
        <slot name="resetBefore"></slot>
        <Button type="default" class="mr-2" v-bind="getResetBtnOptions" @click="resetAction" v-if="showResetButton">
          {{ getResetBtnOptions.text }}
        </Button>
        <slot name="submitBefore"></slot>

        <Button type="primary" class="mr-2" v-bind="getSubmitBtnOptions" @click="submitAction" v-if="showSubmitButton">
          {{ getSubmitBtnOptions.text }}
        </Button>

        <slot name="advanceBefore"></slot>
        <Button type="link" size="small" @click="toggleAdvanced" v-if="showAdvancedButton && !hideAdvanceBtn" data-cy="formAdvanceToggle">
          {{ isAdvanced ? t('component.form.putAway') : t('component.form.unfold') }}
          <BasicArrow class="ml-1" :expand="!isAdvanced" up />
        </Button>
        <slot name="advanceAfter"></slot>
      </Form.Item>
    </div>
  </Col>
</template>
<script lang="ts" setup>
import type { ColEx } from '../types';
import { computed, PropType } from 'vue';
import { Form, Col } from 'ant-design-vue';
import { Button, ButtonProps } from '@/components/Button';
import { BasicArrow } from '@/components/Basic';
import { useFormContext } from '../hooks/useFormContext';
import { useI18n } from '@/hooks/web/useI18nOut';
import { propTypes } from '@/utils/propTypes';

defineOptions({ name: 'BasicFormAction' });

const props = defineProps({
  showActionButtonGroup: propTypes.bool.def(true),
  showResetButton: propTypes.bool.def(true),
  showSubmitButton: propTypes.bool.def(true),
  showAdvancedButton: propTypes.bool.def(true),
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
  actionSpan: propTypes.number.def(8),
  isAdvanced: propTypes.bool,
  hideAdvanceBtn: propTypes.bool,
  layout: propTypes.oneOf(['horizontal', 'vertical', 'inline']).def('horizontal'),
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
