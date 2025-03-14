<template>
    <div class="recipe-manager">
      <h3>Gestion des recettes</h3>
      <ul>
        <li v-for="recipe in recipes" :key="recipe.id">
          {{ recipe.name }}
          <button @click="editRecipe(recipe.id)">Modifier</button>
          <button @click="deleteRecipe(recipe.id)">Supprimer</button>
        </li>
      </ul>
      <button @click="addRecipe">Ajouter une recette</button>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue'
  
  // Importer les styles globaux
  import '@/assets/styles/ChefStyles.css'
  
  // Exemple de données fictives pour les recettes
  const recipes = ref([
    { id: 1, name: 'Pizza Margherita' },
    { id: 2, name: 'Salade César' },
    { id: 3, name: 'Pâtes Carbonara' },
  ])
  
  // Fonction pour ajouter une recette
  function addRecipe() {
    console.log('Ajouter une nouvelle recette')
  }
  
  // Fonction pour modifier une recette
  function editRecipe(recipeId: number) {
    console.log(`Modifier la recette avec l'ID : ${recipeId}`)
  }
  
  // Fonction pour supprimer une recette
  function deleteRecipe(recipeId: number) {
    console.log(`Supprimer la recette avec l'ID : ${recipeId}`)
  }
  </script>