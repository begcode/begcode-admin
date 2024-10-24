<template>
  <a-list :class="prefixCls" bordered :pagination="getPagination">
    <template v-for="item in getData" :key="item.id">
      <a-list-item class="list-item" @click="handleTitleClick(item)" :style="{ cursor: isTitleClickable ? 'pointer' : '' }">
        <a-list-item-meta>
          <template #title>
            <div class="title">
              <a-typography-paragraph
                style="width: 100%; margin-bottom: 0 !important"
                :delete="!!item.titleDelete"
                :ellipsis="$props.titleRows && $props.titleRows > 0 ? { rows: $props.titleRows, tooltip: !!item.title } : false"
                :content="item.title"
              />
              <div class="extra" v-if="item.extra">
                <a-tag class="tag" :color="item.color">
                  {{ item.extra }}
                </a-tag>
              </div>
            </div>
          </template>

          <template #avatar>
            <a-avatar v-if="item.avatar" class="avatar" :src="item.avatar" />
            <template v-else-if="item.priority">
              <a-avatar v-if="item.priority === PriorityTypes.L" class="avatar priority-L" title="一般消息">
                <template #icon>
                  <Icon icon="entypo:info" />
                </template>
              </a-avatar>
              <a-avatar v-if="item.priority === PriorityTypes.M" class="avatar priority-M" title="重要消息">
                <template #icon>
                  <Icon icon="bi:exclamation-lg" />
                </template>
              </a-avatar>
              <a-avatar v-if="item.priority === PriorityTypes.H" class="avatar priority-H" title="紧急消息">
                <template #icon>
                  <Icon icon="ant-design:warning-filled" />
                </template>
              </a-avatar>
            </template>
            <span v-else> {{ item.avatar }}</span>
          </template>

          <template #description>
            <div>
              <div class="description" v-if="item.description">
                <a-typography-paragraph
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
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>
<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign';
import { Time } from '@/components/Time';
import { PriorityTypes, ListItem } from './data';

// types
import type { StyleValue } from '@/utils/types';
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
  let size = _isNumber(pageSize) ? pageSize : 5;
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
  const size = _isNumber(pageSize) ? pageSize : Number(pageSize) && 5;

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
