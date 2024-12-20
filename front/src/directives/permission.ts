/**
 * Global authority directive
 * Used for fine-grained control of component permissions
 * @Example v-auth="RoleEnum.TEST"
 */
import { usePermission } from '@/hooks/web/usePermission';

function isAuth(el: Element, binding: any) {
  const value = binding.value;
  if (!value) return;
  const { hasPermission } = usePermission();
  if (!hasPermission(value)) {
    el.parentNode?.removeChild(el);
  }
}

const mounted = (el: Element, binding: DirectiveBinding<any>) => {
  isAuth(el, binding);
};

const authDirective: Directive = {
  mounted,
};

export function setupPermissionDirective(app: App) {
  app.directive('auth', authDirective);
}

export default authDirective;
