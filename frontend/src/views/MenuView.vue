<template>
  <div>
    <NavBar />
    <MenuHeader />
    <SearchBar
      :categories="categories"
      :tags="tags"
      @search="handleSearch"
      @filter="handleFilter"
    />
    <div v-if="Object.keys(filteredMenu).length > 0">
      <CategoryList
        v-for="(dishes, category) in filteredMenu"
        :key="category"
        :category="category"
        :dishes="dishes"
      />
    </div>
    <div v-else>
      <p>Aucun plat trouvé.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { getMenuGroupedByCategory } from '../services/apiClient';
import NavBar from '../components/NavBar.vue';
import MenuHeader from '../components/menu/MenuHeader.vue';
import CategoryList from '../components/menu/CategoryList.vue';
import SearchBar from '../components/menu/SearchBar.vue';

const menu = ref({});
const searchQuery = ref('');
const selectedCategory = ref('');
const selectedTag = ref('');

// Extraire les catégories et les tags uniques
const categories = computed(() => Object.keys(menu.value));
const tags = computed(() => {
  const allTags = new Set();
  Object.values(menu.value).forEach(dishes => {
    dishes.forEach(dish => {
      dish.tags.forEach(tag => allTags.add(tag));
    });
  });
  return Array.from(allTags);
});

// Filtrer le menu en fonction de la recherche, de la catégorie et du tag
const filteredMenu = computed(() => {
  let filtered = menu.value;

  // Filtrer par catégorie
  if (selectedCategory.value) {
    filtered = {
      [selectedCategory.value]: filtered[selectedCategory.value] || []
    };
  }

  // Filtrer par tag
  if (selectedTag.value) {
    const filteredByTag = {};
    for (const [category, dishes] of Object.entries(filtered)) {
      const filteredDishes = dishes.filter(dish =>
        dish.tags.some(tag => tag.name === selectedTag.value)
      );
      if (filteredDishes.length > 0) {
        filteredByTag[category] = filteredDishes;
      }
    }
    filtered = filteredByTag;
  }

  // Filtrer par recherche
  if (searchQuery.value) {
    const filteredBySearch = {};
    for (const [category, dishes] of Object.entries(filtered)) {
      const filteredDishes = dishes.filter(dish =>
        dish.name.toLowerCase().includes(searchQuery.value.toLowerCase())
      );
      if (filteredDishes.length > 0) {
        filteredBySearch[category] = filteredDishes;
      }
    }
    filtered = filteredBySearch;
  }

  return filtered;
});

// Charger le menu depuis l'API
const fetchMenu = async () => {
  menu.value = await getMenuGroupedByCategory();
};

// Mettre à jour la recherche
const handleSearch = query => {
  searchQuery.value = query;
};

// Mettre à jour les filtres
const handleFilter = ({ category, tag }) => {
  selectedCategory.value = category;
  selectedTag.value = tag;
};

onMounted(fetchMenu);
</script>

<style scoped>
/* Ajoutez des styles globaux si nécessaire */
</style>