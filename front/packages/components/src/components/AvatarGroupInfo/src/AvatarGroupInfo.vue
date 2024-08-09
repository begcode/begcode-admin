<template>
  <div class="flex items-center justify-between">
    <AvatarGroup v-if="avatarGroupData.length" size="small" :max-count="maxCount" :shape="shape">
      <Tooltip :title="item[avatarTipField] || item" placement="top" v-for="item in avatarGroupData">
        <Avatar :src="avatarSlotName === 'src' ? item[avatarSlotField] : undefined">
          <template #icon v-if="avatarSlotName === 'icon' && item[avatarSlotField]">
            <Icon :icon="item[avatarSlotField]" />
          </template>
          <span v-if="avatarSlotName === 'default'">{{ (avatarTipField && item[avatarTipField]) || item }}</span>
        </Avatar>
      </Tooltip>
    </AvatarGroup>
    <Button
      v-if="(avatarGroupData.length || statsValue > 0) && disabled"
      pre-icon="ant-design:file-search-outlined"
      type="link"
      size="small"
      @click.stop="openModal('view')"
      style="padding: 0"
    ></Button>
    <Button
      v-if="!disabled"
      pre-icon="ant-design:edit-outlined"
      type="link"
      size="small"
      @click.stop="openModal('edit')"
      style="padding: 0"
    ></Button>
  </div>
</template>
<script lang="ts" setup>
import { computed, ref } from 'vue';
import type { PropType } from 'vue';
import { Avatar, Tooltip, AvatarGroup, Badge } from 'ant-design-vue';
import type { SelectValue } from 'ant-design-vue/es/select';
import { isArray } from 'lodash-es';
import { Icon } from '@/components/Icon';
import { Button } from '@/components/Button';

defineOptions({
  name: 'AvatarGroupInfo',
  inheritAttrs: false,
});

const props = defineProps({
  value: {
    type: [Array, Object, String, Number] as PropType<SelectValue>,
  },
  shape: {
    type: String,
    default: 'square',
  },
  maxCount: {
    type: Number,
    default: 3,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
  // 是否支持多选，默认 true
  multiple: {
    type: Boolean,
    default: true,
  },
  // 最多显示多少个 tag
  maxTagCount: {
    type: Number,
    default: 3,
  },
  api: {
    type: Function,
    default: null,
  },
  statsApi: {
    type: Function,
    default: null,
  },
  fieldNames: {
    type: Object,
    default: () => ({
      label: 'label',
      value: 'value',
      children: 'children',
    }),
  },
  // default icon src
  avatarSlotName: {
    type: String,
    default: 'default',
  },
  avatarSlotField: {
    type: String,
    default: '',
  },
  avatarTipField: {
    type: String,
    default: '',
  },
  // array | object | splitString
  valueType: {
    type: String,
    default: '',
  },
  query: {
    type: Object,
    default: () => ({}),
  },
});

const emit = defineEmits(['click', 'change', 'register', 'update:value']);

//接收选择的值

const avatarGroupData = computed(() => {
  const data = props.value;
  if (isArray(data)) {
    return data;
  } else if (data) {
    return [data];
  } else if (statsValue.value > 0) {
    return ['总数：' + statsValue.value];
  } else {
    return [];
  }
});

const openModal = buttonType => {
  emit('click', buttonType);
};

const statsValue = ref(0);

if (props.statsApi && Object.keys(props.query).length > 0) {
  props.statsApi(props.query).then(res => {
    statsValue.value = res;
  });
}
</script>
<style scoped>
.select-row .left {
  width: calc(10%);
}
.select-row .right {
  width: 82px;
}
.select-row .full {
  width: 100%;
}
.select-row :deep(.ant-select-search__field) {
  display: none !important;
}
</style>
