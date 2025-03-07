<script lang="tsx">
import { isDef } from '@/utils/is';
import { useSortable } from '@/hooks/web/useSortable';
import { useModalContext } from '@/components/Modal/src/hooks/useModalContext';
import { theme } from 'ant-design-vue';
import { FileBasicColumn } from '@/components/Upload/src/types/typing';
import { SortOptions } from '@/components/Upload/src/props';

export default defineComponent({
  name: 'FileList',
  props: {
    columns: {
      type: Array as PropType<FileBasicColumn[]>,
      default: null,
    },
    actionColumn: {
      type: Object as PropType<FileBasicColumn>,
      default: null,
    },
    dataSource: {
      type: Array as PropType<any[]>,
      default: null,
    },
    openDrag: {
      type: Boolean,
      default: false,
    },
    dragOptions: {
      type: Object as PropType<SortOptions>,
      default: () => ({}),
    },
  },
  setup(props, { emit }) {
    const modalFn = useModalContext();
    const sortableContainer = ref<HTMLTableSectionElement>();

    watch(
      () => props.dataSource,
      () => {
        nextTick(() => {
          modalFn?.redoModalHeight?.();
        });
      },
    );
    if (props.openDrag) {
      onMounted(() =>
        useSortable(sortableContainer, {
          ...props.dragOptions,
          onEnd: ({ oldIndex, newIndex }) => {
            // position unchanged
            if (oldIndex === newIndex) {
              return;
            }
            const { onAfterEnd } = props.dragOptions;
            if (isDef(oldIndex) && isDef(newIndex)) {
              const data = [...props.dataSource];
              const [oldItem] = data.splice(oldIndex, 1);
              data.splice(newIndex, 0, oldItem);
              nextTick(() => {
                emit('update:dataSource', data);
                _isFunction(onAfterEnd) && onAfterEnd(data);
              });
            }
          },
        }).initSortable(),
      );
    }
    const { useToken } = theme;
    const { token } = useToken();
    const borderStyle = { border: `1px solid ${token.value.colorBorder}` };
    return () => {
      const { columns, actionColumn, dataSource } = props;
      const columnList = [...columns, actionColumn];
      return (
        <div class="overflow-x-auto">
          <table class="file-table" style={borderStyle}>
            <colgroup>
              {columnList.map(item => {
                const { width = 0, dataIndex } = item;
                const style: CSSProperties = {
                  width: `${width}px`,
                  minWidth: `${width}px`,
                };
                return <col style={width ? style : {}} key={dataIndex} />;
              })}
            </colgroup>
            <thead>
              <tr class="file-table-tr">
                {columnList.map(item => {
                  const { title = '', align = 'center', dataIndex } = item;
                  return (
                    <th class={['file-table-th', align]} key={dataIndex} style={borderStyle}>
                      {title}
                    </th>
                  );
                })}
              </tr>
            </thead>
            <tbody ref={sortableContainer}>
              {dataSource.map((record = {}, index) => {
                return (
                  <tr class="file-table-tr" key={`${index + record.name || ''}`}>
                    {columnList.map(item => {
                      const { dataIndex = '', customRender, align = 'center' } = item;
                      const render = customRender && _isFunction(customRender);
                      return (
                        <td class={['file-table-td break-all', align]} style={borderStyle} key={dataIndex}>
                          {render ? customRender?.({ text: record[dataIndex], record }) : record[dataIndex]}
                        </td>
                      );
                    })}
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      );
    };
  },
});
</script>
<style>
.file-table {
  width: 100%;
  border-collapse: collapse;
}

.file-table .center {
  text-align: center;
}

.file-table .left {
  text-align: left;
}

.file-table .right {
  text-align: right;
}

.file-table-th,
.file-table-td {
  padding: 12px 8px;
}

.file-table thead {
  background-color: #fafafaff;
}
</style>
