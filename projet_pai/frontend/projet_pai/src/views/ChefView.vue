<template>
    <div class="chef-interface">
      <h1>Interface Cuisinier</h1>
      <div class="blocks-container">
        <!-- Bloc Commandes -->
        <div class="block">
          <h2>Commandes à préparer</h2>
          <button @click="navigateTo('/chef/orders')">Voir détails</button>
        </div>
        <!-- Bloc Recettes -->
        <div class="block">
          <h2>Gestion des recettes</h2>
          <button @click="navigateTo('/chef/recipes')">Voir détails</button>
        </div>
        <!-- Bloc Stocks -->
        <div class="block">
          <h2>Gestion des stocks</h2>
          <button @click="navigateTo('/chef/stocks')">Voir détails</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { useRouter } from 'vue-router'
  
  // Utilisation du routeur pour naviguer vers les pages dédiées
  const router = useRouter()
  
  function navigateTo(path: string) {
    router.push(path)
  }
  </script>
  
  <style scoped>
  .chef-interface {
    text-align: center;
    padding: 2rem;
  }
  
  .blocks-container {
    display: flex;
    justify-content: space-around;
    gap: 1rem;
    margin-top: 2rem;
  }
  
  .block {
    width: 250px;
    height: 150px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border: 2px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
  }
  
  .block:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
  }
  
  .block h2 {
    margin: 0;
    font-size: 1.2rem;
    color: #333;
  }
  
  .block button {
    margin-top: 1rem;
    padding: 0.5rem 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 1rem;
  }
  
  .block button:hover {
    background-color: #0056b3;
  }
  </style>