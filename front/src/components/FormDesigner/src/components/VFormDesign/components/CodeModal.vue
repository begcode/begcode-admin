<template>
  <a-modal
    title="代码"
    :footer="null"
    :open="state.visible"
    @cancel="state.visible = false"
    wrapClassName="v-code-modal"
    style="top: 20px"
    width="850px"
    :destroyOnClose="true"
  >
    <PreviewCode :editorJson="editorVueJson" fileFormat="vue" />
  </a-modal>
</template>
<script lang="ts">
import { formatRules, removeAttrs } from '../../../utils';
import PreviewCode from './PreviewCode.vue';
import { IFormConfig } from '../../../typings/v-form-component';

const codeVueFront: string = `<template>
  <div>
    <v-form-create
      :formConfig="formConfig"
      :formData="formData"
      v-model="fApi"
    />
    <a-button @click="submit">提交</a-button>
  </div>
</template>
<script>

export default {
  name: 'Demo',
  data () {
    return {
      fApi:{},
      formData:{},
      formConfig: `;
/* eslint-disable */
let codeVueLast = `
    }
  },
  methods: {
    async submit() {
      const data = await this.fApi.submit()
      console.log(data)
     }
  }
}
<\/script>`;

export default defineComponent({
  name: 'CodeModal',
  components: { PreviewCode },
  setup() {
    const state: any = reactive({
      visible: false,
      jsonData: {},
    });

    const showModal = (formConfig: IFormConfig) => {
      formConfig.schemas && formatRules(formConfig.schemas);
      state.visible = true;
      state.jsonData = formConfig;
    };

    const editorVueJson = computed(() => {
      return codeVueFront + JSON.stringify(removeAttrs(state.jsonData as any), null, '\t') + codeVueLast;
    });

    return { state, editorVueJson, showModal };
  },
});
</script>
