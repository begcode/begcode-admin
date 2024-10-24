<template>
  <a-list :class="prefixCls">
    <a-row :gutter="16">
      <template v-for="item in applicationList" :key="item.title">
        <a-col :span="6">
          <a-list-item>
            <a-card :hoverable="true" :class="`${prefixCls}__card`">
              <div :class="`${prefixCls}__card-title`">
                <Icon class="icon" v-if="item.icon" :icon="item.icon" :color="item.color" />
                {{ item.title }}
              </div>
              <div :class="`${prefixCls}__card-num`">
                活跃用户：<span>{{ item.active }}</span> 万
              </div>
              <div :class="`${prefixCls}__card-num`">
                新增用户：<span>{{ item.new }}</span>
              </div>
              <Icon :class="`${prefixCls}__card-download`" v-if="item.download" :icon="item.download" />
            </a-card>
          </a-list-item>
        </a-col>
      </template>
    </a-row>
  </a-list>
</template>
<script lang="ts" setup>
import { applicationList } from './data';

const prefixCls = 'account-center-application';
</script>
<style lang="less">
.account-center-application {
  &__card {
    width: 100%;
    margin-bottom: -12px;

    .ant-card-body {
      padding: 16px;
    }

    &-title {
      margin-bottom: 5px;
      font-size: 16px;
      font-weight: 500;

      .icon {
        margin-top: -5px;
        font-size: 22px;
      }
    }

    &-num {
      margin-left: 24px;
      line-height: 36px;
      color: @text-color-secondary;

      span {
        margin-left: 5px;
        font-size: 18px;
      }
    }

    &-download {
      float: right;
      font-size: 20px !important;
      color: @primary-color;
    }
  }
}
</style>
