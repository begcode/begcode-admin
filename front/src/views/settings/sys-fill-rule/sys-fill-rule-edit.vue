<script lang="tsx">
import { getCurrentInstance, reactive, computed, defineComponent, h, ref, resolveComponent, Component, nextTick, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { isBoolean, isFunction } from 'lodash-es';

import config from './config/edit-config';
import { SysFillRule, ISysFillRule } from '@/models/settings/sys-fill-rule.model';

import ServerProvider from '@/api-service/index';
import { BasicForm, Button, Icon, PageWrapper } from '@begcode/components';
import { getButtonConfig } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';
import Sortable from 'sortablejs';
import { VxeTableInstance } from 'vxe-table';

export default defineComponent({
  // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
  name: 'SystemSysFillRuleEdit',
  props: {
    entityId: {
      type: [String, Number] as PropType<string | number>,
      default: '',
    },
    containerType: {
      type: String,
      default: 'router',
    },
    baseData: {
      type: Object,
      default: () => ({}),
    },
    savedOpen: {
      type: Boolean,
      default: false,
    },
  },
  components: {
    BasicForm,
  },
  async setup(props) {
    const ctx = getCurrentInstance()?.proxy;
    const apiService = ctx?.$apiService as typeof ServerProvider;
    const relationshipApis: any = {
      ruleItems: apiService.settings.fillRuleItemService.retrieve,
    };
    const route = useRoute();
    const router = useRouter();
    const go = useGo();
    const tabStore = useMultipleTabStore();
    const activeNames = ref<any[]>([]);
    const handleChange = (val: any[]) => {
      activeNames.value = val;
      ctx?.$emit('change', activeNames.value);
    };
    let sysFillRuleId = ref('');
    if (props.containerType === 'router') {
      sysFillRuleId.value = route.params?.entityId as string;
    } else {
      sysFillRuleId.value = props.entityId as string;
    }
    const sysFillRule = ref<ISysFillRule>(new SysFillRule());
    sysFillRule.value = Object.assign(sysFillRule.value, props.baseData);
    if (sysFillRuleId.value) {
      const data = await apiService.settings.sysFillRuleService.find(Number(sysFillRuleId.value));
      if (data) {
        sysFillRule.value = Object.assign(sysFillRule.value, data);
      }
    }
    const formItemsConfig = config.fields();
    const submitButtonTitlePrefix = sysFillRuleId.value ? '更新' : '保存';
    const saveOrUpdateApi = sysFillRuleId.value
      ? apiService.settings.sysFillRuleService.update
      : apiService.settings.sysFillRuleService.create;
    const saveOrUpdate = () => {
      validate()
        .then(result => {
          if (result) {
            sysFillRule.value = Object.assign(sysFillRule.value, result);
            saveOrUpdateApi(sysFillRule.value)
              .then(res => {
                sysFillRule.value = Object.assign(sysFillRule.value, res);
                sysFillRuleId.value = sysFillRule.value.id + '';
                message.success(submitButtonTitlePrefix + '成功！');
                if (props.containerType === 'router') {
                  const { fullPath } = route; //获取当前路径
                  tabStore.closeTabByKey(fullPath, router).then(() => {
                    go('/settings/sys-fill-rule/' + sysFillRuleId.value + '/edit', true);
                  });
                } else {
                  if (!props.savedOpen) {
                    ctx?.$emit('refresh', { update: true, containerType: props.containerType });
                  }
                  (ctx?.$refs['BASE_ENTITY'] as any).setFieldsValue(sysFillRule.value);
                }
              })
              .catch(error => {
                console.log('error', error);
                message.error(submitButtonTitlePrefix + '失败！');
              });
          } else {
            message.error('数据验证失败！');
          }
        })
        .catch(error => {
          console.log('error', error);
          message.error('数据验证失败！');
        });
    };
    //获得关联表属性。
    const pageConfig = reactive<any>({
      active: '0',
      operations: [
        {
          title: '关闭',
          type: 'default',
          theme: 'close',
          skipValidate: true,
          click: async () => {
            if (props.containerType === 'router') {
              const { fullPath } = route; //获取当前路径
              await tabStore.closeTabByKey(fullPath, router);
            } else {
              ctx?.$emit('cancel', { containerType: props.containerType, update: false });
            }
          },
        },
        {
          hide: () => {
            return !!sysFillRule.value.id;
          },
          type: 'primary',
          theme: 'save',
          click: saveOrUpdate,
        },
        {
          hide: () => {
            return !sysFillRule.value.id;
          },
          theme: 'update',
          type: 'primary',
          click: saveOrUpdate,
        },
      ],
    });
    const isEdit = computed(() => {
      return true;
    });
    const validate = async () => {
      let isValid = true;
      let result = {};
      var refKeys = Object.keys(ctx?.$refs as object);
      for (const refKey of refKeys) {
        const component: any = ctx?.$refs[refKey];
        if (['BASE_ENTITY', 'FormList'].includes(refKey)) {
          if (component && component.validate) {
            const validateResult = await component.validate();
            if (!validateResult) {
              isValid = false;
              break;
            } else {
              result = { ...result, ...validateResult };
            }
          }
        } else {
          if (component && component.validate) {
            const validateResult = await component.validate(true);
            if (!validateResult) {
              const { fullData } = component.getTableData();
              fullData.forEach(row => {
                if (typeof row.id === 'string' && row.id.startsWith('row_')) {
                  row.id = null;
                }
              });
              result[refKey] = fullData;
            } else {
              isValid = false;
            }
          }
        }
      }
      if (!isValid) {
        return false;
      } else {
        return result;
      }
    };
    const formGroup = computed(() => [
      {
        title: props.containerType === 'router' ? '填充规则' : null,
        operation: [],
        component: {
          name: 'a-form',
          props: {
            modelName: 'BASE_ENTITY',
            model: sysFillRule.value,
            labelWidth: '120px',
            fieldMapToTime: [],
            compact: true,
            alwaysShowLines: 1,
            schemas: formItemsConfig,
            // formItemsRender,
            size: 'default',
            disabled: false,
            showAdvancedButton: false,
            showResetButton: false,
            showSubmitButton: false,
            showActionButtonGroup: false,
            resetButtonOptions: {
              type: 'default',
              size: 'default',
              text: '关闭',
              preIcon: null,
            },
            actionColOptions: {
              span: 18,
            },
            submitButtonOptions: {
              type: 'primary',
              size: 'default',
              text: submitButtonTitlePrefix,
              preIcon: null,
            },
            resetFunc: () => {
              ctx?.$emit('cancel', { update: false, containerType: props.containerType });
            },
            submitFunc: saveOrUpdate,
          },
          on: {},
        },
      },
      {
        title: '关联表',
        operation: [],
        component: [
          {
            name: 'vxe-grid',
            title: '配置项列表列表',
            props: {
              modelName: 'ruleItems',
              data: sysFillRule.value.ruleItems,
              columns: config.ruleItemsColumns(),
              border: true,
              showOverflow: true,
              editConfig: {
                trigger: 'click',
                mode: 'row',
                beforeEditMethod({ row, rowIndex, column, columnIndex }) {
                  console.log('beforeEditMethod', row, rowIndex, column, columnIndex);
                  return true;
                },
              },
              rowConfig: {
                useKey: true,
              },
              onEditClosed: ({ $table, row, column }) => {
                console.log('$table', $table);
                if ($table.isUpdateByRow(row)) {
                  row.loading = true;
                  apiService.settings.sysFillRuleService
                    .update({ id: row.id, [column.field]: row[column.field] }, [row.id], [column.field])
                    .then(data => {
                      $table.reloadRow(row, data, column.field);
                      message.success('保存成功！');
                    })
                    .finally(() => {
                      row.loading = false;
                    });
                }
              },
              fieldMapToTime: [],
              compact: true,
              size: 'default',
              disabled: !isEdit.value,
              toolbarConfig: {
                buttons: [
                  { code: 'insert_actived', name: '新增', icon: 'fa fa-plus' },
                  { code: 'remove', name: '删除', icon: 'fa fa-trash-o' },
                ],
                // 表格右上角自定义按钮
                tools: [
                  // { code: 'myPrint', name: '自定义打印' }
                ],
                import: false,
                export: false,
                print: false,
                custom: false,
              },
            },
            slots: {
              dragBtn: () => {
                return h('span', { class: 'drag-btn' }, h('i', { class: 'vxe-icon-sort' }, []));
              },
              dragTip: () => {
                return h('span', {}, ['拖动']);
              },
            },
          },
        ],
      },
    ]);
    let sortable1: any;

    const rowDrop = () => {
      const $table = ctx?.$refs['ruleItems'] as VxeTableInstance;
      sortable1 = Sortable.create($table.$el.querySelector('.body--wrapper>.vxe-table--body tbody'), {
        handle: '.drag-btn',
        onEnd: sortableEvent => {
          const newIndex = sortableEvent.newIndex as number;
          const oldIndex = sortableEvent.oldIndex as number;
          // const currRow = demo1.tableData.splice(oldIndex, 1)[0]
          // demo1.tableData.splice(newIndex, 0, currRow)
        },
      });
    };
    let initTime: any;
    onUnmounted(() => {
      clearTimeout(initTime);
      if (sortable1) {
        sortable1.destroy();
      }
    });
    await nextTick(() => {
      // 加载完成之后在绑定拖动事件
      initTime = setTimeout(() => {
        rowDrop();
      }, 500);
    });
    const getXGridSlots = component => {
      const slots: any = {};
      if (component.recordActions) {
        const buttons = reactive(component.recordActions.map(action => getButtonConfig(action)));
        buttons.filter(button => button.icon).forEach(button => (button.slots = { default: () => <Icon icon={button.icon} /> }));
        buttons.filter(button => !button.icon).forEach(button => (button.slots = { default: () => button.text }));
        slots.recordAction = row => (
          <div>
            <a-space>
              {buttons.map(button => {
                return (
                  <a-button
                    {...{
                      type: button.type || 'primary',
                      shape: button.shape || 'circle',
                      title: button.text || button.name,
                      onClick: () => {
                        if (button.onClick) {
                          button.onClick(row.row, button.name);
                        } else {
                          console.log(button.name, 'onClick事件未定义！');
                        }
                      },
                    }}
                    v-slots={button.slots}
                  ></a-button>
                );
              })}
            </a-space>
          </div>
        );
      } else {
        slots.recordAction = () => <div />;
      }
      if (component.slots) {
        Object.assign(slots, component.slots);
      }
      return slots;
    };
    const formSlots = () => [];
    const renderChild = () => {
      const wrapperPros: any = {};
      if (!pageConfig?.canExpand) {
        wrapperPros.bordered = false;
        wrapperPros.size = 'small';
      }
      return formGroup.value.map(item => {
        var componentRef = item.component;
        if (componentRef && !(componentRef instanceof Array)) {
          if (componentRef.name === 'a-form') {
            if (pageConfig?.canExpand) {
              // @ts-ignore
              return h('a-collapse-panel', {}, h(BasicForm, { ...componentRef.props, ref: componentRef.props.modelName }, formSlots));
            } else {
              // @ts-ignore
              return h(BasicForm, { ...componentRef.props, ref: componentRef.props.modelName }, formSlots);
            }
          } else {
            const component = resolveComponent(componentRef.name);
            return h(
              resolveComponent(pageConfig?.canExpand ? 'a-collapse-panel' : 'a-card'),
              { ...wrapperPros },
              h(component, { ...componentRef.props, ref: componentRef.props.modelName }, () => []),
            );
          }
        } else if (componentRef && componentRef instanceof Array) {
          return h(resolveComponent(pageConfig?.canExpand ? 'a-collapse-panel' : 'a-card'), { ...wrapperPros }, () =>
            h(resolveComponent('a-tabs'), {}, () =>
              componentRef.map((child, index) => {
                const childComponent: Component = resolveComponent(child.name) as Component;
                return h(
                  resolveComponent('a-tab-pane'),
                  { tab: child.title || index, key: index, disabled: child.disabled && child.disabled() },
                  () =>
                    child.disabled && child.disabled()
                      ? []
                      : [
                          h(
                            childComponent,
                            { ...child.props, ref: child.props.modelName },
                            child?.name === 'vxe-grid' ? getXGridSlots(child) : child.slots || {},
                          ),
                        ],
                );
              }),
            ),
          );
        } else {
          return <div>无内容</div>;
        }
      });
    };
    const slots: any = {
      rightFooter: () => (
        <div>
          <a-space>
            {pageConfig.operations.map((operation: any) => {
              const buttonSlots: any = {};
              if (operation.icon) {
                buttonSlots.icon = () => <Icon icon={operation.icon} />;
              }
              if (operation.text) {
                buttonSlots.default = () => operation.text;
              }
              const hideButton = isBoolean(operation.hide) ? operation.hide : isFunction(operation.hide) ? operation.hide() : false;
              switch (operation.theme) {
                case 'save':
                  if (!buttonSlots.icon) {
                    buttonSlots.icon = () => <Icon icon={'ant-design:save-outlined'} />;
                  }
                  if (!buttonSlots.default) {
                    buttonSlots.default = () => '保存';
                  }
                  return hideButton ? (
                    <span />
                  ) : (
                    <a-button
                      {...{
                        type: operation.type || 'default',
                        onClick: () => {
                          validate().then(result => {
                            operation.click(result);
                          });
                        },
                      }}
                      v-slots={buttonSlots}
                    ></a-button>
                  );
                case 'update':
                  if (!buttonSlots.icon) {
                    buttonSlots.icon = () => <Icon icon={'ant-design:check-outlined'} />;
                  }
                  if (!buttonSlots.default) {
                    buttonSlots.default = () => '更新';
                  }
                  return hideButton ? (
                    <span />
                  ) : (
                    <a-button
                      {...{
                        type: operation.type || 'default',
                        onClick: () => {
                          validate().then(result => {
                            operation.click(result);
                          });
                        },
                      }}
                      v-slots={buttonSlots}
                    ></a-button>
                  );
                default:
                  return hideButton ? (
                    <span />
                  ) : (
                    <a-button
                      {...{
                        type: operation.type || 'default',
                        onClick: () => {
                          if (operation.skipValidate) {
                            operation.click();
                          } else {
                            validate().then(result => {
                              operation.click(result);
                            });
                          }
                        },
                      }}
                    >
                      {operation.title}
                    </a-button>
                  );
              }
            })}
          </a-space>
        </div>
      ),
      default: () => {
        if (pageConfig?.canExpand) {
          return (
            <div>
              <a-collapse value={activeNames} onchange={handleChange} v-slots={{ default: () => renderChild() }} />
            </div>
          );
        } else {
          if (props.containerType === 'router') {
            return (
              <div>
                <a-card
                  {...{
                    props: {
                      shadow: 'never',
                    },
                    bordered: false,
                    size: 'small',
                  }}
                  v-slots={{
                    title: () =>
                      h(Button, { preIcon: 'ant-design:form-outlined', type: 'link' }, () => [
                        sysFillRuleId.value ? '编辑填充规则' : '新增填充规则',
                      ]),
                    default: () => renderChild(),
                  }}
                ></a-card>
              </div>
            );
          } else {
            return renderChild();
          }
        }
      },
    };
    return {
      // pageControl,
      sysFillRuleId,
      saveOrUpdate,
      formGroup,
      pageConfig,
      slots,
      rowDrop,
      sysFillRule,
    };
  },
  methods: {},
  render() {
    if (this.containerType === 'modal' || this.containerType === 'drawer') {
      // this.slots.actions = this.slots.rightFooter;
      delete this.slots.rightFooter;
      return <a-card {...this.pageConfig} v-slots={this.slots} />;
    } else {
      return (
        <PageWrapper
          {...{
            props: {
              title: this.pageConfig?.title || '编辑',
            },
          }}
          v-slots={this.slots}
        />
      );
    }
  },
});
</script>
