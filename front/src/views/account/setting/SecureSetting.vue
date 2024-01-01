<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <template v-for="item in secureSettingList" :key="item.key">
        <List.Item>
          <List.Item.Meta>
            <template #title>
              {{ item.title }}
              <div
                class="float-right mt-10px mr-30px text-blue-500 text-font-normal cursor-pointer"
                v-if="item.extra"
                @click="extraClick(item.key)"
              >
                {{ item.extra }}
              </div>
            </template>
            <template #description>
              <div>{{ item.description }}</div>
            </template>
          </List.Item.Meta>
        </List.Item>
      </template>
    </List>
  </CollapseContainer>
  <UpdatePassword ref="updatePasswordRef" />
</template>
<script lang="ts" setup>
import { ref } from 'vue';
import { List } from 'ant-design-vue';
import { CollapseContainer } from '@begcode/components';

import { secureSettingList } from './data';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { useUserStore } from '@/store/modules/user';
import { useMessage } from '@/hooks/web/useMessage';
const UpdatePassword = createAsyncComponent(() => import('@/layouts/default/header/components/user-dropdown/UpdatePassword.vue'));
const { createMessage } = useMessage();
const userStore = useUserStore();
const updatePasswordRef = ref();
function extraClick(key) {
  if (key == '1') {
    updatePasswordRef.value.show(userStore.getUserInfo.login);
  } else {
    createMessage.warning('暂不支持');
  }
}
</script>
<style scoped>
.extra {
  float: right;
  margin-top: 10px;
  margin-right: 30px;
  font-weight: normal;
  color: #1890ff;
  cursor: pointer;
}
</style>
