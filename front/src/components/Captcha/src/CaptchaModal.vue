<template>
  <BasicModal
    @register="registerModal"
    width="450px"
    :minHeight="100"
    :title="title"
    @ok="handleSubmit"
    destroyOnClose
    :canFullscreen="false"
  >
    <BasicForm @register="registerForm">
      <template #captchaSlot="{ model, field }">
        <div style="width: 100%; display: flex">
          <a-input style="width: 200px" v-model:value="model[field]" placeholder="请输入图片验证码" />
          <div class="margin-left10">
            <img
              class="pointer"
              v-if="randCodeData.requestCodeSuccess"
              style="margin-top: 2px; max-width: initial; height: 30px"
              :src="randCodeData.randCodeImage"
              @click="getCaptchaCode"
            />
            <img
              v-else
              class="pointer"
              style="margin-top: 2px; max-width: initial; height: 30px"
              :src="codeImage"
              @click="getCaptchaCode"
            />
          </div>
        </div>
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script lang="ts" setup>
import { BasicModal, useModalInner } from '@/components/Modal';
import { BasicForm, FormSchema, useForm } from '@/components/Form';
import codeImage from '@/assets/images/checkcode.png';
import { message } from 'ant-design-vue';

defineOptions({
  name: 'CaptchaModal',
});

const props = defineProps({
  imageCaptchaApi: {
    type: Function,
    default: null,
  },
  smsCaptchaApi: {
    type: Function,
    default: null,
  },
});

const emit = defineEmits(['register', 'ok']);

const title = ref<string>('验证码');
const schemas: FormSchema[] = [
  {
    field: 'captcha',
    component: 'Input',
    label: '图片验证码',
    rules: [{ required: true }],
    slot: 'captchaSlot',
  },
];
//表单配置
const [registerForm, { resetFields, validate }] = useForm({
  schemas: schemas,
  showActionButtonGroup: false,
  baseRowStyle: { 'justify-content': 'center', display: 'grid', 'margin-top': '10px' },
  rowProps: { justify: 'center' },
  labelCol: { span: 24 },
  wrapperCol: { span: 24 },
});

//表单赋值
const [registerModal, { setModalProps, closeModal }] = useModalInner(async data => {
  setModalProps({ confirmLoading: true });
  //重置表单
  await resetFields();
  await getCaptchaCode();
  setModalProps({ confirmLoading: false });
});

//存放二维码的数据
const randCodeData = reactive({
  randCodeImage: '',
  requestCodeSuccess: false,
  checkKey: -1,
});

/**
 * 获取验证码
 */
async function getCaptchaCode() {
  await resetFields();
  randCodeData.checkKey = new Date().getTime();
  props.imageCaptchaApi(randCodeData.checkKey).then(res => {
    randCodeData.randCodeImage = res;
    randCodeData.requestCodeSuccess = true;
  });
}

/**
 * 第三方配置点击事件
 */
async function handleSubmit() {
  let values = await validate();
  props
    .smsCaptchaApi({ captcha: values.captcha, checkKey: randCodeData.checkKey })
    .then(res => {
      if (res.success) {
        emit('ok');
        closeModal();
      } else {
        message.warning(res.message);
        getCaptchaCode();
      }
    })
    .catch(res => {
      message.warning(res.message);
      getCaptchaCode();
    });
}

/**
 * 关闭弹窗
 */
function handleCancel() {
  closeModal();
}
</script>

<style scoped>
.margin-left10 {
  margin-left: 10px;
}
:deep(.ant-row) {
  display: block;
}
</style>
