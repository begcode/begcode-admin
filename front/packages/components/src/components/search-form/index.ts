import searchForm from './search-form.vue';
import searchFormItem from './search-form-item.vue';
import { TypeOperator } from './filter-operator';
import { withInstall } from '@/utils';

export const SearchForm = withInstall(searchForm);
export const SearchFormItem = withInstall(searchFormItem);
export { TypeOperator };
