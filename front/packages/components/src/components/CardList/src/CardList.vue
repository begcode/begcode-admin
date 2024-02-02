<template>
  <div class="p-2">
    <!--    <div class="p-4 mb-2 bg-white">-->
    <!--      <BasicForm @register="registerForm" />-->
    <!--    </div>-->
    <div class="p-2 bg-white">
      <List :grid="{ gutter: 5, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: grid }" :data-source="data" :pagination="paginationProp">
        <template #header>
          <div class="flex justify-between space-x-2">
            <div>
              <slot name="header_left" />
            </div>
            <div>
              <slot name="header_right"></slot>
              <Space>
                <template v-for="button in toolButtons">
                  <Tooltip v-if="!button.hidden">
                    <template #title>{{ button.title }}</template>
                    <ImageUpload v-if="button.name === 'uploadImg'">{{ button.title }}</ImageUpload>
                    <Upload v-else-if="button.name === 'uploadFile'">{{ button.title }}</Upload>
                    <Button :disabled="button.disabled" @click="button.click" v-else>
                      <Icon :icon="button.icon" v-if="button.icon"></Icon>
                      {{ button.title }}
                    </Button>
                  </Tooltip>
                </template>
                <Tooltip>
                  <template #title>
                    <div class="w-50">每行显示数量</div>
                    <Slider id="slider" v-bind="sliderProp" v-model:value="grid" @change="sliderChange" />
                  </template>
                  <Button><TableOutlined />列数</Button>
                </Tooltip>
              </Space>
            </div>
          </div>
        </template>
        <template #renderItem="{ item }">
          <ListItem>
            <Card>
              <template #title></template>
              <template #cover>
                <div :class="height">
                  <Image v-if="item.showImage" :src="item[props.imageField]" v-bind="imageConfig" @click="imageClick($event, item)" />
                </div>
              </template>
              <template class="ant-card-actions" #actions>
                <template v-for="operation in rowOperations">
                  <Button @click="operation.click(item)">{{ operation.title }}</Button>
                </template>
                <Dropdown
                  :trigger="['hover']"
                  :dropMenuList="[
                    {
                      text: '删除',
                      event: '1',
                      popConfirm: {
                        title: '是否确认删除',
                        confirm: handleDelete.bind(null, item.id),
                      },
                    },
                  ]"
                  popconfirm
                >
                  <EllipsisOutlined />
                </Dropdown>
              </template>
              <CardMeta>
                <template #title>
                  <TypographyParagraph :content="titleValue(item)" :ellipsis="true" />
                </template>
                <template #avatar>
                  <Avatar :src="item.avatar" v-if="showAvatar" />
                </template>
                <template #description v-if="showDesc">{{ descValue(item) }}</template>
              </CardMeta>
            </Card>
          </ListItem>
        </template>
      </List>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { List, Card, Image, TypographyParagraph, Tooltip, Slider, Avatar, Space, Upload } from 'ant-design-vue';
import { EllipsisOutlined, RedoOutlined, TableOutlined } from '@ant-design/icons-vue';
import { Dropdown } from '@/components/Dropdown';
import { useForm } from '@/components/Form';
import { propTypes } from '@/utils/propTypes';
import { Button } from '@/components/Button';
import Icon from '@/components/Icon/Icon.vue';
import { isFunction } from 'lodash-es';
import { useSlider, grid } from './data';
import ImageUpload from '@/components/Upload/src/components/ImageUpload.vue';

const ListItem = List.Item;
const CardMeta = Card.Meta;
// 获取slider属性
const sliderProp = computed(() => useSlider(4));
// 组件接收参数
const props = defineProps({
  // 请求API的参数
  params: propTypes.object.def({}),
  //api
  api: propTypes.func,
  // imageField
  imageField: propTypes.string.def('url'),
  resultField: propTypes.string.def('data'),
  totalField: propTypes.string.def('total'),
  toolButtons: propTypes.array.def([]),
  rowOperations: propTypes.array.def([]),
  showAvatar: propTypes.bool.def(false),
  showDesc: propTypes.bool.def(true),
  metaDesc: propTypes.oneOfType([propTypes.string, propTypes.func]).def(''),
  metaTitle: propTypes.oneOfType([propTypes.string, propTypes.func]).def(''),
  metaAvatar: propTypes.oneOfType([propTypes.string, propTypes.func]).def(''),
  imageConfig: propTypes.object.def({ preview: true }),
});
//暴露内部方法
const emit = defineEmits(['getMethod', 'delete']);
//数据
const data = ref([]);
// 切换每行个数
// cover图片自适应高度
//修改pageSize并重新请求数据

const height = computed(() => {
  return `h-${120 - grid.value * 6}`;
});

const imageClick = (e, item) => {
  if (props.imageConfig.preview) {
    Image.Preview({
      visible: true,
      current: item[props.imageField],
      images: [item[props.imageField]],
    });
  } else {
    if (isFunction(props.imageConfig.click)) {
      props.imageConfig.click(e, item);
    }
  }
};

//表单
const [registerForm, { validate }] = useForm({
  schemas: [{ field: 'type', component: 'Input', label: '类型' }],
  labelWidth: 80,
  baseColProps: { span: 6 },
  actionColOptions: { span: 24 },
  autoSubmitOnEnter: true,
  submitFunc: handleSubmit,
});
//表单提交
async function handleSubmit() {
  const data = await validate();
  await fetch(data);
}
function sliderChange(n) {
  pageSize.value = n * 4;
  fetch();
}

// 自动请求并暴露内部方法
onMounted(() => {
  fetch();
  emit('getMethod', fetch);
});

async function fetch(p = {}) {
  const { api, params } = props;
  if (api && isFunction(api)) {
    const res = await api({ ...params, page: page.value, pageSize: pageSize.value, ...p });
    data.value = res[props.resultField];
    total.value = res[props.totalField];
  }
}
//分页相关
const page = ref(0);
const pageSize = ref(36);
const total = ref(0);
const paginationProp = ref({
  showSizeChanger: false,
  showQuickJumper: true,
  pageSize,
  current: page,
  total,
  showTotal: (total: number) => `总 ${total} 条`,
  onChange: pageChange,
  onShowSizeChange: pageSizeChange,
});

function pageChange(p: number, pz: number) {
  page.value = p;
  pageSize.value = pz;
  fetch();
}

function pageSizeChange(_current, size: number) {
  pageSize.value = size;
  fetch();
}

async function handleDelete(id: number) {
  emit('delete', id);
}

const titleValue = item => {
  if (typeof props.metaTitle === 'string') {
    return item[props.metaTitle];
  } else if (typeof props.metaTitle === 'function') {
    return props.metaTitle(item);
  } else {
    return '';
  }
};
const descValue = item => {
  if (typeof props.metaDesc === 'string') {
    return item[props.metaDesc];
  } else if (typeof props.metaTitle === 'function') {
    return props.metaDesc(item);
  } else {
    return '';
  }
};
</script>
<style>
.w-50 {
  width: 12.5rem;
}
.p-2 {
  padding: 0.5rem;
}
.space-x-2 > :not([hidden]) ~ :not([hidden]) {
  --un-space-x-reverse: 0;
  margin-left: calc(0.5rem * calc(1 - var(--un-space-x-reverse)));
  margin-right: calc(0.5rem * var(--un-space-x-reverse));
}
.bg-white {
  --un-bg-opacity: 1;
  background-color: rgb(255 255 255 / var(--un-bg-opacity));
}
</style>
