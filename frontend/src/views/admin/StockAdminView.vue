<template>
  <div>
    <NavBar />
    <h2>Gestion des stocks</h2>
    <div class="stock-admin-actions">
      <button @click="toggleCategory">
        {{ showCategory ? 'Fermer' : 'Créer une catégorie' }}
      </button>
      <button @click="toggleIngredient">
        {{ showIngredient ? 'Fermer' : 'Créer un ingrédient' }}
      </button>
    </div>
    <div class="stock-admin-container">
      <!-- Affiche la liste des stocks seulement si aucun formulaire n'est ouvert -->
      <div v-if="!showCategory && !showIngredient" class="stock-list">
        <div v-for="cat in categories" :key="cat.id" class="category-block">
          <h3>
            <img v-if="cat.icon" :src="cat.icon" alt="icon" width="24" height="24" style="vertical-align:middle;"/>
            {{ cat.name }}
          </h3>
          <ul>
            <li v-for="ing in ingredientsByCategory(cat.id)" :key="ing.id">
              <img v-if="ing.photoUrl" :src="ing.photoUrl" alt="photo" width="24" height="24" style="vertical-align:middle;"/>
              {{ ing.name }} ({{ ing.unit }}) - Stock : {{ ing.initialStock }}
            </li>
          </ul>
        </div>
      </div>
      <!-- Formulaires -->
      <StockCategoryList v-if="showCategory" @refresh="handleRefresh" />
      <StockIngredientList v-if="showIngredient" @refresh="handleRefresh" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllCategories, getAllIngredients } from '../../services/apiClient'
import StockCategoryList from '../../components/stocks/StockCategoryList.vue';
import StockIngredientList from '../../components/stocks/StockIngredientList.vue';
import NavBar from '../../components/NavBar.vue'

const showCategory = ref(false)
const showIngredient = ref(false)

const categories = ref([])
const ingredients = ref([])

const fetchCategories = async () => {
  categories.value = await getAllCategories()
}
const fetchIngredients = async () => {
  ingredients.value = await getAllIngredients()
}

const ingredientsByCategory = (catId) => {
  return ingredients.value.filter(ing => ing.categoryId === catId || (ing.category && ing.category.id === catId))
}

// Pour que l'ouverture d'un formulaire ferme l'autre et masque la liste
const toggleCategory = () => {
  showCategory.value = !showCategory.value
  if (showCategory.value) showIngredient.value = false
}
const toggleIngredient = () => {
  showIngredient.value = !showIngredient.value
  if (showIngredient.value) showCategory.value = false
}

// Rafraîchit la liste après ajout/suppression/modif
const handleRefresh = () => {
  fetchCategories();
  fetchIngredients();
}

onMounted(() => {
  fetchCategories()
  fetchIngredients()
})
</script>

<style scoped>
.stock-admin-container {
  display: flex;
  gap: 2rem;
  margin-top: 2rem;
  align-items: flex-start;
}
.stock-admin-actions {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}
.stock-list {
  flex: 2;
}
.category-block {
  margin-bottom: 2rem;
  background: #f8f8f8;
  padding: 1rem;
  border-radius: 8px;
}
</style>