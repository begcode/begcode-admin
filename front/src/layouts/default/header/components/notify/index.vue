<template>
  <div :class="prefixCls">
    <Badge :count="count" :overflowCount="9" :offset="[-6, -12]" dot @click="clickBadge">
      <BellOutlined />
    </Badge>
    <DynamicNotice ref="dynamicNoticeRef" v-bind="dynamicNoticeProps" />
    <DetailModal @register="registerDetail" />
    <sys-message-modal @register="registerMessageModal" @refresh="reloadCount"></sys-message-modal>
  </div>
</template>
<script lang="ts" setup>
import { computed, ref, reactive, onMounted, getCurrentInstance, onUnmounted } from 'vue';
import { Badge } from 'ant-design-vue';
import { BellOutlined } from '@ant-design/icons-vue';
import { useDesign, useModal } from '@begcode/components';
import { tabListData } from './data';
import DetailModal from '@/views/monitor/mynews/DetailModal.vue';
import DynamicNotice from '@/views/monitor/mynews/DynamicNotice.vue';
import { readAllMsg } from '@/views/monitor/mynews/mynews.api';
import { useMessage } from '@/hooks/web/useMessage';
import { AnnoCategory } from '@/models/enumerations/anno-category.model';
import SysMessageModal from '@/views/system/message/components/SysMessageModal.vue';
import announcementService from '@/api-service/system/announcement.service';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const { prefixCls } = useDesign('header-notify');
const instance: any = getCurrentInstance();
const dynamicNoticeProps = reactive({ path: '', formData: {} });
const [registerDetail, detailModal] = useModal();
const { createMessage } = useMessage();
const listData = ref(tabListData);

const count = computed(() => {
  let count = 0;
  for (let i = 0; i < listData.value.length; i++) {
    count += listData.value[i].count;
  }
  return count;
});

const [registerMessageModal, { openModal: openMessageModal }] = useModal();
function clickBadge() {
  //消息列表弹窗前去除角标
  for (let i = 0; i < listData.value.length; i++) {
    listData.value[i].count = 0;
  }
  openMessageModal(true, {});
}

const popoverVisible = ref<boolean>(false);

function mapAnnouncement(item) {
  return {
    ...item,
    title: item.title,
    description: item.msgAbstract,
    datetime: item.sendTime,
  };
}

// 获取系统消息
async function loadData() {
  try {
    // let { anntMsgList, anntMsgTotal, sysMsgTotal } = await listCementByUser({
    //   pageSize: 5,
    // });

    let sysMsgList = await announcementService.retrieveUnread(AnnoCategory.SYSTEM_INFO, {});
    // listData.value[0].list = anntMsgList.map(mapAnnouncement);
    listData.value[1].list = (sysMsgList.records || []).map(mapAnnouncement);
    // listData.value[0].count = anntMsgTotal;
    listData.value[1].count = sysMsgList.total || 0;
  } catch (e) {
    console.warn('系统消息通知异常：', e);
  }
}

loadData();

function onNoticeClick(record) {
  try {
    announcementService.read(record.id);
    loadData();
  } catch (e) {
    console.error(e);
  }
  if (record.openType === 'component') {
    dynamicNoticeProps.path = record.openPage;
    dynamicNoticeProps.formData = { id: record.busId };
    instance.refs.dynamicNoticeRef?.detail(record.openPage);
  } else {
    detailModal.openModal(true, {
      record,
      isUpdate: true,
    });
  }
  popoverVisible.value = false;
}

// 清空消息
function onEmptyNotify() {
  popoverVisible.value = false;
  readAllMsg({}, loadData);
}
async function reloadCount(id) {
  try {
    await announcementService.read(id);
    await loadData();
  } catch (e) {
    console.error(e);
  }
}
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-header-notify';

.@{prefix-cls} {
  padding-bottom: 1px;

  &__overlay {
    max-width: 340px;

    .ant-popover-inner-content {
      padding: 0;
    }

    .ant-tabs-nav {
      margin-bottom: 12px;
    }

    .ant-list-item {
      padding: 12px 24px;
      transition: background-color 300ms;
    }

    .bottom-buttons {
      text-align: center;
      border-top: 1px solid #f0f0f0;
      height: 42px;

      .ant-btn {
        border: 0;
        height: 100%;

        &:first-child {
          border-right: 1px solid #f0f0f0;
        }
      }
    }
  }

  .ant-tabs-content {
    width: 300px;
  }

  .ant-badge {
    display: flex;
    align-items: center;
    font-size: 18px;

    .ant-badge-count {
      @badget-size: 16px;
      width: @badget-size;
      height: @badget-size;
      min-width: @badget-size;
      line-height: @badget-size;
      padding: 0;

      .ant-scroll-number-only > p.ant-scroll-number-only-unit {
        font-size: 14px;
        height: @badget-size;
      }
    }

    .ant-badge-multiple-words {
      padding: 0 0 0 2px;
      font-size: 12px;
    }

    svg {
      width: 0.9em;
    }
  }
}

// 兼容黑暗模式
[data-theme='dark'] .@{prefix-cls} {
  &__overlay {
    .ant-list-item {
      &:hover {
        background-color: #111b26;
      }
    }

    .bottom-buttons {
      border-top: 1px solid #303030;

      .ant-btn {
        &:first-child {
          border-right: 1px solid #303030;
        }
      }
    }
  }
}
</style>
