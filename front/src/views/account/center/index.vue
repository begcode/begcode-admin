<template>
  <div :class="prefixCls">
    <a-row :class="`${prefixCls}-top`">
      <a-col :span="9" :class="`${prefixCls}-col`">
        <a-row>
          <a-col :span="8">
            <div :class="`${prefixCls}-top__avatar`">
              <img width="70" :src="avatar" />
              <span>BegCode</span>
              <div>海纳百川，有容乃大</div>
            </div>
          </a-col>
          <a-col :span="16">
            <div :class="`${prefixCls}-top__detail`">
              <template v-for="detail in details" :key="detail.title">
                <p>
                  <Icon :icon="detail.icon" />
                  {{ detail.title }}
                </p>
              </template>
            </div>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="7" :class="`${prefixCls}-col`">
        <CollapseContainer title="标签" :canExpand="false">
          <template v-for="tag in tags" :key="tag">
            <a-tag class="mb-2">
              {{ tag }}
            </a-tag>
          </template>
        </CollapseContainer>
      </a-col>
      <a-col :span="8" :class="`${prefixCls}-col`">
        <CollapseContainer :class="`${prefixCls}-top__team`" title="团队" :canExpand="false">
          <div v-for="(team, index) in teams" :key="index" :class="`${prefixCls}-top__team-item`">
            <Icon :icon="team.icon" :color="team.color" />
            <span>{{ team.title }}</span>
          </div>
        </CollapseContainer>
      </a-col>
    </a-row>
    <div :class="`${prefixCls}-bottom`">
      <a-tabs>
        <template v-for="item in achieveList" :key="item.key">
          <a-tab-pane :tab="item.name">
            <component :is="tabs[item.component]" />
          </a-tab-pane>
        </template>
      </a-tabs>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { CollapseContainer } from '@/components/Container';
import Article from './Article.vue';
import Application from './Application.vue';
import Project from './Project.vue';

import headerImg from '@/assets/images/header.jpg';
import { tags, teams, details, achieveList } from './data';
import { useUserStore } from '@/store/modules/user';

const userStore = useUserStore();

const tabs = {
  Article,
  Application,
  Project,
};

const prefixCls = 'account-center';

const avatar = computed(() => userStore.getUserInfo.avatar || headerImg);
</script>
<style lang="less" scoped>
.account-center {
  &-col:not(:last-child) {
    padding: 0 10px;

    &:not(:last-child) {
      border-right: 1px dashed rgb(206, 206, 206, 0.5);
    }
  }

  &-top {
    padding: 10px;
    margin: 16px 16px 12px 16px;
    background-color: @component-background;
    border-radius: 3px;

    &__avatar {
      text-align: center;

      img {
        margin: auto;
        border-radius: 50%;
      }

      span {
        display: block;
        font-size: 20px;
        font-weight: 500;
      }

      div {
        margin-top: 3px;
        font-size: 12px;
      }
    }

    &__detail {
      padding-left: 20px;
      margin-top: 15px;
    }

    &__team {
      &-item {
        display: inline-block;
        padding: 4px 24px;
      }

      span {
        margin-left: 3px;
      }
    }
  }

  &-bottom {
    padding: 10px;
    margin: 0 16px 16px 16px;
    background-color: @component-background;
    border-radius: 3px;
  }
}
</style>
