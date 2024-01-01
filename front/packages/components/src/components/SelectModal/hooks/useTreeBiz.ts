import type { Ref } from 'vue';
import { inject, reactive, ref, computed, unref, watch, nextTick } from 'vue';
import { TreeActionType } from '@/components/Tree';
import { listToTree } from '@/utils/common/compUtils';

export function useTreeBiz(treeRef, getList, props, realProps) {
  //接收下拉框选项
  const selectOptions = inject('selectOptions', ref<Array<object>>([]));
  //接收已选择的值
  const selectValues = <object>inject('selectValues', reactive({}));
  // 是否正在加载回显
  const loadingEcho = inject<Ref<boolean>>('loadingEcho', ref(false));
  //数据集
  const treeData = ref<Array<object>>([]);
  //已选择的值
  const checkedKeys = ref<Array<string | number>>([]);
  //选则的行记录
  const selectRows = ref<Array<object>>([]);
  //是否是打开弹框模式
  const openModal = ref(false);
  // 是否开启父子关联，如果不可以多选，就始终取消父子关联
  const getCheckStrictly = computed(() => (realProps.multiple ? props.checkStrictly : true));
  // 是否是首次加载回显，只有首次加载，才会显示 loading
  let isFirstLoadEcho = true;

  /**
   * 监听selectValues变化
   */
  watch(
    selectValues,
    ({ value: values }: Recordable) => {
      if (!values) {
        return;
      }
      if (openModal.value == false && values.length > 0) {
        loadingEcho.value = isFirstLoadEcho;
        isFirstLoadEcho = false;
        onLoadData(null, values.join(',')).finally(() => {
          loadingEcho.value = false;
        });
      }
    },
    { immediate: true },
  );

  /**
   * 获取树实例
   */
  function getTree() {
    const tree = unref(treeRef);
    if (!tree) {
      throw new Error('tree is null!');
    }
    return tree;
  }

  /**
   * 设置树展开级别
   */
  function expandTree() {
    nextTick(() => {
      if (props.defaultExpandLevel && props.defaultExpandLevel > 0) {
        getTree().filterByLevel(props.defaultExpandLevel);
      }
      //设置列表默认选中
      checkedKeys.value = selectValues['value'];
    }).then();
  }

  /**
   * 树节点选择
   */
  function onSelect(keys, info) {
    if (props.checkable == false) {
      checkedKeys.value = props.checkStrictly ? keys.checked : keys;
      const { selectedNodes } = info;
      let rows = <any[]>[];
      selectedNodes.forEach(item => {
        rows.push(item);
      });
      selectRows.value = rows;
    }
  }

  /**
   * 树节点选择
   */
  function onCheck(keys, info) {
    if (props.checkable == true) {
      // 如果不能多选，就只保留最后一个选中的
      if (!realProps.multiple) {
        if (info.checked) {
          checkedKeys.value = [info.node.eventKey];
          let rowKey = props.rowKey;
          let temp = info.checkedNodes.find(n => n[rowKey] === info.node.eventKey);
          selectRows.value = [temp.props.node];
        } else {
          checkedKeys.value = [];
          selectRows.value = [];
        }
        return;
      }
      checkedKeys.value = props.checkStrictly ? keys.checked : keys;
      const { checkedNodes } = info;
      let rows = <any[]>[];
      checkedNodes.forEach(item => {
        rows.push(item);
      });
      selectRows.value = rows;
    }
  }

  /**
   * 获取数列表
   * @param res
   */
  function getTreeRow() {
    let ids = '';
    if (unref(checkedKeys).length > 0) {
      ids = checkedKeys.value.join(',');
    }
    getList({ ids: ids }).then(res => {
      selectRows.value = res;
    });
  }

  /**
   * 勾选全部
   */
  async function checkALL(checkAll) {
    getTree().checkAll(checkAll);
    await nextTick();
    checkedKeys.value = getTree().getCheckedKeys();
    if (checkAll) {
      getTreeRow();
    } else {
      selectRows.value = [];
    }
  }

  /**
   * 展开全部
   */
  function expandAll(expandAll) {
    getTree().expandAll(expandAll);
  }

  /**
   * 加载树数据
   */
  async function onLoadData(treeNode, ids) {
    let params = {};
    let startPid = '';
    if (treeNode) {
      startPid = treeNode.eventKey;
      params['pid'] = treeNode.value;
    }
    if (ids) {
      startPid = '';
      params['ids'] = ids;
    }
    let record = await getList(params);
    let optionData = record;
    if (!props.serverTreeData) {
      //前端处理数据为tree结构
      record = listToTree(record, props, startPid);
      if (record.length == 0 && treeNode) {
        checkHasChild(startPid, treeData.value);
      }
    }

    if (openModal.value == true) {
      //弹框模式下加载全部数据
      if (!treeNode) {
        treeData.value = record;
      } else {
        return new Promise((resolve: (value?: unknown) => void) => {
          if (!treeNode.children) {
            resolve();
            return;
          }
          const asyncTreeAction: TreeActionType | null = unref(treeRef);
          if (asyncTreeAction) {
            asyncTreeAction.updateNodeByKey(treeNode.eventKey, { children: record });
            asyncTreeAction.setExpandedKeys([treeNode.eventKey, ...asyncTreeAction.getExpandedKeys()]);
          }
          resolve();
          return;
        });
      }
      expandTree();
    } else {
      const options = <any[]>[];
      optionData.forEach(item => {
        options.push({ label: item[props.titleKey], value: item[props.primaryKey] });
      });
      selectOptions.value = options;
    }
  }

  /**
   * 异步加载时检测是否含有下级节点
   * @param pid 父节点
   * @param treeArray  tree数据
   */
  function checkHasChild(pid, treeArray) {
    if (treeArray && treeArray.length > 0) {
      for (let item of treeArray) {
        if (item.key == pid) {
          if (!item.child) {
            item.isLeaf = true;
          }
          break;
        } else {
          checkHasChild(pid, item.children);
        }
      }
    }
  }

  /**
   * 获取已选择数据
   */
  function getSelectTreeData(success) {
    const options = <any[]>[];
    const values = <any[]>[];
    selectRows.value.forEach(item => {
      options.push({ label: item[props.labelKey], value: item[props.rowKey] });
    });
    checkedKeys.value.forEach(item => {
      values.push(item);
    });
    selectOptions.value = options;
    success && success(options, values);
  }

  /**
   * 弹出框显示隐藏触发事件
   */
  async function visibleChange(visible) {
    if (visible) {
      //弹出框打开时加载全部数据
      openModal.value = true;
      await onLoadData(null, null);
    } else {
      openModal.value = false;
    }
  }

  return [
    {
      visibleChange,
      selectOptions,
      selectValues,
      onLoadData,
      onCheck,
      onSelect,
      checkALL,
      expandAll,
      checkedKeys,
      selectRows,
      treeData,
      getCheckStrictly,
      getSelectTreeData,
    },
  ];
}
