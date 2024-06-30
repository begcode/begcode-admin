import { onMounted, onUnmounted, reactive, watch } from 'vue';
import { debounce, isArray, isObject, isString, mergeWith } from 'lodash-es';
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
  } else {
    return t('common.chooseText');
  }
}

export function useSetOperationColumn(gridCustomConfig, rowOperations, xGrid) {
  const rowOperationRefs = reactive<any>({});
  const rowOperationRef = (name, operationRef) => {
    rowOperationRefs[name] = operationRef;
  };
  if (gridCustomConfig?.rowOperations && isArray(gridCustomConfig.rowOperations)) {
    if (gridCustomConfig.rowOperations.length === 0) {
      rowOperations.value = [];
    } else {
      rowOperations.value = rowOperations.value.filter(item =>
        gridCustomConfig.rowOperations.some(rowItem => (isObject(rowItem) ? item.name === rowItem['name'] : item.name === rowItem)),
      );
    }
  }
  const setOperationColumnWidth = debounce(
    () => {
      let maxButtonCount = Math.max(...Object.values(rowOperationRefs).map(operationRef => operationRef?.getElementCount() || 0));
      if (maxButtonCount === 0) {
        maxButtonCount = 1;
      }
      const operationColumn = xGrid.value?.getColumnByField?.('recordOperation');
      if (operationColumn) {
        const titleLength = operationColumn.title?.length || 0;
        const widthValue = Math.max(titleLength * 14, maxButtonCount * 40) + 20;
        if (operationColumn.width !== widthValue) {
          console.log('setOperationColumnWith:', widthValue);
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
  mergeWith(gridOptions, componentGridOptions, (objValue: any, srcValue: any, key: any) => {
    if (isArray(objValue) && ['buttons', 'tools'].includes(key)) {
      if (!srcValue) {
        return objValue;
      } else if (isArray(srcValue) && srcValue.length === 0) {
        return srcValue;
      } else if (isArray(srcValue) && srcValue.length > 0) {
        const newObjValue: any[] = [];
        srcValue.forEach((srcItem: any) => {
          if (isObject(srcItem)) {
            const objItem = objValue.find(item => item.code === srcItem['code']) || {};
            newObjValue.push(Object.assign(objItem, srcItem));
          } else if (isString(srcItem)) {
            const objItem = objValue.find(item => item.code === srcItem);
            objItem && newObjValue.push(objItem);
          }
        });
        return newObjValue;
      }
    }
  });
}
