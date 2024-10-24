<template>
  <div :class="[`${prefixCls}`]">
    <div class="my-account">账户</div>
    <div class="account-row-item clearfix">
      <div class="account-label gray-75">手机</div>
      <span class="gray" v-if="userDetail.mobileText">{{ userDetail.mobile }}</span>
      <span class="pointer blue-e5 phone-margin" @click="updatePhone" v-if="userDetail.mobile">修改</span>
      <span class="pointer blue-e5 phone-margin" @click="bindPhone" v-else>绑定</span>
      <span class="pointer blue-e5" @click="unbindPhone" v-if="userDetail.mobile">解绑</span>
      <span class="pointer blue-e5" @click="updatePhone" v-else>绑定</span>
    </div>
    <div class="account-row-item clearfix">
      <div class="account-label gray-75">邮箱</div>
      <span class="gray">{{ userDetail.email ? userDetail.email : '未填写' }}</span>
      <span class="pointer blue-e5 phone-margin" @click="updateEmail">修改</span>
      <span class="pointer blue-e5" @click="unbindEmail" v-if="userDetail.email">解绑</span>
      <span class="pointer blue-e5" @click="unbindEmail" v-else>绑定</span>
      <span class="pointer blue-e5" style="margin-left: 5px" @click="checkEmail" v-if="userDetail.email">验证</span>
    </div>
  </div>

  <UserReplacePhoneModal @register="registerModal" @success="initUserDetail" />
  <UserReplaceEmailModal @register="registerEmailModal" @success="initUserDetail" />
</template>
<script lang="ts" setup>
import { useUserStore } from '@/store/modules/user';
import UserReplacePhoneModal from './commponents/UserPhoneModal.vue';
import UserReplaceEmailModal from './commponents/UserEmailModal.vue';
import { useModal } from '@/components/Modal';
import { useDesign } from '@/hooks/web/useDesign';
import accountService from '@/api-service/account/account.service';
const { prefixCls } = useDesign('j-user-account-setting-container');

const userDetail = ref<any>([]);
const userStore = useUserStore();
const [registerModal, { openModal }] = useModal();
const [registerEmailModal, { openModal: openEmailModal }] = useModal();

const wechatData = reactive<any>({
  bindWechat: false,
  name: '昵称',
});

/**
 * 初始化用户数据
 */
function initUserDetail() {
  //获取用户数据
  accountService.getAccount().then(data => {
    if (data.mobile) {
      userDetail.value.mobileText = data.mobile.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
    }
    userDetail.value = data;
  });
}

/**
 * 修改手机号
 */
function updatePhone() {
  openModal(true, {
    record: {
      mobile: userDetail.value.mobile,
      login: userDetail.value.login,
      id: userDetail.value.id,
      mobileText: userDetail.value.mobileText,
    },
  });
}

/**
 * 修改邮箱
 */
function updateEmail() {
  openEmailModal(true, {
    record: { email: userDetail.value.email, id: userDetail.value.id },
  });
}

/**
 * 手机号解绑
 */
function unbindPhone() {
  console.log('手机号解绑');
}

/**
 * 邮箱解绑
 */
function unbindEmail() {
  console.log('邮箱解绑');
}

/**
 * 邮箱验证
 */
function checkEmail() {
  console.log('邮箱验证');
}

/**
 * 绑定手机号
 */
function bindPhone() {
  openModal(true, {
    record: { username: userDetail.value.username, id: userDetail.value.id },
  });
}

/**
 * 微信绑定解绑事件
 */
function wechatBind() {
  console.log('微信绑定解绑事件');
}

onMounted(() => {
  initUserDetail();
});
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-j-user-account-setting-container';
.@{prefix-cls} {
  padding: 30px 40px 0 20px;

  .account-row-item {
    align-items: center;
    /*begin 兼容暗夜模式*/
    border-bottom: 1px solid @border-color-base;
    /*end 兼容暗夜模式*/
    box-sizing: border-box;
    display: flex;
    height: 71px;
    position: relative;
  }

  .account-label {
    text-align: left;
    width: 160px;
  }

  .gray-75 {
    /*begin 兼容暗夜模式*/
    color: @text-color !important;
    /*end 兼容暗夜模式*/
  }

  .pointer {
    cursor: pointer;
  }

  .blue-e5 {
    color: #1e88e5;
  }

  .phone-margin {
    margin-left: 24px;
    margin-right: 24px;
  }

  .clearfix:after {
    clear: both;
  }

  .clearfix:before {
    content: '';
    display: table;
  }

  .account-padding {
    padding: 30px 40px 0 20px;
  }

  .my-account {
    font-size: 17px;
    font-weight: 700 !important;
    /*begin 兼容暗夜模式*/
    color: @text-color;
    /*end 兼容暗夜模式*/
    margin-bottom: 20px;
  }
}
</style>
