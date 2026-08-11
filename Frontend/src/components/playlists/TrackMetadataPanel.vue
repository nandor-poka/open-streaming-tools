<script setup lang="ts">
import type { Track } from '@/types/Track'
const props = defineProps<{ metadata?: Track['metadata'] }>()
</script>

<template>
  <div class="metadata-panel">
    <div><strong>Bitrate:</strong> {{ props.metadata?.bitrate ?? 'N/A' }} kbps</div>
    <div><strong>Sample Rate:</strong> {{ props.metadata?.sampleRate ?? 'N/A' }} Hz</div>
    <div><strong>Size:</strong> {{ props.metadata?.sizeBytes ? (props.metadata.sizeBytes / (1024*1024)).toFixed(2) + ' MB' : 'N/A' }}</div>
  </div>
</template>

<style scoped>
.metadata-panel{
  background: #fff;
  border:1px solid #eee;
  padding:0.5rem;
  margin:0.5rem 0;
  font-size:0.9rem;
}
</style>

