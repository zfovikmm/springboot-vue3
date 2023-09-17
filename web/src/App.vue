<template>
  <div class="common-layout">
    <a-layout>
      <TheHeader></TheHeader>
      <router-view/>
    </a-layout>
  </div>
</template>

<script lang="ts" setup>
  import TheHeader from './components/the-header.vue';
</script>




