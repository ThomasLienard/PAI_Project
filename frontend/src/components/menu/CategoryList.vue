<template>
  <div class="category">
    <h2>{{ category }}</h2>
    <div class="dishes">
      <DishCard v-for="dish in dishes" :key="dish.id" :dish="dish" />
    </div>
  </div>
</template>

<script setup>
import DishCard from './DishCard.vue'

defineProps({
  category: {
    type: String,
    required: true
  },
  dishes: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
.category {
  margin-bottom: 2rem;
}

.dishes {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1rem;
}
</style>