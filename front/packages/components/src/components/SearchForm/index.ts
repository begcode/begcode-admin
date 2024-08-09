import searchForm from './src/search-form.vue';
import searchFormItem from './src/search-form-item.vue';
import { TypeOperator, clearSearchFieldValue } from './src/search-form-helper';
import { withInstall } from '@/utils';

export const SearchForm = withInstall(searchForm);
export const SearchFormItem = withInstall(searchFormItem);
export { TypeOperator, clearSearchFieldValue };
