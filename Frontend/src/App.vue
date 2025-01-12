<script setup lang="ts">
import Dashboard from './components/Dashboard.vue'
import StageLinQLog from './components/StageLinQLog.vue'
</script>

<template>
  <header>
    <div class="wrapper">
      <Dashboard msg="Open Streaming Tools Dashboard" />
    </div>
  </header>

  <main>
    <StageLinQLog />
  </main>
</template>
