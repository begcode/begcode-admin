import { defHttp } from '@/utils/http/axios';

const apiUrl = '/api/cache-manage';

export default {
  // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

  getAll(): Promise<string[]> {
    return defHttp.get({ url: apiUrl });
  },

  clear(cacheName: string): Promise<any> {
    return defHttp.delete({ url: `${apiUrl}/${cacheName}` });
  },

  // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove
};
