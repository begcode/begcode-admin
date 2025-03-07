import { PluginOption } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import purgeIcons from 'vite-plugin-purge-icons';
import UnoCSS from 'unocss/vite';
import VitePluginCertificate from 'vite-plugin-mkcert';
import { viteStaticCopy } from 'vite-plugin-static-copy';
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers';
import { configCompressPlugin } from './compress';
import { configVisualizerConfig } from './visualizer';
import { configThemePlugin } from './theme';
import { configSvgIconsPlugin } from './svgSprite';
import { configHtmlPlugin } from './html';

const lodashKeys = [
  'camelCase',
  'clone',
  'cloneDeep',
  'debounce',
  'difference',
  'forOwn',
  'fromPairs',
  'get',
  'merge',
  'mergeWith',
  'omit',
  'pickBy',
  'intersection',
  'intersectionWith',
  'isArray',
  'isBoolean',
  'isEmpty',
  'isEqual',
  'isFunction',
  'isNil',
  'isNull',
  'isNumber',
  'isObject',
  'isString',
  'lowerFirst',
  'random',
  'set',
  'throttle',
  'unset',
  'upperFirst',
  'uniqueId',
  'unionWith',
  'uniqBy',
  'uniq',
  'kebabCase',
];
const lodashKeysMap = lodashKeys.map(key => [key, `_${key}`]) as any;

export function createVitePlugins(viteEnv: ViteEnv, isBuild: boolean) {
  const { VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE } = viteEnv;

  const vitePlugins: (PluginOption | PluginOption[])[] = [
    // have to
    vue(),
    // have to
    vueJsx(),
    AutoImport({
      include: [
        /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
        /\.vue$/,
        /\.vue\?vue/, // .vue
        /\.md$/, // .md
      ],
      imports: [
        'vue',
        'vue-router',
        {
          'lodash-es': lodashKeysMap,
        },
      ],
      viteOptimizeDeps: true,
      dts: 'src/types/auto-imports.d.ts',
    }),
    Components({
      dts: 'src/types/components.d.ts',
      resolvers: [
        AntDesignVueResolver({
          importStyle: false,
        }),
      ],
    }),
    VitePluginCertificate({
      source: 'coding',
    }),
    UnoCSS(),
  ];

  !isBuild &&
    vitePlugins.push(
      viteStaticCopy({
        targets: [
          {
            src: 'node_modules/swagger-ui-dist/*.{html,css,png,js}',
            dest: 'static/swagger-ui',
          },
          {
            src: 'node_modules/axios/dist/axios.min.js',
            dest: 'static/swagger-ui',
          },
          {
            src: 'dev/swagger-ui/index.html',
            dest: 'static/swagger-ui',
          },
          {
            src: 'mock/data/*.csv',
            dest: 'mock/data',
          },
          {
            src: 'mock/fake-data/*.csv',
            dest: 'mock/fake-data',
          },
        ],
      }),
    );

  // vite-plugin-svg-icons
  vitePlugins.push(configSvgIconsPlugin(isBuild));

  // vite-plugin-purge-icons
  vitePlugins.push(purgeIcons());

  // vite-plugin-html
  vitePlugins.push(configHtmlPlugin(viteEnv, isBuild));

  // rollup-plugin-visualizer
  vitePlugins.push(configVisualizerConfig());

  // vite-plugin-theme
  vitePlugins.push(configThemePlugin(isBuild));

  // The following plugins only work in the production environment
  if (isBuild) {
    // rollup-plugin-gzip
    vitePlugins.push(configCompressPlugin(VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE));
  }

  return vitePlugins;
}
