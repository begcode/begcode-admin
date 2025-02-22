import 'uno.css';
import '@/design/index.less';
import 'ant-design-vue/dist/reset.css';
import 'vxe-table/lib/style.css';
// Register icon sprite
// 注册图标
import 'virtual:svg-icons-register';

import { registerGlobComp } from './components/registerGlobComp';
import { setupGlobDirectives } from './directives';
import { setupI18n } from './i18n/setupI18n';
import { setupErrorHandle } from './logics/error-handle';
import { initAppConfigStore } from './logics/initAppConfig';
import { createRouter, router, setupRouter } from './router';
import { setupRouterGuard } from './router/guard';
import { setupStore } from './store';

import App from './App.vue';

import { useSso } from './hooks/web/useSso';
import Log from '@/utils/Log';
// import { registerPackages } from '@/utils/monorepo/registerPackages';
import apiService from '@/api-service/index';
import { useUserStore } from '@/store/modules/user';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

async function bootstrap() {
  // 创建应用实例
  const app = createApp(App);

  app.config.globalProperties.$apiService = apiService;
  app.config.globalProperties.$log = Log;

  // 创建路由
  createRouter();

  // 配置 store
  setupStore(app);

  // Multilingual configuration
  // 多语言配置
  // Asynchronous case: language files may be obtained from the server side
  // 异步案例：语言文件可能从服务器端获取
  // 多语言配置,异步情况:语言文件可以从服务器端获得
  await setupI18n(app);

  const userStore = useUserStore();
  app.config.globalProperties.$getToken = userStore.getToken;

  // Initialize internal system configuration
  // 初始化内部系统配置
  initAppConfigStore();

  // 注册外部模块路由
  // registerPackages(app);

  // Register global components
  // 注册全局组件
  registerGlobComp(app);

  //CAS单点登录
  await useSso().ssoLogin();

  // Configure routing
  // 配置路由
  setupRouter(app);

  // router-guard
  // 路由守卫
  setupRouterGuard(router);

  // Register global directive
  // 注册全局指令
  setupGlobDirectives(app);

  // Configure global error handling
  // 配置全局错误处理
  setupErrorHandle(app);

  // https://next.router.vuejs.org/api/#isready
  // 当路由准备好时再执行挂载
  // await router.isReady();

  // 挂载应用
  app.mount('#app', true);
}

bootstrap();
