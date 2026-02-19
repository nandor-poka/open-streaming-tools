<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
const settingsStore = SettingsStore()
</script>

<template>
  <h2>Twitch / Credentials</h2>
  <hr />
  <table>
    <tbody>
      <tr>
        <td>Path to the file containing the client ID</td>
        <td>
          <input v-model="settingsStore.clientIdFilePath" type="text" id="clientIDPath" />
        </td>
        <td>
          Fully qualified path to the file that has the Twitch client ID.
        </td>
      </tr>
      <tr>
        <td>Path to the file containing the client secret</td>
        <td>
          <input v-model="settingsStore.clientSecretFilePath" type="text" id="clientSecretPath" />
        </td>
        <td>
          Fully qualified path to the file that has the Twitch client secret.
        </td>
      </tr>
      <tr>
        <td>Auto shoutout list</td>
        <td>
          <textarea v-model="settingsStore.autoShoutoutList" rows="10" cols="35" id="autoShoutoutList"></textarea>
        </td>
      </tr>
    </tbody>
  </table>
</template>

