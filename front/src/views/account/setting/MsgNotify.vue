<template>
  <CollapseContainer title="新消息通知" :canExpand="false">
    <a-list>
      <template v-for="item in msgNotifyList" :key="item.key">
        <a-list-item>
          <a-list-item-meta>
            <template #title>
              {{ item.title }}
              <a-switch class="float-right mt-10px mr-30px" checked-children="开" un-checked-children="关" default-checked />
            </template>
            <template #description>
              <div>{{ item.description }}</div>
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list>
  </CollapseContainer>
</template>
<script lang="ts" setup>
import { msgNotifyList } from './data';
</script>
<style lang="less" scoped>
.extra {
  float: right;
  margin-top: 10px;
  margin-right: 30px;
}
</style>
