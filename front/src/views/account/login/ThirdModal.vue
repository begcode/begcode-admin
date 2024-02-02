<template>
  <!-- 第三方登录绑定账号密码输入弹框 -->
  <Modal title="请输入密码" v-model:open="thirdPasswordShow" @ok="thirdLoginCheckPassword" @cancel="thirdLoginNoPassword">
    <InputPassword placeholder="请输入密码" v-model:value="thirdLoginPassword" style="margin: 15px; width: 80%" />
  </Modal>

  <!-- 第三方登录提示是否绑定账号弹框 -->
  <Modal :footer="null" :closable="false" v-model:open="thirdConfirmShow" :class="'ant-modal-confirm'">
    <div class="ant-modal-confirm-body-wrapper">
      <div class="ant-modal-confirm-body">
        <QuestionCircleFilled style="color: #faad14" />
        <span class="ant-modal-confirm-title">提示</span>
        <div class="ant-modal-confirm-content">已有同名账号存在,请确认是否绑定该账号？</div>
      </div>
      <div class="ant-modal-confirm-btns">
        <Button @click="thirdLoginUserCreate" :loading="thirdCreateUserLoding">创建新账号</Button>
        <Button @click="thirdLoginUserBind" type="primary">确认绑定</Button>
      </div>
    </div>
  </Modal>

  <!-- 第三方登录绑定手机号 -->
  <Modal title="绑定手机号" v-model:open="bindingPhoneModal" :maskClosable="false">
    <Form class="p-4 enter-x" style="margin: 15px 10px">
      <FormItem class="enter-x">
        <Input size="large" placeholder="请输入手机号" v-model:value="thirdPhone" class="fix-auto-fill">
          <template #prefix>
            <Icon icon="ant-design:mobile-outlined" :style="{ color: 'rgba(0,0,0,.25)' }"></Icon>
          </template>
        </Input>
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="thirdCaptcha"
          placeholder="请输入验证码"
          :sendCodeApi="sendCodeApi"
        >
          <template #prefix>
            <Icon icon="ant-design:mail-outlined" :style="{ color: 'rgba(0,0,0,.25)' }"></Icon>
          </template>
        </CountdownInput>
      </FormItem>
    </Form>
    <template #footer>
      <Button type="primary" @click="thirdHandleOk">确定</Button>
    </template>
  </Modal>
</template>
<script lang="ts" setup>
import { ref, unref } from 'vue';
import { Form, Input, Button, InputPassword, FormItem, Modal } from 'ant-design-vue';
import { CountdownInput } from '@begcode/components';
import { useThirdLogin } from '@/hooks/system/useThirdLogin';
import { QuestionCircleFilled } from '@ant-design/icons-vue';
import { defHttp } from '@/utils/http/axios';
import { useGlobSetting } from '@/hooks/setting';
import { useMessage } from '@/hooks/web/useMessage';
import { useUserStore } from '@/store/modules/user';
import { setThirdCaptcha, getSmsCaptcha } from '@/api-service/sys/user';
import { useI18n } from '@/hooks/web/useI18n';

defineOptions({
  name: 'ThirdModal',
});

const { createMessage, notification } = useMessage();
const { t } = useI18n();
const glob = useGlobSetting();
const userStore = useUserStore();
//第三方类型
const thirdType = ref('');
//第三方登录相关信息
const thirdLoginInfo = ref<any>({});
//状态
const thirdLoginState = ref(false);
//绑定手机号弹窗
const bindingPhoneModal = ref(false);
//第三方用户UUID
const thirdUserUuid = ref('');
//提示窗
const thirdConfirmShow = ref(false);
//绑定密码弹窗
const thirdPasswordShow = ref(false);
//绑定密码
const thirdLoginPassword = ref('');
//绑定用户
const thirdLoginUser = ref('');
//加载中
const thirdCreateUserLoding = ref(false);
//绑定手机号
const thirdPhone = ref('');
//验证码
const thirdCaptcha = ref('');
//第三方登录
function onThirdLogin(source) {
  let url = `${glob.uploadUrl}/sys/thirdLogin/render/${source}`;
  window.open(
    url,
    `login ${source}`,
    'height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no',
  );
  thirdType.value = source;
  thirdLoginInfo.value = {};
  thirdLoginState.value = false;
  let receiveMessage = function (event) {
    let token = event.data;
    if (typeof token === 'string') {
      //如果是字符串类型 说明是token信息
      if (token === '登录失败') {
        createMessage.warning(token);
      } else if (token.includes('绑定手机号')) {
        bindingPhoneModal.value = true;
        let strings = token.split(',');
        thirdUserUuid.value = strings[1];
      } else {
        doThirdLogin(token);
      }
    } else if (typeof token === 'object') {
      //对象类型 说明需要提示是否绑定现有账号
      if (token['isObj'] === true) {
        thirdConfirmShow.value = true;
        thirdLoginInfo.value = { ...token };
      }
    } else {
      createMessage.warning('不识别的信息传递');
    }
  };
  window.addEventListener('message', receiveMessage, false);
}
// 根据token执行登录
function doThirdLogin(token) {
  if (unref(thirdLoginState) === false) {
    thirdLoginState.value = true;
    userStore.ThirdLogin({ token, thirdType: unref(thirdType) }).then(res => {
      console.log('res====>doThirdLogin', res);
      if (res && res.userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${res.userInfo.realname}`,
          duration: 3,
        });
      } else {
        requestFailed(res);
      }
    });
  }
}

function requestFailed(err) {
  notification.error({
    message: '登录失败',
    description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
    duration: 4,
  });
}
// 绑定已有账号 需要输入密码
function thirdLoginUserBind() {
  thirdLoginPassword.value = '';
  thirdLoginUser.value = thirdLoginInfo.value.uuid;
  thirdConfirmShow.value = false;
  thirdPasswordShow.value = true;
}
//创建新账号
function thirdLoginUserCreate() {
  thirdCreateUserLoding.value = true;
  // 账号名后面添加两位随机数
  thirdLoginInfo.value.suffix = parseInt(Math.random() * 98 + 1);
  defHttp
    .post({ url: '/sys/third/user/create', params: { thirdLoginInfo: unref(thirdLoginInfo) } }, { isTransformResponse: false })
    .then(res => {
      if (res.success) {
        let token = res.result;
        doThirdLogin(token);
        thirdConfirmShow.value = false;
      } else {
        createMessage.warning(res.message);
      }
    })
    .finally(() => {
      thirdCreateUserLoding.value = false;
    });
}
// 核实密码
function thirdLoginCheckPassword() {
  let params = Object.assign({}, unref(thirdLoginInfo), { password: unref(thirdLoginPassword) });
  defHttp.post({ url: '/sys/third/user/checkPassword', params }, { isTransformResponse: false }).then(res => {
    if (res.success) {
      thirdLoginNoPassword();
      doThirdLogin(res.result);
    } else {
      createMessage.warning(res.message);
    }
  });
}
// 没有密码 取消操作
function thirdLoginNoPassword() {
  thirdPasswordShow.value = false;
  thirdLoginPassword.value = '';
  thirdLoginUser.value = '';
}

//倒计时执行前的函数
function sendCodeApi() {
  //return setThirdCaptcha({mobile:unref(thirdPhone)});
  return getSmsCaptcha({ mobile: unref(thirdPhone), smsmode: '0' });
}
//绑定手机号点击确定按钮
function thirdHandleOk() {
  if (!unref(thirdPhone)) {
    cmsFailed('请输入手机号');
  }
  if (!unref(thirdCaptcha)) {
    cmsFailed('请输入验证码');
  }
  let params = {
    mobile: unref(thirdPhone),
    captcha: unref(thirdCaptcha),
    thirdUserUuid: unref(thirdUserUuid),
  };
  defHttp.post({ url: '/sys/thirdLogin/bindingThirdPhone', params }, { isTransformResponse: false }).then(res => {
    if (res.success) {
      bindingPhoneModal.value = false;
      doThirdLogin(res.result);
    } else {
      createMessage.warning(res.message);
    }
  });
}
function cmsFailed(err) {
  notification.error({
    message: '登录失败',
    description: err,
    duration: 4,
  });
  return;
}
</script>
