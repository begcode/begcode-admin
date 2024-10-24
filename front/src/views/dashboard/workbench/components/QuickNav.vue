<template>
  <a-card title="快捷导航">
    <template v-for="item in navItems" :key="item.title">
      <a-card-grid>
        <span class="flex flex-col items-center">
          <Icon :icon="item.icon" :color="item.color" size="20" />
          <span class="text-md mt-2 truncate">{{ item.title }}</span>
        </span>
      </a-card-grid>
    </template>
  </a-card>
</template>
<script lang="ts" setup>
import { navItems } from './data';
</script>
