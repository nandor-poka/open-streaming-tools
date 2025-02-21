<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { UnitStore } from '@/stores/UnitStore'
import Navbar from './Navbar.vue'

const unitStore = UnitStore()
const message = 'Log goes here'
</script>

<template>
  <Navbar />
  <div>
    <h4 class="blue">{{ message }}</h4>
  </div>

  <div>
    <ul>
      <li v-for="mesg in unitStore.stagelinQmessages" :key="mesg">
        {{ mesg }}
      </li>
    </ul>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
