<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
const settingsStore = SettingsStore()
</script>

<template>
  <h2>General</h2>
  <p>General application settings.</p>
  <hr />
  <table>
    <tbody>
      <tr>
        <th>Setting</th>
        <th>Value</th>
        <th>Comments</th>
      </tr>
      <tr>
        <td>User name for the Twitch channel to send message to</td>
        <td>
          <input v-model="settingsStore.channelUserName" type="text" id="channelUserName" />
        </td>
        <td>
          The name of the Twitch user who owns the channel that the bot will send message to. Typically your own user name for your own channel.
        </td>
      </tr>
      <tr>
        <td>User name for the Twitch user that acts as the bot</td>
        <td>
          <input v-model="settingsStore.botUserName" type="text" id="botUserName" />
        </td>
        <td>
          The name of the Twitch user who owns the channel that the bot will send message to. Typically your own user name for your own channel.
        </td>
      </tr>
    </tbody>
  </table>
</template>

