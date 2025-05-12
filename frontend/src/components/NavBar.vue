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
  padding: 0 1rem;
  height: 48px;
  min-height: 0;
  max-height: 56px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #222;
  box-sizing: border-box;
}

ul {
  list-style: none;
  display: flex;
  gap: 1rem;
  margin: 0;
  padding: 0;
}

li {
  color: white;
}

button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 1rem;
  padding: 0 8px;
  height: 32px;
  line-height: 32px;
}
</style>