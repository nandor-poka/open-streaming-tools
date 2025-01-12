<script setup lang="ts">
import { Client } from '@stomp/stompjs';
const message = 'Log goes here'
</script>

<script lang="ts">
const messages = new Array<string>()
const client = new Client({
  brokerURL: 'ws://localhost:8080/stagelinq',
  onConnect: () => {
    client.subscribe('/topic', message =>{
      //console.log(`Received: ${message.body}`)
      messages.push(message.body)
    }
    );
    client.publish({ destination: '/app/incoming', body: 'Hello Denon' });
  },
});
messages.push("dummy message")
client.activate();
console.log(client)
</script>
<template>
  <div>
    <h4 class="blue">{{ message }}</h4>
  </div>
  <div>
    <ul>
      <li v-for="message in messages" :key="message.id">
        {{ message }}
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
