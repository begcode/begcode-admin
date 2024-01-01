/**
 * Used to parse the .env.development proxy configuration
 */
import type { ProxyOptions } from 'vite';

type ProxyItem = [string, string];

type ProxyList = ProxyItem[];

type ProxyTargetList = Record<string, ProxyOptions>;

const httpsRE = /^https:\/\//;

/**
 * Generate proxy
 * @param list
 */
export function createProxy(list: ProxyList = []) {
  const ret: ProxyTargetList = {};
  for (const [prefix, target] of list) {
    const isHttps = httpsRE.test(target);

    const prefixes = prefix.split(',');
    prefixes.forEach(prefixLine => {
      // https://github.com/http-party/node-http-proxy#options
      ret[prefixLine] = {
        target: target,
        changeOrigin: true,
        ws: true,
        // rewrite: path => path.replace(new RegExp(`^${prefixLine}`), ''),
        // https is require secure=false
        ...(isHttps ? { secure: false } : {}),
      };
    });
  }
  return ret;
}
