// 单点登录核心类
import { getToken } from '@/utils/auth';
import { getUrlParam } from '@/utils/util';
import { useGlobSetting } from '@/hooks/setting';
import { validateCasLogin } from '@/api-service/sys/user';
import { useUserStore } from '@/store/modules/user';
const globSetting = useGlobSetting();
const openSso = globSetting.openSso;

export function useSso() {
  const locationUrl = `${document.location.protocol}//${window.location.host}/`;
  /**
   * 单点登录
   */
  async function ssoLogin() {
    if (openSso == 'true') {
      const token = getToken();
      const ticket = getUrlParam('ticket');
      if (!token) {
        if (ticket) {
          await validateCasLogin({
            ticket,
            service: locationUrl,
          }).then(res => {
            const userStore = useUserStore();
            userStore.setToken(res.token);
            return userStore.afterLoginAction(true, {});
          });
        } else {
          window.location.href = `${globSetting.casBaseUrl}/login?service=${encodeURIComponent(locationUrl)}`;
        }
      }
    }
  }

  /**
   * 退出登录
   */
  async function ssoLoginOut() {
    window.location.href = `${globSetting.casBaseUrl}/logout?service=${encodeURIComponent(locationUrl)}`;
  }
  return { ssoLogin, ssoLoginOut };
}
