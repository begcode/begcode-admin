<template>
  <PageWrapper title="关于">
    <template #headerContent>
      <div class="flex justify-between items-center">
        <span class="flex-1">
          <a :href="GITHUB_URL" target="_blank">{{ name }}</a>
          是一个基于Vue3.0、Vite、 Ant-Design-Vue、TypeScript 的后台解决方案，目标是为中大型项目开发,提供现成的开箱解决方案及丰富的示例。
        </span>
      </div>
    </template>
    <Descriptions @register="infoRegister" class="enter-y" />
    <Descriptions @register="register" class="my-4 enter-y" />
    <Descriptions @register="registerDev" class="enter-y" />
    <Descriptions @register="backendRegister" class="enter-y" />
  </PageWrapper>
</template>
<script lang="ts" setup>
import { Tag } from 'ant-design-vue';
import { DescItem, useDescriptions } from '@/components/Descriptions';
import { GITHUB_URL, SITE_URL, DOC_URL } from '@/settings/siteSetting';

const { pkg, lastBuildTime } = __APP_INFO__;

const { dependencies, devDependencies, name, version } = pkg;

const schema: DescItem[] = [];
const devSchema: DescItem[] = [];
const backendData: any[] = [];
const backendSchema: DescItem[] = [];
backendSchema.push({ field: 'spring boot', label: 'spring boot' });
backendData['spring boot'] = '2.7.1';

const commonTagRender = (color: string) => curVal => h(Tag, { color }, () => curVal);
const commonLinkRender = (text: string) => href => h('a', { href, target: '_blank' }, text);

const infoSchema: DescItem[] = [
  {
    label: '版本',
    field: 'version',
    render: commonTagRender('blue'),
  },
  {
    label: '最后编译时间',
    field: 'lastBuildTime',
    render: commonTagRender('blue'),
  },
  {
    label: '文档地址',
    field: 'doc',
    render: commonLinkRender('文档地址'),
  },
  {
    label: '预览地址',
    field: 'preview',
    render: commonLinkRender('预览地址'),
  },
  {
    label: 'Github',
    field: 'github',
    render: commonLinkRender('Github'),
  },
];

const infoData = {
  version,
  lastBuildTime,
  doc: DOC_URL,
  preview: SITE_URL,
  github: GITHUB_URL,
};

Object.keys(dependencies).forEach(key => {
  schema.push({ field: key, label: key });
});

Object.keys(devDependencies).forEach(key => {
  devSchema.push({ field: key, label: key });
});

const [register] = useDescriptions({
  title: '生产环境依赖',
  data: dependencies,
  schema: schema,
  column: 3,
});

const [registerDev] = useDescriptions({
  title: '开发环境依赖',
  data: devDependencies,
  schema: devSchema,
  column: 3,
});

const [infoRegister] = useDescriptions({
  title: '项目信息',
  data: infoData,
  schema: infoSchema,
  column: 2,
});
const [backendRegister] = useDescriptions({
  title: '后端技术栈',
  data: backendData,
  schema: backendSchema,
  column: 2,
});
</script>
