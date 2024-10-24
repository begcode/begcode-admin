<template>
  <ScrollContainer>
    <div ref="wrapperRef" class="user-account-setting" :class="[prefixCls, activeKey === '5' ? 'vip-background' : '']">
      <a-tabs
        tab-position="left"
        :tabBarStyle="tabBarStyle"
        @tabClick="componentClick"
        v-model:activeKey="activeKey"
        :class="showVip ? 'vip-height' : ''"
      >
        <template v-for="item in componentList" :key="item.key">
          <a-tab-pane>
            <template #tab>
              <span style="display: flex; align-items: center; cursor: pointer">
                <!--<Icon :icon="item.icon" class="icon-font-color"/>-->
                <span style="width: 30px">
                  <img v-if="activeKey === item.key || isDark" :src="item.img2" style="height: 18px" />
                  <img v-else :src="item.img1" style="height: 16px" />
                </span>
                {{ item.name }}
              </span>
            </template>
            <component :is="item.component" v-if="activeKey === item.key && !item.isSlot" />
            <slot name="component" v-if="activeKey === item.key && item.isSlot" />
          </a-tab-pane>
        </template>
      </a-tabs>
    </div>
  </ScrollContainer>
</template>

<script lang="ts">
import { ScrollContainer } from '@/components/Container';
import { useDesign } from '@/hooks/web/useDesign';
import { settingList } from './UserSetting.data';
import BaseSetting from './BaseSetting.vue';
import AccountSetting from './AccountSetting.vue';
import TenantSetting from './TenantSetting.vue';
import WeChatDingSetting from './WeChatDingSetting.vue';
import { useRouter } from 'vue-router';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { ThemeEnum } from '@/enums/appEnum';
export default defineComponent({
  components: {
    ScrollContainer,
    BaseSetting,
    AccountSetting,
    TenantSetting,
    WeChatDingSetting,
  },
  props: {
    componentList: {
      type: Array,
      default: settingList,
    },
  },
  setup() {
    const { prefixCls } = useDesign('user-account-setting-container');
    const { getDarkMode } = useRootSetting();
    const isDark = computed(() => getDarkMode.value === ThemeEnum.DARK);
    const activeKey = ref<string>('1');
    //是否为vip
    const showVip = ref<boolean>(false);
    //vip编码
    const vipCode = ref<string>('');
    const router = useRouter();
    const componentList = computed(() => {
      if (showVip.value) {
        return settingList;
      }
      return settingList.filter(item => item.component != 'MyVipSetting');
    });

    /**
     * 组件标题点击事件,解决第二次不加载数据
     * @param key
     */
    function componentClick(key) {
      activeKey.value = key;
    }

    function goToMyTeantPage() {
      //如果请求参数包含我的租户，直接跳转过去
      let query = router.currentRoute.value.query;
      if (query && query.page === 'tenantSetting') {
        activeKey.value = '2';
      }
    }
    return {
      prefixCls,
      settingList,
      tabBarStyle: {
        width: '220px',
        marginBottom: '200px',
      },
      componentClick,
      activeKey,
      isDark,
    };
  },
});
</script>
<style lang="less" scoped>
.user-account-setting {
  margin: 20px;

  .base-title {
    padding-left: 0;
  }

  //tabs弹窗左边样式
  :deep(.ant-tabs-nav) {
    height: 260px;
  }
  //tabs弹窗右边边样式
  :deep(.ant-tabs-content-holder) {
    position: relative;
    left: 12px;
    height: auto !important;
  }
}
//tab点击样式
:deep(.ant-tabs-tab-active) {
  border-radius: 0 20px 20px 0;
  background-color: #1294f7 !important;
  color: #fff !important;
  .icon-font-color {
    color: #fff;
  }
}
:deep(.ant-tabs-tab.ant-tabs-tab-active .ant-tabs-tab-btn) {
  color: white !important;
}
:deep(.ant-tabs-ink-bar) {
  visibility: hidden;
}
:deep(.ant-tabs-nav-list) {
  padding-top: 14px;
  padding-right: 14px;
}

.vip-height {
  //tabs弹窗左边样式
  :deep(.ant-tabs-nav) {
    height: 310px !important;
  }
}
.vip-background {
  :deep(.ant-tabs-content-holder) {
    background: transparent;
  }
  :deep(.ant-tabs-tabpane) {
    padding-left: 0 !important;
  }
}
</style>

<style lang="less">
@prefix-cls: ~'@{namespace}-user-account-setting-container';

.@{prefix-cls} {
  .ant-tabs-tab-active {
    background-color: @item-active-bg;
  }
  //tabs弹窗左边样式
  .ant-tabs-nav {
    background-color: @component-background;
  }
  //tabs弹窗右边边样式
  .ant-tabs-content-holder {
    background: @component-background;
  }
}
</style>
