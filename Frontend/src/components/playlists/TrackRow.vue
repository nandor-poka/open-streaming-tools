<script setup lang="ts">
import type { Track } from '@/types/Track'
import { defineProps, defineEmits } from 'vue'
const props = defineProps<{ track: Track }>()
const emit = defineEmits<{
  (e: 'toggle-expand', id: number | string): void
}>()
function onToggle(){
  emit('toggle-expand', props.track.id ?? props.track.title)
}
</script>

<template>
  <tr @click.prevent="onToggle" class="track-row">
    <td>{{ track.title }}</td>
    <td>{{ track.artist || 'Unknown' }}</td>
    <td>{{ track.key ?? '-' }}</td>
    <td>{{ track.duration ? new Date(track.duration * 1000).toISOString().substr(14,5) : '-' }}</td>
  </tr>
</template>

<style scoped>
.track-row{ cursor:pointer }
.track-row:hover{ background:#f5f5f5 }
</style>

