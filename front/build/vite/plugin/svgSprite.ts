/**
 *  Vite Plugin for fast creating SVG sprites.
 * https://github.com/anncwb/vite-plugin-svg-icons
 */

import path from 'path';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';

export function configSvgIconsPlugin(isBuild: boolean) {
  return createSvgIconsPlugin({
    iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
    svgoOptions: isBuild,
    symbolId: 'icon-[dir]-[name]',
  });
}
