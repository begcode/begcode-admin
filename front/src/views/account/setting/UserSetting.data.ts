import anquan1 from './icons/anquan1.png';
import anquan2 from './icons/anquan2.png';
import app1 from './icons/app1.png';
import app2 from './icons/app2.png';
import geren1 from './icons/geren1.png';
import geren2 from './icons/geren2.png';
import { FormSchema } from '@/components/Form';
import { rules } from '@/utils/helper/validator';

export interface ListItem {
  key: string;
  title: string;
  description: string;
  extra?: string;
  avatar?: string;
  color?: string;
}

// tab的list
export const settingList = [
  {
    key: '1',
    name: '个人信息',
    component: 'BaseSetting',
    icon: 'ant-design:user-outlined',
    img1: geren1,
    img2: geren2,
  },
  {
    key: '3',
    name: '账号安全',
    component: 'AccountSetting',
    icon: 'ant-design:lock-outlined',
    img1: anquan1,
    img2: anquan2,
  },
  {
    key: '4',
    name: '第三方APP',
    component: 'WeChatDingSetting',
    icon: 'ant-design:contacts-outlined',
    img1: app1,
    img2: app2,
  },
];

/**
 * 用户表单
 */
export const formSchema: FormSchema[] = [
  {
    field: 'realname',
    component: 'Input',
    label: '姓名',
    colProps: { span: 24 },
    required: true,
  },
  {
    field: 'birthday',
    component: 'DatePicker',
    label: '生日',
    colProps: { span: 24 },
    componentProps: {
      showTime: false,
      valueFormat: 'YYYY-MM-DD',
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'sex',
    component: 'RadioGroup',
    label: '性别',
    colProps: { span: 24 },
    componentProps: {
      options: [
        {
          label: '男',
          value: 1,
        },
        {
          label: '女',
          value: 2,
        },
      ],
    },
  },
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];

//密码弹窗
export const formPasswordSchema: FormSchema[] = [
  {
    label: '用户账号',
    field: 'username',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '旧密码',
    field: 'oldpassword',
    component: 'InputPassword',
    required: true,
  },
  {
    label: '新密码',
    field: 'password',
    component: 'StrengthMeter',
    componentProps: {
      placeholder: '请输入新密码',
    },
    rules: [
      {
        required: true,
        message: '请输入新密码',
      },
    ],
  },
  {
    label: '确认新密码',
    field: 'confirmpassword',
    component: 'InputPassword',
    dynamicRules: ({ values }) => rules.confirmPassword(values, true),
  },
];
