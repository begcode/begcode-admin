<template>
  <CollapseContainer title="账号绑定" :canExpand="false">
    <a-list>
      <template v-for="item in accountBindList" :key="item.key">
        <a-list-item>
          <a-list-item-meta>
            <template #avatar>
              <Icon v-if="item.avatar" class="avatar" :icon="item.avatar" :color="item.color" />
            </template>
            <template #title>
              {{ item.title }}
              <a-button type="link" size="small" v-if="item.extra" class="extra">
                {{ item.extra }}
              </a-button>
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
import { CollapseContainer } from '@/components/Container';
import { accountBindList } from './data';
</script>
<style lang="less" scoped>
.avatar {
  font-size: 40px !important;
}

.extra {
  float: right;
  margin-top: 10px;
  margin-right: 30px;
  cursor: pointer;
}
</style>
