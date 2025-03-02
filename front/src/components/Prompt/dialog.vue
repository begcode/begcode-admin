<template>
  <a-modal
    v-model:open="open"
    :title="title"
    @ok="handleSubmit"
    :destroyOnClose="true"
    :width="width || '500px'"
    okText="确定"
    cancelText="取消"
  >
    <div class="pt-5 pr-3px">
      <basic-form @register="register" />
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { FormSchema, useForm } from '@/components/Form';

const props = defineProps<{
  title: string;
  addFormSchemas: FormSchema[];
  onOK?: Fn;
  width?: string;
  labelWidth?: number;
  layout?: 'horizontal' | 'vertical' | 'inline';
}>();

const open = ref<boolean>(true);

const [register, { validate }] = useForm({
  schemas: props.addFormSchemas,
  showActionButtonGroup: false,
  labelWidth: props.labelWidth || 80,
  layout: props.layout || 'horizontal',
});

async function handleSubmit() {
  const row = await validate();
  if (props.onOK) {
    await props.onOK(row.txt);
  }
  open.value = false;
}
</script>
