<template>
  <a-list item-layout="vertical" :class="prefixCls">
    <template v-for="item in articleList" :key="item.title">
      <a-list-item>
        <a-list-item-meta>
          <template #description>
            <div :class="`${prefixCls}__content`">
              {{ item.content }}
            </div>
          </template>
          <template #title>
            <p :class="`${prefixCls}__title`">
              {{ item.title }}
            </p>
            <div>
              <template v-for="tag in item.description" :key="tag">
                <a-tag class="mb-2">
                  {{ tag }}
                </a-tag>
              </template>
            </div>
          </template>
        </a-list-item-meta>
        <div>
          <template v-for="action in actions" :key="action.text">
            <div :class="`${prefixCls}__action`">
              <Icon v-if="action.icon" :class="`${prefixCls}__action-icon`" :icon="action.icon" :color="action.color" />
              {{ action.text }}
            </div>
          </template>
          <span :class="`${prefixCls}__time`">{{ item.time }}</span>
        </div>
      </a-list-item>
    </template>
  </a-list>
</template>
<script lang="ts" setup>
import { actions, articleList } from './data';
const prefixCls = 'account-center-article';
</script>
<style lang="less" scoped>
.account-center-article {
  &__title {
    margin-bottom: 12px;
    font-size: 18px;
  }

  &__content {
    color: rgba(0, 0, 0, 0.65);
  }

  &__action {
    display: inline-block;
    padding: 0 16px;
    color: rgba(0, 0, 0, 0.45);

    &:nth-child(1),
    &:nth-child(2) {
      border-right: 1px solid rgba(206, 206, 206, 0.4);
    }

    &-icon {
      margin-right: 3px;
    }
  }

  &__time {
    position: absolute;
    right: 20px;
    color: rgba(0, 0, 0, 0.45);
  }
}
</style>
