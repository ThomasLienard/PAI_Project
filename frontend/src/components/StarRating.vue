<!-- filepath: frontend/src/components/StarRating.vue -->
<template>
    <div class="star-rating">
      <span
        v-for="star in 5"
        :key="star"
        class="star"
        :class="{ filled: star <= modelValue }"
        @click="setRating(star)"
        @mouseover="hoverRating = star"
        @mouseleave="hoverRating = 0"
        :style="{ cursor: readOnly ? 'default' : 'pointer' }"
      >
        {{ star <= (hoverRating || modelValue) ? '★' : '☆' }}
      </span>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  
  const props = defineProps({
    modelValue: { type: Number, default: 0 },
    readOnly: { type: Boolean, default: false }
  })
  const emit = defineEmits(['update:modelValue'])
  
  const hoverRating = ref(0)
  
  function setRating(star) {
    if (!props.readOnly) {
      emit('update:modelValue', star)
    }
  }
  </script>
  
  <style scoped>
  .star-rating {
    display: inline-block;
    font-size: 1.5em;
    color: #FFD700;
    user-select: none;
  }
  .star {
    transition: color 0.2s;
  }
  .star.filled {
    color: #FFD700;
  }
  </style>