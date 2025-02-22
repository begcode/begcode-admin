import { ComponentType } from './componentType';
import { useI18n } from '@/hooks/web/useI18n';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

/**
 * @description: 生成placeholder
 */
export function createPlaceholderMessage(component: ComponentType) {
  const { t } = useI18n();
  if (!component) return;
  if (component.includes('RangePicker')) {
    return [t('common.chooseText'), t('common.chooseText')];
  }
  if (component.includes('Input') || component.includes('Complete') || component.includes('Rate')) {
    return t('common.inputText');
  }
  return t('common.chooseText');
}

export function useSetOperationColumn(gridCustomConfig, rowOperations, xGrid) {
  const rowOperationRefs = reactive<any>({});
  const rowOperationRef = (name, operationRef) => {
    rowOperationRefs[name] = operationRef;
  };
  if (gridCustomConfig?.rowOperations && _isArray(gridCustomConfig.rowOperations)) {
    if (gridCustomConfig.rowOperations.length === 0) {
      rowOperations.value = [];
    } else {
      rowOperations.value = rowOperations.value.filter(item =>
        gridCustomConfig.rowOperations.some(rowItem => (_isObject(rowItem) ? item.name === rowItem.name : item.name === rowItem)),
      );
    }
  }
  const setOperationColumnWidth = _debounce(
    () => {
      let maxButtonCount = Math.max(...Object.values(rowOperationRefs).map(operationRef => (operationRef as any)?.getElementCount() || 0));
      if (maxButtonCount === 0) {
        maxButtonCount = 1;
      }
      const operationColumn = xGrid.value?.getColumnByField?.('recordOperation');
      if (operationColumn) {
        const titleLength = operationColumn.title?.length || 0;
        const widthValue = Math.max(titleLength * 14, maxButtonCount * 40) + 20;
        if (operationColumn.width !== widthValue) {
          operationColumn.width = widthValue;
        }
      }
    },
    500,
    { leading: true },
  );
  watch(
    rowOperationRefs,
    () => {
      setOperationColumnWidth();
    },
    { flush: 'post' },
  );
  return {
    rowOperationRef,
  };
}

export function useSetShortcutButtons(componentName, extraButtons) {
  const tabStore = useMultipleTabStore();
  const setShortcutButtons = () => {
    const buttons = extraButtons.value.filter(button => button.show);
    tabStore.setShortcutButtons(componentName, buttons);
  };

  onMounted(() => {
    setShortcutButtons();
  });
  onUnmounted(() => {
    tabStore.setShortcutButtons(componentName, []);
  });
}

export function useColumnsConfig(columns, selectType, gridCustomConfig) {
  if (selectType !== 'checkbox') {
    const checkBoxColumn = columns.find(column => column.type === 'checkbox');
    if (checkBoxColumn) {
      if (['radio', 'seq'].includes(selectType)) {
        checkBoxColumn.type = selectType;
      } else if (selectType === 'none') {
        checkBoxColumn.visible = false;
      }
    }
  }
  if (gridCustomConfig?.hideColumns?.length > 0) {
    const filterColumns = columns.filter(column => !gridCustomConfig.hideColumns.includes(column.field));
    columns.length = 0;
    columns.push(...filterColumns);
  }
  return {
    columns,
  };
}

export function useMergeGridProps(gridOptions, componentGridOptions) {
  _mergeWith(gridOptions, componentGridOptions, (objValue: any, srcValue: any, key: any) => {
    if (_isArray(objValue) && ['buttons', 'tools'].includes(key)) {
      if (!srcValue) {
        return objValue;
      } else if (_isArray(srcValue) && srcValue.length === 0) {
        return srcValue;
      } else if (_isArray(srcValue) && srcValue.length > 0) {
        const newObjValue: any[] = [];
        srcValue.forEach((srcItem: any) => {
          if (_isObject(srcItem)) {
            const objItem = objValue.find(item => item.code === srcItem.code) || {};
            newObjValue.push(Object.assign(objItem, srcItem));
          } else if (_isString(srcItem)) {
            const objItem = objValue.find(item => item.code === srcItem);
            objItem && newObjValue.push(objItem);
          }
        });
        return newObjValue;
      }
    } else {
      if (['height'].includes(key)) {
        return srcValue;
      }
    }
  });
}

export function useSlider(min = 6, max = 12) {
  // 每行显示个数滑动条
  const getMarks = () => {
    const l = {};
    for (let i = min; i < max + 1; i++) {
      l[i] = {
        style: {
          color: '#fff',
        },
        label: i,
      };
    }
    return l;
  };
  return {
    min,
    max,
    marks: getMarks(),
    step: 1,
  };
}

export function transformToFilterTree(data: any[], fieldNames = { children: 'children', title: 'title', key: 'key' }, dataType: any): any {
  const filterItem: any = {};
  filterItem.title = dataType.title;
  filterItem.key = dataType.filterName;
  filterItem.value = dataType.filterName;
  filterItem.type = 'filterGroup';
  const generateFilterItem = (data: any[]) => {
    const result: any[] = [];
    data.forEach(recordItem => {
      const filterSubItem: any = {};
      filterSubItem.filterName = dataType.filterName;
      filterSubItem.filterValue = recordItem[fieldNames.key];
      filterSubItem.title = recordItem[fieldNames.title] || recordItem[fieldNames.key];
      filterSubItem.type = 'filterItem';
      filterSubItem.key = dataType.filterName + recordItem[fieldNames.key];
      filterSubItem.value = dataType.filterName + recordItem[fieldNames.key];
      filterSubItem.record = recordItem;
      if (fieldNames.children && recordItem[fieldNames.children]) {
        filterSubItem.children = generateFilterItem(recordItem[fieldNames.children]);
      }
      result.push(filterSubItem);
    });
    return result;
  };
  filterItem.children = generateFilterItem(data);
  return filterItem;
}
