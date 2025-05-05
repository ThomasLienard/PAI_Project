<template>
  <div>
    <NavBar />
      <h1>Interface d'administration</h1>
      <div class="admin-buttons">
        <router-link to="/admin/user">
          <button>Gestion des utilisateurs</button>
        </router-link>
        <router-link to="/admin/menus">
          <button>Gestion des menus</button>
        </router-link>
        <router-link to="/admin/orders">
          <button>Gestion des commandes</button>
        </router-link>
        <router-link to="/admin/statistics">
          <button>Gestion des statistiques</button>
        </router-link>
        <router-link to="/admin/suppliers">
          <button>Gestion des fournisseurs</button>
        </router-link>
      </div>
  </div>
</template>

<script setup>
import NavBar from '../../components/NavBar.vue';
</script>

<style scoped>
.admin-buttons {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

button {
  padding: 1rem;
  font-size: 1rem;
  cursor: pointer;
}
</style>