<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
import { TrackStore } from '@/stores/TrackStore'
import type { Axios } from 'axios'
import { inject } from 'vue'
const trackStore = TrackStore()
const settingsStore = SettingsStore()
const axios: Axios = inject('axios') as Axios
const twitchClient = new WebSocket('wss://eventsub.wss.twitch.tv/ws')

twitchClient.onopen = ()=> {
    console.log("Websocket to Twitch opened")
  }
  twitchClient.onmessage = (weboscketMessage) =>{
    const twitchMessage = JSON.parse(weboscketMessage.data)
    console.log(twitchMessage)
    switch (twitchMessage.metadata.message_type) {
      case "session_welcome":
        if (!settingsStore.twitchStatus){
          break
        }
        axios.post('api/subscribeToTwtitch',{
          sessionId: twitchMessage.payload.session.id
        }).then(function(response){
          settingsStore.twitchResponse = response.data

          axios.get('api/getSubscriptions').then(function(response){
            console.log(response)
          }).catch(function (error) {
          // handle error
          console.log(error)
        })
        })
        .catch(function (error) {
          // handle error
          console.log(error)
        })
        console.log(twitchMessage.payload.session.id)
        break;
       case "notification":
        switch(twitchMessage.metadata.subscription_type){
          case "channel.channel_points_custom_reward_redemption.add":
            break;
          case "channel.chat.message":
            switch(twitchMessage.payload.event.message.text){
            case "!recommend":
              axios.get('api/getInKeyRecommendation/'+trackStore.currentKey, {
                headers: {
                  'Content-Type': 'application/json',
                },
                })
                .catch(function (error) {
                  // handle error
                  console.log(error)
                })
            break;
            default:
            break;
            }
          break;
        }
      }
  }


</script>


