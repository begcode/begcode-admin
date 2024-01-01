<template>
  <Dropdown placement="bottomLeft" :overlayClassName="`${prefixCls}-dropdown-overlay`">
    <span :class="[prefixCls, `${prefixCls}--${theme}`]" class="flex">
      <img :class="`${prefixCls}__header`" :src="getAvatarUrl" />
      <span :class="`${prefixCls}__info hidden md:block`">
        <span :class="`${prefixCls}__name`" class="truncate">
          {{ getUserInfo.firstName }}
        </span>
      </span>
    </span>

    <template #overlay>
      <Menu @click="handleMenuClick">
        <MenuItem key="doc" :text="t('layout.header.dropdownItemDoc')" icon="ion:document-text-outline" v-if="getShowDoc" />
        <Menu.Divider v-if="getShowDoc" />
        <MenuItem v-if="getShowApi" key="api" :text="t('layout.header.dropdownChangeApi')" icon="ant-design:swap-outlined" />
        <MenuItem key="account" :text="t('layout.header.dropdownItemSwitchAccount')" icon="ant-design:setting-outlined" />
        <MenuItem key="password" :text="t('layout.header.dropdownItemSwitchPassword')" icon="ant-design:edit-outlined" />
        <!--        <MenuItem key="depart" :text="t('layout.header.dropdownItemSwitchDepart')" icon="ant-design:cluster-outlined" />-->
        <MenuItem v-if="getUseLockPage" key="lock" :text="t('layout.header.tooltipLock')" icon="ion:lock-closed-outline" />
        <MenuItem key="logout" :text="t('layout.header.dropdownItemLoginOut')" icon="ion:power-outline" />
      </Menu>
    </template>
  </Dropdown>
  <LockAction ref="lockActionRef" @register="register" />
  <ChangeApi @register="registerApi" />
  <UpdatePassword v-if="passwordVisible" ref="updatePasswordRef" />
</template>
<script lang="ts" setup>
import { computed, ref } from 'vue';
import { Dropdown, Menu } from 'ant-design-vue';
import type { MenuInfo } from 'ant-design-vue/lib/menu/src/interface';

import { SITE_URL } from '@/settings/siteSetting';

import { useUserStore } from '@/store/modules/user';
import { useHeaderSetting } from '@/hooks/setting/useHeaderSetting';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { useGo } from '@/hooks/web/usePage';

import headerImg from '@/assets/images/header.jpg';
import { propTypes, openWindow, useDesign, useModal } from '@begcode/components';

import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';

import { getFileAccessHttpUrl, getRefPromise } from '@begcode/components';

type MenuEvent = 'logout' | 'doc' | 'lock' | 'cache' | 'depart' | 'account' | 'password' | 'api';

const MenuItem = createAsyncComponent(() => import('./DropMenuItem.vue'));
const LockAction = createAsyncComponent(() => import('../lock/LockModal.vue'));
const UpdatePassword = createAsyncComponent(() => import('./UpdatePassword.vue'));
const ChangeApi = createAsyncComponent(() => import('../ChangeApi/index.vue'));

defineOptions({ name: 'UserDropdown' });

defineProps({
  theme: propTypes.oneOf(['dark', 'light']),
});

const { t } = useI18n();
const { createMessage } = useMessage();

const { prefixCls } = useDesign('header-user-dropdown');
const { getShowDoc, getUseLockPage, getShowApi } = useHeaderSetting();
const userStore = useUserStore();
const go = useGo();
const passwordVisible = ref(false);
const lockActionVisible = ref(false);
const lockActionRef = ref(null);

const getUserInfo = computed(() => {
  const { firstName = '', imageUrl, desc } = userStore.getUserInfo || {};
  return { firstName, avatar: imageUrl || headerImg, desc };
});

const getAvatarUrl = computed(() => {
  let { avatar } = getUserInfo.value;
  if (avatar === headerImg) {
    return avatar;
  } else {
    return getFileAccessHttpUrl(avatar);
  }
});

const [register, { openModal }] = useModal();
const [registerApi, { openModal: openApiModal }] = useModal();

/**
 * 多部门弹窗逻辑
 */
const loginSelectRef = ref();

async function handleLock() {
  await getRefPromise(lockActionRef);
  openModal(true);
}

function handleApi() {
  openApiModal(true, {});
}

//  login out
function handleLoginOut() {
  userStore.confirmLoginOut();
}

// open doc
function openDoc() {
  openWindow(SITE_URL);
}
// 切换部门
function updateCurrentDepart() {
  loginSelectRef.value.show();
}
// 修改密码
const updatePasswordRef = ref();
async function updatePassword() {
  passwordVisible.value = true;
  await getRefPromise(updatePasswordRef);
  updatePasswordRef.value.show(userStore.getUserInfo.login);
}

function handleMenuClick(e: MenuInfo) {
  switch (e.key as MenuEvent) {
    case 'logout':
      handleLoginOut();
      break;
    case 'doc':
      openDoc();
      break;
    case 'lock':
      handleLock();
      break;
    case 'depart':
      updateCurrentDepart();
      break;
    case 'password':
      updatePassword();
      break;
    case 'account':
      go(`/account/settings`);
      break;
    case 'api':
      handleApi();
      break;
  }
}
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-header-user-dropdown';

.@{prefix-cls} {
  align-items: center;
  height: @header-height;
  padding: 0 0 0 10px;
  padding-right: 10px;
  overflow: hidden;
  font-size: 12px;
  cursor: pointer;

  img {
    width: 24px;
    height: 24px;
    margin-right: 12px;
  }

  &__header {
    border-radius: 50%;
  }

  &__name {
    font-size: 14px;
  }

  &--dark {
    &:hover {
      background-color: @header-dark-bg-hover-color;
    }
  }

  &--light {
    &:hover {
      background-color: @header-light-bg-hover-color;
    }

    .@{prefix-cls}__name {
      color: @text-color-base;
    }

    .@{prefix-cls}__desc {
      color: @header-light-desc-color;
    }
  }

  &-dropdown-overlay {
    .ant-dropdown-menu-item {
      min-width: 160px;
    }
  }
}
</style>
