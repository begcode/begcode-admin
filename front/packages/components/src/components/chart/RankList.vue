<template>
  <div class="rank">
    <h4 class="title">{{ title }}</h4>
    <ul class="list" :style="{ height: height ? `${height}px` : 'auto', overflow: 'auto' }">
      <li :key="index" v-for="(item, index) in list">
        <span :class="index < 3 ? 'active' : null">{{ index + 1 }}</span>
        <span>{{ item.name }}</span>
        <span>{{ item.total }}</span>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent({
  name: 'RankList',
  props: {
    title: {
      type: String,
      default: '',
    },
    list: {
      type: Array,
      default: null,
    },
    height: {
      type: Number,
      default: null,
    },
  },
});
</script>
<style scoped>
.rank {
  padding: 0 32px 32px 72px;
}
.rank .list {
  margin: 25px 0 0;
  padding: 0;
  list-style: none;
}
.rank .list li {
  margin-top: 16px;
}
.rank .list li span {
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  line-height: 22px;
}
.rank .list li span:first-child {
  background-color: #f5f5f5;
  border-radius: 20px;
  display: inline-block;
  font-size: 12px;
  font-weight: 600;
  margin-right: 24px;
  height: 20px;
  line-height: 20px;
  width: 20px;
  text-align: center;
}
.rank .list li span.active {
  background-color: #314659;
  color: #fff;
}
.rank .list li span:last-child {
  float: right;
}
.mobile .rank {
  padding: 0 32px 32px 32px;
}
</style>
