<script lang="tsx">
import { getCurrentInstance, reactive, computed, defineComponent, h, ref, resolveComponent, Component, nextTick, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { isBoolean, isFunction } from 'lodash-es';

import config from './config/edit-config';
import { UReportFile, IUReportFile } from '@/models/report/u-report-file.model';

import ServerProvider from '@/api-service/index';
import { BasicForm, Button, Icon, PageWrapper } from '@begcode/components';
import { useGo } from '@/hooks/web/usePage';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

export default defineComponent({
  // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
  name: 'ReportUReportFileEdit',
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
    const relationshipApis: any = {};
    const route = useRoute();
    const router = useRouter();
    const go = useGo();
    const tabStore = useMultipleTabStore();
    const activeNames = ref<any[]>([]);
    const handleChange = (val: any[]) => {
      activeNames.value = val;
      ctx?.$emit('change', activeNames.value);
    };
    let uReportFileId = ref('');
    if (props.containerType === 'router') {
      uReportFileId.value = route.params?.entityId as string;
    } else {
      uReportFileId.value = props.entityId as string;
    }
    const uReportFile = ref<IUReportFile>(new UReportFile());
    uReportFile.value = Object.assign(uReportFile.value, props.baseData);
    if (uReportFileId.value) {
      const data = await apiService.report.uReportFileService.find(Number(uReportFileId.value));
      if (data) {
        uReportFile.value = Object.assign(uReportFile.value, data);
      }
    }
    const formItemsConfig = config.fields();
    const submitButtonTitlePrefix = uReportFileId.value ? '更新' : '保存';
    const saveOrUpdateApi = uReportFileId.value ? apiService.report.uReportFileService.update : apiService.report.uReportFileService.create;
    const saveOrUpdate = () => {
      validate()
        .then(result => {
          if (result) {
            uReportFile.value = Object.assign(uReportFile.value, result);
            saveOrUpdateApi(uReportFile.value)
              .then(res => {
                uReportFile.value = Object.assign(uReportFile.value, res);
                uReportFileId.value = uReportFile.value.id + '';
                message.success(submitButtonTitlePrefix + '成功！');
                if (props.containerType === 'router') {
                  const { fullPath } = route; //获取当前路径
                  tabStore.closeTabByKey(fullPath, router).then(() => {
                    go('/report/u-report-file/' + uReportFileId.value + '/edit', true);
                  });
                } else {
                  if (!props.savedOpen) {
                    ctx?.$emit('refresh', { update: true, containerType: props.containerType });
                  }
                  (ctx?.$refs['BASE_ENTITY'] as any).setFieldsValue(uReportFile.value);
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
            return !!uReportFile.value.id;
          },
          type: 'primary',
          theme: 'save',
          click: saveOrUpdate,
        },
        {
          hide: () => {
            return !uReportFile.value.id;
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
        title: props.containerType === 'router' ? '报表存储' : null,
        operation: [],
        component: {
          name: 'a-form',
          props: {
            modelName: 'BASE_ENTITY',
            model: uReportFile.value,
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
    ]);
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
                      : [h(childComponent, { ...child.props, ref: child.props.modelName }, () => child.slots || {})],
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
                        uReportFileId.value ? '编辑报表存储' : '新增报表存储',
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
      uReportFileId,
      saveOrUpdate,
      formGroup,
      pageConfig,
      slots,
      uReportFile,
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
