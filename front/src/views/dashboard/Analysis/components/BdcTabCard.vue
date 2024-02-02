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
        <TabPane loading="true" tab="受理监管" key="1">
          <Row>
            <Col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
              <Bar :chartData="barData" :option="{ title: { text: '受理量统计', textStyle: { fontWeight: 'lighter' } } }" height="40vh" />
            </Col>
            <Col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <QuickNav :loading="loading" class="enter-y" :bordered="false" :body-style="{ padding: 0 }" />
            </Col>
          </Row>
        </TabPane>
        <TabPane tab="交互监管" key="2">
          <Row>
            <Col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
              <BarMulti
                :chartData="barMultiData"
                :option="{ title: { text: '平台与部门交互量统计', textStyle: { fontWeight: 'lighter' } } }"
                height="40vh"
              />
            </Col>
            <Col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <QuickNav :loading="loading" class="enter-y" :bordered="false" :body-style="{ padding: 0 }" />
            </Col>
          </Row>
        </TabPane>
        <TabPane tab="存储监管" key="3">
          <Row>
            <Col :xl="16" :lg="12" :md="12" :sm="24" :xs="24" style="display: flex">
              <Gauge :chartData="{ name: 'C盘', value: 70 }" height="30vh"></Gauge>
              <Gauge :chartData="{ name: 'D盘', value: 50 }" height="30vh"></Gauge>
            </Col>
            <Col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <QuickNav :loading="loading" class="enter-y" :bordered="false" :body-style="{ padding: 0 }" />
            </Col>
          </Row>
        </TabPane>
      </Tabs>
    </div>
  </Card>
</template>
<script lang="ts" setup>
import { Card, Tabs, TabPane, Row, Col, RangePicker } from 'ant-design-vue';
import { Bar, BarMulti, Gauge } from '@begcode/components';
import QuickNav from './QuickNav.vue';

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
const barMultiData: any[] = [];
for (let j = 0; j < 2; j++) {
  for (let i = 0; i < 12; i += 1) {
    barMultiData.push({
      type: j == 0 ? 'jeecg' : 'jeebt',
      name: `${i + 1}月`,
      value: Math.floor(Math.random() * 1000) + 200,
    });
  }
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
