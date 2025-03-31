<template>
  <div class="dish-card">
    <img :src="dish.imageUrl" alt="Image du plat" v-if="dish.imageUrl" />
    <h3>{{ dish.name }}</h3>
    <p>{{ dish.description }}</p>
    <p class="price">{{ dish.price.toFixed(2) }} €</p>
    <!-- Affichage des tags -->
    <div class="tags" v-if="dish.tags && dish.tags.length > 0">
      <span v-for="tag in dish.tags" :key="tag.id" class="tag">
        {{ tag.name }}
      </span>
    </div>
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

.tags {
  margin-top: 0.5rem;
}

.tag {
  display: inline-block;
  background-color: #f0f0f0;
  color: #333;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  margin-right: 0.3rem;
}
</style>