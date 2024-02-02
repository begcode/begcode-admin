import { FormSchema } from '@begcode/components';
import apiService from '@/api-service/index';

const uploadImageService = apiService.files.uploadImageService;
const relationshipApis: any = {
  category: apiService.files.resourceCategoryService.tree,
};

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: ({ values }) => {
        return values && values.id;
      },
      dynamicDisabled: true,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'Url地址',
      field: 'url',
      component: 'SelectFile',

      componentProps: ({ formModel }) => ({
        onSelectFile: (file: any) => {
          formModel['file'] = file;
        },
        onChange: (file: any) => {
          if (file instanceof File) {
            formModel['url'] = file.name;
          }
        },
      }),
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '完整文件名',
      field: 'fullName',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入完整文件名', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '文件名',
      field: 'name',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入文件名', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '扩展名',
      field: 'ext',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入扩展名', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '文件类型',
      field: 'type',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入文件类型', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '本地路径',
      field: 'path',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入本地路径', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '本地存储目录',
      field: 'folder',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入本地存储目录', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '使用实体名称',
      field: 'ownerEntityName',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入使用实体名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '使用实体ID',
      field: 'ownerEntityId',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入使用实体ID', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '业务标题',
      field: 'businessTitle',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入业务标题', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '业务自定义描述内容',
      field: 'businessDesc',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入业务自定义描述内容', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '业务状态',
      field: 'businessStatus',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入业务状态', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建时间',
      field: 'createAt',
      show: false,
      component: 'DatePicker',
      componentProps: { valueFormat: 'YYYY-MM-DD hh:mm:ss', placeholder: '请选择创建时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '文件大小',
      field: 'fileSize',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入文件大小', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '小图Url',
      field: 'smartUrl',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入小图Url', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '中等图Url',
      field: 'mediumUrl',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入中等图Url', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '文件被引用次数',
      field: 'referenceCount',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入文件被引用次数', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建者Id',
      field: 'createdBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入创建者Id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '创建时间',
      field: 'createdDate',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入创建时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '修改者Id',
      field: 'lastModifiedBy',
      show: false,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入修改者Id', controls: false, style: 'width: 100%' },
      rules: [],
    },
    {
      label: '修改时间',
      field: 'lastModifiedDate',
      show: false,
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入修改时间', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '所属分类',
      field: 'category',
      component: 'ApiTreeSelect',
      componentProps: {
        api: relationshipApis.category,
        style: 'width: 100%',
        labelInValue: true,
        numberToString: true,
        fieldNames: { children: 'children', value: 'id', label: 'title' },
        resultField: 'records',
        placeholder: '请选择所属分类',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
