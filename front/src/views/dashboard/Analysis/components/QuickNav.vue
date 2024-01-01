<template>
  <Card title="快捷导航" v-bind="$attrs">
    <template v-for="item in navItems" :key="item">
      <CardGrid @click="goPage">
        <span class="flex flex-col items-center">
          <Icon :icon="item.icon" :color="item.color" size="20" />
          <span class="text-md mt-2">{{ item.title }}</span>
        </span>
      </CardGrid>
    </template>
  </Card>
</template>
<script lang="ts" setup>
import { Card } from 'ant-design-vue';
import { useMessage } from '@/hooks/web/useMessage';
import { Icon } from '@begcode/components';

const CardGrid = Card.Grid;
const $message = useMessage();
const navItems = [
  {
    title: '业务受理',
    icon: 'ion:home-outline',
    color: '#1fdaca',
  },
  {
    title: '业务管理',
    icon: 'ion:grid-outline',
    color: '#bf0c2c',
  },
  {
    title: '文件管理',
    icon: 'ion:layers-outline',
    color: '#e18525',
  },
  {
    title: '信息查询',
    icon: 'ion:settings-outline',
    color: '#3fb27f',
  },
];

function goPage() {
  $message.createMessage.success('根据业务自行处理跳转页面!');
}
</script>
