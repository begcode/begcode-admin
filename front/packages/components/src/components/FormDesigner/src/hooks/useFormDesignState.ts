import { inject, Ref } from 'vue';
import { IFormDesignMethods } from '../typings/form-type';
import { IFormConfig } from '../typings/v-form-component';

/**
 * 获取formDesign状态
 */
export function useFormDesignState(): { formConfig: Ref<IFormConfig>; formDesignMethods: IFormDesignMethods } {
  const formConfig = inject<Ref<IFormConfig>>('formConfig') as Ref<IFormConfig>;
  const formDesignMethods = inject<IFormDesignMethods>('formDesignMethods') as IFormDesignMethods;
  return { formConfig, formDesignMethods };
}

export function useFormModelState(): { formModel: any; setFormModel: ((key: String, value: any) => void) | undefined } {
  const formModel = inject<any>('formModel');
  const setFormModel = inject<(key: String, value: any) => void>('setFormModelMethod');
  return { formModel, setFormModel };
}
