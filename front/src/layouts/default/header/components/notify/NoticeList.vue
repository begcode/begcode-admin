<template>
  <List :class="prefixCls" bordered :pagination="getPagination">
    <template v-for="item in getData" :key="item.id">
      <List.Item class="list-item" @click="handleTitleClick(item)" :style="{ cursor: isTitleClickable ? 'pointer' : '' }">
        <List.Item.Meta>
          <template #title>
            <div class="title">
              <Typography.Paragraph
                style="width: 100%; margin-bottom: 0 !important"
                :delete="!!item.titleDelete"
                :ellipsis="$props.titleRows && $props.titleRows > 0 ? { rows: $props.titleRows, tooltip: !!item.title } : false"
                :content="item.title"
              />
              <div class="extra" v-if="item.extra">
                <Tag class="tag" :color="item.color">
                  {{ item.extra }}
                </Tag>
              </div>
            </div>
          </template>

          <template #avatar>
            <Avatar v-if="item.avatar" class="avatar" :src="item.avatar" />
            <template v-else-if="item.priority">
              <Avatar v-if="item.priority === PriorityTypes.L" class="avatar priority-L" title="一般消息">
                <template #icon>
                  <Icon icon="entypo:info" />
                </template>
              </Avatar>
              <Avatar v-if="item.priority === PriorityTypes.M" class="avatar priority-M" title="重要消息">
                <template #icon>
                  <Icon icon="bi:exclamation-lg" />
                </template>
              </Avatar>
              <Avatar v-if="item.priority === PriorityTypes.H" class="avatar priority-H" title="紧急消息">
                <template #icon>
                  <Icon icon="ant-design:warning-filled" />
                </template>
              </Avatar>
            </template>
            <span v-else> {{ item.avatar }}</span>
          </template>

          <template #description>
            <div>
              <div class="description" v-if="item.description">
                <Typography.Paragraph
                  style="width: 100%; margin-bottom: 0 !important"
                  :ellipsis="descRows && descRows > 0 ? { rows: descRows, tooltip: !!item.description } : false"
                  :content="item.description"
                />
              </div>
              <div class="datetime">
                <Time :value="item.datetime" :title="item.datetime" />
              </div>
            </div>
          </template>
        </List.Item.Meta>
      </List.Item>
    </template>
  </List>
</template>
<script lang="ts" setup>
import { computed, PropType, ref, watch, unref } from 'vue';
import { List, Avatar, Tag, Typography } from 'ant-design-vue';
import { isNumber } from 'lodash-es';
import { useDesign } from '@begcode/components';
import { Time } from '@begcode/components';
import { PriorityTypes, ListItem } from './data';

// types
import type { StyleValue } from '@/utils/types';
import type { FunctionalComponent } from 'vue';
import type { ParagraphProps } from 'ant-design-vue/es/typography/Paragraph';

const props = defineProps({
  list: {
    type: Array as PropType<ListItem[]>,
    default: () => [],
  },
  pageSize: {
    type: [Boolean, Number] as PropType<Boolean | Number>,
    default: 5,
  },
  currentPage: {
    type: Number,
    default: 1,
  },
  titleRows: {
    type: Number,
    default: 1,
  },
  descRows: {
    type: Number,
    default: 2,
  },
  onTitleClick: {
    type: Function as PropType<(Recordable) => void>,
  },
});

const emit = defineEmits(['update:currentPage']);

const { prefixCls } = useDesign('header-notify-list');
const current = ref(props.currentPage || 1);
const getData = computed(() => {
  const { pageSize, list } = props;
  if (pageSize === false) return [];
  let size = isNumber(pageSize) ? pageSize : 5;
  return list.slice(size * (unref(current) - 1), size * unref(current));
});

watch(
  () => props.currentPage,
  v => {
    current.value = v;
  },
);

const getPagination = computed(() => {
  const { list, pageSize } = props;
  // compatible line 104
  // if typeof pageSize is boolean, Number(true) && 5 = 5, Number(false) && 5 = 0
  const size = isNumber(pageSize) ? pageSize : Number(pageSize) && 5;

  if (size > 0 && list && list.length > size) {
    return {
      total: list.length,
      pageSize: size,
      current: unref(current),
      onChange(page) {
        current.value = page;
        emit('update:currentPage', page);
      },
    };
  } else {
    return false;
  }
});

function handleTitleClick(item: ListItem) {
  props.onTitleClick && props.onTitleClick(item);
}

const isTitleClickable = computed(() => !!props.onTitleClick);
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-header-notify-list';

.@{prefix-cls} {
  width: 340px;

  &::-webkit-scrollbar {
    display: none;
  }

  :deep(.ant-pagination-disabled) {
    display: inline-block !important;
  }

  .list-item {
    padding: 6px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s;

    .title {
      margin-bottom: 8px;
      font-weight: normal;

      .extra {
        float: right;
        margin-top: -1.5px;
        margin-right: 0;
        font-weight: normal;

        .tag {
          margin-right: 0;
        }
      }
    }

    .avatar {
      margin-top: 4px;
    }

    .description {
      font-size: 12px;
      line-height: 18px;
    }

    .datetime {
      margin-top: 4px;
      font-size: 12px;
      line-height: 18px;
    }
  }

  .list-item {
    .priority-L,
    .priority-M,
    .priority-H {
      font-size: 12px;
    }

    .priority-L {
      background-color: #7cd1ff;
    }

    .priority-M {
      background-color: #ffa743;
    }

    .priority-H {
      background-color: #f8766c;
    }

    .description {
      font-size: 12px;
      line-height: 18px;
    }
  }
}
</style>
