<template>
  <div></div>
</template>
<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { useMultipleTabStore } from '@/store/modules/multipleTab';

const { currentRoute, replace } = useRouter();

const { params, query } = unref(currentRoute);
const { path } = params;
const tabStore = useMultipleTabStore();
const redirectPageParam = tabStore.redirectPageParam;

Reflect.deleteProperty(params, '_redirect_type');
Reflect.deleteProperty(params, 'path');

const _path = Array.isArray(path) ? path.join('/') : path;

if (redirectPageParam) {
  if (redirectPageParam.redirect_type === 'name') {
    replace({
      name: redirectPageParam.name,
      query: redirectPageParam.query,
      params: redirectPageParam.params,
    });
  } else {
    replace({
      path: _path.startsWith('/') ? _path : '/' + _path,
      query,
    });
  }
}
</script>
