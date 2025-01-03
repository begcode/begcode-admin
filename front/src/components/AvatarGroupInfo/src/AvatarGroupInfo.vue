<template>
  <div class="flex items-center justify-between">
    <a-avatar-group v-if="avatarGroupData.length" size="small" :max-count="maxCount" :shape="shape">
      <a-tooltip :title="tipPrefix + item[avatarTipField]" placement="top" v-for="item in avatarGroupData">
        <a-avatar :src="avatarSlotName === 'src' ? item[avatarSlotField] : undefined">
          <template #icon v-if="avatarSlotName === 'icon' && item[avatarSlotField]">
            <Icon :icon="item[avatarSlotField]" />
          </template>
          <span v-if="avatarSlotName === 'default'">{{ avatarTipField ? item[avatarTipField] : item }}</span>
        </a-avatar>
      </a-tooltip>
    </a-avatar-group>
    <BasicButton
      v-if="(avatarGroupData.length || statsValue > 0) && disabled"
      pre-icon="ant-design:file-search-outlined"
      type="link"
      size="small"
      @click.stop="openModal('view')"
      style="padding: 0"
    ></BasicButton>
    <BasicButton
      v-if="!disabled"
      pre-icon="ant-design:edit-outlined"
      type="link"
      size="small"
      @click.stop="openModal('edit')"
      style="padding: 0"
    ></BasicButton>
  </div>
</template>
<script lang="ts" setup>
defineOptions({
  name: 'AvatarGroupInfo',
  inheritAttrs: false,
});

const props = defineProps({
  value: {
    type: [Array, Object, String, Number],
  },
  shape: {
    type: String as PropType<'circle' | 'square'>,
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
  tipPrefix: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['click', 'change', 'register', 'update:value']);

const statsValue = ref(0);
const tipPrefix = ref(props.tipPrefix);
// 接收选择的值
const avatarGroupData = computed(() => {
  const data = props.value;
  if (_isArray(data)) {
    return data;
  } else if (data) {
    return [data];
  } else if (statsValue.value) {
    tipPrefix.value = '总数：';
    return [statsValue.value];
  } else {
    return [];
  }
});

const openModal = buttonType => {
  emit('click', buttonType);
};

watchEffect(() => {
  if (props.statsApi && Object.keys(props.query).length > 0) {
    props.statsApi(props.query).then(res => {
      statsValue.value = res;
    });
  }
});
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
