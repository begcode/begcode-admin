import { render, createVNode, nextTick } from 'vue';
import type { PromptFormProps } from '../typing';
import { error } from '@/utils/log';

export function usePromptForm() {
  function createPromptForm(options: PromptFormProps) {
    let instance = null;
    const box = document.createElement('div');
    const vm = createVNode(JPrompt, {
      // 注册
      async onRegister(ins) {
        instance = ins;
        await nextTick();
        ins.openModal(options);
      },
      // 销毁
      afterClose() {
        render(null, box);
        document.body.removeChild(box);
      },
    });
    // 挂载到 body
    render(vm, box);
    document.body.appendChild(box);

    function getInstance(): any {
      if (instance == null) {
        error('useJPrompt instance is undefined!');
      }
      return instance;
    }

    function updateModal(options: PromptFormProps) {
      getInstance()?.updateModal(options);
    }

    function closeModal() {
      getInstance()?.closeModal();
    }

    function setLoading(loading) {
      getInstance()?.setLoading(loading);
    }

    return {
      closeModal,
      updateModal,
      setLoading,
    };
  }

  return {
    createPromptForm,
  };
}
