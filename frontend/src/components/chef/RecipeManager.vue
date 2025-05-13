<template>
  <div>
    <NavBar />
    <div class="recipe-manager">
      <h3>Gestion des recettes</h3>
      <RecipeList
        :recipes="recipes"
        @edit="openEditForm"
        @delete="deleteRecipe"
      />
      <button v-if="!showCreateForm && !editingRecipe" @click="showCreateForm = true">Ajouter une recette</button>
      <RecipeForm
        v-if="showCreateForm || editingRecipe"
        :ingredients="ingredients"
        :recipe="editingRecipe"
        @save="handleSave"
        @cancel="closeForm"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import NavBar from '../../components/NavBar.vue'
import RecipeList from './RecipeList.vue'
import RecipeForm from './RecipeForm.vue'
import apiClient from '@/services/apiClient'

const recipes = ref([])
const ingredients = ref([])
const showCreateForm = ref(false)
const editingRecipe = ref(null)

const fetchRecipes = async () => {
  const res = await apiClient.get('/cuisinier/recipes')
  recipes.value = res.data
}
const fetchIngredients = async () => {
  const res = await apiClient.get('/cuisinier/ingredients')
  ingredients.value = res.data
}

onMounted(() => {
  fetchRecipes()
  fetchIngredients()
})

function openEditForm(recipe) {
  editingRecipe.value = recipe
  showCreateForm.value = false
}
function closeForm() {
  editingRecipe.value = null
  showCreateForm.value = false
}
async function handleSave(recipeData) {
  if (editingRecipe.value) {
    await apiClient.patch(`/cuisinier/recipes/modify/${editingRecipe.value.id}`, recipeData)
  } else {
    await apiClient.post('/cuisinier/recipes/create', recipeData)
  }
  await fetchRecipes()
  closeForm()
}
async function deleteRecipe(id) {
  await apiClient.delete(`/cuisinier/recipes/${id}`)
  await fetchRecipes()
}
</script>

<style scoped>
.recipe-manager {
  max-width: 700px;
  margin: 40px auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  padding: 32px 24px;
}
.recipe-manager h3 {
  text-align: center;
  margin-bottom: 24px;
  color: #2c3e50;
}
button {
  margin: 8px 4px 0 0;
  padding: 8px 18px;
  border: none;
  border-radius: 6px;
  background: #3498db;
  color: #fff;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
button:hover {
  background: #217dbb;
}
</style>