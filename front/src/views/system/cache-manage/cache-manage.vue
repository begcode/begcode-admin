<template>
  <a-card :bordered="false">
    <a-table :columns="columns" :data-source="data" rowKey="name">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'action'">
          <a-button type="primary" @click="clear(text)">
            <template #icon><ClearOutlined /></template>
            清除
          </a-button>
        </template>
      </template>
    </a-table>
  </a-card>
</template>

<script>
import { ClearOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';

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

const data = [];

export default {
  components: {
    ClearOutlined,
  },
  data() {
    return {
      data,
      columns,
    };
  },
  created() {
    this.getAll();
  },
  methods: {
    getAll() {
      this.$apiService.system.cacheManagerService.getAll().then(res => {
        res.forEach(cacheName => this.data.push({ name: cacheName }));
      });
    },
    clear(cacheName) {
      this.$apiService.system.cacheManagerService.clear(cacheName).then(res => {
        message.success('清除缓存成功。');
      });
    },
  },
};
</script>
