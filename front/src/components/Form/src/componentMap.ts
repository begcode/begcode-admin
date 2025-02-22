/**
 * Component list, register here to setting it in the form
 */
import {
  AutoComplete,
  Cascader,
  Checkbox,
  DatePicker,
  Divider,
  Input,
  InputNumber,
  Radio,
  Rate,
  Select,
  Slider,
  Switch,
  TimePicker,
  Transfer,
  TreeSelect,
} from 'ant-design-vue';
import type { ComponentType } from './types';

import ApiRadioGroup from './components/ApiRadioGroup.vue';
import RadioButtonGroup from './components/RadioButtonGroup.vue';
import ApiSelect from './components/ApiSelect.vue';
import ApiTree from './components/ApiTree.vue';
import ApiTreeSelect from './components/ApiTreeSelect.vue';
import ApiCascader from './components/ApiCascader.vue';
import SelectFile from './components/SelectFile.vue';
import ApiTransfer from './components/ApiTransfer.vue';
import ColorPicker from './components/ColorPicker.vue';
import RangeDate from './components/RangeDate.vue';
import AreaSelect from './components/AreaSelect.vue';
import RangeNumber from './components/RangeNumber.vue';
import InputPop from './components/InputPop.vue';
import AddInput from './components/AddInput.vue';
import RangeTime from './components/RangeTime.vue';
import EditorPop from './components/EditorPop.vue';
import { Tinymce as Editor } from '@/components/Tinymce';
import { Time } from '@/components/Time';
import { BasicTitle } from '@/components/Basic';
import { CountdownInput } from '@/components/CountDown';
import { IconPicker } from '@/components/Icon';
import { StrengthMeter } from '@/components/StrengthMeter';
import { BasicUpload, ImageUpload } from '@/components/Upload';
import { createAsyncComponent } from '@/utils/factory/createAsyncComponent';
import { CropperAvatar } from '@/components/Cropper';

const componentMap = new Map<ComponentType | string, Component>();

componentMap.set('Time', Time);
componentMap.set('Input', Input);
componentMap.set('InputGroup', Input.Group);
componentMap.set('InputPassword', Input.Password);
componentMap.set('InputSearch', Input.Search);
componentMap.set('InputTextArea', Input.TextArea);
componentMap.set('InputNumber', InputNumber);
componentMap.set('AutoComplete', AutoComplete);

componentMap.set('Select', Select);
componentMap.set('ApiSelect', ApiSelect);
componentMap.set('ApiTree', ApiTree);
componentMap.set('TreeSelect', TreeSelect);
componentMap.set('ApiTreeSelect', ApiTreeSelect);
componentMap.set('ApiRadioGroup', ApiRadioGroup);
componentMap.set('Switch', Switch);
componentMap.set('RadioButtonGroup', RadioButtonGroup);
componentMap.set('RadioGroup', Radio.Group);
componentMap.set('Checkbox', Checkbox);
componentMap.set('CheckboxGroup', Checkbox.Group);
componentMap.set('ApiCascader', ApiCascader);
componentMap.set('Cascader', Cascader);
componentMap.set('Slider', Slider);
componentMap.set('Rate', Rate);
componentMap.set('Transfer', Transfer);
componentMap.set('ApiTransfer', ApiTransfer);

componentMap.set('DatePicker', DatePicker);
componentMap.set('MonthPicker', DatePicker.MonthPicker);
componentMap.set('RangePicker', DatePicker.RangePicker);
componentMap.set('WeekPicker', DatePicker.WeekPicker);
componentMap.set('TimePicker', TimePicker);
componentMap.set('TimeRangePicker', TimePicker.TimeRangePicker);
componentMap.set('StrengthMeter', StrengthMeter);
componentMap.set('IconPicker', IconPicker);
componentMap.set('InputCountDown', CountdownInput);

componentMap.set('Upload', BasicUpload);
componentMap.set('SelectFile', SelectFile);
componentMap.set('Divider', Divider);
componentMap.set('Avatar', CropperAvatar);
componentMap.set('BasicTitle', BasicTitle);
componentMap.set('ImageUpload', ImageUpload);
componentMap.set('ColorPicker', ColorPicker);
componentMap.set('RangeDate', RangeDate);
componentMap.set('AreaSelect', AreaSelect);
componentMap.set('RangeNumber', RangeNumber);
componentMap.set('InputPop', InputPop);
componentMap.set('AddInput', AddInput);
componentMap.set('RangeTime', RangeTime);
componentMap.set('Editor', Editor);
componentMap.set('EditorPop', EditorPop);
componentMap.set(
  'SelectModal',
  createAsyncComponent(() => import('@/components/SelectModal/src/SelectModal.vue')),
);

componentMap.set(
  'AreaLinkage',
  createAsyncComponent(() => import('./components/AreaLinkage.vue')),
);
componentMap.set(
  'MarkdownEditor',
  createAsyncComponent(() => import('./components/MarkdownEditor.vue')),
);
componentMap.set(
  'CodeEditor',
  createAsyncComponent(() => import('@/components/CodeEditor/src/CodeEditor.vue')),
);
componentMap.set(
  'EasyCron',
  createAsyncComponent(() => import('@/components/EasyCron/EasyCronInput.vue')),
);

export function add<T extends string, R extends Component>(compName: ComponentType | T, component: R) {
  componentMap.set(compName, component);
}

export function del<T extends string>(compName: ComponentType | T) {
  componentMap.delete(compName);
}

export { componentMap };
