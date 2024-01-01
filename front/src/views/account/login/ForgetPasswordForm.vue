<template>
  <template v-if="getShow">
    <!--节点-->
    <a-steps style="margin-bottom: 20px" :current="currentTab">
      <a-step title="手机验证" />
      <a-step title="更改密码" />
      <a-step title="完成" />
    </a-steps>
    <!--组件-->
    <div>
      <step1 v-if="currentTab === 0" @nextStep="nextStep" />
      <step2 v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep" :accountInfo="accountInfo" />
      <step3 v-if="currentTab === 2" @prevStep="prevStep" @finish="finish" />
    </div>
  </template>
</template>
<script lang="ts" setup>
import { reactive, ref, computed, unref } from 'vue';
import { Form } from 'ant-design-vue';
import { useI18n } from '@/hooks/web/useI18n';
import { useLoginState, useFormRules, LoginStateEnum } from './useLogin';
import step1 from '../forget-password/step1.vue';
import step2 from '../forget-password/step2.vue';
import step3 from '../forget-password/step3.vue';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const FormItem = Form.Item;
const { t } = useI18n();
const { handleBackLogin, getLoginState } = useLoginState();
const { getFormRules } = useFormRules();

const formRef = ref();
const loading = ref(false);
const currentTab = ref(0);
const formData = reactive({
  login: '',
  mobile: '',
  code: '',
});

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);

async function handleReset() {
  const form = unref(formRef);
  if (!form) return;
  await form.resetFields();
}
const accountInfo = reactive({
  obj: {
    login: '',
    mobile: '',
    code: '',
  },
});
/**
 * 下一步
 * @param data
 */
function nextStep(data) {
  accountInfo.obj = data;
  if (currentTab.value < 4) {
    currentTab.value += 1;
  }
}
/**
 * 上一步
 * @param data
 */
function prevStep(data) {
  accountInfo.obj = data;
  if (currentTab.value > 0) {
    currentTab.value -= 1;
  }
}
/**
 * 结束
 */
function finish() {
  currentTab.value = 0;
}
</script>
