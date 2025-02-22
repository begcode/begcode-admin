import type { RouteRecordNormalized, Router } from 'vue-router';
import { createRouter, createWebHashHistory } from 'vue-router';
import type { AppRouteModule, AppRouteRecordRaw } from '@/router/types';
import { EXCEPTION_COMPONENT, LAYOUT, getParentLayout } from '@/router/constant';
import { warn } from '@/utils/Log';
import { getToken } from '@/utils/auth';
import { _eval } from '@/utils/util';
import { useI18n } from '@/hooks/web/useI18n';

export type LayoutMapKey = 'LAYOUT';
const IFRAME = () => import('@/views/system/iframe/FrameBlank.vue');
const LayoutContent = () => import('@/layouts/default/content/index.vue');

const LayoutMap = new Map<string, () => Promise<typeof import('*.vue')>>();

LayoutMap.set('LAYOUT', LAYOUT);
LayoutMap.set('IFRAME', IFRAME);
LayoutMap.set('LayoutsContent', LayoutContent);

let dynamicViewsModules: Record<string, () => Promise<Recordable>>;

// Dynamic introduction
function asyncImportRoute(routes: AppRouteRecordRaw[] | undefined) {
  if (!dynamicViewsModules) {
    dynamicViewsModules = import.meta.glob('../../views/**/*.{vue,tsx}');
  }
  if (!routes) return;
  routes.forEach(item => {
    if (item?.meta?.title) {
      const { t } = useI18n();
      if (item.meta.title.includes("t('") && t) {
        item.meta.title = new Function('t', `return ${item.meta.title}`)(t);
      }
    }
    // @ts-ignore
    if (item?.hidden) {
      item.meta.hideMenu = true;
      //是否隐藏面包屑
      item.meta.hideBreadcrumb = true;
    }
    // @ts-ignore
    if (item?.route == 0) {
      item.meta.ignoreRoute = true;
    }
    item.meta.ignoreKeepAlive = !item?.meta.keepAlive;
    const token = getToken();
    item.component = (item.component || '').replace(/{{([^}}]+)?}}/g, (_s1, s2) => _eval(s2)).replace('${token}', token);
    if (/^\/?http(s)?/.test(item.component as string)) {
      item.component = item.component.substring(1, item.component.length);
    }
    if (/^http(s)?/.test(item.component as string)) {
      if (item.meta?.internalOrExternal) {
        // @ts-ignore 外部打开
        item.path = item.component;
      } else {
        // @ts-ignore 内部打开
        item.meta.frameSrc = item.component;
      }
      delete item.component;
    }
    if (!item.component && item.meta?.frameSrc) {
      item.component = 'IFRAME';
    }
    let { component, name } = item;
    const { children } = item;
    if (component) {
      const layoutFound = LayoutMap.get(component.toUpperCase());
      if (layoutFound) {
        item.component = layoutFound;
      } else {
        if (component.indexOf('dashboard/') > -1) {
          //当数据标sys_permission中component没有拼接index时前端需要拼接
          if (component.indexOf('/index') < 0) {
            component = `${component}/index`;
          }
        }
        item.component = dynamicImport(dynamicViewsModules, component as string);
      }
    } else if (name) {
      item.component = getParentLayout();
    }
    children && asyncImportRoute(children);
  });
}

function dynamicImport(dynamicViewsModules: Record<string, () => Promise<Recordable>>, component: string) {
  const keys = Object.keys(dynamicViewsModules);
  const matchKeys = keys.filter(key => {
    const k = key.replace('../../views', '');
    const startFlag = component.startsWith('/');
    const endFlag = component.endsWith('.vue') || component.endsWith('.tsx');
    const startIndex = startFlag ? 0 : 1;
    const lastIndex = endFlag ? k.length : k.lastIndexOf('.');
    return k.substring(startIndex, lastIndex) === component;
  });
  if (matchKeys?.length === 1) {
    const matchKey = matchKeys[0];
    return dynamicViewsModules[matchKey];
  } else if (matchKeys?.length > 1) {
    warn(
      'Please do not create `.vue` and `.TSX` files with the same file name in the same hierarchical directory under the views folder. This will cause dynamic introduction failure',
    );
  } else {
    warn(`在src/views/下找不到\`${component}.vue\` 或 \`${component}.tsx\`, 请自行创建!`);
    return EXCEPTION_COMPONENT;
  }
}

// Turn background objects into routing objects
// 将背景对象变成路由对象
export function transformObjToRoute<T = AppRouteModule>(routeList: AppRouteModule[]): T[] {
  routeList.forEach(route => {
    const component = route.component as string;
    if (component) {
      if (component.toUpperCase() === 'LAYOUT') {
        route.component = LayoutMap.get(component.toUpperCase());
      } else {
        route.children = [_cloneDeep(route)];
        route.component = LAYOUT;
        //某些情况下如果name如果没有值， 多个一级路由菜单会导致页面404
        if (!route.name) {
          warn(`找不到菜单对应的name, 请检查数据!${JSON.stringify(route)}`);
        }
        route.name = `${route.name}Parent`;
        route.path = '';
        const meta = route.meta || {};
        meta.single = true;
        meta.affix = false;
        route.meta = meta;
      }
    } else {
      warn(`请正确配置路由：${route?.name}的component属性`);
    }
    route.children && asyncImportRoute(route.children);
  });
  return routeList as unknown as T[];
}

/**
 * Convert multi-level routing to level 2 routing
 * 将多级路由转换为 2 级路由
 */
export function flatMultiLevelRoutes(routeModules: AppRouteModule[]) {
  const modules: AppRouteModule[] = _cloneDeep(routeModules);

  for (let index = 0; index < modules.length; index++) {
    const routeModule = modules[index];
    // 判断级别是否 多级 路由
    if (!isMultipleRoute(routeModule)) {
      // 声明终止当前循环， 即跳过此次循环，进行下一轮
      continue;
    }
    // 路由等级提升
    promoteRouteLevel(routeModule);
  }
  return modules;
}

// Routing level upgrade
//提升路由级别
function promoteRouteLevel(routeModule: AppRouteModule) {
  // Use vue-router to splice menus
  // 使用vue-router拼接菜单
  // createRouter 创建一个可以被 Vue 应用程序使用的路由实例
  let router: Router | null = createRouter({
    routes: [routeModule as unknown as RouteRecordNormalized],
    history: createWebHashHistory(),
  });
  // getRoutes： 获取所有 路由记录的完整列表。
  const routes = router.getRoutes();
  // 将所有子路由添加到二级路由
  addToChildren(routes, routeModule.children || [], routeModule);
  router = null;

  // omit lodash的函数 对传入的item对象的children进行删除
  routeModule.children = routeModule.children?.map(item => _omit(item, 'children'));
}

// Add all sub-routes to the secondary route
// 将所有子路由添加到二级路由
function addToChildren(routes: RouteRecordNormalized[], children: AppRouteRecordRaw[], routeModule: AppRouteModule) {
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    const route = routes.find(item => item.name === child.name);
    if (!route) {
      continue;
    }
    routeModule.children = routeModule.children || [];
    if (!routeModule.children.find(item => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteModule);
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule);
    }
  }
}

// Determine whether the level exceeds 2 levels
// 判断级别是否超过2级
function isMultipleRoute(routeModule: AppRouteModule) {
  // Reflect.has 与 in 操作符 相同, 用于检查一个对象(包括它原型链上)是否拥有某个属性
  if (!routeModule || !Reflect.has(routeModule, 'children') || !routeModule.children?.length) {
    return false;
  }

  const children = routeModule.children;

  let flag = false;
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    if (child.children?.length) {
      flag = true;
      break;
    }
  }
  return flag;
}
/**
 * 组件地址前加斜杠处理
 * @updateBy:lsq
 * @updateDate:2021-09-08
 */
export function addSlashToRouteComponent<T = AppRouteModule>(routeList: AppRouteRecordRaw[]) {
  routeList.forEach(route => {
    const component = route.component as string;
    if (component) {
      const layoutFound = LayoutMap.get(component);
      if (!layoutFound) {
        route.component = component.startsWith('/') ? component : `/${component}`;
      }
    }
    route.children && addSlashToRouteComponent(route.children);
  });
  return routeList as unknown as T[];
}
