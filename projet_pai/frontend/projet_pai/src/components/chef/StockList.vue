// Ce composant affiche et gère les stocks d'ingrédients.
// Rôle :
// - Afficher la liste des ingrédients avec leurs quantités disponibles.
// - Permettre au cuisinier de mettre à jour les quantités (ajout ou retrait).
// - Alerter en cas de stock faible pour certains ingrédients.
// - Synchroniser les données avec le backend pour maintenir les stocks à jour.

<template>
  <div>
    <NavBar />
      <div>
        <h3>Liste des stocks</h3>
        <p>Ce composant affichera les stocks d'ingrédients.</p>
      </div>
  </div>
  </template>
  
  <script setup>
    import NavBar from '../../components/NavBar.vue';
  </script>
  
  <style scoped>
  </style>