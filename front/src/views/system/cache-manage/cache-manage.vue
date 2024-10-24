<template>
  <a-card :bordered="false">
    <a-table :columns="columns" :data-source="data" rowKey="name">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'action'">
          <a-button type="primary" @click="clear(text)">
            <template #icon><Icon icon="ant-design:clear-outlined" /></template>
            清除
          </a-button>
        </template>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts" setup>
import { message } from 'ant-design-vue';
import apiService from '@/api-service';

const columns = [
  {
    title: '缓存名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '操作',
    key: 'action',
    dataIndex: 'action',
  },
];

const data = ref<any[]>([]);

function getAll() {
  apiService.system.cacheManagerService.getAll().then(res => {
    res.forEach(cacheName => data.value.push({ name: cacheName }));
  });
}

function clear(cacheName) {
  apiService.system.cacheManagerService.clear(cacheName).then(res => {
    message.success('清除缓存成功。');
  });
}

onMounted(() => {
  getAll();
});
</script>
