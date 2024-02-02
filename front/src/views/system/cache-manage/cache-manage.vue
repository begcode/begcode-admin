<template>
  <Card :bordered="false">
    <Table :columns="columns" :data-source="data" rowKey="name">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'action'">
          <Button type="primary" @click="clear(text)">
            <template #icon><ClearOutlined /></template>
            清除
          </Button>
        </template>
      </template>
    </Table>
  </Card>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { ClearOutlined } from '@ant-design/icons-vue';
import { Card, Table, Button, message } from 'ant-design-vue';
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
    console.log('res', res);
    message.success('清除缓存成功。');
  });
}

onMounted(() => {
  getAll();
});
</script>
