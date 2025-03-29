<template>
  <div class="dish-card">
    <img :src="dish.imageUrl" alt="Image du plat" v-if="dish.imageUrl" />
    <h3>{{ dish.name }}</h3>
    <p>{{ dish.description }}</p>
    <p class="price">{{ dish.price.toFixed(2) }} €</p>
  </div>
</template>

<script setup>
defineProps({
  dish: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.dish-card {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
}

.dish-card img {
  max-width: 100%;
  border-radius: 8px;
}

.price {
  font-weight: bold;
  color: #333;
}
</style>