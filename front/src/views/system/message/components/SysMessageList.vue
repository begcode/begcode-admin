<template>
  <List item-layout="horizontal" :data-source="messageList">
    <template #loadMore>
      <div
        v-if="messageList && messageList.length > 0 && !loadEndStatus && !loadingMoreStatus"
        :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }"
      >
        <Button @click="onLoadMore">加载更多</Button>
      </div>
      <div
        v-if="messageList && messageList.length > 0 && loadEndStatus"
        :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }"
      >
        没有更多了
      </div>
    </template>

    <template #renderItem="{ item }">
      <List.Item>
        <template #actions>
          <Rate :value="item.starFlag == '1' ? 1 : 0" :count="1" @click="clickStar(item)" style="cursor: pointer" disabled />
        </template>

        <List.Item.Meta :description="item.createTime">
          <template #title>
            <div style="position: relative">
              <span style="display: inline-block; position: absolute; left: -16px">
                <ExclamationOutlined v-if="noRead(item)" title="未读消息" style="color: red" />
              </span>
              <span>{{ getMsgCategory(item) }}</span>
              <span v-if="item.busType == 'bpm'" class="bpm-cuiban-content" v-html="item.content"> </span>
              <Tooltip v-else>
                <template #title>
                  <div v-html="item.content"></div>
                </template>
                {{ item.title }}
              </Tooltip>

              <a @click="showMessageDetail(item)" style="margin-left: 16px">查看详情</a>
            </div>
          </template>
          <template #avatar>
            <template v-if="item.busType == 'email'">
              <Badge dot v-if="noRead(item)" class="msg-no-read">
                <Avatar style="background: #79919d"><MailOutlined style="font-size: 16px" title="未读消息" /></Avatar>
              </Badge>
              <Avatar v-else style="background: #79919d"><MailOutlined style="font-size: 16px" /></Avatar>
            </template>

            <template v-else-if="item.busType == 'bpm_task'">
              <Badge dot v-if="noRead(item)" class="msg-no-read">
                <Avatar style="background: #79919d"><InteractionOutlined style="font-size: 16px" title="未读消息" /></Avatar>
              </Badge>
              <Avatar v-else style="background: #79919d"><InteractionOutlined style="font-size: 16px" /></Avatar>
            </template>

            <template v-else-if="item.busType == 'bpm'">
              <Badge dot v-if="noRead(item)" class="msg-no-read">
                <Avatar style="background: #79919d"><AlertOutlined style="font-size: 16px" title="未读消息" /></Avatar>
              </Badge>
              <Avatar v-else style="background: #79919d"><AlertOutlined style="font-size: 16px" /></Avatar>
            </template>

            <template v-else>
              <Badge dot v-if="noRead(item)" class="msg-no-read">
                <Avatar style="background: #79919d"><BellFilled style="font-size: 16px" title="未读消息" /></Avatar>
              </Badge>
              <Avatar v-else style="background: #79919d"><BellFilled style="font-size: 16px" /></Avatar>
            </template>
          </template>
        </List.Item.Meta>
      </List.Item>
    </template>
  </List>
</template>

<script lang="ts" setup>
import {
  FilterOutlined,
  CloseOutlined,
  BellFilled,
  ExclamationOutlined,
  MailOutlined,
  InteractionOutlined,
  AlertOutlined,
} from '@ant-design/icons-vue';
import { useSysMessage, useMessageHref } from './useSysMessage';
import { List, Rate, Badge, Avatar, Button, Tooltip } from 'ant-design-vue';

defineOptions({ name: 'SysMessageList' });

const props = defineProps({
  star: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['close', 'detail']);

const {
  messageList,
  loadEndStatus,
  loadingMoreStatus,
  onLoadMore,
  noRead,
  getMsgCategory,
  searchParams,
  reset,
  loadData,
  updateStarMessage,
} = useSysMessage();

function reload(params) {
  let { fromUser, rangeDateKey, rangeDate } = params;
  searchParams.fromUser = fromUser || '';
  searchParams.rangeDateKey = rangeDateKey || '';
  searchParams.rangeDate = rangeDate || [];
  if (props.star === true) {
    searchParams.starFlag = '1';
  } else {
    searchParams.starFlag = '';
  }
  reset();
  loadData();
}

function clickStar(item) {
  updateStarMessage(item);
  if (item.starFlag === '1') {
    item.starFlag = '0';
  } else {
    item.starFlag = '1';
  }
}

const { goPage } = useMessageHref(emit);

function showMessageDetail(record) {
  record.readFlag = '1';
  goPage(record);
  emit('close', record.id);
}
defineExpose({
  reload,
  showMessageDetail,
});
</script>
<style scoped lang="less">
.msg-no-read {
  :deep(.ant-badge-dot) {
    top: 5px;
    right: 3px;
  }
}
:deep(.bpm-cuiban-content) p {
  display: inherit;
  margin-bottom: 0;
  margin-top: 0;
}
</style>
