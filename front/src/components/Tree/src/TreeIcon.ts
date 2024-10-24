import Icon from '@/components/Icon/Icon.vue';

export const TreeIcon = ({ icon }: { icon: VNode | string | undefined }) => {
  if (!icon) return null;
  if (_isString(icon)) {
    return h(Icon, { icon, class: 'mr-2' });
  }
  return h(Icon);
};
