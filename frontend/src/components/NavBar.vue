<template>
  <nav>
    <ul>
      <li v-if="isAuthenticated"><button @click="goHome">Home</button></li>
      <li v-if="isAuthenticated"><button @click="logoutUser">Déconnexion</button></li>
    </ul>
  </nav>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { logout } from '../services/authService';

const isAuthenticated = ref(!!sessionStorage.getItem('jwtToken'));
const router = useRouter();

const goHome = () => {
  const token = sessionStorage.getItem('jwtToken');
  if (token) {
    const userRole = JSON.parse(atob(token.split('.')[1])).role;
    if (userRole === 'ADMIN') {
      router.push('/admin');
    } else if (userRole === 'CUISINIER') {
      router.push('/chef');
    } else if (userRole === 'SERVEUR') {
      router.push('/server');
    } else {
      router.push('/user');
    }
  } else {
    router.push('/');
  }
};

const logoutUser = async () => {
  await logout();
  isAuthenticated.value = false;
  router.push('/login');
};
</script>

<style scoped>
nav {
  background-color: #333;
  padding: 1rem;
}

ul {
  list-style: none;
  display: flex;
  gap: 1rem;
}

li {
  color: white;
}

button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
}
</style>