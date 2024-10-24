<template>
  <div :class="prefixCls">
    <PreviewGroup>
      <slot v-if="!imageList || $slots.default"></slot>
      <template v-else>
        <template v-for="item in getImageList" :key="item.src">
          <a-image v-bind="item">
            <template #placeholder v-if="item.placeholder">
              <a-image v-bind="item" :src="item.placeholder" :preview="false" />
            </template>
          </a-image>
        </template>
      </template>
    </PreviewGroup>
  </div>
</template>
<script lang="ts" setup>
import { Image } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';

interface ImageProps {
  alt?: string;
  fallback?: string;
  src: string;
  width: string | number;
  height?: string | number;
  placeholder?: string | boolean;
  preview?:
    | boolean
    | {
        visible?: boolean;
        onVisibleChange?: (visible: boolean, prevVisible: boolean) => void;
        getContainer: string | HTMLElement | (() => HTMLElement);
      };
}

type ImageItem = string | ImageProps;

const PreviewGroup = Image.PreviewGroup;

defineOptions({ name: 'ImagePreview' });

const props = defineProps({
  functional: {
    type: Boolean,
  },
  imageList: {
    type: Array as PropType<ImageItem[]>,
  },
});

const { prefixCls } = useDesign('image-preview');

const getImageList = computed((): any[] => {
  const { imageList } = props;
  if (!imageList) {
    return [];
  }
  return imageList.map(item => {
    if (_isString(item)) {
      return {
        src: item,
        placeholder: false,
      };
    }
    return item;
  });
});
</script>
<style>
.vben-image-preview .ant-image {
  margin-right: 10px;
}
.vben-image-preview .ant-image-preview-operations {
  background-color: rgba(0, 0, 0, 0.4);
}
</style>
