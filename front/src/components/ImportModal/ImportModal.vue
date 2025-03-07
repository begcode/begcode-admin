<template>
  <div>
    <basic-modal
      v-bind="$attrs"
      @register="register"
      title="导入EXCEL"
      :width="600"
      @cancel="handleClose"
      :confirmLoading="uploading"
      destroyOnClose
    >
      <!--是否校验-->
      <div style="margin: 0 5px 1px" v-if="online">
        <span style="display: inline-block; height: 32px; line-height: 32px; vertical-align: middle">是否开启校验:</span>
        <span style="margin-left: 6px">
          <a-switch
            :checked="validateStatus == 1"
            @change="handleChangeValidateStatus"
            checked-children="是"
            un-checked-children="否"
            size="small"
          />
        </span>
      </div>
      <!--上传-->
      <a-upload name="file" accept=".xls,.xlsx" :multiple="true" :fileList="fileList" :remove="handleRemove" :beforeUpload="beforeUpload">
        <basic-button pre-icon="ant-design:upload-outlined">选择导入文件</basic-button>
      </a-upload>
      <!--页脚-->
      <template #footer>
        <basic-button @click="handleClose">关闭</basic-button>
        <basic-button type="primary" @click="handleImport" :disabled="uploadDisabled" :loading="uploading">{{
          uploading ? '上传中...' : '开始上传'
        }}</basic-button>
      </template>
    </basic-modal>
  </div>
</template>

<script lang="ts" setup>
import { useModalInner } from '@/components/Modal';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { defHttp } from '@/utils/http/axios';
import { useGlobSetting } from '@/hooks/setting';
import { useMessage } from '@/hooks/web/useMessage';

defineOptions({
  name: 'ImportModal',
});

const props = defineProps({
  url: {
    type: String,
    default: '',
    required: false,
  },
  biz: {
    type: String,
    default: '',
    required: false,
  },
  //是否online导入
  online: {
    type: Boolean,
    default: false,
    required: false,
  },
});

const emit = defineEmits(['ok', 'register']);

const { createMessage, createWarningModal } = useMessage();
//注册弹框
const [register, { closeModal }] = useModalInner(data => {
  reset(data);
});
const glob = useGlobSetting();
const attrs = useAttrs();
const uploading = ref(false);
//文件集合
const fileList = ref([]);
//上传url
const uploadAction = ref('');
const foreignKeys = ref('');
//校验状态
const validateStatus = ref(0);
const getBindValue = Object.assign({}, unref(props), unref(attrs));
//监听url
watchEffect(() => {
  props.url && (uploadAction.value = `${glob.uploadUrl}${props.url}`);
});
//按钮disabled状态
const uploadDisabled = computed(() => !(unref(fileList).length > 0));

//关闭方法
function handleClose() {
  closeModal() && reset();
}

//校验状态切换
function handleChangeValidateStatus(checked) {
  validateStatus.value = !!checked ? 1 : 0;
}

//移除上传文件
function handleRemove(file) {
  const index = unref(fileList).indexOf(file);
  const newFileList = unref(fileList).slice();
  newFileList.splice(index, 1);
  fileList.value = newFileList;
}

//上传前处理
function beforeUpload(file) {
  fileList.value = [...unref(fileList), file];
  return false;
}

//文件上传
function handleImport() {
  let { biz, online } = props;
  const formData = new FormData();
  if (biz) {
    formData.append('isSingleTableImport', biz);
  }
  if (unref(foreignKeys) && unref(foreignKeys).length > 0) {
    formData.append('foreignKeys', unref(foreignKeys));
  }
  if (_isObject(foreignKeys.value)) {
    formData.append('foreignKeys', JSON.stringify(foreignKeys.value));
  }
  if (!!online) {
    formData.append('validateStatus', unref(validateStatus));
  }
  unref(fileList).forEach(file => {
    formData.append('files[]', file);
  });
  uploading.value = true;

  //TODO 请求怎样处理的问题
  let headers = {
    'Content-Type': 'multipart/form-data;boundary = ' + new Date().getTime(),
  };
  defHttp.post({ url: props.url, params: formData, headers }, { isTransformResponse: false }).then(res => {
    uploading.value = false;
    if (res.success) {
      if (res.code == 201) {
        errorTip(res.message, res.result);
      } else {
        createMessage.success(res.message);
      }
      handleClose();
      reset();
      emit('ok');
    } else {
      createMessage.warning(res.message);
    }
  });
}

//错误信息提示
function errorTip(tipMessage, fileUrl) {
  let href = glob.uploadUrl + fileUrl;
  createWarningModal({
    title: '导入成功,但是有错误数据!',
    centered: false,
    content: `<div>
                        <span>${tipMessage}</span><br/>
                        <span>具体详情请<a href = ${href} target="_blank"> 点击下载 </a> </span>
                      </div>`,
  });
}

//重置
function reset(arg?) {
  fileList.value = [];
  uploading.value = false;
  foreignKeys.value = arg;
  validateStatus.value = 0;
}
</script>
