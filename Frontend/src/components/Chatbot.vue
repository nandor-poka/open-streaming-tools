<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { inject,useTemplateRef, onMounted } from 'vue'
import type { Axios } from 'axios'
const getRecButton = useTemplateRef("getRecommends")
const axios: Axios = inject('axios') as Axios

onMounted(() => {
if(getRecButton.value){
  getRecButton.value.onclick= function(){
    axios
    .get('api/getInKeyRecommendation/2', {
      method: 'get',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then(function (response) {
      console.log(response.data)
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
  }
}})
</script>
<template>
      <Navbar />
      <input value="get recommendations" ref="getRecommends" type="button" name="getRecommends" id="getRecommends"/>
</template>
