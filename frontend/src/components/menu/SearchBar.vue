<template>
  <div class="search-bar">
    <input
      type="text"
      v-model="searchQuery"
      placeholder="Rechercher un plat..."
      @input="onSearch"
    />
    <select v-model="selectedCategory" @change="onFilter">
      <option value="">Toutes les catégories</option>
      <option v-for="category in categories" :key="category" :value="category">
        {{ category }}
      </option>
    </select>
    <select v-model="selectedTag" @change="onFilter">
      <option value="">Tous les tags</option>
      <option v-for="tag in tags" :key="tag.id" :value="tag.name">
        {{ tag.name }}
      </option>
    </select>
  </div>
</template>

<script setup>
import { ref, defineEmits, defineProps } from 'vue';

// Déclare les props pour recevoir les catégories et les tags
defineProps({
  categories: {
    type: Array,
    required: true
  },
  tags: {
    type: Array,
    required: true
  }
});

// Déclare les événements pour transmettre les données au parent
const emit = defineEmits(['search', 'filter']);

const searchQuery = ref('');
const selectedCategory = ref('');
const selectedTag = ref('');

// Émet l'événement de recherche
const onSearch = () => {
  emit('search', searchQuery.value);
};

// Émet l'événement de filtre
const onFilter = () => {
  emit('filter', { category: selectedCategory.value, tag: selectedTag.value });
};
</script>

<style scoped>
.search-bar {
  margin: 1rem 0;
  text-align: center;
}

.search-bar input,
.search-bar select {
  width: 30%;
  padding: 0.5rem;
  font-size: 1rem;
  margin: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>