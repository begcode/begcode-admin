<template>
  <Card title="项目" v-bind="$attrs">
    <template #extra>
      <Button type="link" size="small">更多</Button>
    </template>

    <template v-for="item in groupItems" :key="item.title">
      <CardGrid class="!md:w-1/3 !w-full">
        <span class="flex">
          <Icon :icon="item.icon" :color="item.color" size="30" />
          <span class="text-lg ml-4">{{ item.title }}</span>
        </span>
        <div class="flex mt-2 h-10 text-secondary">{{ item.desc }}</div>
        <div class="flex justify-between text-secondary">
          <span>{{ item.group }}</span>
          <span>{{ item.date }}</span>
        </div>
      </CardGrid>
    </template>
  </Card>
</template>
<script lang="ts" setup>
import { Card, CardGrid, Button } from 'ant-design-vue';
import { Icon } from '@begcode/components';
import { groupItems } from './data';
</script>
