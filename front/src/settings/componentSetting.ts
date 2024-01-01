// Used to configure the general configuration of some components without modifying the components
// 用于配置某些组件的常规配置，而无需修改组件
import type { SorterResult } from '../components/Table';

export default {
  // basic-table setting
  // 表格配置
  table: {
    // Form interface request general configuration
    // support xxx.xxx.xxx
    // 表格接口请求通用配置，可在组件prop覆盖
    // 支持 xxx.xxx.xxx格式
    fetchSetting: {
      // The field name of the current page passed to the background
      // 传给后台的当前页字段
      pageField: 'page',
      // The number field name of each page displayed in the background
      // 传给后台的每页显示多少条的字段
      sizeField: 'pageSize',
      // Field name of the form data returned by the interface
      // 接口返回表格数据的字段
      listField: 'items',
      // Total number of tables returned by the interface field name
      // 接口返回表格总数的字段
      totalField: 'total',
    },
    // Number of pages that can be selected
    // 可选的分页选项
    pageSizeOptions: ['10', '50', '80', '100'],
    // Default display quantity on one page
    //默认每页显示多少条
    defaultPageSize: 10,
    // Default Size
    // 表格默认尺寸
    defaultSize: 'middle',
    // Custom general sort function
    // 默认排序方法
    defaultSortFn: (sortInfo: SorterResult) => {
      if (sortInfo instanceof Array) {
        let sortInfoArray: any[] = [];
        for (let item of sortInfo) {
          let info = getSort(item);
          if (info) {
            sortInfoArray.push(info);
          }
        }
        return {
          sortInfoString: JSON.stringify(sortInfoArray),
        };
      } else {
        let info = getSort(sortInfo);
        return info || {};
      }
    },
    // Custom general filter function
    // 自定义过滤方法
    defaultFilterFn: (data: Partial<Recordable<string[]>>) => {
      return data;
    },
  },
  vxeTable: {
    table: {
      border: true,
      stripe: true,
      columnConfig: {
        resizable: true,
        isCurrent: true,
        isHover: true,
      },
      rowConfig: {
        isCurrent: true,
        isHover: true,
      },
      emptyRender: {
        name: 'AEmpty',
      },
      printConfig: {},
      exportConfig: {},
      customConfig: {
        storage: true,
      },
    },
    grid: {
      toolbarConfig: {
        enabled: true,
        export: true,
        zoom: true,
        print: true,
        refresh: true,
        custom: true,
      },
      pagerConfig: {
        pageSizes: [20, 50, 100, 500],
        pageSize: 20,
        autoHidden: true,
      },
      proxyConfig: {
        form: true,
        props: {
          result: 'items',
          total: 'total',
        },
      },
      zoomConfig: {},
    },
  },
  // scrollbar setting
  // 滚动组件配置
  scrollbar: {
    // Whether to use native scroll bar
    // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
    // 是否使用原生滚动样式
    // 开启后，菜单，弹窗，抽屉会使用原生滚动条组件
    native: false,
  },
  //表单配置
  form: {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 4 },
      xl: { span: 6 },
      xxl: { span: 4 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 18 },
    },
    //表单默认冒号
    colon: true,
  },
};

/**
 * 获取排序信息
 * @param item
 */
function getSort(item) {
  const { field, order } = item;
  if (field && order) {
    let sortType = 'ascend' == order ? 'asc' : 'desc';
    return {
      // 排序字段
      column: field,
      // 排序方式 asc/desc
      order: sortType,
    };
  }
  return '';
}
