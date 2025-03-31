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
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/login">Se connecter</RouterLink>
        <RouterLink to="/register">Créer un compte</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>