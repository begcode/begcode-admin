<template>
  <div class="ding-ding-container" :class="[`${prefixCls}`]">
    <div class="ding-header">
      <ul class="ding-menu-tab">
        <li :class="activeKey === 'ding' ? 'active' : ''" @click="dingLiClick('ding')"><a>钉钉集成</a></li>
        <li :class="activeKey === 'wechat' ? 'active' : ''" @click="dingLiClick('wechat')"><a>企业微信集成?</a></li>
      </ul>
    </div>
    <div v-show="activeKey === 'ding'" class="base-collapse">
      <ThirdAppDingTalkConfigForm />
    </div>
    <div v-show="activeKey === 'wechat'"></div>
  </div>
</template>

<script lang="ts">
import ThirdAppDingTalkConfigForm from './ThirdAppDingTalkConfigForm.vue';
import { useDesign } from '@/hooks/web/useDesign';

export default defineComponent({
  name: 'ThirdAppConfigList',
  components: {
    ThirdAppDingTalkConfigForm,
  },
  setup() {
    const { prefixCls } = useDesign('j-dd-container');

    //选中的key
    const activeKey = ref<string>('ding');

    /**
     * tab点击事件
     * @param key
     */
    function dingLiClick(key) {
      activeKey.value = key;
    }

    return {
      activeKey,
      dingLiClick,
      prefixCls,
    };
  },
});
</script>

<style lang="less" scoped>
.ding-ding-container {
  border-radius: 4px;
  height: calc(100% - 80px);
  margin: 16px;
}
.ding-header {
  align-items: center;
  /*begin 兼容暗夜模式*/
  border-bottom: 1px solid @border-color-base;
  /*end 兼容暗夜模式*/
  box-sizing: border-box;
  display: flex;
  height: 50px;
  justify-content: space-between;
  padding: 0 24px;

  ul {
    margin-bottom: 0;
  }
}
.ding-menu-tab {
  display: flex;
  height: 100%;

  li {
    align-items: center;
    border-bottom: 2px solid transparent;
    display: flex;
    height: 100%;
    margin-right: 38px;

    a {
      /*begin 兼容暗夜模式*/
      color: @text-color !important;
      /*end 兼容暗夜模式*/
      font-size: 15px;
      font-weight: 700;
    }
  }
}
.active {
  border-bottom-color: #2196f3 !important;

  a {
    color: #333 !important;
  }
}
</style>

<style lang="less">
@prefix-cls: ~'@{namespace}-j-dd-container';
/*begin 兼容暗夜模式*/
.@{prefix-cls} {
  background: @component-background;

  .ding-header {
    border-bottom: 1px solid @border-color-base;
  }

  .ding-menu-tab {
    li {
      a {
        color: @text-color !important;
      }
    }
  }

  .ant-collapse-borderless {
    background-color: @component-background;
  }

  .ant-collapse {
    background-color: @component-background;
  }
}
/*end 兼容暗夜模式*/
</style>
