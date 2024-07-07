<template>
  <FormItem
    :colon="!field.hideLabel"
    :labelCol="{ md: field.hideLabel ? 1 : field.labelSpan || 8, xs: field.hideLabel ? 0 : 24 }"
    :wrapperCol="{ md: 24 - (field.hideLabel ? 1 : field.labelSpan || 8), xs: 24 }"
    :labelAlign="field.labelAlign || 'right'"
    :name="field.field"
    style="width: 100%"
  >
    <template #label @click="changeFieldValue">
      <Badge v-if="config.showBadge">
        <template #count v-if="showBadge">
          <Icon icon="ant-design:check-circle-outlined" style="color: #f5222d" />
        </template>
        <span>{{ field.title }}</span>
      </Badge>
      <span v-else-if="!field.hideLabel">{{ field.title }}</span>
    </template>
    <Row :gutter="[8, 8]">
      <Col :md="field.labelSpan || 8" v-if="field.showFieldNames && field.showFieldComponent === 'Select'">
        <Select :dropdownMatchSelectWidth="false" :showArrow="true" placeholder="字段选择" v-model:value="field.defaultFieldName">
          <SelectOption :value="operator.value" v-for="(operator, operatorIndex) in field.fieldNames" :key="operatorIndex">
            {{ operator.title }}
          </SelectOption>
        </Select>
      </Col>
      <Col :md="field.showFieldNamesSpan || 8" v-if="field.showFieldNames && field.showFieldComponent === 'CheckGroup'">
        <CheckboxGroup v-model:value="field.defaultFieldName" :options="field.fieldNames" />
      </Col>
      <Col :md="8" v-if="field.showOperator">
        <Select
          :dropdownMatchSelectWidth="false"
          :showArrow="false"
          placeholder="匹配规则"
          :value="field.operator"
          @change="val => handleSelected(val, field)"
        >
          <SelectOption :value="operator.value" v-for="(operator, operatorIndex) in operatorByType(field.type)" :key="operatorIndex"
            >{{ operator.title }}
          </SelectOption>
        </Select>
      </Col>
      <Col :md="field.showOperator || field.showFieldNames ? 24 - (field.showFieldNamesSpan || 8) : 24">
        <DatePicker
          v-bind="field.componentProps"
          v-if="field.componentType === 'Date'"
          v-model="field.value"
          :placeholder="field?.componentProps?.placeholder || '请选择日期'"
          style="width: 100%"
        />
        <DatePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'DateTime'"
          v-model:value="field.value"
          :placeholder="field?.componentProps?.placeholder || '请选择时间'"
          :show-time="true"
          date-format="YYYY-MM-DD HH:mm:ss"
          style="width: 100%"
        />
        <TimePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'Time'"
          v-model:value="field.value"
          format="HH:mm:ss"
          :placeholder="field?.componentProps?.placeholder || '请选择时间'"
          style="width: 100%"
        />
        <RangePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'DateRange'"
          v-model:value="field.value"
          style="width: 100%"
        />
        <RangeDate
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'RangeDate'"
          v-model:value="field.value"
          style="width: 100%"
        />
        <RangePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'DateTimeRange'"
          v-model:value="field.value"
          style="width: 100%"
          show-time
        />
        <RangePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'WeekRange'"
          v-model:value="field.value"
          style="width: 100%"
          picker="week"
        />
        <RangePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'MonthRange'"
          v-model:value="field.value"
          style="width: 100%"
          picker="month"
        />
        <RangePicker
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'YearRange'"
          v-model:value="field.value"
          style="width: 100%"
          picker="year"
        />
        <InputNumber
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'Number'"
          style="width: 100%"
          :placeholder="field?.componentProps?.placeholder || '请输入数值'"
          v-model:value="field.value"
        />
        <Switch v-bind="field.componentProps" v-model="field.value" v-else-if="field.componentType === 'Switch'">
          <Icon icon="ant-design:check-outlined" slot="checkedChildren" />
          <Icon icon="ant-design:close-outlined" slot="unCheckedChildren" />
        </Switch>
        <RadioGroup v-bind="field.componentProps" v-model:value="field.value" v-else-if="field.componentType === 'RadioGroup'">
        </RadioGroup>
        <Select
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'Select'"
          :placeholder="field?.componentProps?.placeholder || '请选择'"
          v-model:value="field.value"
          style="width: 100%"
          allowClear
        >
          <SelectOption v-for="(item, index) in field.componentProps.options || []" :value="item.value" :key="index">
            {{ item.label }}
          </SelectOption>
        </Select>
        <ApiSelect
          v-else-if="field.componentType === 'ApiSelect'"
          v-bind="componentProps"
          :placeholder="componentProps?.placeholder || '请选择'"
          v-model:value="field.value"
        />
        <ApiTreeSelect
          v-else-if="field.componentType === 'ApiTreeSelect'"
          v-bind="field.componentProps"
          :placeholder="field?.componentProps?.placeholder || '请选择'"
          v-model:value="field.value"
        />
        <Select
          v-bind="field.componentProps"
          v-else-if="field.componentType === 'TagsInput'"
          mode="tags"
          v-model="field.value"
          style="width: 100%"
          :placeholder="field?.componentProps?.placeholder || '请输入值并回车'"
        />
        <Input
          v-bind="field.componentProps"
          v-else
          v-model:value="field.value"
          :placeholder="field?.componentProps?.placeholder || '请输入'"
          allowClear
          style="width: 100%"
        />
      </Col>
    </Row>
  </FormItem>
</template>
<script lang="ts">
import {
  FormItem,
  Input,
  Row,
  Col,
  Select,
  SelectOption,
  Switch,
  Badge,
  RadioGroup,
  InputNumber,
  RangePicker,
  TimePicker,
  DatePicker,
  CheckboxGroup,
  message,
} from 'ant-design-vue';
import { TypeOperator } from './filter-operator';
import Icon from '@/components/Icon/Icon.vue';
import { ApiSelect } from '@/components/Form/index';
import { ApiTreeSelect } from '@/components/Form/index';
import { isFunction } from 'lodash-es';
import RangeDate from '@/components/Form/src/components/RangeDate.vue';

export default {
  name: 'SearchFormItem',
  components: {
    Badge,
    RangeDate,
    Icon,
    ApiSelect,
    ApiTreeSelect,
    FormItem,
    Input,
    Row,
    Col,
    Select,
    SelectOption,
    Switch,
    RadioGroup,
    InputNumber,
    RangePicker,
    TimePicker,
    DatePicker,
    CheckboxGroup,
  },
  props: {
    field: {
      type: Object,
      required: true,
    },
    config: {
      type: Object,
      default: () => {
        return {
          showBadge: false,
        };
      },
    },
  },
  computed: {
    showBadge() {
      return (
        (this.field.value !== null && this.field.value !== undefined && this.field.value !== '') ||
        (Array.prototype.isPrototypeOf(this.field.value) && this.field.value.length !== 0)
      );
    },
    componentProps() {
      if (isFunction(this.field.componentProps)) {
        return this.field.componentProps();
      } else {
        return this.field.componentProps;
      }
    },
  },
  created() {
    if (!this.field.componentType) {
      this.componentByField(this.field);
    }
  },
  methods: {
    operatorByType(type) {
      switch (type) {
        case 'Boolean':
        case 'Enum':
        case 'UUID':
          return TypeOperator.common;
        case 'Integer':
        case 'Long':
        case 'Float':
        case 'Double':
        case 'LocalDate':
        case 'BigDecimal':
          return TypeOperator.range;
        case 'ZonedDateTime':
          return TypeOperator.dateTime;
        case 'String':
          return TypeOperator.text;
        case 'RelationId':
          return TypeOperator.range;
        default:
          return TypeOperator.null;
      }
    },
    handleSelectField(_fieldName, field) {
      if (field.componentType === 'Select') {
        field.componentProps = {
          ...field.componentProps,
          options: field.options,
        };
      }
    },
    handleSelected(operator, field) {
      switch (field.type) {
        case 'Boolean':
          field.componentType = 'Switch';
          if (operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'CheckBox';
            field.value = [];
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
          }
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'Enum':
          field.componentType = 'Select';
          field.props = { multiple: false };
          if (operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'Select';
            field.props = { multiple: true };
            field.value = [];
          }
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'UUID':
        case 'String':
          field.componentType = 'Text';
          if (operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'TagsInput';
            field.value = [];
          }
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'Integer':
        case 'Long':
        case 'Float':
        case 'Double':
        case 'BigDecimal':
          field.componentType = 'Number';
          if (operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'TagsInput';
            field.value = [];
          }
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'LocalDate':
        case 'Date':
          field.componentType = 'Date';
          if (operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'TagsInput';
            field.value = [];
            field.props = { type: 'Date' };
          }
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'ZonedDateTime':
        case 'Instant':
        case 'Duration':
          field.componentType = 'DateTime';
          field.value = null;
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'Blob':
        case 'AnyBlob':
        case 'ImageBlob':
        case 'TextBlob':
        case 'ByteBuffer':
          field.componentType = null;
          field.value = null;
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        case 'RelationId':
          field.componentType = 'SelectListModal';
          if (operator === 'in') {
            field.componentType = 'SelectListModal';
            field.props = { multiple: true };
            field.value = [];
          }
          if (operator === 'specified') {
            field.componentType = 'Switch';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = null;
          }
          break;
        default:
          field.componentType = null;
      }
      field.operator = operator;
    },
    componentByField(field) {
      switch (field.type) {
        case 'Boolean':
          field.componentType = 'Switch';
          if (field.operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'CheckBox';
            field.options = [
              { label: '是', value: true },
              { label: '否', value: false },
            ];
            field.value = [];
          }
          break;
        case 'Enum':
          field.componentType = 'Select';
          field.props = { multiple: false };
          if (field.operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'Select';
            field.props = { multiple: true };
          }
          break;
        case 'UUID':
        case 'String':
          field.componentType = 'Text';
          if (field.operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'TagsInput';
            field.value = [];
          }
          break;
        case 'Integer':
        case 'Long':
        case 'Float':
        case 'Double':
        case 'BigDecimal':
          field.componentType = 'Number';
          if (field.operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'TagsInput';
            field.value = [];
          }
          break;
        case 'LocalDate':
        case 'Date':
          field.componentType = 'Date';
          if (field.operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'TagsInput';
            field.value = [];
            field.props = { type: 'Date' };
          }
          break;
        case 'ZonedDateTime':
        case 'Instant':
        case 'Duration':
          field.componentType = 'DateTime';
          break;
        case 'Blob':
        case 'AnyBlob':
        case 'ImageBlob':
        case 'TextBlob':
        case 'ByteBuffer':
          field.componentType = null;
          break;
        case 'RelationId':
          if (field.operator === 'in' || field.operator === 'notIn') {
            field.componentType = 'SelectListModal';
            field.props = { multiple: true };
          }
          field.componentType = 'SelectListModal';
          break;
        default:
          field.componentType = null;
      }
    },
    changeFieldValue() {
      if (!this.config.showBadge) {
        return;
      }
      if (
        this.field.value !== null &&
        this.field.value !== undefined &&
        this.field.value !== '' &&
        !(this.field.value instanceof Array && this.field.value.length)
      ) {
        switch (this.field.componentType) {
          case 'TagsInput':
            this.field.value = [];
            break;
          default:
            this.field.value = null;
        }
        this.componentByField(this.field);
        message.info('过滤条件已经清除！');
      }
    },
  },
};
</script>
