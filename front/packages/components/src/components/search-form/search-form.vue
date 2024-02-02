<template>
  <Form>
    <Row :gutter="[8, 16]">
      <Col :md="item.span || 4" v-for="(item, index) in config.fieldList.filter(field => !field.hidden)" :key="index">
        <!-- <Col  :md="item.span || 4" v-for="(item, index) in config.fieldList" :key="index"> -->
        <search-form-item :field="item" />
      </Col>
      <Col v-for="index of formActionSlotsShow ? blankCols : 0" :key="'blank' + index" style="flex: 1"></Col>
      <!--      <Col :span="blankCols" />-->

      <Col :md="config.showExportButton ? 6 : 4" v-if="config.compact && formActionSlotsShow">
        <FormAction v-bind="formActionConfig" @toggle-advanced="handleToggleAdvanced">
          <template #[item]="data" v-for="item in Object.keys(formActionSlots)">
            <slot :name="item" v-bind="data || {}">
              <div class="mr-2 inline-block" :key="item" v-if="formActionSlots[item].includes('mathType')">
                <RadioGroup v-model:value="config.matchType" button-style="solid">
                  <RadioButton value="and">AND</RadioButton>
                  <RadioButton value="or">OR</RadioButton>
                </RadioGroup>
              </div>
              <div class="mr-2 ml-2 inline-block" :key="item" v-if="formActionSlots[item].includes('exportButton')">
                <Button type="primary" button-style="solid" @click="exportClick" preIcon="ant-design:file-excel-filled">导出</Button>
              </div>
            </slot>
          </template>
        </FormAction>
      </Col>
    </Row>
    <BasicModal
      title="高级搜索设置"
      :footer="null"
      :open="settingModalVisible"
      @ok="settingModalVisible = false"
      @cancel="settingModalVisible = false"
    >
      <BasicForm v-bind="formConfig"></BasicForm>
    </BasicModal>
    <FormAction v-bind="formActionConfig" @toggle-advanced="handleToggleAdvanced" v-if="!config.compact && formActionSlotsShow">
      <template #[item]="data" v-for="item in Object.keys(formActionSlots)">
        <slot :name="item" v-bind="data || {}" v-if="formActionSlots[item].length">
          <div class="mr-2 inline-block" :key="item" v-if="formActionSlots[item].includes('exportButton')">
            <Button button-style="solid" @click="exportClick">导出</Button>
          </div>
        </slot>
      </template>
    </FormAction>
  </Form>
</template>

<script lang="ts">
import { Form, Button, Row, Col, RadioGroup, RadioButton } from 'ant-design-vue';
import SearchFormItem from './search-form-item.vue';
import FormAction from '@/components/Form/src/components/FormAction.vue';
import { createFormContext } from '@/components/Form/src/hooks/useFormContext';
import { cloneObject } from '@/utils';
import BasicModal from '@/components/Modal/src/BasicModal.vue';
import BasicForm from '@/components/Form/src/BasicForm.vue';
import formConfig from './form-field';

export default {
  name: 'SearchForm',
  components: { BasicForm, BasicModal, SearchFormItem, FormAction, Form, Button, Row, Col, RadioGroup, RadioButton },
  props: {
    config: {
      type: Object,
      required: true,
      default: () => {
        return {};
      },
    },
    /*
     * 这个回调函数接收一个数组参数 即查询条件
     * */
    callback: {
      type: String,
      required: false,
      default: 'handleSuperQuery',
    },

    // 当前是否在加载中
    loading: {
      type: Boolean,
      default: false,
    },

    // 保存查询条件的唯一 code，通过该 code 区分
    // 默认为 null，代表以当前路由全路径为区分Code
    saveCode: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      formConfig: {
        schemas: formConfig.fields,
        model: this.config,
        showSubmitButton: true,
        showResetButton: true,
        submitButtonOptions: {
          preIcon: '',
          text: '确认',
        },
        resetButtonOptions: {
          text: '关闭',
          preIcon: '',
        },
        resetFunc() {
          this.settingModalVisible = false;
        },
        submitFunc(params) {
          console.log('submit.params', params);
          this.settingModalVisible = false;
        },
        actionColOptions: {
          span: 24,
        },
      },
      fieldTreeData: [],
      formActionConfig: {
        isAdvanced: true,
        hideAdvanceBtn: this.config.hideAdvanceBtn || false,
        showResetButton: !(this.config.hideResetButton || false),
        showSubmitButton: !(this.config.hideResetButton || false),
        submitButtonOptions: Object.assign({}, this.config.submitButtonOptions || {}),
        resetButtonOptions: Object.assign({}, this.config.resetButtonOptions || {}),
        isLoad: false,
        actionSpan: this.config.actionSpan || (this.config.compact ? 6 : 0),
      },
      prompt: {
        visible: false,
        value: '',
      },
      settingModalVisible: false,
      visible: false,
      queryParamsModel: [],
      // 保存查询条件的treeData
      saveTreeData: [],
      // 保存查询条件的前缀名
      saveCodeBefore: 'JSuperQuerySaved_',
      // 查询类型，过滤条件匹配（and、or）
      matchType: 'and',
      superQueryFlag: false,
    };
  },
  computed: {
    izMobile() {
      return this.device === 'mobile';
    },
    blankCols() {
      let count = 0;
      this.config.fieldList
        .filter(field => !field.hidden)
        .forEach(field => {
          count = count + (field.span || 4);
          if (count > 24) {
            count = field.span || 4;
          }
          if (count === 24) {
            count = 0;
          }
        });
      if (count > this.config.showExportButton ? 18 : 20) {
        count = 0;
      }
      return 24 - count - (this.config.showExportButton ? 6 : 4);
    },
    tooltipProps() {
      return this.izMobile ? { visible: false } : {};
    },
    fullSaveCode() {
      let saveCode = this.saveCode;
      if (saveCode == null || saveCode === '') {
        saveCode = this.$route.fullPath;
      }
      return this.saveCodeBefore + saveCode;
    },
    formActionSlots() {
      const configResult: any = {
        resetBefore: [],
        advanceAfter: [],
        advanceBefore: [],
        submitBefore: [],
      };
      if (this.config.showExportButton) {
        configResult.advanceAfter.push('exportButton');
      }
      return configResult;
    },
    formActionSlotsShow() {
      const configResult: any = {
        resetBefore: [],
        advanceAfter: [],
        advanceBefore: [],
        submitBefore: [],
      };
      if (this.config.showExportButton) {
        configResult.advanceAfter.push('exportButton');
      }
      if (!this.config.hideMatchType) {
        configResult.resetBefore.push('matchType');
      }
      const buttonCount =
        (this.formActionConfig.hideAdvanceBtn ? 0 : 1) +
        (this.formActionConfig.showResetButton ? 1 : 0) +
        (this.formActionConfig.showSubmitButton ? 1 : 0) +
        configResult.advanceBefore.length +
        configResult.advanceAfter.length +
        configResult.submitBefore.length +
        configResult.resetBefore.length;
      return buttonCount > 0;
    },
  },

  created() {
    createFormContext({
      resetAction: this.resetAction,
      submitAction: this.submitAction,
    });
  },

  methods: {
    resetAction(params) {
      console.log('reset', params);
      return new Promise(() => {
        this.config.fieldList.forEach(field => {
          field.value = field.defaultValue || null;
        });
        this.$emit('formSearch');
      });
    },
    showSettingModal() {
      this.settingModalVisible = true;
    },
    submitAction(params) {
      return new Promise(() => {
        this.$emit('formSearch');
      });
    },
    handleToggleAdvanced() {
      // this.formActionConfig.isAdvanced = !this.formActionConfig.isAdvanced;
      this.$emit('close');
      // console.log('this.formActionConfig.isAdvanced', this.formActionConfig.isAdvanced)
    },
    exportClick() {
      this.$emit('export');
    },
    show() {
      if (!this.queryParamsModel || this.queryParamsModel.length === 0) {
        this.resetLine();
      }
      this.visible = true;
    },

    getDictInfo(item) {
      let str = '';
      if (!item.dictTable) {
        str = item.dictCode;
      } else {
        str = item.dictTable + ',' + item.dictText + ',' + item.dictCode;
      }
      console.log('高级查询字典信息', str);
      return str;
    },
    handleOk() {
      this.$emit('formSearch', this.searchConfig);
    },
    emitCallback(event = {}) {
      let { params = [], matchType = this.matchType } = event;
      this.superQueryFlag = params && params.length > 0;
      for (let param of params) {
        if (Array.isArray(param.val)) {
          param.val = param.val.join(',');
        }
      }
      console.debug('---高级查询参数--->', { params, matchType });
      this.$emit(this.callback, params, matchType);
    },
    handleCancel() {
      this.$emit('close');
    },
    handleAdd() {
      this.addNewLine();
    },
    addNewLine() {
      this.queryParamsModel.push({ rule: 'eq' });
    },
    resetLine() {
      this.superQueryFlag = false;
      this.queryParamsModel = [];
      this.addNewLine();
    },
    handleDel(index) {
      this.queryParamsModel.splice(index, 1);
    },
    handleSelected(node, item) {
      let { type, options, dictCode, dictTable, dictText, customReturnField, popup } = node.dataRef;
      item['type'] = type;
      item['options'] = options;
      item['dictCode'] = dictCode;
      item['dictTable'] = dictTable;
      item['dictText'] = dictText;
      item['customReturnField'] = customReturnField;
      if (popup) {
        item['popup'] = popup;
      }
      this.$set(item, 'val', undefined);
    },
    handleOpen() {
      this.show();
    },
    handleReset() {
      this.resetLine();
      this.emitCallback();
    },
    isNullArray(array) {
      //判断是不是空数组对象
      if (!array || array.length === 0) {
        return true;
      }
      if (array.length === 1) {
        let obj = array[0];
        if (!obj.field || obj.val == null || obj.val === '' || !obj.rule) {
          return true;
        }
      }
      return false;
    },
    // 去掉数组中的空对象
    removeEmptyObject(arr) {
      let array = cloneObject(arr);
      for (let i = 0; i < array.length; i++) {
        let item = array[i];
        if (item == null || Object.keys(item).length <= 0) {
          array.splice(i--, 1);
        } else {
          if (Array.isArray(item.options)) {
            delete item.options;
          }
        }
      }
      return array;
    },

    /** 判断是否允许多选 */
    allowMultiple(item) {
      return item.rule === 'in';
    },

    handleRuleChange(item, newValue) {
      let oldValue = item.rule;
      this.$set(item, 'rule', newValue);
      // 上一个规则是否是 in，且type是字典或下拉
      if (oldValue === 'in') {
        if (item.dictCode || item.options instanceof Array) {
          let value = item.val;
          if (typeof item.val === 'string') {
            value = item.val.split(',')[0];
          } else if (Array.isArray(item.val)) {
            value = item.val[0];
          }
          this.$set(item, 'val', value);
        }
      }
    },

    handleChangeJPopup(item, e, values) {
      item.val = values[item.popup['destFields']];
    },
  },
};
</script>

<style scoped>
.j-super-query-box {
  display: inline-block;
}
.j-super-query-modal .j-super-query-history-card :deep(.ant-card-body),
.j-super-query-modal .j-super-query-history-card :deep(.ant-card-head-title) {
  padding: 0;
}
.j-super-query-modal .j-super-query-history-card :deep(.ant-card-head) {
  padding: 4px 8px;
  min-height: initial;
}
.j-super-query-modal .j-super-query-history-empty :deep(.ant-empty-image) {
  height: 80px;
  line-height: 80px;
  margin-bottom: 0;
}
.j-super-query-modal .j-super-query-history-empty :deep(img) {
  width: 80px;
  height: 65px;
}
.j-super-query-modal .j-super-query-history-empty :deep(.ant-empty-description) {
  color: #afafaf;
  margin: 8px 0;
}
.j-super-query-modal .j-super-query-history-tree .j-history-tree-title {
  width: calc(76%);
  position: relative;
  display: inline-block;
}
.j-super-query-modal .j-super-query-history-tree .j-history-tree-title-closer {
  color: #999999;
  position: absolute;
  top: 0;
  right: 0;
  width: 24px;
  height: 24px;
  text-align: center;
  opacity: 0;
  transition:
    opacity 0.3s,
    color 0.3s;
}
.j-super-query-modal .j-super-query-history-tree .j-history-tree-title-closer:hover {
  color: #666666;
}
.j-super-query-modal .j-super-query-history-tree .j-history-tree-title-closer:active {
  color: #333333;
}
.j-super-query-modal .j-super-query-history-tree .j-history-tree-title:hover .j-history-tree-title-closer {
  opacity: 1;
}
.j-super-query-modal .j-super-query-history-tree :deep(.ant-tree-switcher) {
  display: none;
}
.j-super-query-modal .j-super-query-history-tree :deep(.ant-tree-node-content-wrapper) {
  width: 100%;
}
</style>
