// Ce composant affiche la liste des commandes à préparer.
// Rôle :
// - Récupérer les commandes depuis le backend via une API.
// - Afficher les commandes avec leur statut (ex. : "En cours", "Prêt").
// - Permettre au cuisinier de marquer une commande comme terminée.

<template>
    <div class="order-list">
      <h3>Liste des commandes</h3>
      <ul>
        <li v-for="order in orders" :key="order.id">
          {{ order.name }} - Statut : {{ order.status }}
          <button @click="markAsCompleted(order.id)">Marquer comme terminé</button>
        </li>
      </ul>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue'
  
  // Importer les styles globaux
  import '@/assets/styles/ChefStyles.css'
  
  // Exemple de données fictives pour les commandes
  const orders = ref([
    { id: 1, name: 'Pizza Margherita', status: 'En cours' },
    { id: 2, name: 'Salade César', status: 'Prêt' },
    { id: 3, name: 'Pâtes Carbonara', status: 'En cours' },
  ])
  
  // Fonction pour marquer une commande comme terminée
  function markAsCompleted(orderId: number) {
    const order = orders.value.find(o => o.id === orderId)
    if (order) {
      order.status = 'Terminé'
    }
  }
  </script>