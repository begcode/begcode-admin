<template>
  <div :class="[prefixCls, `${prefixCls}--${theme}`]">
    <a-breadcrumb :routes="routes">
      <template #itemRender="{ route, routes: routesMatched, paths }">
        <Icon :icon="getIcon(route)" v-if="getShowBreadCrumbIcon && getIcon(route)" />
        <span v-if="!hasRedirect(routesMatched, route)">
          {{ t(route.name || route.meta.title) }}
        </span>
        <router-link v-else to="" @click="handleClick(route, paths, $event as Event)" style="color: inherit">
          {{ t(route.name || route.meta.title) }}
        </router-link>
      </template>
    </a-breadcrumb>
  </div>
</template>
<script lang="ts" setup>
import type { RouteLocationMatched } from 'vue-router';
import type { Menu } from '@/router/types';
import { useRouter } from 'vue-router';

import { useDesign } from '@/hooks/web/useDesign';
import { useRootSetting } from '@/hooks/setting/useRootSetting';
import { useGo } from '@/hooks/web/usePage';
import { useI18n } from '@/hooks/web/useI18n';

import { filter } from '@/utils/helper/treeHelper';
import { getMenus } from '@/router/menus';

import { REDIRECT_NAME } from '@/router/constant';
import { getAllParentPath } from '@/router/helper/menuHelper';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

defineOptions({
  name: 'LayoutBreadcrumb',
});

const props = defineProps({
  theme: {
    type: String as PropType<'light' | 'dark'>,
  },
});

const routes = ref<RouteLocationMatched[]>([]);
const { currentRoute } = useRouter();
const { prefixCls } = useDesign('layout-breadcrumb');
const { getShowBreadCrumbIcon } = useRootSetting();
const go = useGo();

const { t } = useI18n();
watchEffect(async () => {
  if (currentRoute.value.name === REDIRECT_NAME) return;
  const menus = await getMenus();

  const routeMatched = currentRoute.value.matched;
  const cur = routeMatched?.[routeMatched.length - 1];
  let path = currentRoute.value.path;

  if (cur && cur?.meta?.currentActiveMenu) {
    path = cur.meta.currentActiveMenu as string;
  }

  const parent = getAllParentPath(menus, path);
  const filterMenus = menus.filter(item => item.path === parent[0]);
  const matched = getMatched(filterMenus, parent) as any;

  if (!matched || matched.length === 0) return;

  const breadcrumbList = filterItem(matched);

  if (currentRoute.value.meta?.currentActiveMenu) {
    breadcrumbList.push({
      ...currentRoute.value,
      name: currentRoute.value.meta?.title || currentRoute.value.name,
    } as unknown as RouteLocationMatched);
  }
  routes.value = breadcrumbList;
});

function getMatched(menus: Menu[], parent: string[]) {
  const metched: Menu[] = [];
  menus.forEach(item => {
    if (parent.includes(item.path)) {
      metched.push({
        ...item,
        name: item.meta?.title || item.name,
      });
    }
    if (item.children?.length) {
      metched.push(...getMatched(item.children, parent));
    }
  });
  return metched;
}

function filterItem(list: RouteLocationMatched[]) {
  return filter(list, item => {
    const { meta, name } = item;
    if (!meta) {
      return !!name;
    }
    const { title, hideBreadcrumb, hideMenu } = meta;
    if (!title || hideBreadcrumb || hideMenu) {
      return false;
    }
    return true;
  }).filter(item => !item.meta?.hideBreadcrumb);
}

function handleClick(route: RouteLocationMatched, paths: string[], e: Event) {
  e?.preventDefault();
  const { children, redirect, meta } = route;

  if (children?.length && !redirect) {
    e?.stopPropagation();
    return;
  }
  if (meta?.carryParam) {
    return;
  }

  if (redirect && _isString(redirect)) {
    go(redirect);
  } else {
    let goPath = '';
    if (paths.length === 1) {
      goPath = paths[0];
    } else {
      const ps = paths.slice(1);
      const lastPath = ps.pop() || '';
      goPath = `${lastPath}`;
    }
    goPath = /^\//.test(goPath) ? goPath : `/${goPath}`;
    go(goPath);
  }
}

function hasRedirect(routes: RouteLocationMatched[], route: RouteLocationMatched) {
  return routes.indexOf(route) !== routes.length - 1;
}

function getIcon(route) {
  return route.icon || route.meta?.icon;
}
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-layout-breadcrumb';

.@{prefix-cls} {
  display: flex;
  padding: 0 8px;
  align-items: center;

  .ant-breadcrumb-link {
    .anticon {
      margin-right: 4px;
      margin-bottom: 2px;
    }
  }

  &--light {
    .ant-breadcrumb-link {
      color: rgba(0, 0, 0, 0.85);

      a {
        color: rgba(0, 0, 0, 0.85);

        &:hover {
          color: @primary-color;
        }
      }
    }
    .ant-breadcrumb-separator,
    .anticon {
      color: rgba(0, 0, 0, 0.85);
    }
  }
}
html[data-theme='dark'] {
  .@{prefix-cls} {
    &--dark {
      .ant-breadcrumb-link {
        color: rgba(255, 255, 255, 0.85);
        a {
          color: rgba(255, 255, 255, 0.85);
          &:hover {
            color: @white;
          }
        }
      }
      .ant-breadcrumb-separator,
      .anticon {
        color: rgba(255, 255, 255, 0.85);
      }
    }
  }
}
html[data-theme='light'] {
  .@{prefix-cls} {
    &--dark {
      .ant-breadcrumb-link {
        color: rgba(255, 255, 255, 1);
        a {
          color: rgba(255, 255, 255, 1);
          &:hover {
            color: @white;
          }
        }
      }
      .ant-breadcrumb-separator,
      .anticon {
        color: rgba(255, 255, 255, 1);
      }
    }
  }
}
</style>
