export const REDIRECT_NAME = 'Redirect';

export const PARENT_LAYOUT_NAME = 'ParentLayout';

export const PAGE_NOT_FOUND_NAME = 'PageNotFound';

export const PAGE_NOT_FOUND_NAME_404 = 'PageNotFound404';

export const EXCEPTION_COMPONENT = () => import('@/views/system/exception/Exception.vue');

/**
 * @description: default layout
 */
export const LAYOUT = () => import('@/layouts/default/index.vue');

/**
 * @description: parent-layout
 */
export const getParentLayout = (_name?: string) => {
  return () =>
    new Promise(resolve => {
      resolve({
        name: _name || PARENT_LAYOUT_NAME,
      });
    });
};
