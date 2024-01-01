export const buttonTheme = {
  add: {
    title: '新增',
    type: 'primary',
    icon: 'ant-design:file-add-filled',
  },
  edit: {
    title: '编辑',
    type: 'primary',
    icon: 'ant-design:edit-outlined',
  },
  cancel: {
    title: '取消',
    type: 'primary',
    icon: 'ant-design:close-circle-outlined',
  },
  confirm: {
    title: '确认',
    type: 'primary',
    icon: 'ant-design:check-circle-outlined',
  },
  save: {
    title: '保存',
    type: 'primary',
    icon: 'ant-design:save-outlined',
  },
  submit: {
    title: '提交',
    type: 'primary',
    icon: 'ant-design:check-outlined',
  },
  delete: {
    title: '删除',
    type: 'primary',
    icon: 'ant-design:delete-outlined',
    info: '是否删除这条数据',
  },
  batchDelete: {
    title: '批量删除',
    type: 'primary',
    icon: 'ant-design:delete-outlined',
  },
  exports: {
    title: '导出',
    type: 'primary',
    icon: 'ant-design:download-outlined',
  },
  imports: {
    title: '导入',
    type: 'primary',
    upload: true,
    icon: 'ant-design:upload-outlined',
  },
  upload: {
    title: '上传',
    upload: true,
    type: 'primary',
    icon: 'ant-design:arrow-up-outlined',
  },
  downloadTemp: {
    title: '模板下载',
    type: 'primary',
    icon: 'ant-design:cloud-download-outlined',
  },
  withdraw: {
    title: '撤回',
    type: 'primary',
    icon: 'recall',
  },
  search: {
    title: '查询',
    type: 'primary',
    icon: 'search',
  },
  refresh: {
    title: '重置',
    type: 'default',
    icon: 'ant-design:sync-outlined',
  },
  preview: {
    title: '预览',
    type: 'primary',
    icon: 'ant-design:fund-view-outlined',
  },
  invalid: {
    title: '作废',
    type: 'primary',
    icon: 'el-icon-document-remove',
  },
  print: {
    title: '打印',
    type: 'primary',
    icon: 'ant-design:printer-outlined',
  },
  distribution: {
    title: '分配',
    type: 'primary',
    icon: 'ant-design:appstore-add-outlined',
  },
  copy: {
    title: '复制',
    type: 'primary',
    icon: 'ant-design:copy-outlined',
  },
  primary: {
    type: 'primary',
  },
  close: {
    title: '关闭',
    type: 'primary',
    icon: 'ant-design:close-circle-outlined',
  },
  publish: {
    title: '发布',
    type: 'primary',
    icon: 'ant-design:send-outlined',
  },
};

function downloadTemp(item) {
  let params = item.params || {};
  if (typeof item.params === 'function') {
    params = item.params();
  }
  return item.api(params || {});
}

export function getButtonConfig(item) {
  const downTHeme = ['downloadTemp', 'exports'];
  if (item.theme) {
    const themeval = (item.theme || '').replace('batch', '');
    const themeConfig = buttonTheme[themeval] || {};
    let text = themeConfig.text;
    if (/^batch/.test(item.theme)) {
      text = `批量${text || ''}`;
    }
    const option = Object.assign({ loading: false, hide: false }, themeConfig, { text }, item);
    if (typeof item.action === 'function' || (downTHeme.includes(item.theme) && item.api)) {
      option.action = function (...arg) {
        if (downTHeme.includes(item.theme) && item.api) {
          option.loading = true;
          downloadTemp(item)
            .then(res => {
              console.log('res', res);
              if (res.code) {
                console.log('下载失败');
              }
              item.action && item.action(option, res);
            })
            .finally(() => {
              option.loading = false;
            });
        } else {
          item.action && item.action(option, ...arg);
        }
      };
    }
    return option;
  } else {
    const option = Object.assign({ loading: false }, item, {
      icon: item.icon || '',
    });
    if (typeof item.action === 'function') {
      option.action = function (...arg) {
        item.action && item.action(option, ...arg);
      };
    }
    return option;
  }
}
