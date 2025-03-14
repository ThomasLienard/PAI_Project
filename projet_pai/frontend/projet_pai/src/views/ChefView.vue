// Cette vue représente l'interface principale pour le cuisinier.
// Elle regroupe trois sections principales :
// 1. Commandes à préparer : Affiche la liste des commandes en attente de préparation.
// 2. Gestion des recettes : Permet au cuisinier de gérer les recettes (ajouter, modifier, supprimer).
// 3. Gestion des stocks : Permet de visualiser et gérer les stocks d'ingrédients.

<template>
    <div class="chef-interface">
      <h1>Interface Cuisinier</h1>
      <section>
        <h2>Commandes à préparer</h2>
        <OrderList />
      </section>
      <section>
        <h2>Gestion des recettes</h2>
        <RecipeManager />
      </section>
      <section>
        <h2>Gestion des stocks</h2>
        <StockList />
      </section>
    </div>
  </template>
  
  <script setup lang="ts">
  import OrderList from '@/components/Chef/OrderList.vue'
  import RecipeManager from '@/components/Chef/RecipeManager.vue'
  import StockList from '@/components/Chef/StockList.vue'
  import '@/assets/styles/ChefStyles.css'
  </script>
  
  <style scoped>
  .chef-interface {
    padding: 1rem;
  }
  </style>