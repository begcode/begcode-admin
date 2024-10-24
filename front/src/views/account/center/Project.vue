<template>
  <a-list :class="prefixCls">
    <a-row :gutter="16">
      <template v-for="item in projectList" :key="item.title">
        <a-col :span="6">
          <a-list-item>
            <a-card :hoverable="true" :class="`${prefixCls}__card`">
              <img :src="demoImg" />
              <div :class="`${prefixCls}__card-title`">
                {{ item.title }}
              </div>
              <div :class="`${prefixCls}__card-content`">
                {{ item.content }}
              </div>
            </a-card>
          </a-list-item>
        </a-col>
      </template>
    </a-row>
  </a-list>
</template>
<script lang="ts" setup>
import demoImg from '@/assets/images/demo.png';
import { projectList } from './data';

const prefixCls = 'account-center-project';
</script>
<style lang="less">
.account-center-project {
  &__card {
    width: 100%;

    .ant-card-body {
      padding: 0 0 24px 0;
    }

    img {
      width: 100%;
      height: 130px;
    }

    &-title {
      margin: 5px 10px;
      font-size: 16px;
      font-weight: 500;
      color: rgba(0, 0, 0, 0.85);
    }

    &-content {
      margin: 5px 10px;
    }
  }
}
</style>
