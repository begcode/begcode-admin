<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="查看详情"
    :minHeight="600"
    :showCancelBtn="false"
    :showOkBtn="false"
    :height="88"
  >
    <Card class="daily-article">
      <CardMeta :title="content.title" :description="'发布人：' + content.sender + ' 发布时间： ' + content.sendTime"> </CardMeta>
      <Divider />
      <div v-html="content.content" class="article-content"></div>
    </Card>
  </BasicModal>
</template>
<script lang="ts" setup>
import { ref, unref } from 'vue';
import { Card, Divider, CardMeta } from 'ant-design-vue';
import { BasicModal, useModalInner } from '@begcode/components';
const isUpdate = ref(true);
const content = ref<any>({});
//表单赋值
const [registerModal, { setModalProps, closeModal }] = useModalInner(async data => {
  isUpdate.value = !!data?.isUpdate;
  if (unref(isUpdate)) {
    content.value = data.record;
  }
});
</script>

<style scoped lang="less">
.detail-iframe {
  border: 0;
  width: 100%;
  height: 100%;
  min-height: 600px;
}
</style>
