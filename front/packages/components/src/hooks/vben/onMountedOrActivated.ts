import { nextTick, onActivated, onMounted } from 'vue';
import type { Fn } from '#/types';

type HookArgs = {
  type: 'mounted' | 'activated';
};

/**
 * 在 OnMounted 或者 OnActivated 时触发
 * @param hook 任何函数（包括异步函数）
 */
function onMountedOrActivated(hook: Fn<HookArgs, any>) {
  let mounted: boolean;

  onMounted(() => {
    hook({ type: 'mounted' });
    nextTick(() => {
      mounted = true;
    });
  });

  onActivated(() => {
    if (mounted) {
      hook({ type: 'activated' });
    }
  });
}

export { onMountedOrActivated };
