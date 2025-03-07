<template>
  <div>
    <div class="v-json-box">
      <CodeEditor :value="editorJson" ref="myEditor" :mode="MODE.JSON" />
    </div>
    <div class="copy-btn-box">
      <a-button @click="handleCopyJson" type="primary" class="copy-btn" data-clipboard-action="copy" :data-clipboard-text="editorJson">
        复制数据
      </a-button>
      <a-button @click="handleExportJson" type="primary">导出代码</a-button>
    </div>
  </div>
</template>

<script lang="ts">
import { message } from 'ant-design-vue';
import { CodeEditor, MODE } from '@/components/CodeEditor';

import { copyText } from '@/utils/copyTextToClipboard';

export default defineComponent({
  name: 'PreviewCode',
  components: {
    CodeEditor,
  },
  props: {
    fileFormat: {
      type: String,
      default: 'json',
    },
    editorJson: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const state = reactive({
      visible: false,
    });

    const exportData = (data: string, fileName = `file.${props.fileFormat}`) => {
      let content = 'data:text/csv;charset=utf-8,';
      content += data;
      const encodedUri = encodeURI(content);
      const actions = document.createElement('a');
      actions.setAttribute('href', encodedUri);
      actions.setAttribute('download', fileName);
      actions.click();
    };

    const handleExportJson = () => {
      exportData(props.editorJson);
    };

    const handleCopyJson = () => {
      // 复制数据
      const value = props.editorJson;
      if (!value) {
        message.warning('代码为空！');
        return;
      }
      copyText(value);
    };

    return {
      ...toRefs(state),
      exportData,
      handleCopyJson,
      handleExportJson,
      MODE,
    };
  },
});
</script>
<style lang="less" scoped>
.copy-btn-box {
  padding-top: 8px;
  text-align: center;

  .copy-btn {
    margin-right: 8px;
  }
}
</style>
