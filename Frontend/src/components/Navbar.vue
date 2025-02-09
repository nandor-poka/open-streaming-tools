<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts"></script>
<template>
  <h1>Open Streaming Tools</h1>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <nav>
    <RouterLink to="/">Dashboard</RouterLink>
    <RouterLink to="/stagelinqlog">StageLinQ Log</RouterLink>
    <RouterLink to="/browsersource">BrowserSource</RouterLink>
  </nav>
</template>
