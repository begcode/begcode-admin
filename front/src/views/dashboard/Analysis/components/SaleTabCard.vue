<template>
  <Card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
    <div class="salesCard">
      <Tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
        <template #rightExtra>
          <div class="extra-wrapper">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <RangePicker :style="{ width: '256px' }" />
          </div>
        </template>
        <TabPane loading="true" tab="销售额" key="1">
          <Row>
            <Col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
              <Bar :chartData="barData" :option="{ title: { text: '销售额排行', textStyle: { fontWeight: 'lighter' } } }" height="40vh" />
            </Col>
            <Col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <RankList title="门店销售排行榜" :list="rankList" />
            </Col>
          </Row>
        </TabPane>
        <TabPane tab="销售趋势" key="2">
          <Row>
            <Col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
              <Bar
                :chartData="barData.reverse()"
                :option="{ title: { text: '销售额排行', textStyle: { fontWeight: 'lighter' } } }"
                height="40vh"
              />
            </Col>
            <Col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <RankList title="门店销售排行榜" :list="rankList" />
            </Col>
          </Row>
        </TabPane>
      </Tabs>
    </div>
  </Card>
</template>
<script lang="ts" setup>
import { Card, Tabs, TabPane, Row, Col, RangePicker } from 'ant-design-vue';
import { Bar, RankList } from '@begcode/components';

defineProps({
  loading: {
    type: Boolean,
  },
});
const rankList: any[] = [];
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: '白鹭岛 ' + (i + 1) + ' 号店',
    total: 1234.56 - i * 100,
  });
}

const barData: any[] = [];
for (let i = 0; i < 12; i += 1) {
  barData.push({
    name: `${i + 1}月`,
    value: Math.floor(Math.random() * 1000) + 200,
  });
}
</script>

<style lang="less" scoped>
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}
</style>
