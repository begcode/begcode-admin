<template>
  <Card title="快捷导航">
    <template v-for="item in navItems" :key="item.title">
      <CardGrid>
        <span class="flex flex-col items-center">
          <Icon :icon="item.icon" :color="item.color" size="20" />
          <span class="text-md mt-2 truncate">{{ item.title }}</span>
        </span>
      </CardGrid>
    </template>
  </Card>
</template>
<script lang="ts" setup>
import { Card, CardGrid } from 'ant-design-vue';
import { navItems } from './data';
import { Icon } from '@begcode/components';
</script>
