import { dateUtil } from '@begcode/components';
import userService from '@/api-service/system/user.service';

export const rules = {
  rule(type, required) {
    if (type === 'email') {
      return this.email(required);
    }
    if (type === 'phone') {
      return this.phone(required);
    }
  },
  email(required) {
    return [
      {
        required: required ? required : false,
        validator: async (_rule, value) => {
          if (required == true && !value) {
            return Promise.reject('请输入邮箱!');
          }
          if (
            value &&
            !new RegExp(
              /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            ).test(value)
          ) {
            return Promise.reject('请输入正确邮箱格式!');
          }
          return Promise.resolve();
        },
        trigger: 'change',
      },
    ];
  },
  phone(required) {
    return [
      {
        required: required,
        validator: async (_, value) => {
          if (required && !value) {
            return Promise.reject('请输入手机号码1!');
          }
          if (!/^1[3456789]\d{9}$/.test(value)) {
            return Promise.reject('手机号码格式有误');
          }
          return Promise.resolve();
        },
        trigger: 'change',
      },
    ];
  },
  startTime(endTime, required) {
    return [
      {
        required: required ? required : false,
        validator: (_, value) => {
          if (required && !value) {
            return Promise.reject('请选择开始时间');
          }
          if (endTime && value && dateUtil(endTime).isBefore(value)) {
            return Promise.reject('开始时间需小于结束时间');
          }
          return Promise.resolve();
        },
        trigger: 'change',
      },
    ];
  },
  endTime(startTime, required) {
    return [
      {
        required: required ? required : false,
        validator: (_, value) => {
          if (required && !value) {
            return Promise.reject('请选择结束时间');
          }
          if (startTime && value && dateUtil(value).isBefore(startTime)) {
            return Promise.reject('结束时间需大于开始时间');
          }
          return Promise.resolve();
        },
        trigger: 'change',
      },
    ];
  },
  confirmPassword(values, required) {
    return [
      {
        required: required ? required : false,
        validator: (_, value) => {
          if (!value) {
            return Promise.reject('密码不能为空');
          }
          if (value !== values.newPassword) {
            return Promise.reject('两次输入的密码不一致!');
          }
          return Promise.resolve();
        },
      },
    ];
  },
  mobileCheckRule(model, schema, required?) {
    return [
      {
        validator: (_, value) => {
          if (!value && required) {
            return Promise.reject(`请输入${schema.label}`);
          }
          return new Promise<void>((resolve, reject) => {
            const params: any = { mobile: value };
            if (model.id) {
              params['id.notEquals'] = model.id;
            }
            userService
              .exist(params)
              .then(exist => {
                !exist ? resolve() : reject('该手机号已存在');
              })
              .catch(err => {
                reject(err.message || '验证失败');
              });
          });
        },
      },
    ];
  },
};
