<script setup lang="ts">
import { RouterLink, RouterView, useRoute } from 'vue-router'
import { ref, watch } from 'vue'

const route = useRoute()

// Définir les routes spécifiques pour chaque rôle
const isAdminRoute = ref(route.path.startsWith('/admin'))
const isChiefRoute = ref(route.path.startsWith('/chef'))
const isServeurRoute = ref(route.path.startsWith('/server'))
const isUserRoute = ref(route.path.startsWith('/user'))

// Variable réactive pour déterminer si le header doit être caché
const isHiddenRoute = ref(false)

// Surveiller les changements de route et mettre à jour les variables
watch(
  () => route.path,
  (newPath) => {
    isAdminRoute.value = newPath.startsWith('/admin')
    isChiefRoute.value = newPath.startsWith('/chef')
    isServeurRoute.value = newPath.startsWith('/server')
    isUserRoute.value = newPath.startsWith('/user')

    // Mettre à jour `isHiddenRoute` en fonction du rôle
    const token = sessionStorage.getItem('jwtToken')
    if (token) {
      const userRole = JSON.parse(atob(token.split('.')[1])).role
      if (userRole === 'ADMIN') {
        isHiddenRoute.value = isAdminRoute.value
      } else if (userRole === 'CUISINIER') {
        isHiddenRoute.value = isChiefRoute.value
      } else if (userRole === 'SERVEUR') {
        isHiddenRoute.value = isServeurRoute.value
      } else {
        isHiddenRoute.value = isUserRoute.value
      }
    } else {
      isHiddenRoute.value = false
    }
  },
  { immediate: true } // Exécuter immédiatement pour initialiser correctement `isHiddenRoute`
)
</script>

<template>
  <!-- Afficher le header uniquement si `isHiddenRoute` est faux -->
  <header v-if="!isHiddenRoute">
    <div class="wrapper">
      <div class="logo">
        <RouterLink to="/">🍽️ RestoApp</RouterLink>
      </div>
      <nav>
        <RouterLink to="/">Accueil</RouterLink>
        <RouterLink to="/about">À propos</RouterLink>
        <RouterLink to="/login">Connexion</RouterLink>
        <RouterLink to="/register">Inscription</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  z-index: 1000;
  padding: 0.5rem 2rem;
}

.wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1280px;
  margin: 0 auto;
}

nav {
  display: flex;
  gap: 1.5rem;
}

nav a {
  text-decoration: none;
  color: #2c3e50;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: background 0.3s, color 0.3s;
}

nav a:hover {
  background-color: #f0f0f0;
  color: #42b983;
}

nav a.router-link-exact-active {
  background-color: #42b983;
  color: white;
}

body {
  padding-top: 70px; /* pour compenser le header fixe */
}
</style>