import type { App } from 'vue';
import { Button, UploadButton, Icon, VXETablePluginAntd } from '@begcode/components';
import {
  Button as AntButton,
  Alert,
  Badge,
  Card,
  Checkbox,
  Col,
  DatePicker,
  Dropdown,
  Form,
  Input,
  InputNumber,
  Layout,
  Menu,
  Row,
  Select,
  Skeleton,
  Switch,
  Space,
  Tabs,
  TimePicker,
  Calendar,
  Radio,
  List,
  Descriptions,
  Tree,
  Table,
  Divider,
  Modal,
  Drawer,
  TreeSelect,
  Tag,
  Tooltip,
  Popover,
  Upload,
  Transfer,
  Steps,
  PageHeader,
  Result,
  Empty,
  Avatar,
  Spin,
  Breadcrumb,
  Collapse,
  Slider,
  Carousel,
  Popconfirm,
  Cascader,
} from 'ant-design-vue';
import VXETable from 'vxe-table';
import VXETablePluginExportXLSX from 'vxe-table-plugin-export-xlsx';

const compList = [AntButton.Group, Icon, UploadButton];

export function registerGlobComp(app: App) {
  compList.forEach(comp => {
    app.component(comp.name || comp.displayName, comp);
  });
  app
    .use(Input)
    .use(Button)
    .use(Breadcrumb)
    .use(Col)
    .use(Row)
    .use(Tree)
    .use(Select)
    .use(Dropdown)
    .use(Menu)
    .use(Badge)
    .use(DatePicker)
    .use(TimePicker)
    .use(Calendar)
    .use(Radio)
    .use(Switch)
    .use(InputNumber)
    .use(Alert)
    .use(Skeleton)
    .use(Card)
    .use(Checkbox)
    .use(Layout)
    .use(Form)
    .use(Space)
    .use(List)
    .use(Descriptions)
    .use(TreeSelect)
    .use(Table)
    .use(Divider)
    .use(Modal)
    .use(Drawer)
    .use(Tooltip)
    .use(Popover)
    .use(Upload)
    .use(Transfer)
    .use(Tag)
    .use(Steps)
    .use(PageHeader)
    .use(Result)
    .use(Empty)
    .use(Avatar)
    .use(Spin)
    .use(Collapse)
    .use(Slider)
    .use(Carousel)
    .use(Popconfirm)
    .use(Cascader)
    .use(VXETable)
    .use(Tabs);
  VXETable.use(VXETablePluginAntd).use(VXETablePluginExportXLSX);
}
